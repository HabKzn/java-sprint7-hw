package manager;

import history.HistoryManager;
import tasks.Epic;
import tasks.Status;
import tasks.SubTask;
import tasks.Task;

import java.time.LocalDateTime;
import java.util.*;

public class InMemoryTaskManager implements TaskManager {
    private int uin;
    public final HashMap<Integer, Task> tasks;
    public final HashMap<Integer, Epic> epics;
    public final HashMap<Integer, SubTask> subTasks;
    public final HistoryManager memoryManager;
    Set<Task> treeSet;

    public InMemoryTaskManager() {
        tasks = new HashMap<>();
        epics = new HashMap<>();
        subTasks = new HashMap<>();
        uin = 0;
        memoryManager = Managers.getDefaultHistory();

        Comparator<Task> taskComparator = new Comparator<Task>() {
            @Override
            public int compare(final Task o1, final Task o2) {
                if (o1.getStartTime() == null && o2.getStartTime() == null) {
                    return 0;
                } else if (o1.getStartTime() == null) {
                    return -1;
                } else if (o2.getStartTime() == null) {
                    return 1;
                } else
                    return o1.getStartTime().compareTo(o2.getStartTime());
            }
        };
        treeSet = new TreeSet<>(taskComparator);
    }

    public int getUin() {
        return uin;
    }

    public void setUin(final int uin) {
        this.uin = uin;
    }


    @Override
    public ArrayList<Epic> getAllEpicsList() {
        return new ArrayList<>(epics.values());
    }

    @Override
    public ArrayList<Task> getAllTasksList() {
        return new ArrayList<>(tasks.values());
    }

    @Override
    public ArrayList<SubTask> getAllSubTasksList() {
        return new ArrayList<>(subTasks.values());
    }

    //удаление всех задач
    @Override
    public void clearTasks() {
        for (Task task : tasks.values()) {
            treeSet.remove(task);
        }
        tasks.clear();
    }

    @Override
    public void clearEpics() {
        epics.clear();
        clearSubTasks();
    }

    @Override
    public void clearSubTasks() {
        for (SubTask subTask : subTasks.values()) {
            treeSet.remove(subTask);
        }
        subTasks.clear();
        epics.clear();
    }


    //получение по идентификатору
    @Override
    public Task getTaskByUin(int uin) {
        if (tasks.get(uin) != null) {
            memoryManager.add(tasks.get(uin));
            return tasks.get(uin);
        } else
            return null;
    }

    @Override
    public Task getSubTaskByUin(int uin) {
        if (subTasks.get(uin) != null) {
            memoryManager.add(subTasks.get(uin));
            return subTasks.get(uin);
        } else
            return null;
    }


    public Task getEpicByUin(int uin) {
        if (epics.get(uin) != null) {
            memoryManager.add(epics.get(uin));
            return epics.get(uin);
        } else
            return null;
    }

    public void createTask(Task task) {

        if (taskIsValid(task)) {
            uin += 1;
            tasks.put(uin, task);
            task.setUin(uin);
            task.setStatus(Status.NEW);
            treeSet.add(task);
        }
    }

    public void createSubTask(SubTask subtask, Epic epic) {

        if (taskIsValid(subtask)) {
            uin += 1;
            subTasks.put(uin, subtask);
            subtask.setUin(uin);
            epic.addSubTaskToEpicList(subtask);
            epic.setStatus(epic.calculateEpicStatus());
            epic.refreshEpicStartTime();
            epic.refreshEpicDuration();
            treeSet.add(subtask);
        }
    }

    public void createEpic(Epic epic) {
        uin += 1;
        epics.put(uin, epic);
        epic.setUin(uin);
        epic.setStatus(Status.NEW);
    }

    //Обновление задач.
    public void updateEpic(Epic epic) {
        if (epics.containsKey(epic.getId())) {
            epics.put(epic.getId(), epic);
        }
    }


    public void updateTask(Task task) {

        if (tasks.containsKey(task.getId())) {
            Task oldTask = tasks.get(task.getId());
            tasks.remove(oldTask.getId());
            treeSet.remove(oldTask);
            if (taskIsValid(task)) {
                tasks.put(task.getId(), task);
                treeSet.add(task);
            } else {
                tasks.put(oldTask.getId(), oldTask);
                treeSet.add(oldTask);
            }
        }
    }

    public void updateSubTask(SubTask subtask) {
        if (subTasks.containsKey(subtask.getId())) {
            SubTask oldSubTask = subTasks.get(subtask.getId());
            subTasks.remove(subtask.getId());

            if (taskIsValid(subtask)) {
                subTasks.put(subtask.getId(), subtask);
                subtask.getEpic().setStatus(subtask.getEpic().calculateEpicStatus());
                treeSet.remove(oldSubTask);
                treeSet.add(subtask);
            }
            subTasks.put(oldSubTask.getId(), oldSubTask);
            oldSubTask.getEpic().setStatus(oldSubTask.getEpic().calculateEpicStatus());
        }
    }


    //Методы для удаления по ID(Вместе с эпиком удаляются и подзадачи)
    @Override
    public void deleteEpicById(int id) {
        if (epics.containsKey(id)) {
            for (SubTask subtask : epics.get(id).getSubTasks()) {
                treeSet.remove(subTasks.get(subtask.getId()));
                subTasks.remove(subtask.getId());
                removeFromHistoryByID(subtask.getId());
            }
            epics.remove(id);
            removeFromHistoryByID(id);
        }
    }

    @Override
    public void deleteTaskById(int id) {
        if (tasks.containsKey(id)) {
            treeSet.remove(tasks.get(id));
            tasks.remove(id);
            removeFromHistoryByID(id);
        }
    }

    @Override
    public void deleteSubTaskById(int id) {
        if(subTasks.containsKey(id)){
            treeSet.remove(subTasks.get(id));
            subTasks.remove(id);
        removeFromHistoryByID(id);}
    }

    @Override
    public List<Task> history() {
        return memoryManager.getTasks();
    }


    @Override
    public void removeFromHistoryByID(final int id) {
        memoryManager.remove(id);
    }


    public Task getTaskUniversal(int id) {
        if (epics.containsKey(id)) {
            return epics.get(id);
        } else if (tasks.containsKey(id)) {
            return tasks.get(id);
        } else return subTasks.getOrDefault(id, null);
    }

    public boolean taskIsValid(Task task) {
        LocalDateTime taskStartTime = task.getStartTime();
        LocalDateTime taskEndTime = task.getEndTime();
        List<Task> tempList = getPrioritizedTask();

        if (!tempList.isEmpty()) {
            for (Task tempTask : tempList) {
                LocalDateTime tempTaskStartTime = tempTask.getStartTime();
                LocalDateTime tempTaskEndTime = tempTask.getStartTime();
                if (tempTaskStartTime.equals(taskStartTime) || tempTaskEndTime.equals(taskEndTime) ||
                        ((tempTaskStartTime.isAfter(taskStartTime)) && (tempTaskStartTime.isBefore(taskEndTime))
                                || tempTaskEndTime.isAfter(taskStartTime) && tempTaskEndTime.isBefore(taskEndTime))) {
                    System.out.println("Новая задача не добавлена, так как пересекается по времени с задачей "
                                       + tempTask.getName() + " c id " + tempTask.getId()
                                       + ". Создайте задачу с другим временем исполнения.");
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public List<Task> getPrioritizedTask() {
        return new ArrayList<>(treeSet);
    }
}


