package com.asset.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBUtil {

	
    public static Connection getDBConnection() {

        try {

            // Load Oracle JDBC Driver
            Class.forName("oracle.jdbc.driver.OracleDriver");

            String url = "jdbc:oracle:thin:@10.58.16.28:1521:xe";
            String user = "system";
            String pass = "bavana123";
 
            Connection connection = DriverManager.getConnection(url, user, pass);

            connection.setAutoCommit(false);

            return connection;

        } catch (ClassNotFoundException | SQLException e) {

            e.printStackTrace();
            return null;

        }
    }
}