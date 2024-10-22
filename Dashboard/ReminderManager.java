package Dashboard;

import Database.DatabaseConnection;

import java.awt.*;
import java.sql.*;
import java.util.Timer;
import java.util.TimerTask;

public class ReminderManager {
    private TrayIcon trayIcon;

    public ReminderManager() {
        // Initialize the system tray icon for notifications
        if (SystemTray.isSupported()) {
            SystemTray tray = SystemTray.getSystemTray();
            Image image = Toolkit.getDefaultToolkit().createImage("resources/icon.png"); // Path to your icon
            trayIcon = new TrayIcon(image, "Task Reminder");
            trayIcon.setImageAutoSize(true);
            try {
                tray.add(trayIcon);
            } catch (AWTException e) {
                System.err.println("TrayIcon could not be added.");
            }
        } else {
            System.out.println("System tray not supported.");
        }

        // Start checking for overdue tasks
        startReminderCheck();
    }

    private void startReminderCheck() {
        Timer timer = new Timer(true);
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                checkForOverdueTasks();
            }
        }, 0, 60000); // Check every 60 seconds
    }

    private void checkForOverdueTasks() {
        try {
            Connection connection = DatabaseConnection.getConnection();
            String query = "SELECT task_name, due_date FROM tasks WHERE completed = false AND due_date < NOW()";
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                String taskName = resultSet.getString("task_name");
                Timestamp dueDate = resultSet.getTimestamp("due_date");

                // Trigger the reminder notification
                showReminderNotification(taskName, dueDate);
            }

            statement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void showReminderNotification(String taskName, Timestamp dueDate) {
        if (trayIcon != null) {
            String message = "Task '" + taskName + "' is overdue! (Due: " + dueDate + ")";
            trayIcon.displayMessage("Task Reminder", message, TrayIcon.MessageType.WARNING);
        }
    }
}
