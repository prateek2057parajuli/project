import Database.DatabaseConnection;

import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import javax.swing.*;
import javax.swing.border.*;

public class LoginForm extends JFrame implements ActionListener {

    private Container container;
    private JLabel title;
    private JLabel nameLabel;
    private JTextField nameField;
    private JLabel passwordLabel;
    private JPasswordField passwordField;
    private JButton loginButton, signupButton;

    public LoginForm() {
        setTitle("Login Form");
        setBounds(300, 90, 500, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);

        container = getContentPane();
        container.setLayout(null);
        container.setBackground(new Color(224, 224, 224));  // Background color

        title = new JLabel("Login Form");
        title.setFont(new Font("Verdana", Font.BOLD, 30));
        title.setForeground(new Color(33, 37, 41));  // Dark color
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
        passwordField.setBorder(new LineBorder(new Color(120, 144, 156), 2)); // Soft border color
        container.add(passwordField);

        // Login button with style
        loginButton = new JButton("Login");
        loginButton.setFont(new Font("Verdana", Font.BOLD, 15));
        loginButton.setSize(120, 35);
        loginButton.setLocation(120, 250);
        loginButton.setBackground(new Color(41, 121, 255));  // Blue color
        loginButton.setForeground(Color.WHITE);  // White text
        loginButton.setBorder(new LineBorder(new Color(33, 150, 243), 2));
        loginButton.setFocusPainted(false);
        loginButton.addActionListener(this);
        container.add(loginButton);

        // Signup button with style
        signupButton = new JButton("Signup");
        signupButton.setFont(new Font("Verdana", Font.BOLD, 15));
        signupButton.setSize(120, 35);
        signupButton.setLocation(260, 250);
        signupButton.setBackground(new Color(76, 175, 80));  // Green color
        signupButton.setForeground(Color.WHITE);  // White text
        signupButton.setBorder(new LineBorder(new Color(67, 160, 71), 2));
        signupButton.setFocusPainted(false);
        signupButton.addActionListener(this);
        container.add(signupButton);

        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == loginButton) {
            String username = nameField.getText();
            String password = new String(passwordField.getPassword());

            if (validateLoginFields(username, password)) {
                if (validateLogin(username, password)) {
                    new ToDoListDashboard();  // Assuming you have implemented the dashboard
                    dispose();  // Close the login form window
                } else {
                    JOptionPane.showMessageDialog(this, "Invalid username or password", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        } else if (e.getSource() == signupButton) {
            new SignupForm();  // Open signup form
            dispose();  // Close the login form
        }
    }

    private boolean validateLoginFields(String username, String password) {
        // Basic field validation
        if (username.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Username cannot be empty", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Password cannot be empty", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (username.length() < 5) {
            JOptionPane.showMessageDialog(this, "Username must be at least 5 characters long", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (password.length() < 6) {
            JOptionPane.showMessageDialog(this, "Password must be at least 6 characters long", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        return true;
    }

    private boolean validateLogin(String username, String password) {
        try {
            DatabaseConnection db = new DatabaseConnection();
            Connection connection = db.getConnection();

            // Prepare a SQL query to validate username and password
            String query = "SELECT * FROM users WHERE username = ? AND password = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);

            ResultSet resultSet = preparedStatement.executeQuery();
            boolean isValid = resultSet.next();  // Check if a record was found

            // Clean up
            resultSet.close();
            preparedStatement.close();
            connection.close();

            return isValid;  // Return true if valid credentials, false otherwise
        } catch (Exception ex) {
            System.err.println("SQL error: " + ex.getMessage());
            return false;
        }
    }

    public static void main(String[] args) {
        new LoginForm();  // Launch the login form
    }
}
