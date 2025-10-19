package com.hotel.ui;

import com.hotel.model.*;
import com.hotel.dao.*;

import com.hotel.exceptions.InvalidReservationException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

public class PaymentSummaryFrame extends JFrame implements ActionListener {

    private User loggedInUser;
    private Hotel selectedHotel;
    private List<Room> selectedRooms;
    private String checkInDateStr;
    private String checkOutDateStr;
    private double totalCost;

    private JTextArea summaryArea;
    private JButton confirmBookingButton;
    private JButton cancelButton;

    // Constructor for multiple rooms
    public PaymentSummaryFrame(User user, Hotel hotel, List<Room> rooms, String inDate, String outDate, double total) {
        this.loggedInUser = user;
        this.selectedHotel = hotel;
        this.selectedRooms = rooms;
        this.checkInDateStr = inDate;
        this.checkOutDateStr = outDate;
        this.totalCost = total;

        initializeFrame();
    }

    // Backward compatibility constructor for single room
    public PaymentSummaryFrame(User user, Hotel hotel, Room room, String inDate, String outDate, double total) {
        this.loggedInUser = user;
        this.selectedHotel = hotel;
        this.selectedRooms = new ArrayList<>();
        this.selectedRooms.add(room);
        this.checkInDateStr = inDate;
        this.checkOutDateStr = outDate;
        this.totalCost = total;

        initializeFrame();
    }

    private void initializeFrame() {
        setTitle("CheckInn: Final Payment Summary");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(650, 550);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(15, 15));

        // --- 1. Header ---
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBorder(BorderFactory.createEmptyBorder(20, 10, 15, 10));

        JLabel header = new JLabel("Confirm Your Booking", SwingConstants.CENTER);
        header.setFont(new Font("SansSerif", Font.BOLD, 24));
        headerPanel.add(header, BorderLayout.CENTER);

        add(headerPanel, BorderLayout.NORTH);

        // --- 2. Summary Area (Center) ---
        JPanel summaryPanel = new JPanel(new BorderLayout());
        summaryPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(10, 30, 10, 30),
                BorderFactory.createTitledBorder(
                        BorderFactory.createLineBorder(new Color(100, 100, 100), 2),
                        "Final Payment Summary",
                        0,
                        0,
                        new Font("SansSerif", Font.BOLD, 14)
                )
        ));

        summaryArea = new JTextArea();
        summaryArea.setEditable(false);
        summaryArea.setFont(new Font("Monospaced", Font.PLAIN, 13));
        summaryArea.setBackground(new Color(45, 45, 45));
        summaryArea.setForeground(Color.WHITE);
        summaryArea.setCaretColor(Color.WHITE);
        summaryArea.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        summaryArea.setLineWrap(true);
        summaryArea.setWrapStyleWord(true);

        displaySummary();

        JScrollPane scrollPane = new JScrollPane(summaryArea);
        scrollPane.setBorder(null);
        summaryPanel.add(scrollPane, BorderLayout.CENTER);

        add(summaryPanel, BorderLayout.CENTER);

        // --- 3. Button Panel (South) ---
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 25, 20));

        cancelButton = new JButton("Cancel & Back to Dashboard");
        cancelButton.addActionListener(this);
        cancelButton.setPreferredSize(new Dimension(230, 40));
        cancelButton.setFont(new Font("SansSerif", Font.BOLD, 13));

        confirmBookingButton = new JButton("Confirm & Pay ₹" + String.format("%.2f", totalCost));
        confirmBookingButton.addActionListener(this);
        confirmBookingButton.setPreferredSize(new Dimension(230, 40));
        confirmBookingButton.setBackground(new Color(76, 175, 80));
        confirmBookingButton.setForeground(Color.WHITE);
        confirmBookingButton.setOpaque(true);
        confirmBookingButton.setBorderPainted(false);
        confirmBookingButton.setFont(new Font("SansSerif", Font.BOLD, 13));

        buttonPanel.add(cancelButton);
        buttonPanel.add(confirmBookingButton);
        add(buttonPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    /** Formats and displays all final booking details. */
    private void displaySummary() {
        long nights = ChronoUnit.DAYS.between(LocalDate.parse(checkInDateStr), LocalDate.parse(checkOutDateStr));

        StringBuilder summary = new StringBuilder();
        summary.append("═══════════════════════════════════════════════════════\n");
        summary.append("                  FINAL PAYMENT SUMMARY\n");
        summary.append("═══════════════════════════════════════════════════════\n\n");

        summary.append(String.format("Guest Name:           %s\n", loggedInUser.getName()));
        summary.append(String.format("Guest ID:             %d\n", loggedInUser.getUserId()));
        summary.append(String.format("Email:                %s\n", loggedInUser.getEmail()));
        summary.append(String.format("Phone:                %s\n\n", loggedInUser.getPhone()));

        summary.append("─────────────────────────────────────────────────────\n\n");

        summary.append(String.format("Hotel:                %s\n", selectedHotel.getName()));
        summary.append(String.format("Location:             %s\n\n", selectedHotel.getAddress()));

        summary.append("─────────────────────────────────────────────────────\n\n");

        summary.append(String.format("Number of Rooms:      %d\n\n", selectedRooms.size()));

        // List each room with details
        double subtotalPerNight = 0;
        for (int i = 0; i < selectedRooms.size(); i++) {
            Room room = selectedRooms.get(i);
            summary.append(String.format("Room %d:\n", i + 1));
            summary.append(String.format("  - Room ID:          %d\n", room.getRoomId()));
            summary.append(String.format("  - Room Number:      %d\n", room.getRoomNo()));
            summary.append(String.format("  - Room Type:        %s\n", room.getType()));
            summary.append(String.format("  - Price/Night:      ₹%.2f\n", room.getPrice()));
            subtotalPerNight += room.getPrice();
            if (i < selectedRooms.size() - 1) {
                summary.append("\n");
            }
        }

        summary.append("\n─────────────────────────────────────────────────────\n\n");

        summary.append(String.format("Check-In Date:        %s\n", checkInDateStr));
        summary.append(String.format("Check-Out Date:       %s\n", checkOutDateStr));
        summary.append(String.format("Number of Nights:     %d\n\n", nights));

        summary.append("─────────────────────────────────────────────────────\n\n");

        summary.append(String.format("Subtotal/Night:       ₹%.2f\n", subtotalPerNight));
        summary.append(String.format("Nights:               × %d\n\n", nights));

        summary.append("═══════════════════════════════════════════════════════\n\n");

        summary.append(String.format("GRAND TOTAL:          ₹%.2f\n\n", totalCost));

        summary.append("═══════════════════════════════════════════════════════\n");

        summaryArea.setText(summary.toString());
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == confirmBookingButton) {
            handleConfirmBooking();
        } else if (e.getSource() == cancelButton) {
            // Cancel booking and return to dashboard
            new DashboardFrame(loggedInUser);
            this.dispose();
        }
    }

    /**
     * Executes the critical booking transaction via the ReservationDAO.
     * Creates a reservation for each selected room.
     */
    private void handleConfirmBooking() {
        int confirm = JOptionPane.showConfirmDialog(this,
                String.format("Are you sure you want to confirm the payment of ₹%.2f for %d room(s)?",
                        totalCost, selectedRooms.size()),
                "Confirm Transaction", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                ReservationDAO reservationDAO = new ReservationDAO();
                List<Integer> reservationIds = new ArrayList<>();
                boolean allSuccessful = true;

                // Create a reservation for each room
                for (Room room : selectedRooms) {
                    // Calculate cost for this specific room
                    long nights = ChronoUnit.DAYS.between(
                            LocalDate.parse(checkInDateStr),
                            LocalDate.parse(checkOutDateStr));
                    double roomCost = nights * room.getPrice();

                    Reservation newReservation = new Reservation(
                            loggedInUser.getUserId(),
                            room.getRoomId(),
                            checkInDateStr,
                            checkOutDateStr,
                            roomCost
                    );

                    int resId = reservationDAO.createReservation(newReservation);

                    if (resId > 0) {
                        reservationIds.add(resId);
                    } else {
                        allSuccessful = false;
                        // If one fails, we should ideally rollback all previous ones
                        // For now, just note the failure
                        break;
                    }
                }

                if (allSuccessful && reservationIds.size() == selectedRooms.size()) {
                    StringBuilder message = new StringBuilder();
                    message.append("Booking SUCCESSFUL!\n\n");
                    message.append(String.format("You have booked %d room(s).\n", selectedRooms.size()));
                    message.append("Your Reservation IDs are:\n");
                    for (int id : reservationIds) {
                        message.append("  - ").append(id).append("\n");
                    }
                    message.append("\nYou can view details in 'My Reservations'.");

                    JOptionPane.showMessageDialog(this,
                            message.toString(),
                            "Booking Confirmed",
                            JOptionPane.INFORMATION_MESSAGE);

                    // Navigate back to Dashboard
                    new DashboardFrame(loggedInUser);
                    this.dispose();
                } else {
                    JOptionPane.showMessageDialog(this,
                            "Booking FAILED! One or more rooms may have been taken, or a database error occurred.\n" +
                                    "Please try again or contact support.",
                            "Booking Error",
                            JOptionPane.ERROR_MESSAGE);
                }

            } catch (InvalidReservationException ex) {
                JOptionPane.showMessageDialog(this,
                        "Internal Error: Invalid reservation dates.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this,
                        "An unexpected system error occurred: " + ex.getMessage(),
                        "System Error",
                        JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        }
    }
}