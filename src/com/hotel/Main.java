package com.hotel;

import com.hotel.exceptions.RoomNotAvailableException;
import com.hotel.exceptions.InvalidReservationException;

public class Main {
    public static void main(String[] args) {

        // Create a hotel
        Hotel hotel = new Hotel("Grand Plaza", "Cityville");

        // Add rooms to the hotel
        Room r1 = new Room(101, "Single", 2000.0);
        Room r2 = new Room(102, "Double", 3500.0);
        Room r3 = new Room(201, "Suite", 5000.0);
        hotel.addRoom(r1);
        hotel.addRoom(r2);
        hotel.addRoom(r3);

        // Create guests
        Guest guest1 = new Guest("santhan", "ss@gmail.com", "9876543210");

        // Attempt reservations with exception handling
        try {
            // First reservation (should succeed)
            hotel.bookRoom(r1, guest1, 1, "2025-10-01", "2025-10-05");

            // Attempt to double-book the same room (should throw RoomNotAvailableException)
            hotel.bookRoom(r1, guest1, 2, "2025-10-06", "2025-10-10");

        } catch (RoomNotAvailableException e) {
            System.out.println("Booking failed: " + e.getMessage());
        } catch (InvalidReservationException e) {
            System.out.println("Invalid reservation: " + e.getMessage());
        }

        System.out.println("\nHotel summary:");
        System.out.println(hotel);
        System.out.println("Rooms list: ");
        for (Room r : hotel.getRooms()) {
            System.out.println(r);
        }
    }
}
