package com.hotel.model;

import com.hotel.exceptions.InvalidReservationException;

public class Reservation {
    private int resId;
    private int userId;
    private int roomId;
    private String checkInDate;
    private String checkOutDate;
    private double total;

    // Constructor for creating a NEW reservation
    public Reservation(int userId, int roomId, String checkInDate, String checkOutDate, double total)
            throws InvalidReservationException {
        // Validation check (Keep your initial business logic!)
        if (checkOutDate.compareTo(checkInDate) <= 0) { // checkOutDate cannot be <= checkInDate
            throw new InvalidReservationException("Check-out date must be after check-in date!");
        }

        this.userId = userId;
        this.roomId = roomId;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
        this.total = total;
    }

    // Constructor for loading EXISTING reservation from DB (in ViewReservationsFrame)
    public Reservation(int resId, int userId, int roomId, String checkInDate, String checkOutDate, double total) {
        this.resId = resId;
        this.userId = userId;
        this.roomId = roomId;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
        this.total = total;
    }

    // --- Getters and Setters ---
    public int getResId() { return resId; }
    public int getUserId() { return userId; }
    public int getRoomId() { return roomId; }
    public String getCheckInDate() { return checkInDate; }
    public String getCheckOutDate() { return checkOutDate; }
    public double getTotal() { return total; }

    public void setResId(int resId) { this.resId = resId; }

    @Override
    public String toString() {
        return "Reservation ID: " + resId + "| User ID: " + userId + "| Room ID: " + roomId +
                "| Check-In: " + checkInDate + "| Check-Out: " + checkOutDate + "| Total: ₹" + total;
    }
}