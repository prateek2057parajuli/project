package Dashboard;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class ReminderManager {
    private Map<String, Date> reminders; // Maps task names to reminder times
    private Timer timer;

    public ReminderManager() {
        this.reminders = new HashMap<>();
        this.timer = new Timer(); // Initialize the timer for scheduling tasks
    }

    public void scheduleReminder(String taskName, Date endTime) {
        reminders.put(taskName, endTime);
        System.out.println("Reminder scheduled for: " + taskName + " at " + endTime);

        // Schedule a notification for the reminder
        long delay = endTime.getTime() - System.currentTimeMillis(); // Calculate delay in milliseconds

        if (delay > 0) {
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    showNotification("Reminder for task: " + taskName);
                    cancelReminder(taskName); // Optionally cancel the reminder after notifying
                }
            }, delay);
        } else {
            System.out.println("The scheduled time is in the past. Reminder not set.");
        }
    }

    public void cancelReminder(String taskName) {
        if (reminders.containsKey(taskName)) {
            reminders.remove(taskName);
            System.out.println("Reminder canceled for: " + taskName);
        } else {
            System.out.println("No reminder found for: " + taskName);
        }
    }

    private void showNotification(String message) {
        // Check if SystemTray is supported
        if (!SystemTray.isSupported()) {
            System.out.println("SystemTray is not supported!");
            return;
        }

        // Create a tray icon
        Image image = Toolkit.getDefaultToolkit().getImage("path/to/your/icon.png"); // Set your icon path here
        SystemTray tray = SystemTray.getSystemTray();
        TrayIcon trayIcon = new TrayIcon(image, "Reminder");
        trayIcon.setImageAutoSize(true);

        // Create a popup menu for the tray icon
        PopupMenu popup = new PopupMenu();
        MenuItem exitItem = new MenuItem("Exit");
        exitItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        popup.add(exitItem);
        trayIcon.setPopupMenu(popup);

        // Add the tray icon to the system tray
        try {
            tray.add(trayIcon);
            trayIcon.displayMessage("Task Reminder", message, TrayIcon.MessageType.INFO);
        } catch (AWTException e) {
            e.printStackTrace();
        }
    }

    public Map<String, Date> getReminders() {
        return reminders;
    }
}
