package com.hotel;

import java.util.ArrayList;
import java.util.List;

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

    public String toString() {
        return "Hotel Name: " + name + "| Address: " + address + "| Total Rooms: " + rooms.size();
    }
}