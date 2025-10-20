package com.hotel.ui;

import com.hotel.model.ReservationDetails;
import com.hotel.model.User;
import com.hotel.dao.ReservationDAO;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class ViewReservationFrame extends JFrame {

    private User loggedInUser;
    private JTable reservationTable;
    private DefaultTableModel tableModel;

    public ViewReservationFrame(User user) {
        this.loggedInUser = user;
        setTitle("CheckInn: Reservations for " + user.getName());
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(700, 400);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        // --- 1. Header ---
        JLabel header = new JLabel("My Booking History", SwingConstants.CENTER);
        header.setFont(new Font("SansSerif", Font.BOLD, 24));
        add(header, BorderLayout.NORTH);

        // --- 2. Table Setup ---
        String[] columnNames = {"Res ID", "Hotel Name", "Room Type", "Check-in", "Check-out", "Total (₹)"};

        // Use a DefaultTableModel to hold the data
        tableModel = new DefaultTableModel(columnNames, 0) {
            // Override isCellEditable to make the table read-only
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        reservationTable = new JTable(tableModel);

        // Add table to a JScrollPane for scrolling
        JScrollPane scrollPane = new JScrollPane(reservationTable);
        add(scrollPane, BorderLayout.CENTER);

        // --- 3. Button Panel ---
        JButton backButton = new JButton("Back");
        backButton.addActionListener(e -> {
            new DashboardFrame(loggedInUser);
            this.dispose();
        });

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(backButton);
        add(buttonPanel, BorderLayout.SOUTH);

        // Load data immediately upon frame creation
        loadReservations();

        setVisible(true);
    }

    /**
     * Fetches reservation data from the database and populates the JTable.
     */
    private void loadReservations() {
        ReservationDAO reservationDAO = new ReservationDAO();

        // Call the DAO method using the current user's ID
        List<ReservationDetails> reservations = reservationDAO.getReservationsByUserId(loggedInUser.getUserId());

        if (reservations.isEmpty()) {
            JOptionPane.showMessageDialog(this, "You have no reservations currently booked.", "No Bookings", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        // Clear any existing data in the table model
        tableModel.setRowCount(0);

        // Iterate through the list and add each ReservationDetails object as a row
        for (ReservationDetails details : reservations) {
            Object[] rowData = new Object[] {
                    details.getResId(),
                    details.getHotelName(),
                    details.getRoomType(),
                    details.getCheckInDate(),
                    details.getCheckOutDate(),
                    String.format("%.2f", details.getTotal()) // Format total for display
            };
            tableModel.addRow(rowData);
        }
    }

    public static void main(String[] args) {
        // Example for testing the view frame directly
        SwingUtilities.invokeLater(() -> new ViewReservationFrame(
                new User(1, "Alice Smith", "alice.smith@example.com", "1234567890", "securePass123")
        ));
    }
}