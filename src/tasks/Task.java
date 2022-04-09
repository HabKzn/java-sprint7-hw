package tasks;

import java.util.Objects;

public class Task {
    private String name;
    private String description;
    private int uin;
    private Status status;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getUin() {
        return uin;
    }

    public void setUin(int uin) {
        this.uin = uin;
    }

    Types getType() {
        return Types.TASK;
    }

    public Task(String name, String description) {
        this.name = name;
        this.description = description;
        status = Status.NEW;
    }

    public Task(String name, String description, Status status) {
        this.name = name;
        this.description = description;
        this.status = status;
    }

    public int getId() {
        return uin;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Status getStatus() {
        return status;
    }

    @Override
    public String toString() {
        return getId() + "," + getType() + "," + getName() + "," + getStatus() + "," + getDescription();
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (!(o instanceof Task)) return false;
        final Task task = (Task) o;
        return getUin() == task.getUin() && getName().equals(task.getName()) && getDescription().equals(task.getDescription()) && getStatus() == task.getStatus();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName(), getDescription(), getUin(), getStatus());
    }
}
