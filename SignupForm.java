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
    private JButton signupButton;
    private JButton loginButton;

    public SignupForm() {
        setTitle("Signup Form");
        setBounds(300, 90, 500, 450);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);

        container = getContentPane();
        container.setLayout(null);

        // Background color
        container.setBackground(new Color(224, 224, 224));

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

        emailLabel = new JLabel("Email:");
        emailLabel.setFont(new Font("Verdana", Font.PLAIN, 18));
        emailLabel.setSize(120, 30);
        emailLabel.setLocation(50, 200);
        container.add(emailLabel);

        emailField = new JTextField();
        emailField.setFont(new Font("Verdana", Font.PLAIN, 15));
        emailField.setSize(250, 30);
        emailField.setLocation(180, 200);
        emailField.setBorder(new LineBorder(new Color(120, 144, 156), 2));
        container.add(emailField);

        // Signup button with style
        signupButton = new JButton("Signup");
        signupButton.setFont(new Font("Verdana", Font.BOLD, 15));
        signupButton.setSize(120, 35);
        signupButton.setLocation(120, 300);
        signupButton.setBackground(new Color(41, 121, 255)); // Blue color
        signupButton.setForeground(Color.WHITE); // White text
        signupButton.setBorder(new LineBorder(new Color(33, 150, 243), 2));
        signupButton.setFocusPainted(false); // Removes focus outline
        signupButton.addActionListener(this);
        container.add(signupButton);

        // Login button with style
        loginButton = new JButton("Login");
        loginButton.setFont(new Font("Verdana", Font.BOLD, 15));
        loginButton.setSize(120, 35);
        loginButton.setLocation(260, 300);
        loginButton.setBackground(new Color(76, 175, 80)); // Green color
        loginButton.setForeground(Color.WHITE); // White text
        loginButton.setBorder(new LineBorder(new Color(67, 160, 71), 2));
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
                saveUserToDatabase(username, password, email);
            }
        } else if (e.getSource() == loginButton) {
            new LoginForm(); // Assuming you have a LoginForm class
            dispose(); // Close the signup form
        }
    }

    private boolean validateInput(String username, String password, String email) {
        if (username.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Username cannot be empty", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (username.length() < 3) {
            JOptionPane.showMessageDialog(this, "Username must be at least 3 characters long", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Password cannot be empty", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (!isValidPassword(password)) {
            JOptionPane.showMessageDialog(this, "Password must be at least 8 characters long and include a number, a capital letter, and a special character", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (email.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Email cannot be empty", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (!isValidEmail(email)) {
            JOptionPane.showMessageDialog(this, "Please enter a valid email address", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        return true;
    }

    // Password validation: minimum 8 characters, at least 1 uppercase, 1 digit, 1 special character
    private boolean isValidPassword(String password) {
        String regex = "^(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$";
        return password.matches(regex);
    }

    // Email validation using regex
    private boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        return email.matches(emailRegex);
    }

    private void saveUserToDatabase(String username, String password, String email) {
        try {
            // Establish a connection
            DatabaseConnection db = new DatabaseConnection();
            Connection connection = db.getConnection();

            // Prepare SQL insert query
            String query = "INSERT INTO users (username, password, email) VALUES (?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);
            preparedStatement.setString(3, email);

            // Execute the query
            int rowAffected = preparedStatement.executeUpdate();

            if (rowAffected > 0) {
                JOptionPane.showMessageDialog(this, "Signup Successful!", "Success", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Signup Failed!", "Error", JOptionPane.ERROR_MESSAGE);
            }

            // Close resources
            preparedStatement.close();
            connection.close();

        } catch (Exception ex) {
            System.out.println("Database Error: " + ex.getMessage());
            JOptionPane.showMessageDialog(this, "Database Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
