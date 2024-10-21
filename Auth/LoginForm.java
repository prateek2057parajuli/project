package Auth;

import Database.DatabaseConnection;
import Dashboard.ToDoListDashboard;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;
import java.sql.*;

public class LoginForm extends JFrame implements ActionListener {

    private Container container;
    private JLabel title;
    private JLabel usernameLabel;
    private JTextField usernameField;
    private JLabel passwordLabel;
    private JPasswordField passwordField;
    private JCheckBox showPasswordCheckBox; // Checkbox to show/hide password
    private JButton loginButton;
    private JButton signupButton;

    public LoginForm() {
        setTitle("Login Form");
        setBounds(300, 90, 500, 450);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);

        container = getContentPane();
        container.setLayout(null);
        container.setBackground(new Color(224, 224, 224)); // Background color

        title = new JLabel("Login Form");
        title.setFont(new Font("Verdana", Font.BOLD, 30));
        title.setForeground(new Color(33, 37, 41)); // Dark color
        title.setSize(300, 40);
        title.setLocation(150, 30);
        container.add(title);

        usernameLabel = new JLabel("Username:");
        usernameLabel.setFont(new Font("Verdana", Font.PLAIN, 18));
        usernameLabel.setSize(120, 30);
        usernameLabel.setLocation(50, 100);
        container.add(usernameLabel);

        usernameField = new JTextField();
        usernameField.setFont(new Font("Verdana", Font.PLAIN, 15));
        usernameField.setSize(250, 30);
        usernameField.setLocation(180, 100);
        usernameField.setBorder(new LineBorder(new Color(120, 144, 156), 2)); // Soft border color
        container.add(usernameField);

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
        showPasswordCheckBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (showPasswordCheckBox.isSelected()) {
                    passwordField.setEchoChar((char) 0); // Show password
                } else {
                    passwordField.setEchoChar('*'); // Hide password
                }
            }
        });
        container.add(showPasswordCheckBox);

        // Login button with style
        loginButton = new JButton("Login");
        loginButton.setFont(new Font("Verdana", Font.BOLD, 15));
        loginButton.setSize(120, 35);
        loginButton.setLocation(120, 230);
        loginButton.setBackground(new Color(41, 121, 255)); // Blue color
        loginButton.setForeground(Color.WHITE); // White text
        loginButton.setBorder(new LineBorder(new Color(33, 150, 243), 2));
        loginButton.setFocusPainted(false);
        loginButton.addActionListener(this);
        container.add(loginButton);

        // Signup button with style
        signupButton = new JButton("Signup");
        signupButton.setFont(new Font("Verdana", Font.BOLD, 15));
        signupButton.setSize(120, 35);
        signupButton.setLocation(260, 230);
        signupButton.setBackground(new Color(76, 175, 80)); // Green color
        signupButton.setForeground(Color.WHITE); // White text
        signupButton.setBorder(new LineBorder(new Color(67, 160, 71), 2));
        signupButton.setFocusPainted(false);
        signupButton.addActionListener(this);
        container.add(signupButton);

        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == loginButton) {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());

            if (validateInput(username, password)) {
                int userId = authenticateUser(username, password);
                if (userId != -1) {
                    // Proceed to the dashboard with the user ID
                    new ToDoListDashboard(userId);
                    dispose(); // Close the login form
                } else {
                    JOptionPane.showMessageDialog(this, "Invalid username or password", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        } else if (e.getSource() == signupButton) {
            new SignupForm(); // Go to signup form
            dispose(); // Close the login form
        }
    }

    private boolean validateInput(String username, String password) {
        if (username.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Username cannot be empty", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Password cannot be empty", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }

    private int authenticateUser(String username, String password) {
        try {
            Connection connection = DatabaseConnection.getConnection();
            // Change 'id' to 'user_id' in the query
            String query = "SELECT user_id FROM Users WHERE username = ? AND password = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);
            ResultSet resultSet = preparedStatement.executeQuery();
    
            // Get user ID if authenticated
            int userId = -1;
            if (resultSet.next()) {
                userId = resultSet.getInt("user_id"); // Change 'id' to 'user_id'
            }
    
            // Clean up
            resultSet.close();
            preparedStatement.close();
            connection.close();
    
            return userId; // Return user ID if authenticated, else -1
        } catch (Exception ex) {
            System.err.println("Database Error: " + ex.getMessage());
            return -1; // Return -1 for any database error or failed authentication
        }
    }
    

    public static void main(String[] args) {
        new LoginForm();
    }
}
