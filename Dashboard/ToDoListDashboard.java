package Dashboard;


import Database.DatabaseConnection;
import Auth.LoginForm;
import Auth.SignupForm;
import java.awt.Color;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;

public class ToDoListDashboard extends JFrame implements ActionListener {

    // Components of the dashboard
    private Container container;
    private JLabel titleLabel;
    private JTextField taskField;
    private JComboBox<String> priorityComboBox;
    private JButton addTaskButton;
    private JButton deleteTaskButton;
    private JButton updateTaskButton;
    private JButton markCompletedButton;
    private JButton sortTaskByPriorityButton;
    private JButton searchTaskButton;
    private JButton generateReportButton;
    private JTextField searchField;

    // Table to display tasks with date
    private JTable taskTable;
    private DefaultTableModel taskTableModel;

    // Table for completed tasks
    private JTable completedTaskTable;
    private DefaultTableModel completedTableModel;

    // Lists to store tasks and completed tasks
    private ArrayList<Task> taskList;
    private ArrayList<CompletedTask> completedTasks;

    // Variable to hold the selected index for updating a task
    private int selectedIndexForUpdate = -1;

    // Date format
    private SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy");

    // Constructor to set up the dashboard
    public ToDoListDashboard() {
        setTitle("To-Do List Dashboard");
        setBounds(300, 90, 900, 750);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);

        container = getContentPane();
        container.setLayout(null);

        // Background color for the container
        container.setBackground(new Color(245, 245, 245));

        titleLabel = new JLabel("To-Do List");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 36));
        titleLabel.setSize(300, 40);
        titleLabel.setLocation(240, 20);
        titleLabel.setForeground(new Color(60, 60, 60));
        container.add(titleLabel);

        taskField = new JTextField();
        taskField.setFont(new Font("Arial", Font.PLAIN, 18));
        taskField.setSize(250, 30);
        taskField.setLocation(50, 80);
        container.add(taskField);

        // Priority ComboBox
        String[] priorities = {"High", "Medium", "Low"};
        priorityComboBox = new JComboBox<>(priorities);
        priorityComboBox.setFont(new Font("Arial", Font.PLAIN, 18));
        priorityComboBox.setSize(100, 30);
        priorityComboBox.setLocation(320, 80);
        container.add(priorityComboBox);

        // Task List setup with table for Task Name, Priority, and Date
        taskList = new ArrayList<>();
        String[] columnNames = {"Task", "Priority", "Date"};
        taskTableModel = new DefaultTableModel(columnNames, 0);
        taskTable = new JTable(taskTableModel);
        JScrollPane taskScrollPane = new JScrollPane(taskTable);
        taskScrollPane.setBounds(50, 130, 480, 270);
        container.add(taskScrollPane);

        // Button Styling
        addTaskButton = new JButton("Add Task");
        styleButton(addTaskButton);
        addTaskButton.setSize(180, 30);
        addTaskButton.setLocation(540, 130);
        addTaskButton.addActionListener(this);
        container.add(addTaskButton);

        deleteTaskButton = new JButton("Delete Task");
        styleButton(deleteTaskButton);
        deleteTaskButton.setSize(180, 30);
        deleteTaskButton.setLocation(540, 170);
        deleteTaskButton.addActionListener(this);
        container.add(deleteTaskButton);

        updateTaskButton = new JButton("Update Task");
        styleButton(updateTaskButton);
        updateTaskButton.setSize(180, 30);
        updateTaskButton.setLocation(540, 210);
        updateTaskButton.addActionListener(this);
        container.add(updateTaskButton);

        markCompletedButton = new JButton("Mark as Completed");
        styleButton(markCompletedButton);
        markCompletedButton.setSize(180, 30);
        markCompletedButton.setLocation(540, 250);
        markCompletedButton.addActionListener(this);
        container.add(markCompletedButton);

        sortTaskByPriorityButton = new JButton("Sort by Priority");
        styleButton(sortTaskByPriorityButton);
        sortTaskByPriorityButton.setSize(180, 30);
        sortTaskByPriorityButton.setLocation(540, 290);
        sortTaskByPriorityButton.addActionListener(this);
        container.add(sortTaskByPriorityButton);

        searchField = new JTextField();
        searchField.setFont(new Font("Arial", Font.PLAIN, 18));
        searchField.setSize(250, 30);
        searchField.setLocation(540, 340);
        container.add(searchField);

        searchTaskButton = new JButton("Search Task");
        styleButton(searchTaskButton);
        searchTaskButton.setSize(180, 30);
        searchTaskButton.setLocation(540, 380);
        searchTaskButton.addActionListener(this);
        container.add(searchTaskButton);

        // Generate Report Button
        generateReportButton = new JButton("Generate Report");
        styleButton(generateReportButton);
        generateReportButton.setSize(180, 30);
        generateReportButton.setLocation(540, 420);
        generateReportButton.addActionListener(this);
        container.add(generateReportButton);

        // Completed Task Table setup with Task Name and Completion Date
        completedTasks = new ArrayList<>();
        String[] completedColumnNames = {"Completed Tasks", "Completion Date"};
        completedTableModel = new DefaultTableModel(completedColumnNames, 0);
        completedTaskTable = new JTable(completedTableModel);
        JScrollPane completedScrollPane = new JScrollPane(completedTaskTable);
        completedScrollPane.setBounds(50, 420, 480, 100);
        container.add(completedScrollPane);

        setVisible(true);
    }

    // Method to style the buttons uniformly
    private void styleButton(JButton button) {
        button.setFont(new Font("Arial", Font.PLAIN, 16));
        button.setBackground(new Color(72, 118, 255));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    // Task class to store task name, priority, and date
    private class Task {
        String taskName;
        String priority;
        String date;

        Task(String taskName, String priority, String date) {
            this.taskName = taskName;
            this.priority = priority;
            this.date = date;
        }
    }

    // CompletedTask class to store completed task name and completion date
    private class CompletedTask {
        String taskName;
        String completionDate;

        CompletedTask(String taskName, String completionDate) {
            this.taskName = taskName;
            this.completionDate = completionDate;
        }
    }

    // Action handler for buttons
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == addTaskButton) {
            String taskName = taskField.getText().trim();
            String priority = (String) priorityComboBox.getSelectedItem();
            String date = dateFormatter.format(new Date());

            // Check for empty task name
            if (taskName.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Task cannot be empty", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Check for duplicate task
            boolean isDuplicate = taskList.stream()
                .anyMatch(task -> task.taskName.equalsIgnoreCase(taskName));

            if (isDuplicate) {
                JOptionPane.showMessageDialog(this, "Task already exists!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Add task if no duplicates found
            Task task = new Task(taskName, priority, date);
            taskList.add(task);
            taskTableModel.addRow(new Object[]{taskName, priority, date});
            taskField.setText("");  // Clear the input field after adding the task
        } else if (e.getSource() == deleteTaskButton) {
            int selectedRow = taskTable.getSelectedRow();
            if (selectedRow != -1) {
                taskList.remove(selectedRow);
                taskTableModel.removeRow(selectedRow);
            } else {
                JOptionPane.showMessageDialog(this, "Please select a task to delete", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else if (e.getSource() == updateTaskButton) {
            selectedIndexForUpdate = taskTable.getSelectedRow();
            if (selectedIndexForUpdate != -1) {
                Task selectedTask = taskList.get(selectedIndexForUpdate);
                taskField.setText(selectedTask.taskName); // Set selected task for updating
            } else {
                JOptionPane.showMessageDialog(this, "Please select a task to update", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else if (e.getSource() == markCompletedButton) {
            int selectedRow = taskTable.getSelectedRow();
            if (selectedRow != -1) {
                // Mark the task as completed
                Task completedTask = taskList.remove(selectedRow);
                taskTableModel.removeRow(selectedRow);

                // Add task to completed list
                String completionDate = dateFormatter.format(new Date());
                CompletedTask task = new CompletedTask(completedTask.taskName, completionDate);
                completedTasks.add(task);
                completedTableModel.addRow(new Object[]{task.taskName, task.completionDate});

                JOptionPane.showMessageDialog(this, "Task marked as completed!", "Success", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Please select a task to mark as completed", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else if (e.getSource() == sortTaskByPriorityButton) {
            Collections.sort(taskList, Comparator.comparingInt(task -> {
                switch (task.priority) {
                    case "High": return 1;
                    case "Medium": return 2;
                    case "Low": return 3;
                    default: return 4;
                }
            }));
            taskTableModel.setRowCount(0);  // Clear the table
            for (Task task : taskList) {
                taskTableModel.addRow(new Object[]{task.taskName, task.priority, task.date});
            }
        } else if (e.getSource() == searchTaskButton) {
            String searchText = searchField.getText().trim();
            taskTableModel.setRowCount(0);  // Clear the table
            for (Task task : taskList) {
                if (task.taskName.toLowerCase().contains(searchText.toLowerCase())) {
                    taskTableModel.addRow(new Object[]{task.taskName, task.priority, task.date});
                }
            }
        } else if (e.getSource() == generateReportButton) {
            generateReport();
        }
    }

    // Method to generate a report of completed tasks
    private void generateReport() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new FileNameExtensionFilter("Text Files", "txt"));
        int userSelection = fileChooser.showSaveDialog(this);
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToSave = fileChooser.getSelectedFile();
            try (FileWriter writer = new FileWriter(fileToSave)) {
                writer.write("Completed Tasks Report\n");
                writer.write("-----------------------\n");
                for (CompletedTask task : completedTasks) {
                    writer.write(task.taskName + " - " + task.completionDate + "\n");
                }
                JOptionPane.showMessageDialog(this, "Report generated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Error saving report: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // Main method to run the application
    public static void main(String[] args) {
        new ToDoListDashboard();
    }
}
