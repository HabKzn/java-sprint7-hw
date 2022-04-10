package tasks;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Epic extends Task {
    private List<SubTask> subTasks;

    public Epic(String name, String description) {
        super(name, description);
        subTasks = new ArrayList<>();
        setStatus(Status.NEW);
    }

    public Epic(String name, String description, Status status) {
        super(name, description);
        subTasks = new ArrayList<>();
        this.setStatus(status);
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
}