package com.hotel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginFrame extends JFrame implements ActionListener {

    private JTextField emailField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton registerButton;

    public LoginFrame() {
        // Basic Frame Setup
        setTitle("CheckInn: Hotel Reservation System - Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 250);
        setResizable(false);
        setLocationRelativeTo(null); // Center the frame

        // Use BorderLayout for the main structure, and GridBagLayout for the central form
        setLayout(new BorderLayout(10, 10));

        // --- 1. Header ---
        JLabel welcomeLabel = new JLabel("Welcome to CheckInn", SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
        add(welcomeLabel, BorderLayout.NORTH);

        // --- 2. Central Form Panel (GridBagLayout for structure) ---
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5); // Padding
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Email Label and Field
        gbc.gridx = 0; gbc.gridy = 0; gbc.anchor = GridBagConstraints.EAST;
        formPanel.add(new JLabel("Email:"), gbc);
        gbc.gridx = 1; gbc.gridy = 0; gbc.weightx = 1.0; gbc.anchor = GridBagConstraints.WEST;
        emailField = new JTextField(15);
        formPanel.add(emailField, gbc);

        // Password Label and Field
        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0; gbc.anchor = GridBagConstraints.EAST;
        formPanel.add(new JLabel("Password:"), gbc);
        gbc.gridx = 1; gbc.gridy = 1; gbc.weightx = 1.0; gbc.anchor = GridBagConstraints.WEST;
        passwordField = new JPasswordField(15);
        formPanel.add(passwordField, gbc);

        add(formPanel, BorderLayout.CENTER);

        // --- 3. Button Panel (FlowLayout) ---
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));

        loginButton = new JButton("Login");
        loginButton.addActionListener(this);

        registerButton = new JButton("Register");
        registerButton.addActionListener(this);

        buttonPanel.add(loginButton);
        buttonPanel.add(registerButton);

        add(buttonPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    /**
     * Handles button clicks for Login and Register.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == loginButton) {
            handleLogin();
        } else if (e.getSource() == registerButton) {
            handleRegister();
        }
    }

    /**
     * Logic for the Login button: Validate credentials using UserDAO.
     */
    private void handleLogin() {
        String email = emailField.getText().trim();
        // NOTE: JPasswordField returns char[] for security, convert to String for DAO
        String password = new String(passwordField.getPassword());

        if (email.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Email and Password cannot be empty.", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        UserDAO userDAO = new UserDAO();
        User user = userDAO.validateUser(email, password);

        if (user != null) {
            JOptionPane.showMessageDialog(this, "Login Successful! Welcome, " + user.getName(), "Success", JOptionPane.INFORMATION_MESSAGE);

            // Core Logic: Open Dashboard and close Login Frame
            new DashboardFrame(user);
            this.dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Invalid Email or Password.", "Login Failed", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Logic for the Register button: Opens the Register frame.
     */
    private void handleRegister() {
        // Open the Register Frame and pass the current email (if any)
        new RegisterFrame(emailField.getText());
        this.dispose(); // Close the login frame (or hide it, dispose is simpler for this flow)
    }

    // Placeholder method to start the application (will be called from Main)
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LoginFrame());
    }
}