package Dashboard;

import Database.DatabaseConnection;
import Auth.LoginForm;
import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.sql.*;
import java.text.*;
import java.util.*;
import java.util.Date;
import javax.swing.Timer;  // Import for periodic reminders
import java.util.List;
import java.util.ArrayList;



public class ToDoListDashboard extends JFrame {
    private JTable taskTable;
    private DefaultTableModel taskTableModel;
    private int userId; // Store the logged-in user's ID
    private Timer reminderTimer; // Timer for checking reminders
    private List<Task> tasks; // Store tasks for reminder checks

    public ToDoListDashboard(int userId) {
        this.userId = userId; // Set the user ID for this session
        setTitle("To-Do List Dashboard");
        setBounds(300, 90, 900, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);

        Container container = getContentPane();
        container.setLayout(null);
        container.setBackground(Color.WHITE); // Light background

        String[] columnNames = {"Complete", "Task Name", "Priority", "Create Time", "End Time"};
        taskTableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                return (columnIndex == 0) ? Boolean.class : String.class;
            }
        };
        taskTable = new JTable(taskTableModel) {
            @Override
            public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
                Component c = super.prepareRenderer(renderer, row, column);
                boolean completed = (boolean) getValueAt(row, 0); 
                if (completed) {
                    c.setFont(c.getFont().deriveFont(Font.BOLD | Font.ITALIC));
                    c.setForeground(Color.GRAY); 
                } else {
                    c.setFont(c.getFont().deriveFont(Font.PLAIN)); 
                    c.setForeground(Color.BLACK); 
                }
                return c;
            }
        };
        taskTable.setFont(new Font("Arial", Font.PLAIN, 14));
        taskTable.setRowHeight(30);
        taskTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));
        taskTable.getTableHeader().setBackground(new Color(230, 230, 230));
        taskTable.getTableHeader().setForeground(Color.BLACK);

        JScrollPane scrollPane = new JScrollPane(taskTable);
        scrollPane.setBounds(50, 70, 800, 400);
        container.add(scrollPane);

        JButton addTaskButton = new JButton("Add Task");
        addTaskButton.setFont(new Font("Arial", Font.BOLD, 14));
        addTaskButton.setBackground(new Color(76, 175, 80)); 
        addTaskButton.setForeground(Color.WHITE); 
        addTaskButton.setBounds(50, 490, 150, 40);
        addTaskButton.addActionListener(e -> new AddTaskFrame(this, userId)); 
        container.add(addTaskButton);

        JButton deleteTaskButton = new JButton("Delete Task");
        deleteTaskButton.setFont(new Font("Arial", Font.BOLD, 14));
        deleteTaskButton.setBackground(new Color(244, 67, 54)); 
        deleteTaskButton.setForeground(Color.WHITE);
        deleteTaskButton.setBounds(220, 490, 150, 40);
        deleteTaskButton.addActionListener(e -> deleteSelectedTask());
        container.add(deleteTaskButton);

        JButton editTaskButton = new JButton("Edit Task");
        editTaskButton.setFont(new Font("Arial", Font.BOLD, 14));
        editTaskButton.setBackground(new Color(33, 150, 243)); 
        editTaskButton.setForeground(Color.WHITE);
        editTaskButton.setBounds(400, 490, 150, 40);
        editTaskButton.addActionListener(e -> editSelectedTask());
        container.add(editTaskButton);

        JButton logoutButton = new JButton("Logout");
        logoutButton.setFont(new Font("Arial", Font.BOLD, 14));
        logoutButton.setBackground(new Color(244, 67, 54)); 
        logoutButton.setForeground(Color.WHITE); 
        logoutButton.setBounds(650, 20, 150, 40);
        logoutButton.addActionListener(e -> logout());
        container.add(logoutButton);

        JLabel titleLabel = new JLabel("Your Tasks");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setBounds(350, 20, 200, 30);
        titleLabel.setForeground(new Color(33, 37, 41)); 
        container.add(titleLabel);

        tasks = new ArrayList<>(); // Initialize task list
        loadTasks();

        taskTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                int row = taskTable.getSelectedRow();
                if (row >= 0 && evt.getClickCount() == 1) {
                    boolean currentValue = (boolean) taskTableModel.getValueAt(row, 0);
                    taskTableModel.setValueAt(!currentValue, row, 0);
                    updateTaskCompletion(row, !currentValue);
                }
            }
        });

        // Initialize the reminder timer (runs every 1 minute = 60000 ms)
        reminderTimer = new Timer(60000, e -> checkForReminders());
        reminderTimer.start(); // Start the timer

        setVisible(true);
    }

    public void loadTasks() {
        taskTableModel.setRowCount(0); 
        tasks.clear(); // Clear existing tasks
        try (Connection conn = DatabaseConnection.getConnection()) {
            String query = "SELECT * FROM Task WHERE user_id = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                String taskName = rs.getString("task_name");
                String priority = rs.getString("priority");
                String createTime = rs.getString("create_time");
                String endTime = rs.getString("end_time");
                boolean completed = rs.getBoolean("completed");

                // Add task to the table and list
                Task task = new Task(taskName, priority, createTime, endTime, completed);
                tasks.add(task);
                taskTableModel.addRow(new Object[]{
                    completed, 
                    taskName, 
                    priority, 
                    createTime, 
                    completed ? "" : endTime
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Method to check for upcoming task deadlines
    private void checkForReminders() {
        Date now = new Date(); // Get current time
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        for (Task task : tasks) {
            if (!task.isCompleted() && !task.getEndTime().isEmpty()) {
                try {
                    Date taskEndTime = sdf.parse(task.getEndTime());
                    // Check if the task is within 10 minutes of the deadline
                    long timeDifference = taskEndTime.getTime() - now.getTime();
                    if (timeDifference > 0 && timeDifference <= 10 * 60 * 1000) {
                        // Show reminder popup
                        JOptionPane.showMessageDialog(this, 
                            "Reminder: Task \"" + task.getTaskName() + "\" is nearing its deadline!",
                            "Task Reminder", JOptionPane.WARNING_MESSAGE);
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    // Other methods like updateTaskCompletion, deleteSelectedTask, editSelectedTask, etc.
    // (You can retain them as in the original code)

    private void logout() {
        dispose(); 
        new LoginForm();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ToDoListDashboard(1)); 
    }
}
