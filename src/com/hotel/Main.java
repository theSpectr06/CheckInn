package com.hotel;

import java.sql.Connection;
import java.util.List;
import com.hotel.exceptions.InvalidReservationException;

public class Main {
    public static void main(String[] args) {

        // --- JDBC CONNECTION TEST (Keep this part) ---
        System.out.println("Attempting to connect to the database...");
        Connection conn = DBUtil.getConnection();

        if (conn != null) {
            System.out.println("✅ Database connection successful!");
            DBUtil.closeConnection(conn);

            // --- USER REGISTRATION TEST ---
            System.out.println("\n--- Testing User Registration ---");
            UserDAO userDAO = new UserDAO();

            // Test Case 1: Successful Registration
            User newUser1 = new User("Alice Smith", "alice.smith@example.com", "1112223333", "securePass123");
            boolean success1 = userDAO.registerUser(newUser1);
            System.out.println("Registration success (Alice)? " + success1);
            System.out.println("User object now has ID: " + newUser1.getUserId());

            // Test Case 2: Failure (Unique email violation, if Alice was successful)
            User newUser2 = new User("Bob Jones", "alice.smith@example.com", "4445556666", "password2");
            boolean success2 = userDAO.registerUser(newUser2);
            System.out.println("Registration success (Bob, same email)? " + success2);

        } else {
            System.out.println("❌ Database connection failed. Cannot proceed with data interaction.");
        }
        System.out.println("\n------------------------------------");

        // --- USER VALIDATION (LOGIN) TEST ---
        System.out.println("\n--- Testing User Login (Validation) ---");
        UserDAO userDAO = new UserDAO();

        // Use the credentials of the successfully registered user (e.g., Alice Smith)
        String testEmail = "alice.smith@example.com";
        String testPass = "securePass123";

        // Test Case 1: Successful Login
        User loggedInUser = userDAO.validateUser(testEmail, testPass);

        if (loggedInUser != null) {
            System.out.println("✅ Login Successful! Welcome: " + loggedInUser.getName());
            // This is the object you'd pass to the DashboardFrame
        } else {
            System.out.println("❌ Login Failed! Invalid credentials.");
        }

        // Test Case 2: Failed Login (Wrong Password)
        User failedLogin = userDAO.validateUser(testEmail, "wrongpassword");
        System.out.println("Login Failed (Wrong Pass)? " + (failedLogin == null ? "Yes" : "No"));

        // Test Case 3: Failed Login (Wrong Email)
        User failedLogin2 = userDAO.validateUser("ghost@hotel.com", testPass);
        System.out.println("Login Failed (Wrong Email)? " + (failedLogin2 == null ? "Yes" : "No"));

        System.out.println("\n------------------------------------");

        // --- HOTEL DAO TEST ---
        System.out.println("\n--- Testing Hotel DAO ---");
        HotelDAO hotelDAO = new HotelDAO();
        List<Hotel> hotels = hotelDAO.getAllHotels();

        if (!hotels.isEmpty()) {
            System.out.println("✅ Hotels Loaded Successfully. Count: " + hotels.size());
            for (Hotel h : hotels) {
                System.out.println(h);
            }
        } else {
            System.out.println("❌ No hotels loaded. Check hotel table prepopulation.");
        }
        System.out.println("\n------------------------------------");

        // --- ROOM DAO TEST ---
        System.out.println("\n--- Testing Room DAO (Availability Search) ---");

        // Use the ID for "The Gramauld Place" (assuming ID 1)
        int testHotelId = 1;

        // Dates that should find all physically available rooms at The Gramauld Place
        // (102, 301)
        String testCheckIn = "2026-01-10";
        String testCheckOut = "2026-01-15";

        RoomDAO roomDAO = new RoomDAO();
        List<Room> availableRooms = roomDAO.searchAvailableRooms(
                testHotelId, testCheckIn, testCheckOut
        );

        if (!availableRooms.isEmpty()) {
            System.out.println("✅ Found " + availableRooms.size() +
                    " available rooms for Hotel ID " + testHotelId +
                    " from " + testCheckIn + " to " + testCheckOut + ":");
            for (Room r : availableRooms) {
                System.out.println(r);
            }
        } else {
            System.out.println("❌ No rooms available for the selected criteria.");
        }

        System.out.println("\n------------------------------------");

        // --- RESERVATION DAO TEST (BOOKING TRANSACTION) ---
        System.out.println("\n--- Testing Reservation DAO (Booking Transaction) ---");

        // 1. Define the input data (assuming a successful login user ID and an available Room ID)
        int testUserId = 1;      // Assume Alice Smith (ID 1)
        int testRoomId = 2;      // Assume Room 102 from Hotel 1 (check your DB for correct ID)
        String checkIn = "2025-12-01";
        String checkOut = "2025-12-05";
        double totalCost = 8000.00; // 4 nights * 2000.00 price

        ReservationDAO reservationDAO = new ReservationDAO();

        try {
            // Create the reservation object
            Reservation newReservation = new Reservation(testUserId, testRoomId, checkIn, checkOut, totalCost);

            // Attempt the booking transaction
            int bookingId = reservationDAO.createReservation(newReservation);

            if (bookingId != -1) {
                System.out.println("✅ Booking SUCCESSFUL! Reservation ID: " + bookingId);
            } else {
                System.out.println("❌ Booking FAILED! Check error messages above.");
            }

        } catch (InvalidReservationException e) {
            System.out.println("❌ Booking failed due to invalid dates: " + e.getMessage());
        }

        System.out.println("\n------------------------------------");

        // --- RESERVATION VIEW TEST ---
        System.out.println("\n--- Testing Reservation View Logic ---");

        //testUserId = 1; // Assuming Alice Smith (ID 1)
        reservationDAO = new ReservationDAO();

        List<ReservationDetails> userReservations = reservationDAO.getReservationsByUserId(testUserId);

        if (!userReservations.isEmpty()) {
            System.out.println("✅ Found " + userReservations.size() + " reservation(s) for User ID " + testUserId + ":");
            // Display the results exactly as they will look in the table
            System.out.println("-------------------------------------------------------------------------------------------------------------------");
            System.out.printf("%-5s | %-25s | %-10s | %-10s | %-10s | %-8s\n",
                    "ID", "Hotel", "Room Type", "Check-in", "Check-out", "Total");
            System.out.println("-------------------------------------------------------------------------------------------------------------------");

            for (ReservationDetails details : userReservations) {
                System.out.printf("%-5d | %-25s | %-10s | %-10s | %-10s | ₹%.2f\n",
                        details.getResId(),
                        details.getHotelName(),
                        details.getRoomType(),
                        details.getCheckInDate(),
                        details.getCheckOutDate(),
                        details.getTotal());
            }
            System.out.println("-------------------------------------------------------------------------------------------------------------------");

        } else {
            System.out.println("❌ No reservations found for User ID " + testUserId);
        }

        System.out.println("\n------------------------------------");
    }
}