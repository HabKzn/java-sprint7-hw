package manager;

import history.HistoryManager;
import tasks.Epic;
import tasks.Status;
import tasks.SubTask;
import tasks.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InMemoryTaskManager implements TaskManager {
    private int uin;
    public final HashMap<Integer, Task> tasks;
    public final HashMap<Integer, Epic> epics;
    public final HashMap<Integer, SubTask> subTasks;
    public final HistoryManager memoryManager;

    public InMemoryTaskManager() {
        tasks = new HashMap<>();
        epics = new HashMap<>();
        subTasks = new HashMap<>();
        uin = 0;
        memoryManager = Managers.getDefaultHistory();

    }

    public int getUin() {
        return uin;
    }

    public void setUin(final int uin) {
        this.uin = uin;
    }

    //получение списка всех задач по типу
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
        tasks.clear();
    }

    @Override
    public void clearEpics() {
        epics.clear();
        clearSubTasks();
    }

    @Override
    public void clearSubTasks() {
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
        uin += 1;
        tasks.put(uin, task);
        task.setUin(uin);
        task.setStatus(Status.NEW);
    }

    public void createSubTask(SubTask subtask, Epic epic) {
        uin += 1;
        subTasks.put(uin, subtask);
        subtask.setUin(uin);
        subtask.setStatus(subtask.getStatus());
        epic.addSubTaskToEpicList(subtask);
        epic.setStatus(epic.calculateEpicStatus());
        epic.refreshEpicStartTime();
        epic.refreshEpicDuration();

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
            tasks.put(task.getId(), task);
        }
    }


    public void updateSubTask(SubTask subtask) {
        if (subTasks.containsKey(subtask.getId())) {
            subTasks.put(subtask.getId(), subtask);
            subtask.getEpic().setStatus(subtask.getEpic().calculateEpicStatus());
            subtask.getEpic().refreshEpicStartTime();
            subtask.getEpic().refreshEpicDuration();

        }
    }


    //Методы для удаления по ID(Вместе с эпиком удаляются и подзадачи)
    @Override
    public void deleteEpicById(int id) {
       if (epics.containsKey(id)){
        for (SubTask subtask : epics.get(id).getSubTasks()) {
            subTasks.remove(subtask.getId());
            removeFromHistoryByID(subtask.getId());
        }
        epics.remove(id);
        removeFromHistoryByID(id);}
    }

    @Override
    public void deleteTaskById(int id) {
        if(tasks.containsKey(id))
        {tasks.remove(id);
        removeFromHistoryByID(id);}
    }

    @Override
    public void deleteSubTaskById(int id) {
        if(subTasks.containsKey(id)){
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

}


