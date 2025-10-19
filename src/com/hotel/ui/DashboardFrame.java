package com.hotel.ui;

import com.hotel.model.User;

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
        setSize(500, 450);
        setResizable(false);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        // --- 1. Welcome Header with top padding ---
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBorder(BorderFactory.createEmptyBorder(30, 10, 20, 10)); // Top padding

        JLabel welcomeLabel = new JLabel("Welcome, " + user.getName() + "!", SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("SansSerif", Font.BOLD, 24));
        headerPanel.add(welcomeLabel, BorderLayout.CENTER);

        add(headerPanel, BorderLayout.NORTH);

        // --- 2. Button Panel (Center) ---
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50)); // Padding

        // Initialize Buttons with Icons
        addReservationButton = new JButton("Add New Reservation");
        viewReservationsButton = new JButton("View My Reservations");
        logoutButton = new JButton("Logout");

        // Add icons to buttons (using Unicode symbols as fallback)
        try {
            addReservationButton.setIcon(UIManager.getIcon("FileView.fileIcon"));
        } catch (Exception e) {
            addReservationButton.setText("➕ Add New Reservation (Book Room)");
        }

        try {
            viewReservationsButton.setIcon(UIManager.getIcon("FileView.directoryIcon"));
        } catch (Exception e) {
            viewReservationsButton.setText("📋 View My Reservations");
        }

        try {
            logoutButton.setIcon(UIManager.getIcon("InternalFrame.closeIcon"));
        } catch (Exception e) {
            logoutButton.setText("🚪 Logout");
        }

        // Style Buttons for uniform size, spacing, and colors
        Dimension buttonSize = new Dimension(350, 50); // Increased size

        // Add Reservation Button - Green
        addReservationButton.setMaximumSize(buttonSize);
        addReservationButton.setPreferredSize(buttonSize);
        addReservationButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        //addReservationButton.setBackground(new Color(76, 175, 80)); // Green
        addReservationButton.setForeground(Color.WHITE);
        //addReservationButton.setOpaque(true);
        addReservationButton.setBorderPainted(false);
        addReservationButton.setFocusPainted(false);
        addReservationButton.setFont(new Font("SansSerif", Font.BOLD, 14));

        // View Reservations Button - Dark Blue
        viewReservationsButton.setMaximumSize(buttonSize);
        viewReservationsButton.setPreferredSize(buttonSize);
        viewReservationsButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        //viewReservationsButton.setBackground(new Color(0, 51, 102)); // Dark Blue
        viewReservationsButton.setForeground(Color.WHITE);
        //viewReservationsButton.setOpaque(true);
        viewReservationsButton.setBorderPainted(false);
        viewReservationsButton.setFocusPainted(false);
        viewReservationsButton.setFont(new Font("SansSerif", Font.BOLD, 14));

        // Logout Button - Red
        logoutButton.setMaximumSize(buttonSize);
        logoutButton.setPreferredSize(buttonSize);
        logoutButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        //logoutButton.setBackground(new Color(183, 28, 28)); // Red
        logoutButton.setForeground(Color.WHITE);
        //logoutButton.setOpaque(true);
        logoutButton.setBorderPainted(false);
        logoutButton.setFocusPainted(false);
        logoutButton.setFont(new Font("SansSerif", Font.BOLD, 14));

        // Add Action Listeners
        addReservationButton.addActionListener(this);
        viewReservationsButton.addActionListener(this);
        logoutButton.addActionListener(this);

        // Add buttons with spacing - centered alignment
        buttonPanel.add(Box.createVerticalGlue());
        buttonPanel.add(addReservationButton);
        buttonPanel.add(Box.createRigidArea(new Dimension(0, 20))); // Vertical spacing
        buttonPanel.add(viewReservationsButton);
        buttonPanel.add(Box.createRigidArea(new Dimension(0, 20))); // Spacing
        buttonPanel.add(logoutButton);
        buttonPanel.add(Box.createVerticalGlue());

        add(buttonPanel, BorderLayout.CENTER);

        // --- 3. Footer Panel ---
        JPanel footerPanel = new JPanel();
        footerPanel.setLayout(new BoxLayout(footerPanel, BoxLayout.Y_AXIS));
        footerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 15, 10));

        JLabel copyrightLabel = new JLabel("Copyright CheckInn 2025", SwingConstants.CENTER);
        copyrightLabel.setFont(new Font("SansSerif", Font.PLAIN, 10));
        copyrightLabel.setForeground(Color.GRAY);
        copyrightLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        //JLabel madeByLabel = new JLabel("Made by theSpectr06 with ♥️, boredom and other cliche things", SwingConstants.CENTER);
        //madeByLabel.setFont(new Font("SansSerif", Font.ITALIC, 10));
        //madeByLabel.setForeground(Color.GRAY);
        //madeByLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        footerPanel.add(copyrightLabel);
        footerPanel.add(Box.createRigidArea(new Dimension(0, 3)));
        //footerPanel.add(madeByLabel);

        add(footerPanel, BorderLayout.SOUTH);

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