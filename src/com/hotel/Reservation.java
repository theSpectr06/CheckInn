package com.hotel;

import com.hotel.exceptions.InvalidReservationException;


public class Reservation {
    private int reserveID;
    private Guest guest;
    private Room room;
    private String InDate;
    private String OutDate;

    public Reservation(int reserveID, Guest guest, Room room, String InDate, String OutDate) 
    throws InvalidReservationException {

        if (OutDate.compareTo(InDate) < 0) {  // simple string comparison; you can replace with Date later
                    throw new InvalidReservationException("Check-out date cannot be before check-in date!");
                }
        
        this.reserveID = reserveID;
        this.guest = guest;
        this.room = room;
        this.InDate = InDate;
        this.OutDate = OutDate;
        room.setAvail(false); // Mark the room as unavailable when reserved
    }

    public String getReserve() {
        return "Reservation ID: " + reserveID + "| " + guest.toString() + "| " + room.toString() + "| Check-In: " + InDate + "| Check-Out: " + OutDate;
    }

    @Override
    public String toString() { return getReserve();
    }

    public int getReserveID() { return reserveID; }
    public Guest getGuest() { return guest; }
    public Room getRoom() { return room; }
    public String getInDate() { return InDate; }
    public String getOutDate() { return OutDate; }
}