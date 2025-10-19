package com.hotel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.temporal.ChronoUnit;
import java.time.LocalDate;
import java.util.List;

public class RoomDetailsFrame extends JFrame implements ActionListener {

    private User loggedInUser;
    private List<Room> selectedRooms;
    private Hotel selectedHotel;
    private String checkInDateStr;
    private String checkOutDateStr;

    // UI Components
    private JTextArea summaryTextArea;
    private JButton nextButton;
    private JButton backButton;

    public RoomDetailsFrame(User user, List<Room> rooms, Hotel hotel, String inDate, String outDate) {
        this.loggedInUser = user;
        this.selectedRooms = rooms;
        this.selectedHotel = hotel;
        this.checkInDateStr = inDate;
        this.checkOutDateStr = outDate;

        setTitle("CheckInn: Room Booking Summary");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(650, 550);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(15, 15));

        // --- 1. Header ---
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBorder(BorderFactory.createEmptyBorder(20, 10, 15, 10));

        JLabel header = new JLabel("Booking Summary", SwingConstants.CENTER);
        header.setFont(new Font("SansSerif", Font.BOLD, 24));
        headerPanel.add(header, BorderLayout.CENTER);

        add(headerPanel, BorderLayout.NORTH);

        // --- 2. Summary Panel (Center) ---
        JPanel summaryPanel = new JPanel(new BorderLayout());
        summaryPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(10, 30, 10, 30),
                BorderFactory.createTitledBorder(
                        BorderFactory.createLineBorder(new Color(100, 100, 100), 2),
                        "Reservation Details",
                        0,
                        0,
                        new Font("SansSerif", Font.BOLD, 14)
                )
        ));

        summaryTextArea = new JTextArea();
        summaryTextArea.setEditable(false);
        summaryTextArea.setFont(new Font("Monospaced", Font.PLAIN, 13));
        summaryTextArea.setBackground(new Color(45, 45, 45));
        summaryTextArea.setForeground(Color.WHITE);
        summaryTextArea.setCaretColor(Color.WHITE);
        summaryTextArea.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        summaryTextArea.setLineWrap(true);
        summaryTextArea.setWrapStyleWord(true);

        JScrollPane scrollPane = new JScrollPane(summaryTextArea);
        scrollPane.setBorder(null);
        summaryPanel.add(scrollPane, BorderLayout.CENTER);

        add(summaryPanel, BorderLayout.CENTER);

        // --- 3. Button Panel (South) ---
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 25, 20));

        backButton = new JButton("Go Back (Change Selection)");
        backButton.addActionListener(this);
        backButton.setPreferredSize(new Dimension(210, 40));
        backButton.setFont(new Font("SansSerif", Font.BOLD, 13));

        nextButton = new JButton("Confirm & Proceed to Payment");
        nextButton.addActionListener(this);
        nextButton.setPreferredSize(new Dimension(240, 40));
        nextButton.setBackground(new Color(76, 175, 80));
        nextButton.setForeground(Color.WHITE);
        nextButton.setOpaque(true);
        nextButton.setBorderPainted(false);
        nextButton.setFont(new Font("SansSerif", Font.BOLD, 13));

        buttonPanel.add(backButton);
        buttonPanel.add(nextButton);
        add(buttonPanel, BorderLayout.SOUTH);

        displaySummary();

        setVisible(true);
    }

    /**
     * Calculates cost and displays the booking summary for multiple rooms.
     */
    private void displaySummary() {
        if (selectedRooms == null || selectedRooms.isEmpty()) {
            summaryTextArea.setText("Error: No rooms selected.");
            nextButton.setEnabled(false);
            return;
        }

        // Calculate the number of nights
        LocalDate inDate = LocalDate.parse(checkInDateStr);
        LocalDate outDate = LocalDate.parse(checkOutDateStr);
        long nights = ChronoUnit.DAYS.between(inDate, outDate);

        if (nights <= 0) {
            summaryTextArea.setText("Error: Invalid number of nights.");
            nextButton.setEnabled(false);
            return;
        }

        // Calculate total cost for all rooms
        double totalCostPerNight = 0;
        for (Room room : selectedRooms) {
            totalCostPerNight += room.getPrice();
        }
        double grandTotal = nights * totalCostPerNight;

        // Build the summary text
        StringBuilder summary = new StringBuilder();
        summary.append("═══════════════════════════════════════════════════════\n");
        summary.append("                    BOOKING DETAILS\n");
        summary.append("═══════════════════════════════════════════════════════\n\n");

        summary.append("Guest Name:           ").append(loggedInUser.getName()).append("\n");
        summary.append("Email:                ").append(loggedInUser.getEmail()).append("\n");
        summary.append("Phone:                ").append(loggedInUser.getPhone()).append("\n\n");

        summary.append("─────────────────────────────────────────────────────\n\n");

        summary.append("Hotel:                ").append(selectedHotel.getName()).append("\n");
        summary.append("Location:             ").append(selectedHotel.getAddress()).append("\n\n");

        summary.append("─────────────────────────────────────────────────────\n\n");

        summary.append("Number of Rooms:      ").append(selectedRooms.size()).append("\n\n");

        // List each room
        for (int i = 0; i < selectedRooms.size(); i++) {
            Room room = selectedRooms.get(i);
            summary.append(String.format("Room %d:\n", i + 1));
            summary.append(String.format("  - Room Number:      %d\n", room.getRoomNo()));
            summary.append(String.format("  - Room Type:        %s\n", room.getType()));
            summary.append(String.format("  - Price/Night:      ₹%.2f\n", room.getPrice()));
            if (i < selectedRooms.size() - 1) {
                summary.append("\n");
            }
        }

        summary.append("\n─────────────────────────────────────────────────────\n\n");

        summary.append("Check-In Date:        ").append(checkInDateStr).append("\n");
        summary.append("Check-Out Date:       ").append(checkOutDateStr).append("\n");
        summary.append("Number of Nights:     ").append(nights).append("\n\n");

        summary.append("─────────────────────────────────────────────────────\n\n");

        summary.append(String.format("Subtotal/Night:       ₹%.2f\n", totalCostPerNight));
        summary.append(String.format("Nights:               × %d\n\n", nights));

        summary.append("═══════════════════════════════════════════════════════\n\n");

        summary.append(String.format("TOTAL AMOUNT:         ₹%.2f\n\n", grandTotal));

        summary.append("═══════════════════════════════════════════════════════\n");

        summaryTextArea.setText(summary.toString());
        nextButton.setEnabled(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == backButton) {
            // Return to MakeReservationFrame
            new MakeReservationFrame(loggedInUser);
            this.dispose();
        } else if (e.getSource() == nextButton) {
            handleNext();
        }
    }

    private void handleNext() {
        // Calculate the final total cost
        LocalDate inDate = LocalDate.parse(checkInDateStr);
        LocalDate outDate = LocalDate.parse(checkOutDateStr);
        long nights = ChronoUnit.DAYS.between(inDate, outDate);

        double totalCost = 0;
        for (Room room : selectedRooms) {
            totalCost += nights * room.getPrice();
        }

        // Pass the list of rooms to PaymentSummaryFrame
        new PaymentSummaryFrame(
                loggedInUser,
                selectedHotel,
                selectedRooms, // Pass the full list of rooms
                checkInDateStr,
                checkOutDateStr,
                totalCost
        );
        this.dispose();
    }
}