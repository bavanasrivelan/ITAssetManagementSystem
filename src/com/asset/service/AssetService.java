package com.asset.service;

import java.util.Date;
import java.util.List;

import com.asset.bean.Asset;
import com.asset.bean.Checkout;
import com.asset.dao.AssetDAO;
import com.asset.dao.CheckoutDAO;
import com.asset.util.ActiveCheckoutExistsException;
import com.asset.util.AssetNotAvailableException;
import com.asset.util.ValidationException;

public class AssetService {

    AssetDAO assetDAO = new AssetDAO();
    CheckoutDAO checkoutDAO = new CheckoutDAO();

    // View Asset Details
    public Asset viewAssetDetails(String assetTag) throws ValidationException {

        if (assetTag == null || assetTag.trim().isEmpty()) {
            throw new ValidationException("Asset Tag cannot be empty");
        }

        return assetDAO.findAsset(assetTag);
    }

    // View All Assets
    public List<Asset> viewAllAssets() {

        return assetDAO.viewAllAssets();

    }

    // Add New Asset
    public boolean addNewAsset(Asset asset) throws ValidationException {

        if (asset.getAssetTag() == null || asset.getAssetTag().trim().isEmpty())
            throw new ValidationException("Invalid Asset Tag");

        if (asset.getCategory() == null || asset.getCategory().trim().isEmpty())
            throw new ValidationException("Invalid Category");

        if (asset.getModel() == null || asset.getModel().trim().isEmpty())
            throw new ValidationException("Invalid Model");

        if (asset.getTotalQuantity() < 0)
            throw new ValidationException("Invalid Quantity");

        if (asset.getAvailableQuantity() < 0
                || asset.getAvailableQuantity() > asset.getTotalQuantity())
            throw new ValidationException("Invalid Available Quantity");

        Asset a = assetDAO.findAsset(asset.getAssetTag());

        if (a != null)
            return false;

        asset.setStatus("ACTIVE");

        return assetDAO.insertAsset(asset);

    }

    // Remove Asset
    public boolean removeAsset(String assetTag)
            throws ValidationException, ActiveCheckoutExistsException {

        if (assetTag == null || assetTag.trim().isEmpty())
            throw new ValidationException("Invalid Asset Tag");

        List<Checkout> list = checkoutDAO.findActiveCheckoutsByAsset(assetTag);

        if (!list.isEmpty())
            throw new ActiveCheckoutExistsException("Asset has Active Checkout");

        return assetDAO.deleteAsset(assetTag);

    }

    // Checkout Asset
    public boolean checkoutAsset(String assetTag,
            String employeeID,
            Date checkoutDate,
            Date dueDate)
            throws ValidationException, AssetNotAvailableException {

        if (assetTag == null || assetTag.trim().isEmpty())
            throw new ValidationException("Invalid Asset Tag");

        if (employeeID == null || employeeID.trim().isEmpty())
            throw new ValidationException("Invalid Employee ID");

        if (checkoutDate == null || dueDate == null)
            throw new ValidationException("Invalid Date");

        Asset asset = assetDAO.findAsset(assetTag);

        if (asset == null)
            return false;

        if (!asset.getStatus().equalsIgnoreCase("ACTIVE")
                || asset.getAvailableQuantity() <= 0)

            throw new AssetNotAvailableException("Asset Not Available");

        Checkout checkout = new Checkout();

        checkout.setCheckoutID(checkoutDAO.generateCheckoutID());
        checkout.setAssetTag(assetTag);
        checkout.setEmployeeID(employeeID);
        checkout.setCheckoutDate(checkoutDate);
        checkout.setDueDate(dueDate);
        checkout.setStatus("OUT");

        boolean r1 = checkoutDAO.recordCheckout(checkout);

        boolean r2 = assetDAO.updateAssetQuantitiesAndStatus(
                assetTag,
                asset.getAvailableQuantity() - 1,
                asset.getStatus());

        return r1 && r2;

    }

    // Return Asset
    public boolean returnAsset(int checkoutID,
            Date actualReturnDate)
            throws ValidationException {

        if (checkoutID <= 0)
            throw new ValidationException("Invalid Checkout ID");

        Checkout checkout = checkoutDAO.findCheckoutByID(checkoutID);

        if (checkout == null)
            return false;

        if (!checkout.getStatus().equalsIgnoreCase("OUT"))
            return false;

        Asset asset = assetDAO.findAsset(checkout.getAssetTag());

        if (asset == null)
            return false;

        boolean r1 = checkoutDAO.updateCheckoutStatusAndReturnDate(
                checkoutID,
                "RETURNED",
                actualReturnDate);

        boolean r2 = assetDAO.updateAssetQuantitiesAndStatus(
                asset.getAssetTag(),
                asset.getAvailableQuantity() + 1,
                asset.getStatus());

        return r1 && r2;

    }

    // Mark Lost or Damaged
    public boolean markAssetLostOrDamaged(int checkoutID,
            String newStatus,
            Date reportedDate)
            throws ValidationException {

        if (checkoutID <= 0)
            throw new ValidationException("Invalid Checkout ID");

        if (!(newStatus.equalsIgnoreCase("LOST")
                || newStatus.equalsIgnoreCase("DAMAGED")))
            throw new ValidationException("Invalid Status");

        Checkout checkout = checkoutDAO.findCheckoutByID(checkoutID);

        if (checkout == null)
            return false;

        Asset asset = assetDAO.findAsset(checkout.getAssetTag());

        if (asset == null)
            return false;

        boolean r1 = checkoutDAO.updateCheckoutStatusAndReturnDate(
                checkoutID,
                newStatus,
                reportedDate);

        boolean r2 = assetDAO.updateAssetQuantitiesAndStatus(
                asset.getAssetTag(),
                asset.getAvailableQuantity(),
                "RETIRED");

        return r1 && r2;

    }

}