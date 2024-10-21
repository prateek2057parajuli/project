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

        Container container = getContentPane();
        container.setLayout(null);

        // Initialize table
        String[] columnNames = {"Task Name", "Priority", "Create Time", "End Time"};
        taskTableModel = new DefaultTableModel(columnNames, 0);
        taskTable = new JTable(taskTableModel);
        JScrollPane scrollPane = new JScrollPane(taskTable);
        scrollPane.setBounds(50, 50, 800, 300);
        container.add(scrollPane);

        // Button to add new task
        JButton addTaskButton = new JButton("Add Task");
        addTaskButton.setBounds(50, 400, 150, 30);
        addTaskButton.addActionListener(e -> new AddTaskFrame(this));
        container.add(addTaskButton);

        // Load tasks from the database
        loadTasks();
        setVisible(true);
    }

    public void loadTasks() {
        taskTableModel.setRowCount(0);  // Clear table before reloading
        try (Connection conn = DatabaseConnection.getConnection()) {
            String query = "SELECT * FROM Task ORDER BY FIELD(priority, 'High', 'Medium', 'Low'), end_time ASC";
            PreparedStatement stmt = conn.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                String taskName = rs.getString("task_name");
                String priority = rs.getString("priority");
                String createTime = rs.getString("create_time");
                String endTime = rs.getString("end_time");

                taskTableModel.addRow(new Object[]{taskName, priority, createTime, endTime});
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new ToDoListDashboard();
    }
}
