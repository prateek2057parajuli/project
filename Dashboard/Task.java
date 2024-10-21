package Dashboard;

public class Task {
    private String taskName;
    private String priority;
    private String createTime;
    private String endTime;
    private boolean completed;

    public Task(String taskName, String priority, String createTime, String endTime, boolean completed) {
        this.taskName = taskName;
        this.priority = priority;
        this.createTime = createTime;
        this.endTime = endTime;
        this.completed = completed;
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

    public boolean isCompleted() {
        return completed;
    }
}
