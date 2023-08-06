import task.InMemoryTaskManager;
import task.TaskManager;
import task.taskData.*;
public class Main {

    public static void main(String[] args) {
        TaskManager inMemoryTaskManager = new InMemoryTaskManager();

        System.out.println("Поехали!");

        //добавляю новые таски
        inMemoryTaskManager.newEpicTask(new Epic("эпик1", "111")); // добавлен эпик id1
        inMemoryTaskManager.newSubTask(new SubTask("саб1", "100", 1)); // добавлен сабтаск id2
        inMemoryTaskManager.newEpicTask(new Epic("эпик2", "222")); // добавлен эпик id3
        inMemoryTaskManager.newSubTask(new SubTask("саб2", "200", 3)); // добавлен сабтаск id4
        inMemoryTaskManager.newSubTask(new SubTask("саб3", "300", 3)); // добавлен сабтаск id5
        inMemoryTaskManager.newTask(new Task("таск1", "1")); // добавлен таск id6
        inMemoryTaskManager.newTask(new Task("таск2", "2")); // добавлен таск id7

        // вызов списков задач всех типов
        System.out.println(inMemoryTaskManager.getEpicTaskMap());
        System.out.println(inMemoryTaskManager.getSubTaskMap());
        System.out.println(inMemoryTaskManager.getTaskMap());
        System.out.println();
        System.out.println(inMemoryTaskManager.getHistory());
        System.out.println();

        //получение по id
        System.out.println(inMemoryTaskManager.getEpicTaskById(1));
        System.out.println(inMemoryTaskManager.getSubTaskById(2));
        System.out.println(inMemoryTaskManager.getTaskById(7));
        System.out.println(inMemoryTaskManager.getSubTaskMapByEpic(1));
        System.out.println();
        System.out.println(inMemoryTaskManager.getHistory());
        System.out.println();

        System.out.println(inMemoryTaskManager.getEpicTaskById(3));
        System.out.println(inMemoryTaskManager.getSubTaskById(4));
        System.out.println(inMemoryTaskManager.getSubTaskById(5));
        System.out.println(inMemoryTaskManager.getSubTaskMapByEpic(3));
        System.out.println();

        //обновил задачи
        inMemoryTaskManager.updateTask(6, new Task("таск1 новый", "1'", Status.DONE));
        inMemoryTaskManager.updateSubTask(5, new SubTask("саб2 новый", "200'", Status.DONE, 1));
        inMemoryTaskManager.updateEpicTask(3, new Epic("эпик2 новый", "222'"));
        inMemoryTaskManager.updateSubTask(4, new SubTask("саб3 новый", "300'", Status.DONE, 3));

        System.out.println(inMemoryTaskManager.getEpicTaskById(1));
        System.out.println(inMemoryTaskManager.getSubTaskById(5));
        System.out.println(inMemoryTaskManager.getTaskById(6));
        System.out.println(inMemoryTaskManager.getSubTaskMapByEpic(1));
        System.out.println();
        System.out.println(inMemoryTaskManager.getTaskById(6).getStatus());
        System.out.println(inMemoryTaskManager.getSubTaskById(5).getStatus());
        System.out.println(inMemoryTaskManager.getEpicTaskById(1).getStatus());
        System.out.println("<<<<<<<<<<<<<");
        System.out.println(inMemoryTaskManager.getEpicTaskById(3).getStatus());
        System.out.println(inMemoryTaskManager.getSubTaskMapByEpic(3));
        System.out.println(">>>>>>>>>>>>>");
        System.out.println(inMemoryTaskManager.getHistory());
        System.out.println();

        System.out.println(inMemoryTaskManager.getEpicTaskById(3));
        System.out.println(inMemoryTaskManager.getSubTaskById(4));
        System.out.println(inMemoryTaskManager.getSubTaskById(5));
        System.out.println(inMemoryTaskManager.getSubTaskMapByEpic(3));
        System.out.println();
        System.out.println(inMemoryTaskManager.getHistory());
        System.out.println();

        //удаление
        inMemoryTaskManager.removeSubTask(5);
        inMemoryTaskManager.removeSubTask(4);
        inMemoryTaskManager.removeTask(8);
        inMemoryTaskManager.removeEpicTask(1);
        inMemoryTaskManager.clearSubTaskMap();
        inMemoryTaskManager.clearTaskMap();
        inMemoryTaskManager.clearEpicTaskMap();

        System.out.println(inMemoryTaskManager.getEpicTaskMap());
        System.out.println(inMemoryTaskManager.getSubTaskMap());
        System.out.println(inMemoryTaskManager.getTaskMap());
        System.out.println(inMemoryTaskManager.getSubTaskMapByEpic(3));
        System.out.println(inMemoryTaskManager.getEpicTaskById(1));

    }
}
