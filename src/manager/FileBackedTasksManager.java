package manager;

import tasks.Epic;
import tasks.Status;
import tasks.SubTask;
import tasks.Task;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;


public class FileBackedTasksManager extends InMemoryTaskManager {
   static File file;

    public FileBackedTasksManager(String stringPath) {
        file = new File(stringPath);
    }

    public static void main(String[] args)  {
        Epic epc1 = new Epic("epc1", "this is epic N1");
        Epic epc2 = new Epic("epc2", "this is epic N2");
        SubTask sbt1 = new SubTask("sbt1", "this is subtask1", epc1);
        SubTask sbt2 = new SubTask("sbt2", "this is subtask2", epc1);
        SubTask sbt3 = new SubTask("sbt3", "this is subtask3", epc2);
        SubTask sbt4 = new SubTask("sbt4", "this is subtask4", epc2);

        FileBackedTasksManager manager = new FileBackedTasksManager("memoryFile.csv");

        manager.createEpic(epc1);
        manager.createEpic(epc2);
        manager.createSubTask(sbt1, sbt1.getEpic());
        manager.createSubTask(sbt2, sbt2.getEpic());
        manager.createSubTask(sbt3, sbt3.getEpic());
        manager.createSubTask(sbt4, sbt4.getEpic());

        manager.getEpicByUin(epc1.getUin());
        manager.getEpicByUin(epc2.getUin());
        manager.getSubTaskByUin(sbt4.getUin());
        manager.getSubTaskByUin(sbt3.getUin());
        manager.getSubTaskByUin(sbt2.getUin());
        manager.getSubTaskByUin(sbt1.getUin());


        FileBackedTasksManager newManager = loadFromFile(file);
        String firstManager = manager.memoryManager.getTasks().toString();
        String secondManager = newManager.memoryManager.getTasks().toString();
        System.out.println(firstManager.equals(secondManager));

    }

    static FileBackedTasksManager loadFromFile(File file) {
        List<String> listOfStrings = null;
        FileBackedTasksManager manager = null;
        try {
            listOfStrings = new ArrayList<>(Files.readAllLines(Paths.get(file.getName())));
            Task task;
            manager = new FileBackedTasksManager(file.getPath());

            for (int i = 1; i < listOfStrings.size() - 2; i++) {
                task = manager.fromString(listOfStrings.get(i));
                if (task instanceof Epic) {
                    manager.epics.put(task.getId(), (Epic) task);
                }
            }
            for (int i = 1; i < listOfStrings.size() - 2; i++) {
                task = manager.fromString(listOfStrings.get(i));
                if (task instanceof SubTask) {
                    manager.subTasks.put(task.getId(), (SubTask) task);
                } else if (task != null && !(task instanceof Epic)) {
                    manager.tasks.put(task.getId(), task);
                }
            }
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка при загрузке");
        }
        //тут восстанавливаем историю
        String[] tempHistory = listOfStrings.get(listOfStrings.size() - 1).split(",");
        for (int i = 0; i < tempHistory.length; i++) {
            int historyElement = Integer.parseInt(tempHistory[i]);
            manager.memoryManager.add(manager.getTaskUniversal(historyElement));
        }

        manager.setUin(manager.epics.size() + manager.tasks.size() + manager.subTasks.size());
        return manager;
    }

    static List<String> splitFileToList(String fileName) {
        try {
            return new ArrayList<>(Files.readAllLines(Paths.get(fileName)));
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка при сохранении");
        }
    }

    public void save() {
        try (PrintWriter pw = new PrintWriter(file.getName())) {
            pw.println("id,type,name,status,description,epic");
            for (Task task : getAllTasksList()) {
                pw.println(task.toString());
            }
            for (SubTask task : getAllSubTasksList()) {
                pw.println(task.toString());
            }
            for (Epic task : getAllEpicsList()) {
                pw.println(task.toString());
            }
            pw.println("");

            List<Task> temp = history();
            if (temp != null && !temp.isEmpty()) {
                pw.print(temp.get(0).getId());
                for (int i = 1; i < temp.size(); i++) {
                    pw.print("," + temp.get(i).getId());
                }
            }
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка при сохранении");
        }
    }

    @Override
    public void createTask(final Task task) {
        super.createTask(task);
        save();
    }

    public void createSubTask(final SubTask subtask, final int epicId) {
        super.createSubTask(subtask, epics.get(epicId));
        save();
    }

    @Override
    public void createEpic(final Epic epic) {
        super.createEpic(epic);
        save();
    }

    @Override
    public void updateEpic(final Epic epic) {
        super.updateEpic(epic);
        save();
    }

    @Override
    public void updateTask(final Task task) {
        super.updateTask(task);
        save();
    }

    @Override
    public void updateSubTask(final SubTask subtask) {
        super.updateSubTask(subtask);
        save();
    }

    @Override
    public void deleteEpicById(final int id) {
        super.deleteEpicById(id);
        save();
    }

    @Override
    public void deleteTaskById(final int id) {
        super.deleteTaskById(id);
        save();
    }

    @Override
    public void deleteSubTaskById(final int id) {
        super.deleteSubTaskById(id);
        save();
    }

    @Override
    public void removeFromHistoryByID(final int id) {
        super.removeFromHistoryByID(id);
        save();
    }

    @Override
    public Epic getEpicByUin(int uin) {
        if (epics.get(uin) != null) {
            memoryManager.add(epics.get(uin));
            save();
            return epics.get(uin);
        } else
            return null;
    }

    @Override
    public Task getTaskByUin(int uin) {
        if (tasks.get(uin) != null) {
            memoryManager.add(tasks.get(uin));
            save();
            return tasks.get(uin);
        } else
            return null;
    }

    @Override
    public Task getSubTaskByUin(int uin) {
        if (subTasks.get(uin) != null) {
            memoryManager.add(subTasks.get(uin));
            save();
            return subTasks.get(uin);
        } else
            return null;
    }


    Task fromString(String value) {
        String[] temp = value.split(",");
        Task task;
        switch (temp[1]) {
            case "SUBTASK":
                task = new SubTask(temp[2], temp[4], Status.valueOf(temp[3]), epics.get(Integer.parseInt(temp[5])));
                break;
            case "EPIC":
                task = new Epic((temp[2]), temp[4], Status.valueOf(temp[3]));
                break;
            case "TASK":
                task = new Task(temp[2], temp[4], Status.valueOf(temp[3]));
                break;
            default:
                return null;
        }
        task.setUin(Integer.parseInt(temp[0]));
        return task;
    }
}
