package tasks;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;

public class SubTask extends Task {
    int epicId = 0;
//    private final Epic epic;
//
//    public SubTask(String name, String description, Epic epic, LocalDateTime startTime, Duration duration) {
//        super(name, description, startTime, duration);
//        this.epic = epic;
//        setStatus(Status.NEW);
//    }
    public SubTask(String name, String description, int epicId, LocalDateTime startTime, Duration duration) {
        super(name, description, startTime, duration);
        this.epicId = epicId;
        setStatus(Status.NEW);
    }

    public SubTask() {

    }

    @Override
    Types getType() {
        return Types.SUBTASK;
    }

    public Epic getEpic() {
        return epic;
    }

    @Override
    public String toString() {
        return getId() + "," + getType() + "," + getName() + "," + getStatus() + "," + getDescription() + ","
                + getEpic().getId() + "," + getStartTime() + "," + getDuration() + "," + getEndTime();
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (!(o instanceof SubTask)) return false;
        if (!super.equals(o)) return false;
        final SubTask subTask = (SubTask) o;
        return getEpic().equals(subTask.getEpic());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getEpic());
    }


    public SubTask cloneSubTask(SubTask subTask){
        SubTask newSubTask = new SubTask(subTask.getName(), subTask.getDescription(), subTask.getEpic(),
                subTask.getStartTime(),subTask.getDuration());
        newSubTask.setUin(subTask.getId());
        newSubTask.setStatus(subTask.getStatus());
        return newSubTask;
    }
}
