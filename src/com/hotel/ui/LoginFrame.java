package com.hotel.ui;

import com.hotel.dao.UserDAO;
import com.hotel.model.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

public class LoginFrame extends JFrame implements ActionListener {

    private JTextField emailField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton registerButton;

    public LoginFrame() {
        // Basic Frame Setup
        setTitle("CheckInn: Hotel Reservation System - Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 450);
        setResizable(false);
        setLocationRelativeTo(null); // Center the frame

        // Use BorderLayout for the main structure
        setLayout(new BorderLayout(10, 10));

        // --- 1. Header (Logo/Title) ---
        JLabel logoLabel;
        try {
            // Load the logo image (using the corrected relative path)
            ImageIcon logoIcon = new ImageIcon(getClass().getResource("resources/banner.png"));
            logoLabel = new JLabel(logoIcon, SwingConstants.CENTER);
        } catch (Exception e) {
            // Fallback if the image fails to load
            logoLabel = new JLabel("Welcome to CheckInn", SwingConstants.CENTER);
            logoLabel.setFont(new Font("SansSerif", Font.BOLD, 28));
        }
        add(logoLabel, BorderLayout.NORTH);

        // --- 2. Central Form Panel (GridBagLayout for structure) ---
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Email Label and Field
        gbc.gridx = 0; gbc.gridy = 0; gbc.anchor = GridBagConstraints.EAST;
        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 5));
        formPanel.add(emailLabel, gbc);

        gbc.gridx = 1; gbc.gridy = 0; gbc.weightx = 1.0; gbc.anchor = GridBagConstraints.WEST;
        emailField = new JTextField(15);
        emailField.setForeground(Color.WHITE);
        emailField.setBorder(BorderFactory.createCompoundBorder(
                emailField.getBorder(),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));

        // Add placeholder for email field
        setPlaceholder(emailField, "Enter your email");

        formPanel.add(emailField, gbc);

        // Password Label and Field
        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0; gbc.anchor = GridBagConstraints.EAST;
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 5));
        formPanel.add(passwordLabel, gbc);

        gbc.gridx = 1; gbc.gridy = 1; gbc.weightx = 1.0; gbc.anchor = GridBagConstraints.WEST;
        passwordField = new JPasswordField(15);
        passwordField.setForeground(Color.WHITE);
        passwordField.setBorder(BorderFactory.createCompoundBorder(
                passwordField.getBorder(),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));

        // Add placeholder for password field
        setPasswordPlaceholder(passwordField, "Enter your password");

        formPanel.add(passwordField, gbc);

        // --- 3. Button Panel (Moved closer to fields) ---
        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2; gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(20, 10, 10, 10); // Gap between fields and buttons

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));

        loginButton = new JButton("Login");
        loginButton.addActionListener(this);
        loginButton.setPreferredSize(new Dimension(120, 35)); // Increased button size
        loginButton.setBackground(new Color(0, 153, 51)); // Darker green
        loginButton.setForeground(Color.WHITE);
        loginButton.setOpaque(true);
        loginButton.setBorderPainted(false);

        registerButton = new JButton("Register");
        registerButton.addActionListener(this);
        registerButton.setPreferredSize(new Dimension(120, 35)); // Increased button size

        buttonPanel.add(loginButton);
        buttonPanel.add(registerButton);

        formPanel.add(buttonPanel, gbc);

        add(formPanel, BorderLayout.CENTER);

        // **Usability Improvement: Set Default Button (ENTER key press)**
        getRootPane().setDefaultButton(loginButton);

        setVisible(true);
    }

    /**
     * Sets a placeholder text for a JTextField.
     */
    private void setPlaceholder(JTextField textField, String placeholder) {
        textField.setForeground(Color.GRAY);
        textField.setText(placeholder);

        textField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (textField.getText().equals(placeholder)) {
                    textField.setText("");
                    textField.setForeground(Color.WHITE);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (textField.getText().isEmpty()) {
                    textField.setForeground(Color.GRAY);
                    textField.setText(placeholder);
                }
            }
        });
    }

    /**
     * Sets a placeholder text for a JPasswordField.
     */
    private void setPasswordPlaceholder(JPasswordField passwordField, String placeholder) {
        passwordField.setEchoChar((char) 0); // Show plain text for placeholder
        passwordField.setForeground(Color.GRAY);
        passwordField.setText(placeholder);

        passwordField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (String.valueOf(passwordField.getPassword()).equals(placeholder)) {
                    passwordField.setText("");
                    passwordField.setEchoChar('•'); // Set echo char back
                    passwordField.setForeground(Color.WHITE);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (passwordField.getPassword().length == 0) {
                    passwordField.setEchoChar((char) 0); // Show plain text for placeholder
                    passwordField.setForeground(Color.GRAY);
                    passwordField.setText(placeholder);
                }
            }
        });
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
        String password = new String(passwordField.getPassword());

        // Check if fields contain placeholder text
        if (email.isEmpty() || email.equals("Enter your email") ||
                password.isEmpty() || password.equals("Enter your password")) {
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
        new RegisterFrame(emailField.getText());
        this.dispose();
    }

    // Placeholder method to start the application (will be called from Main)
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LoginFrame());
    }
}