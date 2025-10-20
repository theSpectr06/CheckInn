package com.hotel;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBUtil {

    // Database credentials and configuration
    private static final String URL = "jdbc:mysql://localhost:3306/CheckInn";
    private static final String USER = System.getenv("DB_USER");
    private static final String PASSWORD = System.getenv("DB_PASSWORD");

    /**
     * Establishes a connection to the MySQL database.
     * @return A valid Connection object, or null if connection fails.
     */
    public static Connection getConnection() throws SQLException {
        // Essential check: Ensure the variables were set before trying to connect
        if (USER == null || PASSWORD == null) {
            System.err.println("FATAL ERROR: DB_USER or DB_PASSWORD environment variable is not set.");
            // Throwing an exception is better than returning null here
            throw new SQLException("Database credentials missing from environment setup.");
        }

        // This is where Java establishes the connection
        return DriverManager.getConnection(URL, USER, PASSWORD);
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
}