package Dashboard;

import Database.DatabaseConnection;
import Auth.LoginForm;
import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.sql.*;
import java.text.*;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Arrays;

public class ToDoListDashboard extends JFrame {
    private JTable taskTable;
    private DefaultTableModel taskTableModel;
    private int userId; // Store the logged-in user's ID

    public ToDoListDashboard(int userId) {
        this.userId = userId; // Set the user ID for this session
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
        taskTable = new JTable(taskTableModel) {
            @Override
            public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
                Component c = super.prepareRenderer(renderer, row, column);
                boolean completed = (boolean) getValueAt(row, 0); // Check if the task is completed
                if (completed) {
                    c.setFont(c.getFont().deriveFont(Font.BOLD | Font.ITALIC)); // Strikethrough style
                    c.setForeground(Color.GRAY); // Gray text for completed tasks
                } else {
                    c.setFont(c.getFont().deriveFont(Font.PLAIN)); // Regular font for incomplete tasks
                    c.setForeground(Color.BLACK); // Black text for incomplete tasks
                }
                return c;
            }
        };
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
        addTaskButton.addActionListener(e -> new AddTaskFrame(this, userId)); // Pass user ID
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

        // Button to log out
        JButton logoutButton = new JButton("Logout");
        logoutButton.setFont(new Font("Arial", Font.BOLD, 14));
        logoutButton.setBackground(new Color(244, 67, 54)); // Red color
        logoutButton.setForeground(Color.WHITE); // White text
        logoutButton.setBorder(BorderFactory.createLineBorder(new Color(229, 57, 53), 2));
        logoutButton.setFocusPainted(false);
        logoutButton.setBounds(650, 20, 150, 40);
        logoutButton.addActionListener(e -> logout());
        container.add(logoutButton);

        // Label to show the title
        JLabel titleLabel = new JLabel("Your Tasks");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setBounds(350, 20, 200, 30);
        titleLabel.setForeground(new Color(33, 37, 41)); // Dark color
        container.add(titleLabel);

        // Load tasks from the database for the logged-in user
        loadTasks();

        // Single click listener to toggle task completion
        taskTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                int row = taskTable.getSelectedRow();
                if (row >= 0 && evt.getClickCount() == 1) { // Single click to toggle completion
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
            // Select tasks for the logged-in user
            String query = "SELECT * FROM Task WHERE user_id = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();

            // Store tasks in a list for sorting
            List<Task> tasks = new ArrayList<>();

            while (rs.next()) {
                String taskName = rs.getString("task_name");
                String priority = rs.getString("priority");
                String createTime = rs.getString("create_time");
                String endTime = rs.getString("end_time");
                boolean completed = rs.getBoolean("completed");

                // Add the task to the list
                tasks.add(new Task(taskName, priority, createTime, endTime, completed));
            }

            // Sort tasks
            Collections.sort(tasks, (t1, t2) -> {
                if (t1.isCompleted() && !t2.isCompleted()) return 1; // t1 comes after t2
                if (!t1.isCompleted() && t2.isCompleted()) return -1; // t1 comes before t2

                // If both are incomplete or completed, sort by priority and end time
                int priorityComparison = comparePriority(t1.getPriority(), t2.getPriority());
                if (priorityComparison != 0) return priorityComparison;

                // If priorities are equal, sort by end time
                return t1.getEndTime().compareTo(t2.getEndTime());
            });

            // Add sorted tasks to the table model
            for (Task task : tasks) {
                taskTableModel.addRow(new Object[]{
                    task.isCompleted(), 
                    task.getTaskName(), 
                    task.getPriority(), 
                    task.getCreateTime(), 
                    task.isCompleted() ? "" : task.getEndTime()
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private int comparePriority(String priority1, String priority2) {
        String[] priorities = {"High", "Medium", "Low"};
        int index1 = Arrays.asList(priorities).indexOf(priority1);
        int index2 = Arrays.asList(priorities).indexOf(priority2);
        return Integer.compare(index1, index2);
    }

    private void updateTaskCompletion(int row, boolean completed) {
        String taskName = (String) taskTableModel.getValueAt(row, 1); // Get task name

        // Update the database for this task
        try (Connection conn = DatabaseConnection.getConnection()) {
            String updateQuery = "UPDATE Task SET completed = ? WHERE task_name = ? AND user_id = ?";
            PreparedStatement stmt = conn.prepareStatement(updateQuery);
            stmt.setBoolean(1, completed);
            stmt.setString(2, taskName);
            stmt.setInt(3, userId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        loadTasks();
    }

    private void deleteSelectedTask() {
        int selectedRow = taskTable.getSelectedRow();
        if (selectedRow >= 0) {
            String taskName = (String) taskTableModel.getValueAt(selectedRow, 1); // Get task name

            // Remove from the database
            try (Connection conn = DatabaseConnection.getConnection()) {
                String deleteQuery = "DELETE FROM Task WHERE task_name = ? AND user_id = ?";
                PreparedStatement stmt = conn.prepareStatement(deleteQuery);
                stmt.setString(1, taskName);
                stmt.setInt(2, userId);
                stmt.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            
            // Remove from the table
            taskTableModel.removeRow(selectedRow);
            loadTasks(); // Reload the task list
        } else {
            JOptionPane.showMessageDialog(this, "Please select a task to delete.", "No Task Selected", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void editSelectedTask() {
        int selectedRow = taskTable.getSelectedRow();
        if (selectedRow >= 0) {
            String taskName = (String) taskTableModel.getValueAt(selectedRow, 1);
            String priority = (String) taskTableModel.getValueAt(selectedRow, 2);
            String endTimeStr = (String) taskTableModel.getValueAt(selectedRow, 4);
            
            // Parse end time to Date
            Date endTime = null;
            if (!endTimeStr.isEmpty()) {
                try {
                    endTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(endTimeStr);
                } catch (ParseException e) {
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(this, "Error parsing end time", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }
    
            new EditTaskFrame(this, taskName, priority, endTime); // Pass task name, priority, and end time
        } else {
            JOptionPane.showMessageDialog(this, "Please select a task to edit.", "No Task Selected", JOptionPane.WARNING_MESSAGE);
        }
    }
    

    private void logout() {
        // Clear user session and return to login screen
        dispose(); // Close the dashboard
        new LoginForm(); // Assuming you have a LoginFrame class
    }

    public static void main(String[] args) {
        // Example usage (replace with actual user login mechanism)
        SwingUtilities.invokeLater(() -> new ToDoListDashboard(1)); // Pass user ID from the login process
    }
}
