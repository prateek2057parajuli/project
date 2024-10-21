package Dashboard;
public class Task {
    private int id;
    private String taskName;
    private String priority;
    private String createTime;
    private String endTime;

    public Task(int id, String taskName, String priority, String createTime, String endTime) {
        this.id = id;
        this.taskName = taskName;
        this.priority = priority;
        this.createTime = createTime;
        this.endTime = endTime;
    }

    public int getId() {
        return id;
    }

    public String getTaskName() {
        return taskName;
    }

    public String getPriority() {
        return priority;
    }

    public String getCreateTime() {
        return createTime;
    }

    public String getEndTime() {
        return endTime;
    }
}
