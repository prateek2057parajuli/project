package Dashboard;

import Database.DatabaseConnection;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.*;
import javax.swing.border.LineBorder;

public class EditTaskFrame extends JFrame implements ActionListener {

    private Container container;
    private JLabel title;
    private JLabel taskNameLabel;
    private JTextField taskNameField;
    private JLabel priorityLabel;
    private JComboBox<String> priorityComboBox;
    private JLabel endTimeLabel;
    private JSpinner endTimeSpinner;
    private JButton updateButton;
    private JButton cancelButton;
    private ToDoListDashboard dashboard;
    private String originalTaskName;
    private int userId;

    public EditTaskFrame(ToDoListDashboard dashboard,int userId, String taskName, String priority, String endTime) {
        this.dashboard = dashboard;
        this.originalTaskName = taskName;
        this.userId = userId;
    
        setTitle("Edit Task");
        setBounds(300, 90, 500, 450);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setResizable(false);

        container = getContentPane();
        container.setLayout(null);
        container.setBackground(new Color(224, 224, 224));

        title = new JLabel("Edit Task");
        title.setFont(new Font("Verdana", Font.BOLD, 30));
        title.setForeground(new Color(33, 37, 41));
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
        taskNameField.setBorder(new LineBorder(new Color(120, 144, 156), 2));
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
        priorityComboBox.setSelectedItem(priority);
        container.add(priorityComboBox);

        endTimeLabel = new JLabel("End Time:");
        endTimeLabel.setFont(new Font("Verdana", Font.PLAIN, 18));
        endTimeLabel.setSize(120, 30);
        endTimeLabel.setLocation(50, 200);
        container.add(endTimeLabel);

        
        // Convert endTime (String) to Date for the SpinnerDateModel
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date parsedEndTime = null;
        try {
            parsedEndTime = dateFormat.parse(endTime);
        } catch (Exception ex) {
            System.out.println("Error parsing date: " + ex.getMessage());
        }
        SpinnerDateModel model = new SpinnerDateModel(parsedEndTime, null, null, java.util.Calendar.HOUR_OF_DAY);
        endTimeSpinner = new JSpinner(model);
        JSpinner.DateEditor editor = new JSpinner.DateEditor(endTimeSpinner, "yyyy-MM-dd HH:mm:ss");
        endTimeSpinner.setEditor(editor);
        endTimeSpinner.setFont(new Font("Verdana", Font.PLAIN, 15));
        endTimeSpinner.setSize(250, 30);
        endTimeSpinner.setLocation(180, 200);
        endTimeSpinner.setBorder(new LineBorder(new Color(120, 144, 156), 2));
        container.add(endTimeSpinner);

        updateButton = new JButton("Update Task");
        updateButton.setFont(new Font("Verdana", Font.BOLD, 15));
        updateButton.setSize(120, 35);
        updateButton.setLocation(120, 280);
        updateButton.setBackground(new Color(41, 121, 255));
        updateButton.setForeground(Color.WHITE);
        updateButton.setBorder(new LineBorder(new Color(33, 150, 243), 2));
        updateButton.setFocusPainted(false);
        updateButton.addActionListener(this);
        container.add(updateButton);

        cancelButton = new JButton("Cancel");
        cancelButton.setFont(new Font("Verdana", Font.BOLD, 15));
        cancelButton.setSize(120, 35);
        cancelButton.setLocation(260, 280);
        cancelButton.setBackground(new Color(211, 47, 47));
        cancelButton.setForeground(Color.WHITE);
        cancelButton.setBorder(new LineBorder(new Color(198, 40, 40), 2));
        cancelButton.setFocusPainted(false);
        cancelButton.addActionListener(e -> dispose());
        container.add(cancelButton);

        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == updateButton) {
            String newTaskName = taskNameField.getText();
            String newPriority = (String) priorityComboBox.getSelectedItem();
            String newEndTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(endTimeSpinner.getValue());

            if (newTaskName.isEmpty() || newEndTime == null) {
                JOptionPane.showMessageDialog(this, "Task name and end time cannot be empty", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            try (Connection conn = DatabaseConnection.getConnection()) {
                String query = "UPDATE Task SET task_name = ?, priority = ?, end_time = ? WHERE user_id = ? AND task_name = ?";
                PreparedStatement stmt = conn.prepareStatement(query);
                stmt.setString(1, newTaskName);
                stmt.setString(2, newPriority);
                stmt.setString(3, newEndTime);
                stmt.setInt(4, userId);
                stmt.setString(5, originalTaskName);
                stmt.executeUpdate();
            
    
                JOptionPane.showMessageDialog(this, "Task updated successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
                dashboard.loadTasks(); // Refresh the task list
                dispose();
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Failed to update task", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
