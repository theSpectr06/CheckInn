package com.hotel;

import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        // Use SwingUtilities.invokeLater to ensure thread safety
        // All Swing work should happen on the Event Dispatch Thread (EDT)
        SwingUtilities.invokeLater(() -> {
            new LoginFrame(); // Start the application with the Login Frame
        });
    }
}