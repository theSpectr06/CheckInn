package com.hotel.dao;

import com.hotel.DBUtil;
import com.hotel.model.Hotel;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class HotelDAO {

    /**
     * Retrieves all registered hotels from the database.
     * @return A list of Hotel objects.
     */
    public List<Hotel> getAllHotels() {
        List<Hotel> hotels = new ArrayList<>();
        String sql = "SELECT hotel_id, name, address FROM hotels";

        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = DBUtil.getConnection();
            if (conn == null) {
                System.err.println("DB connection failed in getAllHotels.");
                return hotels;
            }

            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();

            // Iterate through the results and create Hotel objects
            while (rs.next()) {
                int id = rs.getInt("hotel_id");
                String name = rs.getString("name");
                String address = rs.getString("address");

                Hotel hotel = new Hotel(id, name, address);
                hotels.add(hotel);
            }

        } catch (SQLException e) {
            System.err.println("Error retrieving hotels: " + e.getMessage());
        } finally {
            // Close resources
            DBUtil.closeConnection(conn);
            try { if (rs != null) rs.close(); } catch (SQLException e) { /* ignore */ }
            try { if (ps != null) ps.close(); } catch (SQLException e) { /* ignore */ }
        }
        return hotels;
    }

    // You could add a getHotelById(int id) method here if needed later
}