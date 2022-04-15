package tasks;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Objects;

public class Task {
    private String name;
    private String description;
    private int uin;
    private Status status;
    Duration duration;
    LocalDateTime startTime;

    public Task(final String name, final String description) {
        this.name = name;
        this.description = description;
    }

    public Task(String name, String description, LocalDateTime startTime, Duration duration) {
        this.name = name;
        this.description = description;
        this.startTime = startTime;
        this.duration = duration;
        setStatus(Status.NEW);
    }

    public Duration getDuration() {
        return duration;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }
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
        return getId() + "," + getType() + "," + getName() + "," + getStatus() + "," + getDescription() +
                "," + getStartTime() + "," + getDuration() + "," + getEndTime();
    }

    LocalDateTime getEndTime() {
       if(startTime == null){
           return null;
       }
        return startTime.plus(duration);
    }

    public void setDuration(final Duration duration) {
        this.duration = duration;
    }

    public void setStartTime(final LocalDateTime startTime) {
        this.startTime = startTime;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (!(o instanceof Task)) return false;
        final Task task = (Task) o;
        return getUin() == task.getUin() && Objects.equals(getName(), task.getName()) && Objects.equals(getDescription(), task.getDescription()) && getStatus() == task.getStatus();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName(), getDescription(), getUin(), getStatus());
    }
}
