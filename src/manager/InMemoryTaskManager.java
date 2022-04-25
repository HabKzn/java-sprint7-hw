package manager;

import history.HistoryManager;
import tasks.Epic;
import tasks.Status;
import tasks.SubTask;
import tasks.Task;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;

public class InMemoryTaskManager implements TaskManager {
    private int uin;
    private final HashMap<Integer, Task> managerTasksMap;
    private final HashMap<Integer, Epic> managerEpicsMap;
    private final HashMap<Integer, SubTask> managerSubTasksMap;
    private final HistoryManager memoryManager;
    public Set<Task> orderedTasksSet;

    public InMemoryTaskManager() {
        managerTasksMap = new HashMap<>();
        managerEpicsMap = new HashMap<>();
        managerSubTasksMap = new HashMap<>();
        uin = 0;
        memoryManager = Managers.getDefaultHistory();

        Comparator<Task> taskComparator = (o1, o2) -> {
            if (o1.getStartTime() == null && o2.getStartTime() == null) {
                return 0;
            } else if (o1.getStartTime() == null) {
                return -1;
            } else if (o2.getStartTime() == null) {
                return 1;
            } else
                return o1.getStartTime().compareTo(o2.getStartTime());
        };
        orderedTasksSet = new TreeSet<>(taskComparator);
    }

    public HashMap<Integer, SubTask> getManagerSubTasksMap() {
        return managerSubTasksMap;
    }

    public Set<Task> getOrderedTasksSet() {
        return orderedTasksSet;
    }

    public HashMap<Integer, Epic> getManagerEpicsMap() {
        return managerEpicsMap;
    }

    public HashMap<Integer, Task> getManagerTasksMap() {
        return managerTasksMap;
    }

    public int getUin() {
        return uin;
    }

    public void setUin(final int uin) {
        this.uin = uin;
    }

    public HistoryManager getMemoryManager() {
        return memoryManager;
    }

    @Override
    public ArrayList<Epic> getAllEpicsList() {
        return new ArrayList<>(managerEpicsMap.values());
    }

    @Override
    public ArrayList<Task> getAllTasksList() {
        return new ArrayList<>(managerTasksMap.values());
    }

    @Override
    public ArrayList<SubTask> getAllSubTasksList() {
        return new ArrayList<>(managerSubTasksMap.values());
    }

    //удаление всех задач
    @Override
    public void clearTasks() {
        for (Task task : managerTasksMap.values()) {
            orderedTasksSet.remove(task);
        }
        managerTasksMap.clear();
    }

    @Override
    public void clearEpics() {
        managerEpicsMap.clear();
        clearSubTasks();
    }

    @Override
    public void clearSubTasks() {
        for (SubTask subTask : managerSubTasksMap.values()) {
            orderedTasksSet.remove(subTask);
        }
        managerSubTasksMap.clear();
        managerEpicsMap.clear();
    }


    //получение по идентификатору
    @Override
    public Task getTaskByUin(int uin) {
        if (managerTasksMap.get(uin) != null) {
            memoryManager.add(managerTasksMap.get(uin));
            return managerTasksMap.get(uin);
        } else
            return null;
    }

    @Override
    public Task getSubTaskByUin(int uin) {
        if (managerSubTasksMap.get(uin) != null) {
            memoryManager.add(managerSubTasksMap.get(uin));
            return managerSubTasksMap.get(uin);
        } else
            return null;
    }


    public Task getEpicByUin(int uin) {
        if (managerEpicsMap.get(uin) != null) {
            memoryManager.add(managerEpicsMap.get(uin));
            return managerEpicsMap.get(uin);
        } else
            return null;
    }

    public void createTask(Task task) {

        if (taskIsValid(task)) {
            uin += 1;
            managerTasksMap.put(uin, task);
            task.setUin(uin);
            task.setStatus(Status.NEW);
            orderedTasksSet.add(task);
        }
    }

    public void createSubTask(SubTask subtask, Epic epic) {

        if (taskIsValid(subtask)) {
            uin += 1;
            managerSubTasksMap.put(uin, subtask);
            subtask.setUin(uin);
            epic.addSubTaskToEpicList(subtask);
            epic.refreshEpicData();
            orderedTasksSet.add(subtask);
        }
    }

    public void createEpic(Epic epic) {
        uin += 1;
        managerEpicsMap.put(uin, epic);
        epic.setUin(uin);
        epic.setStatus(Status.NEW);
    }

    //Обновление задач.
    public void updateEpic(Epic epic) {
        if (managerEpicsMap.containsKey(epic.getId())) {
            managerEpicsMap.put(epic.getId(), epic);
            epic.refreshEpicData();
            epic.refreshEndTime();
        }
    }


    public void updateTask(Task task) {

        if (managerTasksMap.containsKey(task.getId())) {

            Task oldTask = cloneTask(managerTasksMap.get(task.getId()));
            managerTasksMap.remove(oldTask.getId());
            orderedTasksSet.remove(oldTask);
            if (taskIsValid(task)) {
                managerTasksMap.put(task.getId(), task);
                orderedTasksSet.add(task);
            } else {
                managerTasksMap.put(oldTask.getId(), oldTask);
                orderedTasksSet.add(oldTask);
            }
        }
    }

    public void updateSubTask(SubTask newSubtask) {
        if (managerSubTasksMap.containsKey(newSubtask.getId())) {
            SubTask oldSubTask = cloneSubTask(managerSubTasksMap.get(newSubtask.getId()));
            Epic epic = oldSubTask.getEpic();
            orderedTasksSet.remove(oldSubTask);
            managerSubTasksMap.remove(oldSubTask.getId());

            if (taskIsValid(newSubtask)) {
                managerSubTasksMap.put(newSubtask.getId(), newSubtask);
                orderedTasksSet.add(newSubtask);
                epic.getSubTasks().remove(oldSubTask);
                epic.getSubTasks().add(newSubtask);
                epic.refreshEpicData();
                epic.refreshEndTime();
            } else {
                managerSubTasksMap.put(oldSubTask.getId(), oldSubTask);
                oldSubTask.getEpic().refreshEpicData();
            }
        }
    }


    //Методы для удаления по ID(Вместе с эпиком удаляются и подзадачи)
    @Override
    public void deleteEpicById(int id) {
        if (managerEpicsMap.containsKey(id)) {
            for (SubTask subtask : managerEpicsMap.get(id).getSubTasks()) {
                orderedTasksSet.remove(managerSubTasksMap.get(subtask.getId()));
                managerSubTasksMap.remove(subtask.getId());
                removeFromHistoryByID(subtask.getId());
            }
            managerEpicsMap.remove(id);
            removeFromHistoryByID(id);
        }
    }

    @Override
    public void deleteTaskById(int id) {
        if (managerTasksMap.containsKey(id)) {
            orderedTasksSet.remove(managerTasksMap.get(id));
            managerTasksMap.remove(id);
            removeFromHistoryByID(id);
        }
    }

    @Override
    public void deleteSubTaskById(int id) {
        if (managerSubTasksMap.containsKey(id)) {
            orderedTasksSet.remove(managerSubTasksMap.get(id));
            managerSubTasksMap.remove(id);
            removeFromHistoryByID(id);
        }
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
        if (managerEpicsMap.containsKey(id)) {
            return managerEpicsMap.get(id);
        } else if (managerTasksMap.containsKey(id)) {
            return managerTasksMap.get(id);
        } else return managerSubTasksMap.getOrDefault(id, null);
    }

    public boolean taskIsValid(Task checkingTask) {
        long checkingStartTime = ZonedDateTime.of(checkingTask.getStartTime(), ZoneId.systemDefault())
                .toInstant().toEpochMilli();
        long checkingEndTime = ZonedDateTime.of(checkingTask.getEndTime(), ZoneId.systemDefault())
                .toInstant().toEpochMilli();
        long checkingDuration = checkingEndTime - checkingStartTime;

        List<Task> tempList = getPrioritizedTask();
        if (checkingDuration < 0) {
            System.out.println("Задача не добавлена/изменена, так как введенная " +
                    "продолжительность выполнения отрицательная");
            return false;
        } else if (tempList.isEmpty()) {
            return true;
        } else {
            for (Task tempTask : tempList) {
                long tempStartTime = ZonedDateTime.of(tempTask.getStartTime(), ZoneId.systemDefault())
                        .toInstant().toEpochMilli();
                long tempEndTime = ZonedDateTime.of(tempTask.getEndTime(), ZoneId.systemDefault())
                        .toInstant().toEpochMilli();
                if (checkingStartTime >= tempStartTime && checkingStartTime < tempEndTime) {
                    System.out.println("Задача не добавлена/изменена," +
                            " так как время исполнения задачи пересекается с существующей задачей " +
                            tempTask.getName() + " c id " + tempTask.getId());
                    return false;
                } else if (checkingEndTime > tempStartTime && checkingEndTime <= tempEndTime) {
                    System.out.println("Задача не добавлена/изменена," +
                            " так как время исполнения задачи пересекается с существующей задачей " +
                            tempTask.getName() + " c id " + tempTask.getId());
                    return false;
                } else if (checkingStartTime <= tempStartTime && checkingEndTime >= tempEndTime) {
                    System.out.println("Задача не добавлена/изменена," +
                            " так как время исполнения задачи пересекается с существующей задачей " +
                            tempTask.getName() + " c id " + tempTask.getId());
                    return false;
                }
            }
        }
        return true;
    }


    @Override
    public ArrayList<Task> getPrioritizedTask() {
         return new ArrayList<Task>(getOrderedTasksSet());
    }



    public Task cloneTask(Task taskToClone) {
        Task newTask = new Task(taskToClone.getName(), taskToClone.getDescription(), taskToClone.getStartTime(), taskToClone.getDuration());
        newTask.setUin(taskToClone.getUin());
        newTask.setStatus(taskToClone.getStatus());
        return newTask;
    }

    public SubTask cloneSubTask(SubTask subTask) {
        SubTask newSubTask = new SubTask(subTask.getName(), subTask.getDescription(), subTask.getEpic(), subTask.getStartTime(), subTask.getDuration());
        newSubTask.setUin(subTask.getId());
        newSubTask.setStatus(subTask.getStatus());
        return newSubTask;
    }

    public void createTaskWhileLoading(Task task) {

        if (taskIsValid(task)) {
            uin = task.getUin();
            managerTasksMap.put(uin, task);
            task.setUin(uin);
            task.setStatus(Status.NEW);
            orderedTasksSet.add(task);
        }
    }

    public void createSubTaskWhileLoading(SubTask subtask) {

        if (taskIsValid(subtask)) {
            uin = subtask.getUin();
            managerSubTasksMap.put(uin, subtask);
            subtask.setUin(uin);
            subtask.getEpic().addSubTaskToEpicList(subtask);
            subtask.getEpic().refreshEpicData();
            orderedTasksSet.add(subtask);
        }
    }

    public void createEpicWhileLoading(Epic epic) {
        uin = epic.getUin();
        managerEpicsMap.put(uin, epic);
        epic.setUin(uin);
        epic.setStatus(epic.getStatus());
    }
}


