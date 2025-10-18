package com.hotel;

import com.hotel.exceptions.InvalidReservationException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class PaymentSummaryFrame extends JFrame implements ActionListener {

    private User loggedInUser;
    private Hotel selectedHotel;
    private Room selectedRoom;
    private String checkInDateStr;
    private String checkOutDateStr;
    private double totalCost;

    private JTextArea summaryArea;
    private JButton confirmBookingButton;
    private JButton cancelButton;

    public PaymentSummaryFrame(User user, Hotel hotel, Room room, String inDate, String outDate, double total) {
        this.loggedInUser = user;
        this.selectedHotel = hotel;
        this.selectedRoom = room;
        this.checkInDateStr = inDate;
        this.checkOutDateStr = outDate;
        this.totalCost = total;

        setTitle("CheckInn: Final Payment Summary");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(550, 480);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        // --- 1. Header ---
        JLabel header = new JLabel("Confirm Your Booking", SwingConstants.CENTER);
        header.setFont(new Font("SansSerif", Font.BOLD, 20));
        add(header, BorderLayout.NORTH);

        // --- 2. Summary Area (Center) ---
        summaryArea = new JTextArea();
        summaryArea.setEditable(false);
        summaryArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        summaryArea.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        displaySummary();

        add(new JScrollPane(summaryArea), BorderLayout.CENTER);

        // --- 3. Button Panel (South) ---
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));

        confirmBookingButton = new JButton("Confirm & Pay (Final Booking)");
        confirmBookingButton.addActionListener(this);
        confirmBookingButton.setBackground(new Color(60, 179, 113)); // Light Green
        confirmBookingButton.setForeground(Color.WHITE);

        cancelButton = new JButton("Cancel & Back to Dashboard");
        cancelButton.addActionListener(this);

        buttonPanel.add(cancelButton);
        buttonPanel.add(confirmBookingButton);
        add(buttonPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    /** Formats and displays all final booking details. */
    private void displaySummary() {
        long nights = ChronoUnit.DAYS.between(LocalDate.parse(checkInDateStr), LocalDate.parse(checkOutDateStr));

        String summary = String.format(
                "=========================================\n" +
                        "            BOOKING SUMMARY\n" +
                        "=========================================\n" +
                        "Guest:           %s (ID: %d)\n" +
                        "Email:           %s\n" +
                        "-----------------------------------------\n" +
                        "Hotel:           %s\n" +
                        "Room ID:         %d\n" +
                        "Room No:         %d (%s)\n" +
                        "Price/Night:     ₹%.2f\n" +
                        "-----------------------------------------\n" +
                        "Check-in Date:   %s\n" +
                        "Check-out Date:  %s\n" +
                        "Total Nights:    %d\n" +
                        "-----------------------------------------\n" +
                        "GRAND TOTAL:     ₹%.2f\n" +
                        "=========================================\n",
                loggedInUser.getName(), loggedInUser.getUserId(), loggedInUser.getEmail(),
                selectedHotel.getName(),
                selectedRoom.getRoomId(), selectedRoom.getRoomNo(), selectedRoom.getType(), selectedRoom.getPrice(),
                checkInDateStr, checkOutDateStr,
                nights,
                totalCost
        );
        summaryArea.setText(summary);
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
     */
    private void handleConfirmBooking() {
        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to confirm the payment of ₹" + totalCost + "?",
                "Confirm Transaction", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                // 1. Create the Reservation Model object
                Reservation newReservation = new Reservation(
                        loggedInUser.getUserId(),
                        selectedRoom.getRoomId(),
                        checkInDateStr,
                        checkOutDateStr,
                        totalCost
                );

                // 2. Execute the Transaction (INSERT + UPDATE)
                ReservationDAO reservationDAO = new ReservationDAO();
                int resId = reservationDAO.createReservation(newReservation);

                if (resId > 0) {
                    JOptionPane.showMessageDialog(this,
                            "Booking SUCCESSFUL! Your Reservation ID is: " + resId +
                                    "\nYou can view details in 'My Reservations'.",
                            "Booking Confirmed", JOptionPane.INFORMATION_MESSAGE);

                    // 3. Navigate back to a refreshed Dashboard
                    new DashboardFrame(loggedInUser);
                    this.dispose();
                } else {
                    // This handles cases where the DAO fails (e.g., rollback occurred)
                    JOptionPane.showMessageDialog(this,
                            "Booking FAILED! The room may have been taken, or a database error occurred. Please try again.",
                            "Booking Error", JOptionPane.ERROR_MESSAGE);
                }

            } catch (InvalidReservationException ex) {
                // Should not happen here if previous frames were correct, but good safety net
                JOptionPane.showMessageDialog(this, "Internal Error: Invalid reservation dates.", "Error", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "An unexpected system error occurred: " + ex.getMessage(), "System Error", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        }
    }
}