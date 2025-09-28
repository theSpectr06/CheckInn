package com.hotel;

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

        Reservation res1 = new Reservation(1, guest1, r1, "2025-10-01", "2025-10-05");

        System.out.println(hotel);
        System.out.println("Rooms list: ");
        for (Room r : hotel.getRooms()) {
            System.out.println(r);
        }
    }
}