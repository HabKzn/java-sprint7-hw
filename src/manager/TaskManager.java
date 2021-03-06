package manager;

import tasks.Epic;
import tasks.SubTask;
import tasks.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public interface TaskManager {
    ArrayList<Epic> getAllEpicsList();

    ArrayList<Task> getAllTasksList();

    ArrayList<SubTask> getAllSubTasksList();

    void clearTasks();

    void clearEpics();

    void clearSubTasks();

    Task getTaskByUin(int uin);

    Task getSubTaskByUin(int uin);

    Task getEpicByUin(int uin);

    void createTask(Task task);

    void createSubTask(SubTask subtask, int epicId);

    void createEpic(Epic epic);

    void updateEpic(Epic epic);

    void updateTask(Task task);

    void updateSubTask(SubTask subtask);

    void deleteEpicById(int id);

    void deleteTaskById(int id);

    void deleteSubTaskById(int id);

    List<Task> history();

    void removeFromHistoryByID(int id);

    Task getTaskUniversal(int id);

    List<Task> getPrioritizedTask();

    HashMap<Integer, Epic> getManagerEpicsMap();

    public HashMap<Integer, Task> getManagerTasksMap();

    public HashMap<Integer, SubTask> getManagerSubTasksMap();

    ArrayList<SubTask> getEpicSubtasks(int id);

}
