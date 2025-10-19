package com.hotel.ui;

import com.hotel.model.*;
import com.hotel.dao.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class RegisterFrame extends JFrame implements ActionListener {

    private JTextField nameField;
    private JTextField emailField;
    private JTextField phoneField;
    private JPasswordField passwordField;
    private JButton registerButton;
    private JButton backButton;

    public RegisterFrame(String initialEmail) {
        setTitle("CheckInn: Register Account");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(450, 350);
        setResizable(false);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        // --- 1. Header ---
        JLabel headerLabel = new JLabel("Create a New Account", SwingConstants.CENTER);
        headerLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
        add(headerLabel, BorderLayout.NORTH);

        // --- 2. Central Form Panel (GridBagLayout) ---
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Helper method to add label and field pairs
        int row = 0;

        // Name
        gbc.gridx = 0; gbc.gridy = row++; gbc.anchor = GridBagConstraints.EAST;
        formPanel.add(new JLabel("Name:"), gbc);
        gbc.gridx = 1; gbc.gridy = row - 1; gbc.weightx = 1.0; gbc.anchor = GridBagConstraints.WEST;
        nameField = new JTextField(20);
        formPanel.add(nameField, gbc);

        // Email
        gbc.gridx = 0; gbc.gridy = row++; gbc.anchor = GridBagConstraints.EAST;
        formPanel.add(new JLabel("Email:"), gbc);
        gbc.gridx = 1; gbc.gridy = row - 1; gbc.weightx = 1.0; gbc.anchor = GridBagConstraints.WEST;
        emailField = new JTextField(20);
        emailField.setText(initialEmail != null ? initialEmail : ""); // Pre-fill email
        formPanel.add(emailField, gbc);

        // Phone
        gbc.gridx = 0; gbc.gridy = row++; gbc.anchor = GridBagConstraints.EAST;
        formPanel.add(new JLabel("Phone:"), gbc);
        gbc.gridx = 1; gbc.gridy = row - 1; gbc.weightx = 1.0; gbc.anchor = GridBagConstraints.WEST;
        phoneField = new JTextField(20);
        formPanel.add(phoneField, gbc);

        // Password
        gbc.gridx = 0; gbc.gridy = row++; gbc.anchor = GridBagConstraints.EAST;
        formPanel.add(new JLabel("Password:"), gbc);
        gbc.gridx = 1; gbc.gridy = row - 1; gbc.weightx = 1.0; gbc.anchor = GridBagConstraints.WEST;
        passwordField = new JPasswordField(20);
        formPanel.add(passwordField, gbc);

        add(formPanel, BorderLayout.CENTER);

        // --- 3. Button Panel (FlowLayout) ---
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));

        registerButton = new JButton("Register");
        registerButton.addActionListener(this);

        backButton = new JButton("Back to Login");
        backButton.addActionListener(this);

        buttonPanel.add(registerButton);
        buttonPanel.add(backButton);

        add(buttonPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    /**
     * Handles button clicks for Register and Back.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == registerButton) {
            handleRegistration();
        } else if (e.getSource() == backButton) {
            new LoginFrame(); // Redirect to LoginFrame
            this.dispose();
        }
    }

    /**
     * Logic for the Register button: Validates input and calls UserDAO.
     */
    private void handleRegistration() {
        String name = nameField.getText().trim();
        String email = emailField.getText().trim();
        String phone = phoneField.getText().trim();
        String password = new String(passwordField.getPassword());

        // Minimal Validation
        if (name.isEmpty() || email.isEmpty() || phone.isEmpty() || password.length() < 6) {
            JOptionPane.showMessageDialog(this, "All fields are required and password must be at least 6 characters.", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            // Create a new User object
            User newUser = new User(name, email, phone, password);
            UserDAO userDAO = new UserDAO();

            // Execute the registration logic
            boolean success = userDAO.registerUser(newUser);

            if (success) {
                JOptionPane.showMessageDialog(this,
                        "Account created successfully. User ID: " + newUser.getUserId() + "\nPlease log in.",
                        "Registration Success", JOptionPane.INFORMATION_MESSAGE);

                // Redirect to LoginFrame
                new LoginFrame();
                this.dispose();
            } else {
                // If registerUser returns false, it usually means an SQL error occurred (like duplicate email)
                JOptionPane.showMessageDialog(this,
                        "Registration failed. The email may already be in use.",
                        "Registration Failed", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            // Catch any unexpected exceptions
            JOptionPane.showMessageDialog(this, "An unexpected error occurred during registration: " + ex.getMessage(), "System Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }
}