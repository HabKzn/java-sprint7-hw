package tasks;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;

public class SubTask extends Task {

    private final Epic epic;

    public SubTask(String name, String description, Epic epic, LocalDateTime startTime, Duration duration) {
        super(name, description, startTime, duration);
        this.epic = epic;
        setStatus(Status.NEW);
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
}
