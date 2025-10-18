package com.hotel;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ReservationDAO {

    /**
     * Executes the reservation and updates room status in a single transaction.
     * * @param reservation The Reservation object to be created.
     * @return The generated reservation ID if successful, or -1 if the transaction failed.
     */
    public int createReservation(Reservation reservation) {

        // SQL 1: Insert the reservation record
        String insertSql = "INSERT INTO reservations (user_id, room_id, check_in, check_out, total) VALUES (?, ?, ?, ?, ?)";

        // SQL 2: Update the room status
        String updateRoomSql = "UPDATE rooms SET available = FALSE WHERE room_id = ?";

        Connection conn = null;
        PreparedStatement psInsert = null;
        PreparedStatement psUpdate = null;
        ResultSet rs = null;
        int newResId = -1;

        try {
            conn = DBUtil.getConnection();
            if (conn == null) return -1;

            // --- 1. START TRANSACTION ---
            conn.setAutoCommit(false); // Disable auto-commit to control the transaction boundary

            // --- 2. INSERT RESERVATION ---
            psInsert = conn.prepareStatement(insertSql, Statement.RETURN_GENERATED_KEYS);
            psInsert.setInt(1, reservation.getUserId());
            psInsert.setInt(2, reservation.getRoomId());
            psInsert.setString(3, reservation.getCheckInDate());
            psInsert.setString(4, reservation.getCheckOutDate());
            psInsert.setDouble(5, reservation.getTotal());

            int rowsInserted = psInsert.executeUpdate();

            if (rowsInserted > 0) {
                rs = psInsert.getGeneratedKeys();
                if (rs.next()) {
                    newResId = rs.getInt(1);
                    reservation.setResId(newResId);
                    System.out.println("Reservation record inserted. ID: " + newResId);
                }
            } else {
                throw new SQLException("Failed to insert reservation record.");
            }

            // --- 3. UPDATE ROOM STATUS ---
            psUpdate = conn.prepareStatement(updateRoomSql);
            psUpdate.setInt(1, reservation.getRoomId());

            int rowsUpdated = psUpdate.executeUpdate();

            if (rowsUpdated != 1) {
                // This means the room wasn't found or was already updated, which is a potential issue.
                throw new SQLException("Failed to update room availability status (Rows updated: " + rowsUpdated + ")");
            }
            System.out.println("Room status updated to unavailable.");

            // --- 4. COMMIT TRANSACTION ---
            conn.commit(); // If both operations succeeded, make changes permanent
            System.out.println("Transaction committed successfully.");

            return newResId;

        } catch (SQLException e) {
            System.err.println("Transaction failed: " + e.getMessage());
            try {
                if (conn != null) {
                    conn.rollback(); // Undo all changes if anything went wrong
                    System.out.println("Transaction rolled back.");
                }
            } catch (SQLException rollbackEx) {
                System.err.println("Rollback failed: " + rollbackEx.getMessage());
            }
        } finally {
            // --- 5. CLEAN UP & RESET ---
            try { if (rs != null) rs.close(); } catch (SQLException e) { /* ignore */ }
            try { if (psInsert != null) psInsert.close(); } catch (SQLException e) { /* ignore */ }
            try { if (psUpdate != null) psUpdate.close(); } catch (SQLException e) { /* ignore */ }
            try { if (conn != null) conn.setAutoCommit(true); } catch (SQLException e) { /* ignore */ } // IMPORTANT: reset to default
            DBUtil.closeConnection(conn);
        }
        return -1; // Return -1 on failure
    }

    public List<ReservationDetails> getReservationsByUserId(int userId) {
        List<ReservationDetails> reservations = new ArrayList<>();

        // SQL query uses INNER JOIN to link the three tables: reservations -> rooms -> hotels
        String sql =
                "SELECT " +
                        "    r.res_id, " +
                        "    h.name AS hotel_name, " + // Get Hotel Name
                        "    room.type AS room_type, " + // Get Room Type
                        "    r.check_in, " +
                        "    r.check_out, " +
                        "    r.total " +
                        "FROM reservations r " +
                        "INNER JOIN rooms room ON r.room_id = room.room_id " +
                        "INNER JOIN hotels h ON room.hotel_id = h.hotel_id " +
                        "WHERE r.user_id = ?";

        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = DBUtil.getConnection();
            if (conn == null) return reservations;

            ps = conn.prepareStatement(sql);
            ps.setInt(1, userId);

            rs = ps.executeQuery();

            while (rs.next()) {
                ReservationDetails details = new ReservationDetails(
                        rs.getInt("res_id"),
                        rs.getString("hotel_name"),
                        rs.getString("room_type"),
                        rs.getString("check_in"),
                        rs.getString("check_out"),
                        rs.getDouble("total")
                );
                reservations.add(details);
            }

        } catch (SQLException e) {
            System.err.println("Error retrieving user reservations: " + e.getMessage());
        } finally {
            DBUtil.closeConnection(conn);
            try { if (rs != null) rs.close(); } catch (SQLException e) { /* ignore */ }
            try { if (ps != null) ps.close(); } catch (SQLException e) { /* ignore */ }
        }
        return reservations;
    }
}