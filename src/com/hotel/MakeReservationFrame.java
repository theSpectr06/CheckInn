package com.hotel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class MakeReservationFrame extends JFrame implements ActionListener {

    private User loggedInUser;
    private List<Hotel> hotels; // List to hold all hotels fetched from DB

    // UI Components
    private JComboBox<String> hotelComboBox;
    private JTextField checkInField; // Using JTextField for simple string date input
    private JTextField checkOutField;
    private JButton checkAvailabilityButton;
    private JButton backButton;

    private JTextArea resultsArea; // To display the search results
    private JButton nextButton; // To proceed to RoomDetailsFrame

    public MakeReservationFrame(User user) {
        this.loggedInUser = user;
        setTitle("CheckInn: Make Reservation");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(700, 600);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        // --- Fetch Hotels on Load ---
        HotelDAO hotelDAO = new HotelDAO();
        this.hotels = hotelDAO.getAllHotels();

        if (this.hotels.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No hotels found in the database. Cannot proceed.", "Data Error", JOptionPane.ERROR_MESSAGE);
            new DashboardFrame(loggedInUser);
            this.dispose();
            return;
        }

        // --- 1. Header ---
        JLabel header = new JLabel("Search Available Rooms", SwingConstants.CENTER);
        header.setFont(new Font("SansSerif", Font.BOLD, 20));
        add(header, BorderLayout.NORTH);

        // --- 2. Input Panel (Top Center) ---
        JPanel inputPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Hotel Dropdown
        gbc.gridx = 0; gbc.gridy = 0; inputPanel.add(new JLabel("Select Hotel:"), gbc);
        hotelComboBox = new JComboBox<>(getHotelNames());
        gbc.gridx = 1; gbc.gridy = 0; gbc.weightx = 1.0; inputPanel.add(hotelComboBox, gbc);

        // Check-in Date
        gbc.gridx = 2; gbc.gridy = 0; gbc.weightx = 0; inputPanel.add(new JLabel("Check-in (YYYY-MM-DD):"), gbc);
        checkInField = new JTextField("2026-02-01", 10);
        gbc.gridx = 3; gbc.gridy = 0; gbc.weightx = 1.0; inputPanel.add(checkInField, gbc);

        // Check-out Date
        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0; inputPanel.add(new JLabel("Check-out (YYYY-MM-DD):"), gbc);
        checkOutField = new JTextField("2026-02-05", 10);
        gbc.gridx = 1; gbc.gridy = 1; gbc.weightx = 1.0; inputPanel.add(checkOutField, gbc);

        // Availability Button
        checkAvailabilityButton = new JButton("Check Availability");
        checkAvailabilityButton.addActionListener(this);
        gbc.gridx = 2; gbc.gridy = 1; gbc.gridwidth = 2; gbc.weightx = 1.0;
        inputPanel.add(checkAvailabilityButton, gbc);

        add(inputPanel, BorderLayout.NORTH);

        // --- 3. Results Panel (Center) ---
        resultsArea = new JTextArea("Click 'Check Availability' to see available rooms.");
        resultsArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(resultsArea);
        add(scrollPane, BorderLayout.CENTER);

        // --- 4. Button Panel (South) ---
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));

        backButton = new JButton("Back to Dashboard");
        backButton.addActionListener(this);

        nextButton = new JButton("Next (Select Room Details)");
        nextButton.setEnabled(false); // Disabled until search results are loaded
        nextButton.addActionListener(this);

        buttonPanel.add(backButton);
        buttonPanel.add(nextButton);
        add(buttonPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    /** Helper method to get an array of hotel names for the JComboBox. */
    private String[] getHotelNames() {
        return hotels.stream().map(Hotel::getName).toArray(String[]::new);
    }

    /** Gets the selected Hotel object based on the JComboBox selection. */
    private Hotel getSelectedHotel() {
        int index = hotelComboBox.getSelectedIndex();
        return (index >= 0) ? hotels.get(index) : null;
    }

    private void handleNext() {
        if (lastAvailableRooms == null || lastAvailableRooms.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please perform a successful availability check first.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Open the RoomDetailsFrame, passing all collected data
        new RoomDetailsFrame(
                loggedInUser,
                lastAvailableRooms,
                lastSelectedHotel,
                lastCheckIn,
                lastCheckOut
        );
        this.dispose();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == backButton) {
            new DashboardFrame(loggedInUser);
            this.dispose();
        } else if (e.getSource() == checkAvailabilityButton) {
            handleCheckAvailability();
        } else if (e.getSource() == nextButton) {
            handleNext();
        }
    }

    /**
     * Handles fetching available rooms from the DB based on user input.
     */
    // Store the rooms and criteria so we can pass them to the next frame
    private List<Room> lastAvailableRooms;
    private String lastCheckIn;
    private String lastCheckOut;
    private Hotel lastSelectedHotel;

    private void handleCheckAvailability() {
        Hotel selectedHotel = getSelectedHotel();
        String checkIn = checkInField.getText().trim();
        String checkOut = checkOutField.getText().trim();

        if (selectedHotel == null || checkIn.isEmpty() || checkOut.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please select a hotel and enter both dates.", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Basic Date Format Validation (YYYY-MM-DD) and Logic Check
        try {
            // Use simple string comparison for date validation (as used in Reservation model)
            if (checkOut.compareTo(checkIn) <= 0) {
                throw new Exception("Check-out date must be after check-in date.");
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Invalid Date Range: " + ex.getMessage(), "Date Error", JOptionPane.ERROR_MESSAGE);
            resultsArea.setText("Search failed. Please correct dates.");
            nextButton.setEnabled(false);
            return;
        }

        try {
            RoomDAO roomDAO = new RoomDAO();
            List<Room> availableRooms = roomDAO.searchAvailableRooms(
                    selectedHotel.getHotelId(), checkIn, checkOut
            );

            // Store the results for the next frame
            this.lastAvailableRooms = availableRooms;
            this.lastCheckIn = checkIn;
            this.lastCheckOut = checkOut;
            this.lastSelectedHotel = selectedHotel; // Store the hotel object

            displayResults(availableRooms, checkIn, checkOut);

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Database error during room search.", "System Error", JOptionPane.ERROR_MESSAGE);
            resultsArea.setText("System error during search.");
            nextButton.setEnabled(false);
            ex.printStackTrace();
        }
    }

    /** Formats and displays the results in the JTextArea. */
    private void displayResults(List<Room> rooms, String checkIn, String checkOut) {
        StringBuilder sb = new StringBuilder();
        sb.append("--- Search Results ---\n");
        sb.append("Hotel: ").append(getSelectedHotel().getName()).append("\n");
        sb.append("Dates: ").append(checkIn).append(" to ").append(checkOut).append("\n\n");

        if (rooms.isEmpty()) {
            sb.append("Sorry, no rooms are available for your selected criteria.");
            nextButton.setEnabled(false);
        } else {
            sb.append(String.format("%-5s | %-10s | %-15s | %-10s\n", "ID", "Room No", "Type", "Price"));
            sb.append("--------------------------------------------------\n");

            for (Room room : rooms) {
                // Display the Room ID (PK) as a reference for the next step!
                sb.append(String.format("%-5d | %-10d | %-15s | ₹%.2f\n",
                        room.getRoomId(),
                        room.getRoomNo(),
                        room.getType(),
                        room.getPrice()));
            }
            nextButton.setEnabled(true);
            sb.append("\nTotal rooms found: ").append(rooms.size()).append(". Press 'Next' to select a room.");
        }
        resultsArea.setText(sb.toString());
    }
}