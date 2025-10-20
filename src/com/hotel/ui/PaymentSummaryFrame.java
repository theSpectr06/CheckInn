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

    // Define the GST Rates
    private static final double TOTAL_GST_RATE = 0.18; // 18% total GST
    private static final double GST_DIVISOR = 1.0 + TOTAL_GST_RATE; // 1.18

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

        // --- 2. Summary Area ---
        JPanel summaryPanel = new JPanel(new BorderLayout());
        summaryPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(10, 30, 10, 30),
                BorderFactory.createTitledBorder(
                        BorderFactory.createLineBorder(new Color(100, 100, 100), 2),
                        "Final Payment Summary (Prices INCLUDE GST)", // Updated Title
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

        displaySummary(); // Calls the new logic

        JScrollPane scrollPane = new JScrollPane(summaryArea);
        scrollPane.setBorder(null);
        summaryPanel.add(scrollPane, BorderLayout.CENTER);

        add(summaryPanel, BorderLayout.CENTER);

        // --- 3. Button Panel ---
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 25, 20));

        cancelButton = new JButton("Cancel & Back to Dashboard");
        cancelButton.addActionListener(this);
        cancelButton.setPreferredSize(new Dimension(230, 40));
        cancelButton.setFont(new Font("SansSerif", Font.BOLD, 13));

        // Update button text with the totalCost
        confirmBookingButton = new JButton("Confirm & Pay ₹" + String.format("%,.2f", totalCost));
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

    /** Formats and displays all final booking details, including the GST breakdown. */
    private void displaySummary() {
        long nights = ChronoUnit.DAYS.between(LocalDate.parse(checkInDateStr), LocalDate.parse(checkOutDateStr));

        // --- GST Calculation ---
        // 1. Calculate the Taxable Value (Base Price)
        double taxableValueTotal = totalCost / GST_DIVISOR; // Gross / 1.18

        // 2. Calculate the total GST amount
        double totalGST = totalCost - taxableValueTotal;

        // 3. Split into CGST and SGST
        double cgst = totalGST / 2.0;
        double sgst = totalGST / 2.0;

        // --- Summary Output ---

        StringBuilder summary = new StringBuilder();
        summary.append("═════════════════════════════════════════════════════════════════════\n");
        summary.append("                  FINAL PAYMENT SUMMARY\n");
        summary.append("═════════════════════════════════════════════════════════════════════\n\n");

        summary.append(String.format("Guest Name:           %s\n", loggedInUser.getName()));
        summary.append(String.format("Guest ID:             %d\n", loggedInUser.getUserId()));
        summary.append(String.format("Email:                %s\n", loggedInUser.getEmail()));
        summary.append(String.format("Phone:                %s\n\n", loggedInUser.getPhone()));

        summary.append("───────────────────────────────────────────────────────────────────\n\n");

        summary.append(String.format("Hotel:                %s\n", selectedHotel.getName()));
        summary.append(String.format("Location:             %s\n\n", selectedHotel.getAddress()));

        // --- Room Details ---
        summary.append("───────────────────────────── ROOMS ───────────────────────────────\n\n");

        double subtotalPerNight = 0;
        for (int i = 0; i < selectedRooms.size(); i++) {
            Room room = selectedRooms.get(i);
            summary.append(String.format("Room %d:\n", i + 1));
            summary.append(String.format("  - Room Number:      %d\n", room.getRoomNo()));
            summary.append(String.format("  - Room Type:        %s\n", room.getType()));
            summary.append(String.format("  - Gross Price/Night:₹%,.2f\n", room.getPrice()));
            subtotalPerNight += room.getPrice();
            if (i < selectedRooms.size() - 1) {
                summary.append("\n");
            }
        }
        summary.append("        (Note: All displayed prices include GST)\n");
        summary.append("\n───────────────────────────────────────────────────────────────────\n\n");

        summary.append(String.format("Check-In Date:        %s\n", checkInDateStr));
        summary.append(String.format("Check-Out Date:       %s\n", checkOutDateStr));
        summary.append(String.format("Number of Nights:     %d\n", nights));

        summary.append("───────────────────────────────────────────────────────────────────\n\n");

        // --- Base Cost and Breakdown ---
        summary.append(String.format("1. Taxable Value:     ₹%,.2f\n", taxableValueTotal));

        summary.append(String.format("2. ADD GST (%.0f%% Total):\n", TOTAL_GST_RATE * 100));
        summary.append(String.format("   - CGST (%.1f%%):     ₹%,.2f\n", TOTAL_GST_RATE * 50, cgst)); // 9.0%
        summary.append(String.format("   - SGST (%.1f%%):     ₹%,.2f\n", TOTAL_GST_RATE * 50, sgst)); // 9.0%

        summary.append(String.format("3. Gross Subtotal:    ₹%,.2f\n", subtotalPerNight * nights));

        summary.append("\n═════════════════════════════════════════════════════════════════════\n\n");

        summary.append(String.format("TOTAL PAYABLE:        ₹%,.2f\n", totalCost)); // Should equal Gross Subtotal + Tax

        summary.append("\n═════════════════════════════════════════════════════════════════════\n");

        summaryArea.setText(summary.toString());
    }

    // Note: The roomCost saved in the Reservation object is currently the gross cost.
    // If your DB required the base cost, you would adjust the roomCost calculation
    // inside handleConfirmBooking(). For simplicity, we keep it as the gross total.

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
                String.format("Are you sure you want to confirm the payment of ₹%,.2f for %d room(s)?",
                        totalCost, selectedRooms.size()),
                "Confirm Transaction", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                ReservationDAO reservationDAO = new ReservationDAO();
                List<Integer> reservationIds = new ArrayList<>();
                boolean allSuccessful = true;

                // Create a reservation for each room
                for (Room room : selectedRooms) {
                    // Calculate cost for this specific room (Gross Cost is saved)
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