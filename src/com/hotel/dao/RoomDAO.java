package com.hotel.dao;

import com.hotel.DBUtil;
import com.hotel.model.Room;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RoomDAO {

    /**
     * Searches for rooms that are physically available (available=TRUE in DB)
     * AND do not have a reservation that overlaps with the requested dates.
     * * @param hotelId The ID of the hotel to search in.
     * @param checkInDate The user's requested check-in date (e.g., "YYYY-MM-DD").
     * @param checkOutDate The user's requested check-out date (e.g., "YYYY-MM-DD").
     * @return A list of available Room objects.
     */
    public List<Room> searchAvailableRooms(int hotelId, String checkInDate, String checkOutDate) {
        List<Room> availableRooms = new ArrayList<>();

        // This SQL query is the core of the dynamic availability check:
        String sql =
                "SELECT r.room_id, r.hotel_id, r.room_no, r.type, r.price, r.available " +
                        "FROM rooms r " +
                        "WHERE r.hotel_id = ? AND r.available = TRUE AND r.room_id NOT IN (" +
                        "    SELECT room_id FROM reservations " +
                        "    WHERE " +
                        "        (? < check_out) AND (check_in < ?)" + // Logic for non-overlapping dates
                        ")";

        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = DBUtil.getConnection();
            if (conn == null) return availableRooms;

            ps = conn.prepareStatement(sql);
            ps.setInt(1, hotelId);          // Parameter 1: hotelId
            ps.setString(2, checkOutDate);  // Parameter 2: ? < check_out (User's check-out date is before existing reservation's check-out)
            ps.setString(3, checkInDate);   // Parameter 3: check_in < ? (Existing reservation's check-in is before user's check-in)

            rs = ps.executeQuery();

            while (rs.next()) {
                Room room = new Room(
                        rs.getInt("room_id"),
                        rs.getInt("hotel_id"),
                        rs.getInt("room_no"),
                        rs.getString("type"),
                        rs.getDouble("price"),
                        rs.getBoolean("available") // Should be TRUE based on WHERE clause
                );
                availableRooms.add(room);
            }

        } catch (SQLException e) {
            System.err.println("Error searching available rooms: " + e.getMessage());
        } finally {
            DBUtil.closeConnection(conn);
            try { if (rs != null) rs.close(); } catch (SQLException e) { /* ignore */ }
            try { if (ps != null) ps.close(); } catch (SQLException e) { /* ignore */ }
        }
        return availableRooms;
    }
}