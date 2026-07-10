package com.asset.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.asset.bean.Checkout;
import com.asset.util.DBUtil;

public class CheckoutDAO {

    Connection con = null;
    PreparedStatement ps = null;
    ResultSet rs = null;

    // Generate Checkout ID
    public int generateCheckoutID() {

        int id = 910001;

        try {

            con = DBUtil.getDBConnection();

            String query = "SELECT MAX(CHECKOUTID) FROM CHECKOUT_TBL";

            ps = con.prepareStatement(query);

            rs = ps.executeQuery();

            if (rs.next()) {

                id = rs.getInt(1);

                if (id == 0)
                    id = 910001;
                else
                    id++;

            }

        } catch (Exception e) {

            e.printStackTrace();

        }

        return id;

    }

    // Record Checkout
    public boolean recordCheckout(Checkout checkout) {

        try {

            con = DBUtil.getDBConnection();

            String query = "INSERT INTO CHECKOUT_TBL VALUES(?,?,?,?,?,?,?)";

            ps = con.prepareStatement(query);

            ps.setInt(1, checkout.getCheckoutID());
            ps.setString(2, checkout.getAssetTag());
            ps.setString(3, checkout.getEmployeeID());

            ps.setDate(4, new java.sql.Date(checkout.getCheckoutDate().getTime()));
            ps.setDate(5, new java.sql.Date(checkout.getDueDate().getTime()));

            ps.setDate(6, null);

            ps.setString(7, checkout.getStatus());

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

    // Update Checkout Status
    public boolean updateCheckoutStatusAndReturnDate(int checkoutID,
            String status,
            Date returnDate) {

        try {

            con = DBUtil.getDBConnection();

            String query = "UPDATE CHECKOUT_TBL SET STATUS=?, RETURNDATE=? WHERE CHECKOUTID=?";

            ps = con.prepareStatement(query);

            ps.setString(1, status);

            ps.setDate(2, new java.sql.Date(returnDate.getTime()));

            ps.setInt(3, checkoutID);

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

    // Find Active Checkouts
    public List<Checkout> findActiveCheckoutsByAsset(String assetTag) {

        List<Checkout> list = new ArrayList<>();

        try {

            con = DBUtil.getDBConnection();

            String query = "SELECT * FROM CHECKOUT_TBL WHERE ASSETTAG=? AND STATUS IN('OUT','LOST','DAMAGED')";

            ps = con.prepareStatement(query);

            ps.setString(1, assetTag);

            rs = ps.executeQuery();

            while (rs.next()) {

                Checkout c = new Checkout();

                c.setCheckoutID(rs.getInt("CHECKOUTID"));
                c.setAssetTag(rs.getString("ASSETTAG"));
                c.setEmployeeID(rs.getString("EMPLOYEEID"));
                c.setCheckoutDate(rs.getDate("CHECKOUTDATE"));
                c.setDueDate(rs.getDate("DUEDATE"));
                c.setReturnDate(rs.getDate("RETURNDATE"));
                c.setStatus(rs.getString("STATUS"));

                list.add(c);

            }

        } catch (Exception e) {

            e.printStackTrace();

        }

        return list;

    }

    // Find Checkout By ID
    public Checkout findCheckoutByID(int checkoutID) {

        Checkout checkout = null;

        try {

            con = DBUtil.getDBConnection();

            String query = "SELECT * FROM CHECKOUT_TBL WHERE CHECKOUTID=?";

            ps = con.prepareStatement(query);

            ps.setInt(1, checkoutID);

            rs = ps.executeQuery();

            if (rs.next()) {

                checkout = new Checkout();

                checkout.setCheckoutID(rs.getInt("CHECKOUTID"));
                checkout.setAssetTag(rs.getString("ASSETTAG"));
                checkout.setEmployeeID(rs.getString("EMPLOYEEID"));
                checkout.setCheckoutDate(rs.getDate("CHECKOUTDATE"));
                checkout.setDueDate(rs.getDate("DUEDATE"));
                checkout.setReturnDate(rs.getDate("RETURNDATE"));
                checkout.setStatus(rs.getString("STATUS"));

            }

        } catch (Exception e) {

            e.printStackTrace();

        }

        return checkout;

    }

}