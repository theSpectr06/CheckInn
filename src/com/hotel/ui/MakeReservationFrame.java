package com.hotel.ui;

import com.hotel.model.*;
import com.hotel.dao.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;
import com.toedter.calendar.JDateChooser;

public class MakeReservationFrame extends JFrame implements ActionListener {

    private User loggedInUser;
    private List<Hotel> hotels;

    // UI Components
    private JComboBox<String> hotelComboBox;
    private JDateChooser checkInDateChooser;
    private JDateChooser checkOutDateChooser;
    private JSpinner roomCountSpinner;
    private JButton checkAvailabilityButton;
    private JButton backButton;

    private JTable resultsTable;
    private DefaultTableModel tableModel;
    private JButton nextButton;

    // Store the rooms and criteria
    private Map<String, List<Room>> roomsByType;
    private String lastCheckIn;
    private String lastCheckOut;
    private Hotel lastSelectedHotel;
    private int requestedRoomCount;

    public MakeReservationFrame(User user) {
        this.loggedInUser = user;
        setTitle("CheckInn: Make Reservation");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(800, 650);
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
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 10, 10, 10));

        JLabel header = new JLabel("Search Available Rooms", SwingConstants.CENTER);
        header.setFont(new Font("SansSerif", Font.BOLD, 20));
        headerPanel.add(header, BorderLayout.CENTER);

        add(headerPanel, BorderLayout.NORTH);

        // --- 2. Input Panel ---
        JPanel inputPanel = new JPanel(new GridBagLayout());
        inputPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Hotel Dropdown
        gbc.gridx = 0; gbc.gridy = 0; gbc.anchor = GridBagConstraints.EAST;
        inputPanel.add(new JLabel("Select Hotel:"), gbc);

        hotelComboBox = new JComboBox<>(getHotelNames());
        gbc.gridx = 1; gbc.gridy = 0; gbc.weightx = 1.0; gbc.anchor = GridBagConstraints.WEST;
        inputPanel.add(hotelComboBox, gbc);

        // Number of Rooms Spinner
        gbc.gridx = 2; gbc.gridy = 0; gbc.weightx = 0; gbc.anchor = GridBagConstraints.EAST;
        inputPanel.add(new JLabel("Rooms Needed:"), gbc);

        SpinnerNumberModel spinnerModel = new SpinnerNumberModel(1, 1, 10, 1);
        roomCountSpinner = new JSpinner(spinnerModel);
        roomCountSpinner.setPreferredSize(new Dimension(80, 30));
        ((JSpinner.DefaultEditor) roomCountSpinner.getEditor()).getTextField().setHorizontalAlignment(JTextField.CENTER);
        gbc.gridx = 3; gbc.gridy = 0; gbc.weightx = 0; gbc.anchor = GridBagConstraints.WEST;
        inputPanel.add(roomCountSpinner, gbc);

        // Check-in Date
        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0; gbc.anchor = GridBagConstraints.EAST;
        inputPanel.add(new JLabel("Check-in Date:"), gbc);

        checkInDateChooser = new JDateChooser();
        checkInDateChooser.setDateFormatString("yyyy-MM-dd");
        checkInDateChooser.setPreferredSize(new Dimension(200, 30));
        checkInDateChooser.setForeground(Color.WHITE);
        checkInDateChooser.setDate(new Date());
        styleJDateChooser(checkInDateChooser);

        gbc.gridx = 1; gbc.gridy = 1; gbc.weightx = 1.0; gbc.anchor = GridBagConstraints.WEST;
        inputPanel.add(checkInDateChooser, gbc);

        // Check-out Date
        gbc.gridx = 2; gbc.gridy = 1; gbc.weightx = 0; gbc.anchor = GridBagConstraints.EAST;
        inputPanel.add(new JLabel("Check-out Date:"), gbc);

        checkOutDateChooser = new JDateChooser();
        checkOutDateChooser.setDateFormatString("yyyy-MM-dd");
        checkOutDateChooser.setPreferredSize(new Dimension(200, 30));
        checkOutDateChooser.setForeground(Color.WHITE);
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_MONTH, 1);
        checkOutDateChooser.setDate(cal.getTime());
        styleJDateChooser(checkOutDateChooser);

        gbc.gridx = 3; gbc.gridy = 1; gbc.weightx = 1.0; gbc.anchor = GridBagConstraints.WEST;
        inputPanel.add(checkOutDateChooser, gbc);

        // Availability Button
        checkAvailabilityButton = new JButton("Check Availability");
        checkAvailabilityButton.addActionListener(this);
        checkAvailabilityButton.setPreferredSize(new Dimension(180, 35));
        checkAvailabilityButton.setBackground(new Color(76, 175, 80));
        checkAvailabilityButton.setForeground(Color.WHITE);
        checkAvailabilityButton.setOpaque(true);
        checkAvailabilityButton.setBorderPainted(false);
        checkAvailabilityButton.setFont(new Font("SansSerif", Font.BOLD, 13));
        gbc.gridx = 1; gbc.gridy = 2; gbc.gridwidth = 2; gbc.weightx = 0; gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(15, 8, 8, 8);
        inputPanel.add(checkAvailabilityButton, gbc);

        add(inputPanel, BorderLayout.NORTH);

        // --- 3. Results Panel ---
        JPanel resultsPanel = new JPanel(new BorderLayout());
        resultsPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.GRAY),
                "Available Room Types - Select One"));

        // Create table model
        String[] columnNames = {"Room Type", "Available Rooms", "Price per Night"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        resultsTable = new JTable(tableModel);
        resultsTable.setRowHeight(35);
        resultsTable.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 13));
        resultsTable.setFont(new Font("SansSerif", Font.PLAIN, 12));
        resultsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Set column widths
        resultsTable.getColumnModel().getColumn(0).setPreferredWidth(250);
        resultsTable.getColumnModel().getColumn(1).setPreferredWidth(150);
        resultsTable.getColumnModel().getColumn(2).setPreferredWidth(150);

        // Add selection listener
        resultsTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                nextButton.setEnabled(resultsTable.getSelectedRow() != -1);
            }
        });

        JScrollPane scrollPane = new JScrollPane(resultsTable);
        resultsPanel.add(scrollPane, BorderLayout.CENTER);

        add(resultsPanel, BorderLayout.CENTER);

        // --- 4. Button Panel ---
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 15));

        backButton = new JButton("Back to Dashboard");
        backButton.addActionListener(this);
        backButton.setPreferredSize(new Dimension(180, 35));
        backButton.setFont(new Font("SansSerif", Font.BOLD, 13));

        nextButton = new JButton("Next (View Room Details)");
        nextButton.setEnabled(false);
        nextButton.addActionListener(this);
        nextButton.setPreferredSize(new Dimension(200, 35));
        nextButton.setBackground(new Color(0, 51, 102));
        nextButton.setForeground(Color.WHITE);
        nextButton.setOpaque(true);
        nextButton.setBorderPainted(false);
        nextButton.setFont(new Font("SansSerif", Font.BOLD, 13));

        buttonPanel.add(backButton);
        buttonPanel.add(nextButton);
        add(buttonPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    private String[] getHotelNames() {
        return hotels.stream().map(Hotel::getName).toArray(String[]::new);
    }

    private void styleJDateChooser(JDateChooser dateChooser) {
        JTextField dateTextField = (JTextField) dateChooser.getDateEditor().getUiComponent();

        dateTextField.setForeground(Color.WHITE);
        dateTextField.setBackground(new Color(45, 45, 45));
        dateTextField.setForeground(Color.WHITE);
        dateTextField.setFont(new Font("SansSerif", Font.PLAIN, 13));
        dateTextField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(100, 100, 100), 1),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        dateTextField.setCaretColor(Color.WHITE);

        Component[] components = dateChooser.getComponents();
        for (Component comp : components) {
            if (comp instanceof JButton) {
                JButton calendarButton = (JButton) comp;
                calendarButton.setBackground(new Color(76, 175, 80));
                calendarButton.setForeground(Color.WHITE);
                calendarButton.setBorder(BorderFactory.createLineBorder(new Color(76, 175, 80), 1));
                calendarButton.setFocusPainted(false);
                calendarButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
                break;
            }
        }
    }

    private Hotel getSelectedHotel() {
        int index = hotelComboBox.getSelectedIndex();
        return (index >= 0) ? hotels.get(index) : null;
    }

    private void handleNext() {
        int selectedRow = resultsTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a room type from the table.", "Selection Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String selectedRoomType = (String) tableModel.getValueAt(selectedRow, 0);
        List<Room> roomsOfType = roomsByType.get(selectedRoomType);

        if (roomsOfType == null || roomsOfType.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Error: Selected room type not available.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Check if enough rooms are available
        if (roomsOfType.size() < requestedRoomCount) {
            JOptionPane.showMessageDialog(this,
                    String.format("Only %d room(s) of type '%s' are available, but you requested %d.",
                            roomsOfType.size(), selectedRoomType, requestedRoomCount),
                    "Insufficient Rooms",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Select the first N rooms of the selected type
        List<Room> selectedRooms = roomsOfType.subList(0, requestedRoomCount);

        // Open RoomDetailsFrame with selected rooms
        new RoomDetailsFrame(
                loggedInUser,
                selectedRooms,
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

    private void handleCheckAvailability() {
        Hotel selectedHotel = getSelectedHotel();
        Date checkInDate = checkInDateChooser.getDate();
        Date checkOutDate = checkOutDateChooser.getDate();
        requestedRoomCount = (Integer) roomCountSpinner.getValue();

        if (selectedHotel == null || checkInDate == null || checkOutDate == null) {
            JOptionPane.showMessageDialog(this, "Please select a hotel and enter both dates.", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String checkIn = sdf.format(checkInDate);
        String checkOut = sdf.format(checkOutDate);

        if (checkOutDate.compareTo(checkInDate) <= 0) {
            JOptionPane.showMessageDialog(this, "Check-out date must be after check-in date.", "Date Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Calendar today = Calendar.getInstance();
        today.set(Calendar.HOUR_OF_DAY, 0);
        today.set(Calendar.MINUTE, 0);
        today.set(Calendar.SECOND, 0);
        today.set(Calendar.MILLISECOND, 0);

        if (checkInDate.before(today.getTime())) {
            JOptionPane.showMessageDialog(this, "Check-in date cannot be in the past.", "Date Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            RoomDAO roomDAO = new RoomDAO();
            List<Room> availableRooms = roomDAO.searchAvailableRooms(
                    selectedHotel.getHotelId(), checkIn, checkOut
            );

            this.lastCheckIn = checkIn;
            this.lastCheckOut = checkOut;
            this.lastSelectedHotel = selectedHotel;

            displayResults(availableRooms);

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Database error during room search.", "System Error", JOptionPane.ERROR_MESSAGE);
            nextButton.setEnabled(false);
            ex.printStackTrace();
        }
    }

    private void displayResults(List<Room> rooms) {
        tableModel.setRowCount(0);
        nextButton.setEnabled(false);

        if (rooms.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Sorry, no rooms are available for your selected criteria.",
                    "No Results",
                    JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        // Group rooms by type
        roomsByType = new LinkedHashMap<>();
        for (Room room : rooms) {
            roomsByType.computeIfAbsent(room.getType(), k -> new ArrayList<>()).add(room);
        }

        // Add each room type as a row
        for (Map.Entry<String, List<Room>> entry : roomsByType.entrySet()) {
            String roomType = entry.getKey();
            List<Room> roomsOfType = entry.getValue();
            double price = roomsOfType.get(0).getPrice();

            Object[] rowData = {
                    roomType,
                    roomsOfType.size(),
                    String.format("₹%.2f", price)
            };
            tableModel.addRow(rowData);
        }

        JOptionPane.showMessageDialog(this,
                String.format("Found %d available room(s) across %d room type(s).\nRequesting %d room(s). Please select a room type.",
                        rooms.size(), roomsByType.size(), requestedRoomCount),
                "Search Complete",
                JOptionPane.INFORMATION_MESSAGE);
    }
}