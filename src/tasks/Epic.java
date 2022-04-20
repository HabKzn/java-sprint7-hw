package tasks;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Epic extends Task {
    private List<SubTask> subTasks;
    private LocalDateTime endTime;

    public Epic(String name, String description) {
        super(name, description);
        subTasks = new ArrayList<>();
       refreshEpicData();
        endTime = getEndTime();
    }

    @Override
    Types getType() {
        return Types.EPIC;
    }

    public List<SubTask> getSubTasks() {
        return subTasks;
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

    public void addSubtask(SubTask subtask) {
        subTasks.add(subtask);
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    // Как вы справедливо заметили, для оптимизации следовало заменить три метода, перебирающих список на один метод,
    // который совершает все манипуляции за один проход. Что я и сделал, правда код стал сложнее для понимания.


    public void refreshEpicData() {

        if(subTasks.size()>0){
        LocalDateTime tempDateTime = subTasks.get(0).getStartTime();
        Duration tempDuration = Duration.ZERO;
        int doneCounter = 0;
        int newCounter = 0;

        for (SubTask subTask : getSubTasks()) {
               if (subTask.getStartTime().isBefore(tempDateTime)) {
                tempDateTime = subTask.getStartTime();
            }
            tempDuration = tempDuration.plus(subTask.getDuration());

            if (subTask.getStatus().equals(Status.DONE)) {
                doneCounter++;
            } else if (subTask.getStatus().equals(Status.NEW)) {
                newCounter++;
            }
               }
            if (doneCounter == getSubTasks().size()) {
                setStatus(Status.DONE);
            } else if (newCounter == getSubTasks().size()) {
                setStatus(Status.NEW);
            } else setStatus(Status.IN_PROGRESS);

        setDuration(tempDuration);
        setStartTime(tempDateTime);}
    }

    public List<SubTask> getSubtasks() {
        return subTasks;
    }

    public void refreshEndTime() {
        endTime = getEndTime();
    }

    @Override
    public LocalDateTime getEndTime() {
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
