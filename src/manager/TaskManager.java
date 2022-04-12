package manager;

import tasks.Epic;
import tasks.SubTask;
import tasks.Task;

import java.util.ArrayList;
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

    void createSubTask(SubTask subtask, Epic epic);

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
}
