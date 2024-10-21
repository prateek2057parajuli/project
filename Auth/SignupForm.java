package Auth;

import Database.DatabaseConnection;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import javax.swing.*;
import javax.swing.border.*;

public class SignupForm extends JFrame implements ActionListener {

    private Container container;
    private JLabel title;
    private JLabel nameLabel;
    private JTextField nameField;
    private JLabel passwordLabel;
    private JPasswordField passwordField;
    private JLabel emailLabel;
    private JTextField emailField;
    private JCheckBox showPasswordCheckBox; // Checkbox to show/hide password
    private JButton signupButton;
    private JButton loginButton;

    public SignupForm() {
        setTitle("Signup Form");
        setBounds(300, 90, 500, 450);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);

        container = getContentPane();
        container.setLayout(null);
        container.setBackground(new Color(224, 224, 224)); // Background color

        title = new JLabel("Signup Form");
        title.setFont(new Font("Verdana", Font.BOLD, 30));
        title.setForeground(new Color(33, 37, 41)); // Dark color
        title.setSize(300, 40);
        title.setLocation(150, 30);
        container.add(title);

        nameLabel = new JLabel("Username:");
        nameLabel.setFont(new Font("Verdana", Font.PLAIN, 18));
        nameLabel.setSize(120, 30);
        nameLabel.setLocation(50, 100);
        container.add(nameLabel);

        nameField = new JTextField();
        nameField.setFont(new Font("Verdana", Font.PLAIN, 15));
        nameField.setSize(250, 30);
        nameField.setLocation(180, 100);
        nameField.setBorder(new LineBorder(new Color(120, 144, 156), 2)); // Soft border color
        container.add(nameField);

        passwordLabel = new JLabel("Password:");
        passwordLabel.setFont(new Font("Verdana", Font.PLAIN, 18));
        passwordLabel.setSize(120, 30);
        passwordLabel.setLocation(50, 150);
        container.add(passwordLabel);

        passwordField = new JPasswordField();
        passwordField.setFont(new Font("Verdana", Font.PLAIN, 15));
        passwordField.setSize(250, 30);
        passwordField.setLocation(180, 150);
        passwordField.setBorder(new LineBorder(new Color(120, 144, 156), 2));
        container.add(passwordField);

        // Checkbox to show/hide password
        showPasswordCheckBox = new JCheckBox("Show Password");
        showPasswordCheckBox.setFont(new Font("Verdana", Font.PLAIN, 12));
        showPasswordCheckBox.setSize(150, 30);
        showPasswordCheckBox.setLocation(180, 185);
        showPasswordCheckBox.setBackground(new Color(224, 224, 224)); // Match background color
        showPasswordCheckBox.addActionListener(e -> passwordField.setEchoChar(showPasswordCheckBox.isSelected() ? (char) 0 : '*'));
        container.add(showPasswordCheckBox);

        emailLabel = new JLabel("Email:");
        emailLabel.setFont(new Font("Verdana", Font.PLAIN, 18));
        emailLabel.setSize(120, 30);
        emailLabel.setLocation(50, 230);
        container.add(emailLabel);

        emailField = new JTextField();
        emailField.setFont(new Font("Verdana", Font.PLAIN, 15));
        emailField.setSize(250, 30);
        emailField.setLocation(180, 230);
        emailField.setBorder(new LineBorder(new Color(120, 144, 156), 2));
        container.add(emailField);

        // Signup button with style
        signupButton = new JButton("Signup");
        signupButton.setFont(new Font("Verdana", Font.BOLD, 15));
        signupButton.setSize(120, 35);
        signupButton.setLocation(120, 290);
        signupButton.setBackground(new Color(76, 175, 80)); // Green color
        signupButton.setForeground(Color.WHITE); // White text
        signupButton.setBorder(new LineBorder(new Color(67, 160, 71), 2));
        signupButton.setFocusPainted(false);
        signupButton.addActionListener(this);
        container.add(signupButton);

        // Login button with style
        loginButton = new JButton("Login");
        loginButton.setFont(new Font("Verdana", Font.BOLD, 15));
        loginButton.setSize(120, 35);
        loginButton.setLocation(260, 290);
        loginButton.setBackground(new Color(41, 121, 255)); // Blue color
        loginButton.setForeground(Color.WHITE); // White text
        loginButton.setBorder(new LineBorder(new Color(33, 150, 243), 2));
        loginButton.setFocusPainted(false);
        loginButton.addActionListener(this);
        container.add(loginButton);

        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == signupButton) {
            String username = nameField.getText();
            String password = new String(passwordField.getPassword());
            String email = emailField.getText();

            if (validateInput(username, password, email)) {
                if (registerUser(username, password, email)) {
                    JOptionPane.showMessageDialog(this, "Signup successful! You can now login.", "Success", JOptionPane.INFORMATION_MESSAGE);
                    new LoginForm(); // Go back to login form
                    dispose(); // Close signup form
                } else {
                    JOptionPane.showMessageDialog(this, "Signup failed. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        } else if (e.getSource() == loginButton) {
            new LoginForm(); // Go to login form
            dispose(); // Close signup form
        }
    }

    private boolean validateInput(String username, String password, String email) {
        if (username.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Username cannot be empty", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Password cannot be empty", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (email.isEmpty() || !email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            JOptionPane.showMessageDialog(this, "Please enter a valid email", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }

    private boolean registerUser(String username, String password, String email) {
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO Users (username, password, email) VALUES (?, ?, ?)")) {
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);
            preparedStatement.setString(3, email);
            return preparedStatement.executeUpdate() > 0; // Returns true if a row is inserted
        } catch (Exception ex) {
            System.err.println("Database Error: " + ex.getMessage());
            return false;
        }
    }

    public static void main(String[] args) {
        new SignupForm();
    }
}
