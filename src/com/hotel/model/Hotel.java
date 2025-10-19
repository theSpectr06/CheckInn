package com.hotel.model;

public class Hotel {
    private int hotelId;      // New field to match the DB
    private String name;
    private String address;


    // Constructor for loading from DB
    public Hotel(int hotelId, String name, String address) {
        this.hotelId = hotelId;
        this.name = name;
        this.address = address;
    }

    // Constructor for new hotel creation (if you needed it)
    public Hotel(String name, String address) {
        this.name = name;
        this.address = address;
    }

    // --- Getters ---
    public int getHotelId() { return hotelId; }
    public String getName() { return name; }
    public String getAddress() { return address; }

    // --- Setters (optional, but good practice) ---
    public void setHotelId(int hotelId) { this.hotelId = hotelId; }

    @Override
    public String toString() {
        return "Hotel ID: " + hotelId + "| Name: " + name + "| Address: " + address;
    }
}