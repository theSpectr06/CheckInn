package com.hotel;

import javax.swing.SwingUtilities;
// Import the specific dark L&F you want to use
import com.formdev.flatlaf.themes.FlatMacDarkLaf;
import com.hotel.ui.LoginFrame;

public class Main {
    public static void main(String[] args) {

        try {
            // Set the macOS-specific dark theme
            FlatMacDarkLaf.setup();
        } catch (Exception ex) {
            System.err.println("Failed to initialize Mac Dark look and feel: " + ex);
        }

        SwingUtilities.invokeLater(() -> {
            new LoginFrame();
        });
    }
}