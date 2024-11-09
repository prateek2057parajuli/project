package Database;

import java.sql.Connection;
import java.sql.DriverManager;

public class DatabaseConnection {
    public static Connection getConnection() {
        String url = "jdbc:mysql://localhost:3306/taskmanagerx"; // Replace with your DB name
        String user = "root";  // Your MySQL username
        String password = "";  // Your MySQL password
        
        Connection connection = null;

        try {
            // Load MySQL JDBC Driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Establish connection to the database
            connection = DriverManager.getConnection(url, user, password);
            System.out.println("Connected to the database!");
        } catch(Exception ex) {
            System.out.println(ex);
        }

        return connection;
    }
}