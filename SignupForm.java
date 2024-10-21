import java.awt.Container;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

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
        setBounds(300, 90, 500, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);

        container = getContentPane();
        container.setLayout(null);

        title = new JLabel("Signup Form");
        title.setFont(new Font("Arial", Font.PLAIN, 30));
        title.setSize(300, 30);
        title.setLocation(150, 30);
        container.add(title);

        nameLabel = new JLabel("Username");
        nameLabel.setFont(new Font("Arial", Font.PLAIN, 20));
        nameLabel.setSize(100, 20);
        nameLabel.setLocation(50, 100);
        container.add(nameLabel);

        nameField = new JTextField();
        nameField.setFont(new Font("Arial", Font.PLAIN, 15));
        nameField.setSize(190, 20);
        nameField.setLocation(200, 100);
        container.add(nameField);

        passwordLabel = new JLabel("Password");
        passwordLabel.setFont(new Font("Arial", Font.PLAIN, 20));
        passwordLabel.setSize(100, 20);
        passwordLabel.setLocation(50, 150);
        container.add(passwordLabel);

        passwordField = new JPasswordField();
        passwordField.setFont(new Font("Arial", Font.PLAIN, 15));
        passwordField.setSize(190, 20);
        passwordField.setLocation(200, 150);
        container.add(passwordField);

        emailLabel = new JLabel("Email");
        emailLabel.setFont(new Font("Arial", Font.PLAIN, 20));
        emailLabel.setSize(100, 20);
        emailLabel.setLocation(50, 200);
        container.add(emailLabel);

        emailField = new JTextField();
        emailField.setFont(new Font("Arial", Font.PLAIN, 15));
        emailField.setSize(190, 20);
        emailField.setLocation(200, 200);
        container.add(emailField);

        signupButton = new JButton("Signup");
        signupButton.setFont(new Font("Arial", Font.PLAIN, 15));
        signupButton.setSize(100, 20);
        signupButton.setLocation(150, 250);
        signupButton.addActionListener(this);
        container.add(signupButton);

        loginButton = new JButton("Login");
        loginButton.setFont(new Font("Arial", Font.PLAIN, 15));
        loginButton.setSize(100, 20);
        loginButton.setLocation(270, 250);
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
            } else {
                JOptionPane.showMessageDialog(this, "Please fill all the fields correctly", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else if (e.getSource() == loginButton) {
            // Open login form
            new LoginForm();  // Assuming you have a LoginForm class
            dispose();  // Close the signup form
        }
    }

    private boolean validateInput(String username, String password, String email) {
        if (username.isEmpty() || password.isEmpty() || email.isEmpty()) {
            return false;
        }
        return true;
    }

    private void saveUserToDatabase(String username, String password, String email) {
        String url = "jdbc:mysql://localhost:3306/todo";  // Database URL
        String dbUser = "root";  // XAMPP default username
        String dbPassword = "";  // XAMPP default password (empty)

        try {
            // Load JDBC driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Establish a connection
            Connection connection = DriverManager.getConnection(url, dbUser, dbPassword);

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

        } catch (ClassNotFoundException | SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Database Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        new SignupForm();
    }
}

