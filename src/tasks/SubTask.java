package tasks;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;

public class SubTask extends Task {

    private int epicId;

    public SubTask(String name, String description, int epicId, LocalDateTime startTime, Duration duration) {
        super(name, description, startTime, duration);
        this.epicId = epicId;
        setStatus(Status.NEW);
    }

    public void setEpicId(int epicId) {
        this.epicId = epicId;
    }

    @Override
    Types getType() {
        return Types.SUBTASK;
    }

    public int getEpicId() {
        return epicId;
    }

    @Override
    public String toString() {
        return getId() + "," + getType() + "," + getName() + "," + getStatus() + "," + getDescription() + ","
                + getEpicId() + "," + getStartTime() + "," + getDuration() + "," + getEndTime();
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (!(o instanceof SubTask)) return false;
        if (!super.equals(o)) return false;
        final SubTask subTask = (SubTask) o;
        return getEpicId() == (subTask.getEpicId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getEpicId());
    }


    public SubTask cloneSubTask(SubTask subTask){
        SubTask newSubTask = new SubTask(subTask.getName(), subTask.getDescription(), subTask.getEpicId(),
                subTask.getStartTime(), subTask.getDuration());
        newSubTask.setUin(subTask.getId());
        newSubTask.setStatus(subTask.getStatus());
        return newSubTask;
    }
}
