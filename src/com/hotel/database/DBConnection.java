package com.hotel.database;

import java.sql.*;

public class DBConnection {
    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/checkinn";
        String user = "root"; // your MySQL username
        String password = "47123663Moon"; // your MySQL password

        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            System.out.println("✅ Connected to MySQL successfully!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
