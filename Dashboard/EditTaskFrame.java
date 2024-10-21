package Dashboard;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import Database.DatabaseConnection;

public class EditTaskFrame extends JFrame {
    private JTextField taskNameField;
    private JComboBox<String> priorityComboBox;
    private JTextField endTimeField;
    private ToDoListDashboard dashboard;

    public EditTaskFrame(ToDoListDashboard dashboard, String taskName, String priority, String endTime) {
        this.dashboard = dashboard;
        setTitle("Edit Task");
        setBounds(400, 200, 400, 300);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new GridLayout(4, 2));

        // Task Name
        add(new JLabel("Task Name:"));
        taskNameField = new JTextField(taskName);
        add(taskNameField);

        // Priority
        add(new JLabel("Priority:"));
        priorityComboBox = new JComboBox<>(new String[]{"High", "Medium", "Low"});
        priorityComboBox.setSelectedItem(priority);
        add(priorityComboBox);

        // End Time
        add(new JLabel("End Time:"));
        endTimeField = new JTextField(endTime);
        add(endTimeField);

        // Update button
        JButton updateButton = new JButton("Update Task");
        updateButton.addActionListener(e -> updateTask(taskName));
        add(updateButton);

        setVisible(true);
    }

    private void updateTask(String oldTaskName) {
        String newTaskName = taskNameField.getText();
        String priority = (String) priorityComboBox.getSelectedItem();
        String endTime = endTimeField.getText();

        // Update the database
        try (Connection conn = DatabaseConnection.getConnection()) {
            String updateQuery = "UPDATE Task SET task_name = ?, priority = ?, end_time = ? WHERE task_name = ?";
            PreparedStatement pstmt = conn.prepareStatement(updateQuery);
            pstmt.setString(1, newTaskName);
            pstmt.setString(2, priority);
            pstmt.setString(3, endTime);
            pstmt.setString(4, oldTaskName); // Old task name
            pstmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        // Refresh the task list in the dashboard
        dashboard.refreshTasks();
        dispose(); // Close the edit frame
    }
}
