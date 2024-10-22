import javax.swing.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class ReminderManager {
    private static Timer reminderTimer = new Timer();

    // Schedule a reminder for a specific task
    public static void scheduleReminder(String taskName, Date endTime) {
        if (endTime == null) return;

        // Calculate the delay from the current time to the task end time
        long delay = endTime.getTime() - System.currentTimeMillis();
        if (delay <= 0) {
            return; // If the task's end time is in the past, no reminder needed
        }

        reminderTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                // Show a reminder message
                JOptionPane.showMessageDialog(null, "Reminder: Task \"" + taskName + "\" is due!", "Task Reminder", JOptionPane.WARNING_MESSAGE);
            }
        }, delay);
    }

    // Cancel all scheduled reminders
    public static void cancelReminders() {
        reminderTimer.cancel();
        reminderTimer = new Timer(); // Reset the timer for future use
    }
}
