package com.hotel;

// This class now represents a row in the 'rooms' table
public class Room {
    private int roomId;       // Matches 'room_id' in DB (PK)
    private int hotelId;      // Matches 'hotel_id' in DB (FK)
    private int roomNo;
    private String type;
    private double price;
    private boolean isAvailable; // Field to hold the dynamic availability status

    // Constructor for loading from DB
    public Room(int roomId, int hotelId, int roomNo, String type, double price, boolean isAvailable) {
        this.roomId = roomId;
        this.hotelId = hotelId;
        this.roomNo = roomNo;
        this.type = type;
        this.price = price;
        this.isAvailable = isAvailable; // This will be set by the DAO query
    }

    // --- Getters ---
    public int getRoomId() { return roomId; }
    public int getHotelId() { return hotelId; }
    public int getRoomNo() { return roomNo; }
    public String getType() { return type; }
    public double getPrice() { return price; }
    public boolean isAvailable() { return isAvailable; }

    // NOTE: We keep setAvail() as a setter to be used by ReservationDAO later.
    public void setAvailable(boolean isAvailable) {
        this.isAvailable = isAvailable;
    }

    @Override
    public String toString() {
        return "Room ID: " + roomId + "| Room No: " + roomNo +
                "| Type: " + type + "| Price: ₹" + price +
                "| Hotel ID: " + hotelId + "| Available: " + isAvailable;
    }
}