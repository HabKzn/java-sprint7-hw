package tasks;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Epic extends Task {
    private List<SubTask> subTasks;
    LocalDateTime endTime;

    public Epic(String name, String description) {
        super(name, description);
        subTasks = new ArrayList<>();
        setStatus(calculateEpicStatus());
        refreshEpicStartTime();
        endTime = getEndTime();
        refreshEpicDuration();
    }

    @Override
    Types getType() {
        return Types.EPIC;
    }

    public List<SubTask> getSubTasks() {
        return subTasks;
    }

    public void setSubTasks(ArrayList<SubTask> subTasks) {
        this.subTasks = subTasks;
    }

    public void addSubTaskToEpicList(SubTask subTask) {
        subTasks.add(subTask);
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (!(o instanceof Epic)) return false;
        if (!super.equals(o)) return false;
        final Epic epic = (Epic) o;
        if (getSubTasks().size() != epic.getSubTasks().size()) return false;
        for (int i = 0; i < getSubTasks().size(); i++) {
            if (!getSubTasks().get(i).equals(epic.getSubTasks().get(i))) {
                return false;
            }
        }
        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getSubTasks());
    }

    public Status calculateEpicStatus() {
        int doneCounter = 0;
        int newCounter = 0;

        if (getSubTasks().isEmpty()) {
            return Status.NEW;
        }
        for (SubTask task : getSubTasks()) {
            if (task.getStatus().equals(Status.DONE)) {
                doneCounter++;
            } else if (task.getStatus().equals(Status.NEW)) {
                newCounter++;
            }
        }
        if (doneCounter == getSubTasks().size()) {
            return Status.DONE;
        } else if (newCounter == getSubTasks().size()) {
            return Status.NEW;
        } else return Status.IN_PROGRESS;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public void refreshEpicStartTime() {
        LocalDateTime tempDateTime = null;
        for (SubTask subTask : getSubTasks()) {

            if (tempDateTime == null) {
                tempDateTime = subTask.getStartTime();
            } else if (subTask.getStartTime().isBefore(tempDateTime)) {
                tempDateTime = subTask.getStartTime();
            }
        }
        startTime = tempDateTime;
    }

    public void refreshEpicDuration() {
        Duration tempDuration = Duration.ZERO;
        for (SubTask subTask : getSubTasks()) {
            tempDuration = tempDuration.plus(subTask.getDuration());
        }
        duration = tempDuration;
    }

    public List<SubTask> getSubtasks() {
        return subTasks;
    }

    public void refreshEndTime() {
        endTime = getEndTime();
    }

    @Override
    LocalDateTime getEndTime() {
        LocalDateTime tempEndTime = null;
        for (SubTask subTask : subTasks) {
            if (tempEndTime == null) {
                tempEndTime = subTask.getEndTime();
            } else if (tempEndTime.isBefore(subTask.getEndTime())) {
                tempEndTime = subTask.getEndTime();
            }
        }
        return tempEndTime;
    }
}
