package Dashboard;

import Database.DatabaseConnection;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class ToDoListDashboard extends JFrame {
    private JTable taskTable;
    private DefaultTableModel taskTableModel;

    public ToDoListDashboard() {
        setTitle("To-Do List Dashboard");
        setBounds(300, 90, 900, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);

        // Create a container with a light background color
        Container container = getContentPane();
        container.setLayout(null);
        container.setBackground(Color.WHITE); // Light background

        // Initialize table with an additional column for completion
        String[] columnNames = {"Complete", "Task Name", "Priority", "Create Time", "End Time"};
        taskTableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                // Set the first column to be a checkbox
                return (columnIndex == 0) ? Boolean.class : String.class;
            }
        };
        taskTable = new JTable(taskTableModel);
        taskTable.setFont(new Font("Arial", Font.PLAIN, 14));
        taskTable.setRowHeight(30);
        taskTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));
        taskTable.getTableHeader().setBackground(new Color(230, 230, 230)); // Light gray header
        taskTable.getTableHeader().setForeground(Color.BLACK); // Black text

        JScrollPane scrollPane = new JScrollPane(taskTable);
        scrollPane.setBounds(50, 70, 800, 400);
        container.add(scrollPane);

        // Button to add new task with a modern look
        JButton addTaskButton = new JButton("Add Task");
        addTaskButton.setFont(new Font("Arial", Font.BOLD, 14));
        addTaskButton.setBackground(new Color(76, 175, 80)); // Green color
        addTaskButton.setForeground(Color.WHITE); // White text
        addTaskButton.setBorder(BorderFactory.createLineBorder(new Color(67, 160, 71), 2));
        addTaskButton.setFocusPainted(false);
        addTaskButton.setBounds(50, 490, 150, 40);
        addTaskButton.addActionListener(e -> new AddTaskFrame(this));
        container.add(addTaskButton);

        // Button to delete a task
        JButton deleteTaskButton = new JButton("Delete Task");
        deleteTaskButton.setFont(new Font("Arial", Font.BOLD, 14));
        deleteTaskButton.setBackground(new Color(244, 67, 54)); // Red color
        deleteTaskButton.setForeground(Color.WHITE); // White text
        deleteTaskButton.setBorder(BorderFactory.createLineBorder(new Color(229, 57, 53), 2));
        deleteTaskButton.setFocusPainted(false);
        deleteTaskButton.setBounds(220, 490, 150, 40);
        deleteTaskButton.addActionListener(e -> deleteSelectedTask());
        container.add(deleteTaskButton);

        // Button to edit a task
        JButton editTaskButton = new JButton("Edit Task");
        editTaskButton.setFont(new Font("Arial", Font.BOLD, 14));
        editTaskButton.setBackground(new Color(33, 150, 243)); // Blue color
        editTaskButton.setForeground(Color.WHITE); // White text
        editTaskButton.setBorder(BorderFactory.createLineBorder(new Color(30, 136, 229), 2));
        editTaskButton.setFocusPainted(false);
        editTaskButton.setBounds(400, 490, 150, 40);
        editTaskButton.addActionListener(e -> editSelectedTask());
        container.add(editTaskButton);

        // Label to show the title
        JLabel titleLabel = new JLabel("Your Tasks");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setBounds(350, 20, 200, 30);
        titleLabel.setForeground(new Color(33, 37, 41)); // Dark color
        container.add(titleLabel);

        // Load tasks from the database
        loadTasks();

        // Add checkbox listener to toggle completion
        taskTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                int row = taskTable.getSelectedRow();
                if (row >= 0 && evt.getClickCount() == 2) { // Double-click to toggle completion
                    boolean currentValue = (boolean) taskTableModel.getValueAt(row, 0);
                    taskTableModel.setValueAt(!currentValue, row, 0); // Toggle checkbox

                    // Update the database for this task
                    updateTaskCompletion(row, !currentValue);
                }
            }
        });

        setVisible(true);
    }

    public void loadTasks() {
        taskTableModel.setRowCount(0);  // Clear table before reloading
        try (Connection conn = DatabaseConnection.getConnection()) {
            // Modified query to select all tasks and sort completed tasks at the end
            String query = "SELECT * FROM Task ORDER BY CASE WHEN completed = 1 THEN 1 ELSE 0 END, "
                         + "FIELD(priority, 'High', 'Medium', 'Low'), end_time ASC";
            PreparedStatement stmt = conn.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                String taskName = rs.getString("task_name");
                String priority = rs.getString("priority");
                String createTime = rs.getString("create_time");
                String endTime = rs.getString("end_time");
                boolean completed = rs.getBoolean("completed");

                // Add the row to the table
                // If the task is completed, show an empty string for the end time
                taskTableModel.addRow(new Object[]{completed, taskName, priority, createTime, completed ? "" : endTime});
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void updateTaskCompletion(int row, boolean completed) {
        String taskName = (String) taskTableModel.getValueAt(row, 1); // Get task name

        // Update the database for this task
        try (Connection conn = DatabaseConnection.getConnection()) {
            String updateQuery = "UPDATE Task SET completed = ? WHERE task_name = ?";
            PreparedStatement pstmt = conn.prepareStatement(updateQuery);
            pstmt.setBoolean(1, completed);
            pstmt.setString(2, taskName); // Assuming task name is unique
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        loadTasks(); // Reload tasks to reflect changes
    }

    private void deleteSelectedTask() {
        int row = taskTable.getSelectedRow();
        if (row >= 0) {
            String taskName = (String) taskTableModel.getValueAt(row, 1); // Get task name

            // Delete the task from the database
            try (Connection conn = DatabaseConnection.getConnection()) {
                String deleteQuery = "DELETE FROM Task WHERE task_name = ?";
                PreparedStatement pstmt = conn.prepareStatement(deleteQuery);
                pstmt.setString(1, taskName); // Assuming task name is unique
                pstmt.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            loadTasks(); // Reload tasks to reflect changes
        } else {
            JOptionPane.showMessageDialog(this, "Please select a task to delete.", "No Task Selected", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void editSelectedTask() {
        int row = taskTable.getSelectedRow();
        if (row >= 0) {
            String taskName = (String) taskTableModel.getValueAt(row, 1);
            String priority = (String) taskTableModel.getValueAt(row, 2);
            String endTime = (String) taskTableModel.getValueAt(row, 4);

            // Open edit dialog
            EditTaskFrame editFrame = new EditTaskFrame(this, taskName, priority, endTime);
            editFrame.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(this, "Please select a task to edit.", "No Task Selected", JOptionPane.WARNING_MESSAGE);
        }
    }

    public void refreshTasks() {
        loadTasks(); // Reload tasks from the database after editing
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ToDoListDashboard());
    }
}
