package com.asset.app;

import java.util.Date;
import java.util.List;
import java.util.Scanner;

import com.asset.bean.Asset;
import com.asset.service.AssetService;
import com.asset.util.ActiveCheckoutExistsException;
import com.asset.util.AssetNotAvailableException;
import com.asset.util.ValidationException;

public class AssetMain {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        AssetService assetService = new AssetService();

        int choice;

        do {

            System.out.println("\n===== IT Asset Management System =====");
            System.out.println("1. Add New Asset");
            System.out.println("2. View Asset Details");
            System.out.println("3. View All Assets");
            System.out.println("4. Checkout Asset");
            System.out.println("5. Return Asset");
            System.out.println("6. Mark Asset Lost/Damaged");
            System.out.println("7. Remove Asset");
            System.out.println("8. Exit");
            System.out.print("Enter Choice : ");

            choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {

            case 1:

                try {

                    Asset asset = new Asset();

                    System.out.print("Asset Tag : ");
                    asset.setAssetTag(sc.nextLine());

                    System.out.print("Category : ");
                    asset.setCategory(sc.nextLine());

                    System.out.print("Model : ");
                    asset.setModel(sc.nextLine());

                    System.out.print("Serial No : ");
                    asset.setSerialNo(sc.nextLine());

                    System.out.print("Total Quantity : ");
                    asset.setTotalQuantity(sc.nextInt());

                    System.out.print("Available Quantity : ");
                    asset.setAvailableQuantity(sc.nextInt());

                    asset.setStatus("ACTIVE");

                    boolean result = assetService.addNewAsset(asset);

                    if (result)
                        System.out.println("Asset Added Successfully");
                    else
                        System.out.println("Asset Already Exists");

                } catch (ValidationException e) {

                    System.out.println(e);

                }

                break;

            case 2:

                try {

                    System.out.print("Enter Asset Tag : ");
                    String tag = sc.next();

                    Asset asset = assetService.viewAssetDetails(tag);

                    if (asset == null)

                        System.out.println("Asset Not Found");

                    else

                        System.out.println(asset);

                } catch (ValidationException e) {

                    System.out.println(e);

                }

                break;

            case 3:

                List<Asset> list = assetService.viewAllAssets();

                for (Asset a : list)

                    System.out.println(a);

                break;

            case 4:

                try {

                    System.out.print("Asset Tag : ");
                    String assetTag = sc.next();

                    System.out.print("Employee ID : ");
                    String emp = sc.next();

                    boolean result = assetService.checkoutAsset(assetTag, emp,
                            new Date(), new Date());

                    if (result)

                        System.out.println("Checkout Successful");

                    else

                        System.out.println("Checkout Failed");

                }

                catch (ValidationException e) {

                    System.out.println(e);

                }

                catch (AssetNotAvailableException e) {

                    System.out.println(e);

                }

                break;

            case 5:

                try {

                    System.out.print("Checkout ID : ");

                    int id = sc.nextInt();

                    boolean result = assetService.returnAsset(id, new Date());

                    if (result)

                        System.out.println("Asset Returned");

                    else

                        System.out.println("Return Failed");

                }

                catch (ValidationException e) {

                    System.out.println(e);

                }

                break;

            case 6:

                try {

                    System.out.print("Checkout ID : ");

                    int id = sc.nextInt();

                    System.out.print("Status (LOST/DAMAGED): ");

                    String status = sc.next();

                    boolean result = assetService.markAssetLostOrDamaged(id,
                            status,
                            new Date());

                    if (result)

                        System.out.println("Updated Successfully");

                    else

                        System.out.println("Update Failed");

                }

                catch (ValidationException e) {

                    System.out.println(e);

                }

                break;

            case 7:

                try {

                    System.out.print("Asset Tag : ");

                    String tag = sc.next();

                    boolean result = assetService.removeAsset(tag);

                    if (result)

                        System.out.println("Asset Removed");

                    else

                        System.out.println("Remove Failed");

                }

                catch (ValidationException e) {

                    System.out.println(e);

                }

                catch (ActiveCheckoutExistsException e) {

                    System.out.println(e);

                }

                break;

            case 8:

                System.out.println("Thank You...");

                break;

            default:

                System.out.println("Invalid Choice");

            }

        } while (choice != 8);

        sc.close();

    }

}