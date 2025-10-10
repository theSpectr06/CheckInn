package com.hotel;

public class Guest {
    private String name;
    private String email;
    private String phone;

    public Guest(String name, String email, String phone) {
        this.name = name;
        this.email = email;
        this.phone = phone;
    }

    public String getName() {
        return name;
    }
    public String getEmail() {
        return email;
    }
    public String getPhone() {
        return phone;
    }

    public String toString() {
        return "Guest Name: " + name + "| Email: " + email + "| Phone: " + phone;
    }
}