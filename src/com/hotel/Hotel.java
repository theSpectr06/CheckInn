package com.hotel;

import java.util.ArrayList;
import java.util.List;

import com.hotel.exceptions.InvalidReservationException;
import com.hotel.exceptions.RoomNotAvailableException;


public class Hotel {
    private String name;
    private String address;
    private List<Room> rooms;

    public Hotel(String name, String address) {
        this.name = name;
        this.address = address;
        this.rooms = new ArrayList<>();
    }

    public List<Room> getRooms() {
        return rooms;
    }

    public void addRoom(Room room) {
        rooms.add(room);
    }

    public void bookRoom(Room room, Guest guest, int reserveID, String inDate, String outDate) throws RoomNotAvailableException, InvalidReservationException {
            if (!room.isAvailable()) {
                throw new RoomNotAvailableException("Room " + room.getRoomNo() + " is already booked!");
            }
    
            // Create a reservation (Reservation class will mark room unavailable)
            Reservation res = new Reservation(reserveID, guest, room, inDate, outDate);
            System.out.println("Reservation successful: " + res);
        }


    public String toString() {
        return "Hotel Name: " + name + "| Address: " + address + "| Total Rooms: " + rooms.size();
    }

    // Search room by room number
    public Room searchRoomByNumber(int roomNumber) {
        for (Room room : rooms) {
            if (room.getRoomNo() == roomNumber) {
                return room;
            }
        }
        return null; // Not found
    }

    // Search rooms by type
    public List<Room> searchRoomsByType(String type) {
        List<Room> results = new ArrayList<>();
        for (Room room : rooms) {
            if (room.getType().equalsIgnoreCase(type)) {
                results.add(room);
            }
        }
        return results;
    }

    // Search available rooms
    public List<Room> searchAvailableRooms() {
        List<Room> results = new ArrayList<>();
        for (Room room : rooms) {
            if (room.isAvailable()) {
                results.add(room);
            }
        }
        return results;
    }
}