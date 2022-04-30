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
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


public class FileBackedTasksManager extends InMemoryTaskManager {
    static File file;

    public FileBackedTasksManager(String stringPath) {
        file = new File(stringPath);
    }

    public FileBackedTasksManager() {
    }


    public static void main(String[] args) {
        FileBackedTasksManager manager = (FileBackedTasksManager) Managers.getFileBacked();
        Epic epc1 = new Epic("epc1", "this is epic N1");
        Epic epc2 = new Epic("epc2", "this is epic N2");
        manager.createEpic(epc1);
        manager.createEpic(epc2);
        SubTask sbt1 = new SubTask("sbt1", "this is subtask1", epc1.getId(),
                LocalDateTime.of(2022, 4, 16, 10, 0), Duration.ofMinutes(60));

        SubTask sbt2 = new SubTask("sbt2", "this is subtask2", epc1.getId(),
                LocalDateTime.of(2022,4,16,11,0), Duration.ofMinutes(60));

        SubTask sbt3 = new SubTask("sbt3", "this is subtask3", epc2.getId(),
                LocalDateTime.of(2022, 4,16,12,0), Duration.ofMinutes(60));

        SubTask sbt4 = new SubTask("sbt4", "this is subtask4", epc2.getId(),
                LocalDateTime.of(2022,4,16,13,0), Duration.ofMinutes(60));




        manager.createSubTask(sbt1, sbt1.getEpicId());
        manager.createSubTask(sbt2, sbt2.getEpicId());
        manager.createSubTask(sbt3, sbt3.getEpicId());
        manager.createSubTask(sbt4, sbt4.getEpicId());

        manager.getEpicByUin(epc1.getUin());
        manager.getEpicByUin(epc2.getUin());
        manager.getSubTaskByUin(sbt4.getUin());
        manager.getSubTaskByUin(sbt3.getUin());
        manager.getSubTaskByUin(sbt2.getUin());
        manager.getSubTaskByUin(sbt1.getUin());

        Task task = new Task("Taskname", "Taskdescription");
        task.setStartTime(LocalDateTime.of(2024,2,3,12,0));
        task.setStatus(Status.DONE);
        task.setDuration(Duration.ofMinutes(60));
        manager.createTask(task);
        manager.updateTask(task);
        manager.getTaskByUin(7);

        Task task2 = new Task("Taskname2", "Taskdescription2");
        task2.setStartTime(LocalDateTime.of(2055,11,3,12,0));
        task2.setStatus(Status.IN_PROGRESS);
        task2.setDuration(Duration.ofMinutes(120));
        manager.createTask(task2);
        manager.updateTask(task2);
        manager.getTaskByUin(8);

        FileBackedTasksManager newManager = loadFromFile(file);
        String firstManager = manager.getMemoryManager().getTasks().toString();
        String secondManager = newManager.getMemoryManager().getTasks().toString();
        System.out.println(firstManager.equals(secondManager));
        System.out.println(firstManager);
        System.out.println(secondManager);

        manager.createEpic(new Epic("newEpicName", "newEpicDescription"));
    }

    public static FileBackedTasksManager loadFromFile(File file) {
        List<String> listOfStrings;
        FileBackedTasksManager manager;
        try {
            listOfStrings = new ArrayList<>(Files.readAllLines(Paths.get(file.getName())));
            manager = new FileBackedTasksManager(file.getPath());

            for (int i = 1; i < listOfStrings.size() - 2; i++) {
                Task task = manager.fromString(listOfStrings.get(i));
                if (task instanceof Epic) {
                    manager.createEpicWhileLoading((Epic)task);
                }
            }
            for (int i = 1; i < listOfStrings.size() - 2; i++) {
                Task task = manager.fromString(listOfStrings.get(i));
                if (task instanceof SubTask) {
                    manager.createSubTaskWhileLoading((SubTask) task);
                } else if (task != null && !(task instanceof Epic)) {
                    manager.createTaskWhileLoading(task);
                }
            }
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка при загрузке");
        }
        //тут восстанавливаем историю
        if(listOfStrings.size()>0){
        String[] tempHistory = listOfStrings.get(listOfStrings.size() - 1).split(",");
        for (int i = 0; i < tempHistory.length; i++) {
            int historyElement = Integer.parseInt(tempHistory[i]);
            manager.getMemoryManager().add(manager.getTaskUniversal(historyElement));
        }}
        manager.setUin(manager.getManagerEpicsMap().size() + manager.getManagerTasksMap().size() + manager.getManagerSubTasksMap().size());
        manager.save();
        return manager;
    }

    public void save()  {
        try (PrintWriter pw = new PrintWriter(file.getName())) {
            pw.println("id,type,name,status,description,epic,startTime,duration,endTime");
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
        super.createSubTask(subtask, epicId);
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
    public void updateTask( Task task) {
        super.updateTask(task);
        save();
    }

    @Override
    public void updateSubTask( SubTask newSubtask) {
        super.updateSubTask(newSubtask);
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
        if (getManagerEpicsMap().get(uin) != null) {
            getMemoryManager().add(getManagerEpicsMap().get(uin));
            save();
            return getManagerEpicsMap().get(uin);
        } else
            return null;
    }

    @Override
    public Task getTaskByUin(int uin) {
        if (getManagerTasksMap().get(uin) != null) {
            getMemoryManager().add(getManagerTasksMap().get(uin));
            save();
            return getManagerTasksMap().get(uin);
        } else
            return null;
    }

    @Override
    public Task getSubTaskByUin(int uin) {
        if (getManagerSubTasksMap().get(uin) != null) {
            getMemoryManager().add(getManagerSubTasksMap().get(uin));
            save();
            return getManagerSubTasksMap().get(uin);
        } else
            return null;
    }


    Task fromString(String value) {
        String[] temp = value.split(",");

        switch (temp[1]) {
            case "SUBTASK":
                SubTask  subTask = new SubTask(temp[2], temp[4], Integer.parseInt(temp[5]), LocalDateTime.parse(temp[6]),  Duration.parse(temp[7]));
                subTask.setStatus(Status.valueOf(temp[3]));
                subTask.setUin(Integer.parseInt(temp[0]));
                return subTask;
            case "EPIC":
                Epic epic = new Epic((temp[2]), temp[4] );
                if (!temp[5].equals("null")) {
                    epic.setStartTime(null);
                }
                if (!temp[6].equals("null")) {
                    epic.setDuration(Duration.parse(temp[6]));
                }
                if (!temp[3].equals("null")) {
                    epic.setStatus(Status.valueOf(temp[3]));
                }
                if (!temp[7].equals("null")) {
                    epic.setEndTime(LocalDateTime.parse(temp[7]));
                }
                epic.setUin(Integer.parseInt(temp[0]));
                return epic;

            case "TASK":
                Task task = new Task(temp[2], temp[4],LocalDateTime.parse(temp[5]), Duration.parse(temp[6]));
                task.setStatus(Status.valueOf(temp[3]));
                task.setUin(Integer.parseInt(temp[0]));
                return task;

            default:
                return null;
        }
    }


}
