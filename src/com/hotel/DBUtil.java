package com.hotel;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBUtil {

    // Database credentials and configuration
    private static final String URL = "jdbc:mysql://localhost:3306/CheckInn";
    private static final String USER = "root";
    private static final String PASSWORD = "47123663Moon";

    /**
     * Establishes a connection to the MySQL database.
     * @return A valid Connection object, or null if connection fails.
     */
    public static Connection getConnection() {
        Connection connection = null;
        try {
            // 1. Load the MySQL JDBC Driver (Optional for modern JDBC but good practice)
            // Class.forName("com.mysql.cj.jdbc.Driver");

            // 2. Establish the connection
            connection = DriverManager.getConnection(URL, USER, PASSWORD);

        } catch (SQLException e) {
            // Handle exceptions for failed connection attempts
            System.err.println("Database Connection Failed! Check connection URL, username, and password.");
            System.err.println("SQL State: " + e.getSQLState());
            System.err.println("Error Code: " + e.getErrorCode());
            e.printStackTrace();
        }
        return connection;
    }

    /**
     * Closes the database connection.
     * @param connection The Connection object to close.
     */
    public static void closeConnection(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                System.err.println("Failed to close database connection.");
                e.printStackTrace();
            }
        }
    }

    // You can add methods here for common tasks like executeQuery or executeUpdate later
}