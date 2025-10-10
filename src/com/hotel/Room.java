package com.hotel;

public class Room {
    private int roomNo;
    private String type;
    private double price;
    private boolean isAvail;

    public Room(int roomNo, String type, double price) {
        this.roomNo = roomNo;
        this.type = type;
        this.price = price;
        this.isAvail = true; // Rooms are available by default
    }

    public int getRoomNo() {
        return roomNo;
    }

    public String getType() {
        return type;
    }
    public double getPrice() {
        return price;
    }

    public boolean isAvailable() {
        return isAvail;
    }

    public void setAvail(boolean isAvail) {
        this.isAvail = isAvail;
    }

    public String toString() {
        return "Room No: " + roomNo + "| Type: " + type + "| Price: ₹" + price + "| Available: " + isAvail;
    }
}