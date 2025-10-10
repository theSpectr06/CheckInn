package com.hotel;

import com.hotel.exceptions.InvalidReservationException;
import java.util.*;
import java.io.*;

public class DataManager {
    private static final String ROOMS_FILE = "src/com/hotel/data/rooms.csv";
    private static final String GUESTS_FILE = "src/com/hotel/data/guests.csv";
    private static final String RES_FILE = "src/com/hotel/data/reservations.csv";

    // Load all data from CSVs
    public static void loadData(Hotel hotel, List<Guest> guests, List<Reservation> reservations) {
        loadRooms(hotel);
        loadGuests(guests);
        loadReservations(hotel, guests, reservations);
    }

    // ----------- ROOMS -----------
    private static void loadRooms(Hotel hotel) {
        List<String[]> rows = CSVutils.readCSV(ROOMS_FILE);
        for (String[] row : rows) {
            try {
                int roomNumber = Integer.parseInt(row[0]);
                String type = row[1];
                double price = Double.parseDouble(row[2]);
                boolean avail = Boolean.parseBoolean(row[3]);

                Room room = new Room(roomNumber, type, price);
                room.setAvail(avail);
                hotel.addRoom(room);
            } catch (Exception e) {
                System.out.println("Error loading room: " + Arrays.toString(row));
            }
        }
    }

    public static void saveRoom(Room room) {
        String[] data = {
            String.valueOf(room.getRoomNo()),
            room.getType(),
            String.valueOf(room.getPrice()),
            String.valueOf(room.isAvailable())
        };
        CSVutils.appendCSV(ROOMS_FILE, data);
    }

    // ----------- GUESTS -----------
    private static void loadGuests(List<Guest> guests) {
        List<String[]> rows = CSVutils.readCSV(GUESTS_FILE);
        for (String[] row : rows) {
            try {
                Guest g = new Guest(row[0], row[1], row[2]);
                guests.add(g);
            } catch (Exception e) {
                System.out.println("Error loading guest: " + Arrays.toString(row));
            }
        }
    }

    public static void saveGuest(Guest guest) {
        String[] data = { guest.getName(), guest.getEmail(), guest.getPhone() };
        CSVutils.appendCSV(GUESTS_FILE, data);
    }

    // ----------- RESERVATIONS -----------
    private static void loadReservations(Hotel hotel, List<Guest> guests, List<Reservation> reservations) {
        List<String[]> rows = CSVutils.readCSV(RES_FILE);
        for (String[] row : rows) {
            try {
                int resID = Integer.parseInt(row[0]);
                String guestEmail = row[1];
                int roomNumber = Integer.parseInt(row[2]);
                String checkIn = row[3];
                String checkOut = row[4];

                Guest guest = guests.stream()
                                    .filter(g -> g.getEmail().equalsIgnoreCase(guestEmail))
                                    .findFirst().orElse(null);
                Room room = hotel.searchRoomByNumber(roomNumber);

                if (guest != null && room != null) {
                    Reservation res = new Reservation(resID, guest, room, checkIn, checkOut);
                    reservations.add(res);
                }
            } catch (InvalidReservationException e) {
                System.out.println("Invalid reservation: " + e.getMessage());
            } catch (Exception e) {
                System.out.println("Error loading reservation: " + Arrays.toString(row));
            }
        }
    }

    public static void saveReservation(Reservation res) {
        String[] data = {
            String.valueOf(res.getReserveID()),
            res.getGuest().getEmail(),
            String.valueOf(res.getRoom().getRoomNo()),
            res.getInDate(),
            res.getOutDate()
        };
        CSVutils.appendCSV(RES_FILE, data);
    }
}
