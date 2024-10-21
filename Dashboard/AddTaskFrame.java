package Dashboard;

import Database.DatabaseConnection;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class AddTaskFrame extends JFrame implements ActionListener {
    private JTextField taskNameField;
    private JComboBox<String> priorityComboBox;
    private JSpinner endTimeSpinner;  // Date spinner
    private ToDoListDashboard dashboard;

    public AddTaskFrame(ToDoListDashboard dashboard) {
        this.dashboard = dashboard;

        setTitle("Add Task");
        setBounds(400, 200, 400, 300);
        setLayout(null);

        JLabel taskLabel = new JLabel("Task Name:");
        taskLabel.setBounds(20, 20, 100, 30);
        add(taskLabel);

        taskNameField = new JTextField();
        taskNameField.setBounds(120, 20, 200, 30);
        add(taskNameField);

        JLabel priorityLabel = new JLabel("Priority:");
        priorityLabel.setBounds(20, 70, 100, 30);
        add(priorityLabel);

        String[] priorities = {"High", "Medium", "Low"};
        priorityComboBox = new JComboBox<>(priorities);
        priorityComboBox.setBounds(120, 70, 200, 30);
        add(priorityComboBox);

        JLabel endTimeLabel = new JLabel("End Time:");
        endTimeLabel.setBounds(20, 120, 100, 30);
        add(endTimeLabel);

        // Initialize date spinner
        SpinnerDateModel model = new SpinnerDateModel();
        endTimeSpinner = new JSpinner(model);
        JSpinner.DateEditor editor = new JSpinner.DateEditor(endTimeSpinner, "yyyy-MM-dd HH:mm:ss");
        endTimeSpinner.setEditor(editor);
        endTimeSpinner.setBounds(120, 120, 200, 30);
        add(endTimeSpinner);

        JButton addButton = new JButton("Add Task");
        addButton.setBounds(150, 180, 100, 30);
        addButton.addActionListener(this);
        add(addButton);

        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String taskName = taskNameField.getText().trim();
        String priority = (String) priorityComboBox.getSelectedItem();
        Date endDate = (Date) endTimeSpinner.getValue();  // Get date from spinner

        if (taskName.isEmpty() || endDate == null) {
            JOptionPane.showMessageDialog(this, "Task name and end time cannot be empty", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Format the date to the desired format
        String formattedEndTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(endDate);
        
        try (Connection conn = DatabaseConnection.getConnection()) {
            String query = "INSERT INTO Task (task_name, priority, create_time, end_time) VALUES (?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, taskName);
            stmt.setString(2, priority);
            stmt.setString(3, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
            stmt.setString(4, formattedEndTime);

            stmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "Task added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            dashboard.loadTasks();  // Reload the task list
            dispose();  // Close AddTask frame
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Failed to add task", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

}
