package com.asset.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.asset.bean.Asset;
import com.asset.util.DBUtil;

public class AssetDAO {

    Connection con = null;
    PreparedStatement ps = null;
    ResultSet rs = null;

    // Find Asset
    public Asset findAsset(String assetTag) {

        Asset asset = null;

        try {

            con = DBUtil.getDBConnection();

            String query = "SELECT * FROM ASSET_TBL WHERE ASSETTAG=?";

            ps = con.prepareStatement(query);
            ps.setString(1, assetTag);

            rs = ps.executeQuery();

            if (rs.next()) {

                asset = new Asset();

                asset.setAssetTag(rs.getString("ASSETTAG"));
                asset.setCategory(rs.getString("CATEGORY"));
                asset.setModel(rs.getString("MODEL"));
                asset.setSerialNo(rs.getString("SERIALNO"));
                asset.setTotalQuantity(rs.getInt("TOTALQUANTITY"));
                asset.setAvailableQuantity(rs.getInt("AVAILABLEQUANTITY"));
                asset.setStatus(rs.getString("STATUS"));

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return asset;
    }

    // View All Assets
    public List<Asset> viewAllAssets() {

        List<Asset> list = new ArrayList<>();

        try {

            con = DBUtil.getDBConnection();

            String query = "SELECT * FROM ASSET_TBL";

            ps = con.prepareStatement(query);

            rs = ps.executeQuery();

            while (rs.next()) {

                Asset asset = new Asset();

                asset.setAssetTag(rs.getString("ASSETTAG"));
                asset.setCategory(rs.getString("CATEGORY"));
                asset.setModel(rs.getString("MODEL"));
                asset.setSerialNo(rs.getString("SERIALNO"));
                asset.setTotalQuantity(rs.getInt("TOTALQUANTITY"));
                asset.setAvailableQuantity(rs.getInt("AVAILABLEQUANTITY"));
                asset.setStatus(rs.getString("STATUS"));

                list.add(asset);

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;

    }

    // Insert Asset
    public boolean insertAsset(Asset asset) {

        try {

            con = DBUtil.getDBConnection();

            String query = "INSERT INTO ASSET_TBL VALUES(?,?,?,?,?,?,?)";

            ps = con.prepareStatement(query);

            ps.setString(1, asset.getAssetTag());
            ps.setString(2, asset.getCategory());
            ps.setString(3, asset.getModel());
            ps.setString(4, asset.getSerialNo());
            ps.setInt(5, asset.getTotalQuantity());
            ps.setInt(6, asset.getAvailableQuantity());
            ps.setString(7, asset.getStatus());

            int row = ps.executeUpdate();

            if (row > 0) {

                con.commit();
                return true;

            }

        } catch (Exception e) {

            e.printStackTrace();

        }

        return false;

    }

    // Update Asset Quantity and Status
    public boolean updateAssetQuantitiesAndStatus(String assetTag,
            int newAvailableQuantity,
            String newStatus) {

        try {

            con = DBUtil.getDBConnection();

            String query = "UPDATE ASSET_TBL SET AVAILABLEQUANTITY=?, STATUS=? WHERE ASSETTAG=?";

            ps = con.prepareStatement(query);

            ps.setInt(1, newAvailableQuantity);
            ps.setString(2, newStatus);
            ps.setString(3, assetTag);

            int row = ps.executeUpdate();

            if (row > 0) {

                con.commit();
                return true;

            }

        } catch (Exception e) {

            e.printStackTrace();

        }

        return false;

    }

    // Delete Asset
    public boolean deleteAsset(String assetTag) {

        try {

            con = DBUtil.getDBConnection();

            String query = "DELETE FROM ASSET_TBL WHERE ASSETTAG=?";

            ps = con.prepareStatement(query);

            ps.setString(1, assetTag);

            int row = ps.executeUpdate();

            if (row > 0) {

                con.commit();
                return true;

            }

        } catch (Exception e) {

            e.printStackTrace();

        }

        return false;

    }

}