package com.hotel.dao;

import com.hotel.DBUtil;
import com.hotel.model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class UserDAO {

    /**
     * Inserts a new user record into the 'users' table.
     * @param user The User object containing registration data.
     * @return true if insertion was successful, false otherwise.
     */
    public boolean registerUser(User user) {
        // SQL statement for insertion. Using ? for prepared statement protection.
        String sql = "INSERT INTO users (name, email, phone, password) VALUES (?, ?, ?, ?)";

        // This array tells JDBC to return the auto-generated primary key (user_id).
        int generatedKeys = Statement.RETURN_GENERATED_KEYS;

        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            // 1. Get the connection
            conn = DBUtil.getConnection();
            if (conn == null) {
                System.err.println("DB connection failed in registerUser.");
                return false;
            }

            // 2. Create the prepared statement
            // The second argument is critical to retrieve the auto-generated ID
            ps = conn.prepareStatement(sql, generatedKeys);

            // 3. Set parameters for the SQL statement
            ps.setString(1, user.getName());
            ps.setString(2, user.getEmail());
            ps.setString(3, user.getPhone());
            // In a real system, you MUST hash the password here (e.g., using BCrypt)!
            ps.setString(4, user.getPassword());

            // 4. Execute the update (INSERT)
            int rowsAffected = ps.executeUpdate();

            if (rowsAffected > 0) {
                // 5. Retrieve the auto-generated user_id
                rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    int userId = rs.getInt(1);
                    user.setUserId(userId); // Update the user object with the new ID
                    System.out.println("User registered successfully with ID: " + userId);
                }
                return true;
            }

        } catch (SQLException e) {
            // Handle SQL errors, especially unique constraint violations (e.g., email already exists)
            System.err.println("Error during user registration: " + e.getMessage());
        } finally {
            // 6. Close resources in the reverse order of creation
            DBUtil.closeConnection(conn); // We rely on DBUtil to close the connection
            try { if (rs != null) rs.close(); } catch (SQLException e) { /* ignore */ }
            try { if (ps != null) ps.close(); } catch (SQLException e) { /* ignore */ }
        }
        return false;
    }

    /**
     * Validates user credentials for login.
     * @param email The user's email.
     * @param password The user's password.
     * @return A User object if credentials are valid, or null otherwise.
     */
    public User validateUser(String email, String password) {
        // Select all user fields where the email matches.
        String sql = "SELECT user_id, name, email, phone, password FROM users WHERE email = ?";

        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = DBUtil.getConnection();
            if (conn == null) {
                System.err.println("DB connection failed in validateUser.");
                return null;
            }

            ps = conn.prepareStatement(sql);
            ps.setString(1, email); // Set the email parameter

            rs = ps.executeQuery(); // Execute the SELECT query

            if (rs.next()) {
                // Found a user with that email. Now check the password.
                String storedPassword = rs.getString("password");

                // NOTE: In a real system, you'd use a hashing library (like BCrypt)
                // to compare the hash of the entered password with the stored hash.
                if (storedPassword.equals(password)) {
                    // Password matches, create and return the User object
                    int userId = rs.getInt("user_id");
                    String name = rs.getString("name");
                    String phone = rs.getString("phone");

                    return new User(userId, name, email, phone, storedPassword);
                }
            }

            // If rs.next() is false (email not found) or password doesn't match
            return null;

        } catch (SQLException e) {
            System.err.println("Error during user validation: " + e.getMessage());
            return null;
        } finally {
            DBUtil.closeConnection(conn);
            try { if (rs != null) rs.close(); } catch (SQLException e) { /* ignore */ }
            try { if (ps != null) ps.close(); } catch (SQLException e) { /* ignore */ }
        }
    }
}

