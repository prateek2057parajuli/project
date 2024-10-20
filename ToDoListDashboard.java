import java.awt.Container;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;

public class ToDoListDashboard extends JFrame implements ActionListener {

    // Components of the dashboard
    private Container container;
    private JLabel titleLabel;
    private JTextField taskField;
    private JButton addTaskButton;
    private JButton deleteTaskButton;
    private JButton updateTaskButton;
    private JButton markCompletedButton;
    private JButton sortTaskButton;
    private JButton searchTaskButton;
    private JTextField searchField;
    private JList<String> taskListDisplay;
    private DefaultListModel<String> listModel;
    private JScrollPane scrollPane;

    // List to store tasks
    private ArrayList<String> taskList;

    // Variable to hold the selected index for updating a task
    private int selectedIndexForUpdate = -1;

    // Constructor to set up the dashboard
    public ToDoListDashboard() {
        setTitle("To-Do List Dashboard");
        setBounds(300, 90, 500, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);

        container = getContentPane();
        container.setLayout(null);

        titleLabel = new JLabel("To-Do List");
        titleLabel.setFont(new Font("Arial", Font.PLAIN, 30));
        titleLabel.setSize(200, 30);
        titleLabel.setLocation(150, 20);
        container.add(titleLabel);

        taskField = new JTextField();
        taskField.setFont(new Font("Arial", Font.PLAIN, 15));
        taskField.setSize(250, 20);
        taskField.setLocation(50, 70);
        container.add(taskField);

        addTaskButton = new JButton("Add Task");
        addTaskButton.setFont(new Font("Arial", Font.PLAIN, 15));
        addTaskButton.setSize(100, 20);
        addTaskButton.setLocation(320, 70);
        addTaskButton.addActionListener(this);
        container.add(addTaskButton);

        deleteTaskButton = new JButton("Delete Task");
        deleteTaskButton.setFont(new Font("Arial", Font.PLAIN, 15));
        deleteTaskButton.setSize(150, 20);
        deleteTaskButton.setLocation(50, 320);
        deleteTaskButton.addActionListener(this);
        container.add(deleteTaskButton);

        updateTaskButton = new JButton("Update Task");
        updateTaskButton.setFont(new Font("Arial", Font.PLAIN, 15));
        updateTaskButton.setSize(150, 20);
        updateTaskButton.setLocation(50, 350);
        updateTaskButton.addActionListener(this);
        container.add(updateTaskButton);

        markCompletedButton = new JButton("Mark as Completed");
        markCompletedButton.setFont(new Font("Arial", Font.PLAIN, 15));
        markCompletedButton.setSize(180, 20);
        markCompletedButton.setLocation(250, 320);
        markCompletedButton.addActionListener(this);
        container.add(markCompletedButton);

        sortTaskButton = new JButton("Sort Tasks");
        sortTaskButton.setFont(new Font("Arial", Font.PLAIN, 15));
        sortTaskButton.setSize(150, 20);
        sortTaskButton.setLocation(250, 350);
        sortTaskButton.addActionListener(this);
        container.add(sortTaskButton);

        searchField = new JTextField();
        searchField.setFont(new Font("Arial", Font.PLAIN, 15));
        searchField.setSize(250, 20);
        searchField.setLocation(50, 380);
        container.add(searchField);

        searchTaskButton = new JButton("Search Task");
        searchTaskButton.setFont(new Font("Arial", Font.PLAIN, 15));
        searchTaskButton.setSize(150, 20);
        searchTaskButton.setLocation(320, 380);
        searchTaskButton.addActionListener(this);
        container.add(searchTaskButton);

        taskList = new ArrayList<>();
        listModel = new DefaultListModel<>();
        taskListDisplay = new JList<>(listModel);
        taskListDisplay.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        taskListDisplay.setFont(new Font("Arial", Font.PLAIN, 15));

        scrollPane = new JScrollPane(taskListDisplay);
        scrollPane.setBounds(50, 110, 370, 200);
        container.add(scrollPane);

        setVisible(true);
    }

    // Action handler for buttons
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == addTaskButton) {
            String task = taskField.getText().trim();
            if (!task.isEmpty()) {
                taskList.add(task);
                listModel.addElement(task);
                taskField.setText("");  // Clear the input field after adding the task
            } else {
                JOptionPane.showMessageDialog(this, "Task cannot be empty", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else if (e.getSource() == deleteTaskButton) {
            int selectedIndex = taskListDisplay.getSelectedIndex();
            if (selectedIndex != -1) {
                taskList.remove(selectedIndex);
                listModel.remove(selectedIndex);
            } else {
                JOptionPane.showMessageDialog(this, "Please select a task to delete", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else if (e.getSource() == updateTaskButton) {
            selectedIndexForUpdate = taskListDisplay.getSelectedIndex();
            if (selectedIndexForUpdate != -1) {
                String selectedTask = taskList.get(selectedIndexForUpdate);
                taskField.setText(selectedTask); // Set selected task for updating
            } else {
                JOptionPane.showMessageDialog(this, "Please select a task to update", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else if (e.getSource() == markCompletedButton) {
            int selectedIndex = taskListDisplay.getSelectedIndex();
            if (selectedIndex != -1) {
                String completedTask = taskList.remove(selectedIndex);
                listModel.remove(selectedIndex);
                JOptionPane.showMessageDialog(this, "Task '" + completedTask + "' marked as completed!", "Task Completed", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Please select a task to mark as completed", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else if (e.getSource() == sortTaskButton) {
            // Sort the taskList alphabetically using Collections.sort
            Collections.sort(taskList);
            updateTaskListDisplay();
        } else if (e.getSource() == searchTaskButton) {
            String searchTerm = searchField.getText().trim();
            if (!searchTerm.isEmpty()) {
                int index = searchTask(searchTerm);
                if (index != -1) {
                    taskListDisplay.setSelectedIndex(index);
                    JOptionPane.showMessageDialog(this, "Task found: " + searchTerm, "Search Result", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this, "Task not found", "Search Result", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        }
    }

    // Method to update the task list display after sorting or other operations
    private void updateTaskListDisplay() {
        listModel.clear();
        for (String task : taskList) {
            listModel.addElement(task);
        }
    }

    // Simple linear search algorithm to find a task by its name
    private int searchTask(String searchTerm) {
        for (int i = 0; i < taskList.size(); i++) {
            if (taskList.get(i).equalsIgnoreCase(searchTerm)) {
                return i;  // Return the index of the found task
            }
        }
        return -1;  // Task not found
    }

    // Main method to run the dashboard directly
    public static void main(String[] args) {
        new ToDoListDashboard();
    }
}

