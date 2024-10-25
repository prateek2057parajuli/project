# Task Management System
!["Login"](/img/login.png)<br/>
!["Dashboard"](/img/dashboard.png)
# Task Management System
A Java-based desktop application for managing tasks, allowing users to add, edit, and delete tasks with priorities and end times. This system is designed with a graphical user interface (GUI) using Java Swing and connects to a database to store and retrieve task data.

## Features
- **Add Task**: Users can add new tasks with a name, priority level (High, Medium, Low), and end time.
- **Edit Task**: Modify existing task details, including task name, priority, and end time.
- **Delete Task**: Remove tasks from the list permanently.
- **Task List Display**: All tasks are displayed in the dashboard, showing task details in an organized way.


## Requirements

- **Java 8 or higher**
- **MySQL** (Ensure MySQL server is running on your system)
- **JDBC Connector**: Add the MySQL JDBC driver to your project library.

### Setup Database
- Run the Server and Database in your Local Machine.
- Create a Database name with `taskmanager`;
```sql
CREATE DATABASE taskmanager;
```
- Execute all the Command of `/Database/taskmanager.sql` from `localhost/phpmyadmin`

## Usage

1. **Compile the Project**: Open the project in your IDE and compile all classes.

2. **Run the Application**: Start the application by running `Main.java`. This will open the dashboard where tasks can be managed.

3. **Add Task**:
   - Click on the "Add Task" button to open a form where you can enter a task name, set priority, and end time.
   - Once added, tasks will appear in the dashboard list.

4. **Edit Task**:
   - Select a task and click "Edit" to modify its details.
   - Update the task name, priority, or end time, then save the changes.

5. **Delete Task**:
   - Select a task and click "Delete" to remove it from the list.


## Contributing
We welcome contributions! If you'd like to contribute to this Malware Detection Project, please check out our [Contribution Guidelines](Contribution.md).

## Code of Conduct
Please review our [Code of Conduct](CodeOfConduct.md) before participating in this app.

## License
This project is licensed under the MIT [License](LICENSE).