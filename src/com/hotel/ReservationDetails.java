package com.hotel;

// Helper class to hold joined data for the View Reservations UI
public class ReservationDetails {
    private int resId;
    private String hotelName;
    private String roomType;
    private String checkInDate;
    private String checkOutDate;
    private double total;

    public ReservationDetails(int resId, String hotelName, String roomType,
                              String checkInDate, String checkOutDate, double total) {
        this.resId = resId;
        this.hotelName = hotelName;
        this.roomType = roomType;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
        this.total = total;
    }

    // --- Getters (Required for populating the Swing JTable later) ---
    public int getResId() { return resId; }
    public String getHotelName() { return hotelName; }
    public String getRoomType() { return roomType; }
    public String getCheckInDate() { return checkInDate; }
    public String getCheckOutDate() { return checkOutDate; }
    public double getTotal() { return total; }

    @Override
    public String toString() {
        return "ID: " + resId + "| Hotel: " + hotelName + "| Type: " + roomType +
                "| In: " + checkInDate + "| Out: " + checkOutDate + "| Total: ₹" + total;
    }
}