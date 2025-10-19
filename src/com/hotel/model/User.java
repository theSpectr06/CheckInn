package com.hotel.model;

public class User {
    // Matches 'user_id' in the DB (will be set after INSERT)
    private int userId;
    private String name;
    private String email;
    private String phone;
    private String password; // Added for registration/login

    // Constructor for new registration (ID will be set by DB)
    public User(String name, String email, String phone, String password) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.password = password;
    }

    // Constructor for loading from DB (when all fields including ID are known)
    public User(int userId, String name, String email, String phone, String password) {
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.password = password;
    }

    // --- Getters and Setters ---

    // Getters for DAO
    public int getUserId() { return userId; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getPhone() { return phone; }
    public String getPassword() { return password; }

    // Setter for the DB to update the ID after insertion
    public void setUserId(int userId) { this.userId = userId; }

    @Override
    public String toString() {
        return "User ID: " + userId + "| Name: " + name + "| Email: " + email + "| Phone: " + phone;
    }
}