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
    private List<Room> availableRooms;
    private Hotel selectedHotel;
    private String checkInDateStr;
    private String checkOutDateStr;

    // UI Components
    private JComboBox<String> roomSelectionComboBox;
    private JLabel summaryLabel;
    private JButton nextButton; // Go to Payment Summary
    private JButton backButton;

    private Room selectedRoom; // The room the user selects

    public RoomDetailsFrame(User user, List<Room> rooms, Hotel hotel, String inDate, String outDate) {
        this.loggedInUser = user;
        this.availableRooms = rooms;
        this.selectedHotel = hotel;
        this.checkInDateStr = inDate;
        this.checkOutDateStr = outDate;

        setTitle("CheckInn: Select Room Details");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(550, 450);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        // --- 1. Header ---
        JLabel header = new JLabel("Final Room Selection and Summary", SwingConstants.CENTER);
        header.setFont(new Font("SansSerif", Font.BOLD, 20));
        add(header, BorderLayout.NORTH);

        // --- 2. Input Panel (Center Top) ---
        JPanel inputPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));

        // Populate room selection dropdown
        String[] roomOptions = availableRooms.stream()
                .map(r -> String.format("Room %d (%s) - ₹%.2f/night", r.getRoomNo(), r.getType(), r.getPrice()))
                .toArray(String[]::new);

        roomSelectionComboBox = new JComboBox<>(roomOptions);
        roomSelectionComboBox.addActionListener(this); // Listen for changes to update summary

        inputPanel.add(new JLabel("Select Room:"));
        inputPanel.add(roomSelectionComboBox);
        add(inputPanel, BorderLayout.NORTH);

        // --- 3. Summary Panel (Center) ---
        summaryLabel = new JLabel("Select a room to see the total cost.", SwingConstants.CENTER);
        summaryLabel.setVerticalAlignment(SwingConstants.TOP);
        summaryLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        summaryLabel.setFont(new Font("Monospaced", Font.PLAIN, 14));

        add(new JScrollPane(summaryLabel), BorderLayout.CENTER);

        // --- 4. Button Panel (South) ---
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));

        backButton = new JButton("Go Back (Change Dates)");
        backButton.addActionListener(this);

        nextButton = new JButton("Next: Payment Summary");
        nextButton.addActionListener(this);
        nextButton.setEnabled(true); // Enabled by default, as rooms are passed in

        buttonPanel.add(backButton);
        buttonPanel.add(nextButton);
        add(buttonPanel, BorderLayout.SOUTH);

        // Initial summary display
        updateSummary();

        setVisible(true);
    }

    /** Calculates cost and updates the summary display based on the selected room. */
    private void updateSummary() {
        int selectedIndex = roomSelectionComboBox.getSelectedIndex();
        if (selectedIndex == -1) {
            summaryLabel.setText("No rooms available for selection.");
            selectedRoom = null;
            nextButton.setEnabled(false);
            return;
        }

        selectedRoom = availableRooms.get(selectedIndex);

        // Calculate the number of nights
        LocalDate inDate = LocalDate.parse(checkInDateStr);
        LocalDate outDate = LocalDate.parse(checkOutDateStr);
        // Use ChronoUnit to calculate the difference in days
        long nights = ChronoUnit.DAYS.between(inDate, outDate);

        if (nights <= 0) { // Should be prevented by MakeReservationFrame, but safety check
            summaryLabel.setText("Error: Invalid number of nights.");
            nextButton.setEnabled(false);
            return;
        }

        double roomPrice = selectedRoom.getPrice();
        double totalCost = nights * roomPrice;

        // Build the summary string
        String summary = String.format(
                "<html><div style='width: 400px; padding: 10px;'>" +
                        "<b>Booking Details:</b><br><hr>" +
                        "Hotel: <b>%s</b><br>" +
                        "Room: <b>%d</b> (%s)<br>" +
                        "Price per Night: <b>₹%.2f</b><br><br>" +

                        "Check-In: <b>%s</b><br>" +
                        "Check-Out: <b>%s</b><br>" +
                        "Nights: <b>%d</b><br><hr>" +

                        "<b>ESTIMATED TOTAL:</b> <b>₹%.2f</b>" +
                        "</div></html>",
                selectedHotel.getName(),
                selectedRoom.getRoomNo(),
                selectedRoom.getType(),
                roomPrice,
                checkInDateStr,
                checkOutDateStr,
                nights,
                totalCost
        );

        summaryLabel.setText(summary);
        nextButton.setEnabled(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == backButton) {
            // Return to MakeReservationFrame, preserving the user data
            new MakeReservationFrame(loggedInUser);
            this.dispose();
        } else if (e.getSource() == roomSelectionComboBox) {
            // Update summary when a different room is selected
            updateSummary();
        } else if (e.getSource() == nextButton) {
            // Proceed to the Payment Summary Frame
            handleNext();
        }
    }

    private void handleNext() {
        if (selectedRoom == null) {
            JOptionPane.showMessageDialog(this, "Please select a room first.", "Selection Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Re-calculate the final total cost
        LocalDate inDate = LocalDate.parse(checkInDateStr);
        LocalDate outDate = LocalDate.parse(checkOutDateStr);
        long nights = ChronoUnit.DAYS.between(inDate, outDate);
        double totalCost = nights * selectedRoom.getPrice();

        // Pass ALL necessary data to the Payment Summary Frame
        new PaymentSummaryFrame(
                loggedInUser,
                selectedHotel,
                selectedRoom,
                checkInDateStr,
                checkOutDateStr,
                totalCost
        );
        this.dispose();
    }
}