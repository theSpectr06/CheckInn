package com.hotel;

import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        // Create hotel
        Hotel hotel = new Hotel("Grand Plaza", "Cityville");

        // Lists to hold guests and reservations
        List<Guest> guests = new ArrayList<>();
        List<Reservation> reservations = new ArrayList<>();

        // Load data from CSVs
        DataManager.loadData(hotel, guests, reservations);

        // Print summary
        System.out.println("\nHotel summary:");
        System.out.println(hotel);

        System.out.println("\nRooms list:");
        for (Room r : hotel.getRooms()) {
            System.out.println(r);
        }

        System.out.println("\nGuests list:");
        for (Guest g : guests) {
            System.out.println(g);
        }

        System.out.println("\nReservations list:");
        for (Reservation res : reservations) {
            System.out.println(res);
        }

        // Test search functionality
        System.out.println("\nSearch by room number (102):");
        Room found = hotel.searchRoomByNumber(102);
        System.out.println(found != null ? found : "Room not found.");

        System.out.println("\nSearch available rooms:");
        for (Room r : hotel.searchAvailableRooms()) {
            System.out.println(r);
        }
    }
}
