package com.hotel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class DashboardFrame extends JFrame implements ActionListener {

    private User loggedInUser;

    private JButton addReservationButton;
    private JButton viewReservationsButton;
    private JButton logoutButton;

    public DashboardFrame(User user) {
        this.loggedInUser = user;
        setTitle("CheckInn: Dashboard - Welcome, " + user.getName());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 350);
        setResizable(false);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        // --- 1. Welcome Header ---
        JLabel welcomeLabel = new JLabel("Welcome, " + user.getName() + "!", SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("SansSerif", Font.BOLD, 24));
        add(welcomeLabel, BorderLayout.NORTH);

        // --- 2. Button Panel (Center) ---
        // Use BoxLayout for a vertical column of buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50)); // Padding

        // Initialize Buttons
        addReservationButton = new JButton("Add New Reservation (Book Room)");
        viewReservationsButton = new JButton("View My Reservations");
        logoutButton = new JButton("Logout");

        // Style Buttons for uniform size and spacing
        Dimension buttonSize = new Dimension(300, 40);
        addReservationButton.setMaximumSize(buttonSize);
        viewReservationsButton.setMaximumSize(buttonSize);
        logoutButton.setMaximumSize(buttonSize);

        // Add Action Listeners
        addReservationButton.addActionListener(this);
        viewReservationsButton.addActionListener(this);
        logoutButton.addActionListener(this);

        // Add buttons and glue for centering
        buttonPanel.add(Box.createVerticalGlue());
        buttonPanel.add(addReservationButton);
        buttonPanel.add(Box.createRigidArea(new Dimension(0, 15))); // Vertical spacing
        buttonPanel.add(viewReservationsButton);
        buttonPanel.add(Box.createRigidArea(new Dimension(0, 30))); // More spacing for logout
        buttonPanel.add(logoutButton);
        buttonPanel.add(Box.createVerticalGlue());

        add(buttonPanel, BorderLayout.CENTER);

        setVisible(true);
    }

    /**
     * Handles button clicks for navigation.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == addReservationButton) {
            handleNewReservation();
        } else if (e.getSource() == viewReservationsButton) {
            handleViewReservations();
        } else if (e.getSource() == logoutButton) {
            handleLogout();
        }
    }

    // --- Navigation Handlers ---

    private void handleNewReservation() {
        // Step 4 on the roadmap: Open MakeReservationFrame
        new MakeReservationFrame(loggedInUser);
        this.dispose();
    }

    private void handleViewReservations() {
        // Step 7 on the roadmap: Open ViewReservationFrame
        new ViewReservationFrame(loggedInUser);
        this.dispose();
    }

    private void handleLogout() {
        // Navigation: Back to LoginFrame
        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to log out?",
                "Confirm Logout", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            new LoginFrame();
            this.dispose();
        }
    }

    // Placeholder frames need to be created for compilation
    public static void main(String[] args) {
        // Example for testing the dashboard directly
        SwingUtilities.invokeLater(() -> new DashboardFrame(
                new User(1, "TestUser", "test@test.com", "1234567890", "testpass")
        ));
    }
}