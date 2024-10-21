package Dashboard;

import Database.DatabaseConnection;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.SpinnerDateModel;

public class EditTaskFrame extends JFrame implements ActionListener {

    private Container container;
    private JLabel title;
    private JLabel taskNameLabel;
    private JTextField taskNameField;
    private JLabel priorityLabel;
    private JComboBox<String> priorityComboBox;
    private JLabel endTimeLabel;
    private JSpinner endTimeSpinner;  // Date spinner
    private JButton updateButton;
    private JButton cancelButton;
    private ToDoListDashboard dashboard;
    private String originalTaskName;  // To keep track of the original task name for the database update

    public EditTaskFrame(ToDoListDashboard dashboard, String taskName, String priority, Date endTime) {
        this.dashboard = dashboard;
        this.originalTaskName = taskName;

        setTitle("Edit Task");
        setBounds(300, 90, 500, 450);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setResizable(false);

        container = getContentPane();
        container.setLayout(null);
        container.setBackground(new Color(224, 224, 224)); // Background color

        title = new JLabel("Edit Task");
        title.setFont(new Font("Verdana", Font.BOLD, 30));
        title.setForeground(new Color(33, 37, 41)); // Dark color
        title.setSize(300, 40);
        title.setLocation(150, 30);
        container.add(title);

        taskNameLabel = new JLabel("Task Name:");
        taskNameLabel.setFont(new Font("Verdana", Font.PLAIN, 18));
        taskNameLabel.setSize(120, 30);
        taskNameLabel.setLocation(50, 100);
        container.add(taskNameLabel);

        taskNameField = new JTextField(taskName);
        taskNameField.setFont(new Font("Verdana", Font.PLAIN, 15));
        taskNameField.setSize(250, 30);
        taskNameField.setLocation(180, 100);
        taskNameField.setBorder(new LineBorder(new Color(120, 144, 156), 2)); // Soft border color
        container.add(taskNameField);

        priorityLabel = new JLabel("Priority:");
        priorityLabel.setFont(new Font("Verdana", Font.PLAIN, 18));
        priorityLabel.setSize(120, 30);
        priorityLabel.setLocation(50, 150);
        container.add(priorityLabel);

        String[] priorities = {"High", "Medium", "Low"};
        priorityComboBox = new JComboBox<>(priorities);
        priorityComboBox.setFont(new Font("Verdana", Font.PLAIN, 15));
        priorityComboBox.setSize(250, 30);
        priorityComboBox.setLocation(180, 150);
        priorityComboBox.setSelectedItem(priority); // Set the original priority
        container.add(priorityComboBox);

        endTimeLabel = new JLabel("End Time:");
        endTimeLabel.setFont(new Font("Verdana", Font.PLAIN, 18));
        endTimeLabel.setSize(120, 30);
        endTimeLabel.setLocation(50, 200);
        container.add(endTimeLabel);

        // Initialize date spinner with the original end time
        SpinnerDateModel model = new SpinnerDateModel(endTime, null, null, java.util.Calendar.HOUR_OF_DAY);
        endTimeSpinner = new JSpinner(model);
        JSpinner.DateEditor editor = new JSpinner.DateEditor(endTimeSpinner, "yyyy-MM-dd HH:mm:ss");
        endTimeSpinner.setEditor(editor);
        endTimeSpinner.setFont(new Font("Verdana", Font.PLAIN, 15));
        endTimeSpinner.setSize(250, 30);
        endTimeSpinner.setLocation(180, 200);
        endTimeSpinner.setBorder(new LineBorder(new Color(120, 144, 156), 2)); // Soft border color
        container.add(endTimeSpinner);

        // Update button with style
        updateButton = new JButton("Update Task");
        updateButton.setFont(new Font("Verdana", Font.BOLD, 15));
        updateButton.setSize(120, 35);
        updateButton.setLocation(120, 280);
        updateButton.setBackground(new Color(41, 121, 255)); // Blue color
        updateButton.setForeground(Color.WHITE); // White text
        updateButton.setBorder(new LineBorder(new Color(33, 150, 243), 2));
        updateButton.setFocusPainted(false);
        updateButton.addActionListener(this);
        container.add(updateButton);

        // Cancel button with style
        cancelButton = new JButton("Cancel");
        cancelButton.setFont(new Font("Verdana", Font.BOLD, 15));
        cancelButton.setSize(120, 35);
        cancelButton.setLocation(260, 280);
        cancelButton.setBackground(new Color(211, 47, 47)); // Red color
        cancelButton.setForeground(Color.WHITE); // White text
        cancelButton.setBorder(new LineBorder(new Color(198, 40, 40), 2));
        cancelButton.setFocusPainted(false);
        cancelButton.addActionListener(e -> dispose()); // Close the frame on cancel
        container.add(cancelButton);

        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == updateButton) {
            String newTaskName = taskNameField.getText().trim();
            String priority = (String) priorityComboBox.getSelectedItem();
            Date endDate = (Date) endTimeSpinner.getValue();  // Get date from spinner

            if (newTaskName.isEmpty() || endDate == null) {
                JOptionPane.showMessageDialog(this, "Task name and end time cannot be empty", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Format the date to the desired format
            String formattedEndTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(endDate);
            
            try (Connection conn = DatabaseConnection.getConnection()) {
                String query = "UPDATE Task SET task_name = ?, priority = ?, end_time = ? WHERE task_name = ?";
                PreparedStatement stmt = conn.prepareStatement(query);
                stmt.setString(1, newTaskName);
                stmt.setString(2, priority);
                stmt.setString(3, formattedEndTime);
                stmt.setString(4, originalTaskName); // Update based on the original task name

                stmt.executeUpdate();
                JOptionPane.showMessageDialog(this, "Task updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                dashboard.loadTasks();  // Reload the task list
                dispose();  // Close EditTask frame
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Failed to update task", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
