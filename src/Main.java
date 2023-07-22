public class Main {

    public static void main(String[] args) {
        TaskManager taskManager = new TaskManager();

        System.out.println("Поехали!");

        //добавляю новые таски
        taskManager.newEpicTask(new Epic("эпик1", "111")); // добавлен эпик id1
        taskManager.newSubTask(new SubTask("саб1", "100", 1)); // добавлен сабтаск id2
        taskManager.newEpicTask(new Epic("эпик2", "222")); // добавлен эпик id3
        taskManager.newSubTask(new SubTask("саб2", "200", 3)); // добавлен сабтаск id4
        taskManager.newSubTask(new SubTask("саб3", "300", 3)); // добавлен сабтаск id5
        taskManager.newTask(new Task("таск1", "1")); // добавлен таск id6
        taskManager.newTask(new Task("таск2", "2")); // добавлен таск id7

        // вызов списков задач всех типов
        System.out.println(taskManager.getEpicTaskMap());
        System.out.println(taskManager.getSubTaskMap());
        System.out.println(taskManager.getTaskMap());
        System.out.println();

        //получение по id
        System.out.println(taskManager.getEpicTaskById(1));
        System.out.println(taskManager.getSubTaskById(2));
        System.out.println(taskManager.getTaskById(7));
        System.out.println(taskManager.getSubTaskMapByEpic(1));
        System.out.println();

        System.out.println(taskManager.getEpicTaskById(3));
        System.out.println(taskManager.getSubTaskById(4));
        System.out.println(taskManager.getSubTaskById(5));
        System.out.println(taskManager.getSubTaskMapByEpic(3));
        System.out.println();

        //обновил задачи
        taskManager.updateTask(6, new Task("таск1 новый", "1'", Status.DONE));
        taskManager.updateSubTask(5, new SubTask("саб2 новый", "200'", Status.DONE, 1));
        taskManager.updateEpicTask(3, new Epic("эпик2 новый", "222'"));
        taskManager.updateSubTask(4, new SubTask("саб3 новый", "300'", Status.DONE, 3));

        System.out.println(taskManager.getEpicTaskById(1));
        System.out.println(taskManager.getSubTaskById(5));
        System.out.println(taskManager.getTaskById(6));
        System.out.println(taskManager.getSubTaskMapByEpic(1));
        System.out.println();
        System.out.println(taskManager.getTaskById(6).getStatus());
        System.out.println(taskManager.getSubTaskById(5).getStatus());
        System.out.println(taskManager.getEpicTaskById(1).getStatus());
        System.out.println("<<<<<<<<<<<<<");
        System.out.println(taskManager.getEpicTaskById(3).getStatus());
        System.out.println(taskManager.getSubTaskMapByEpic(3));
        System.out.println(">>>>>>>>>>>>>");

        System.out.println(taskManager.getEpicTaskById(3));
        System.out.println(taskManager.getSubTaskById(4));
        System.out.println(taskManager.getSubTaskById(5));
        System.out.println(taskManager.getSubTaskMapByEpic(3));
        System.out.println();

        //удаление
        taskManager.removeSubTask(5);
        taskManager.removeSubTask(4);
        taskManager.removeTask(8);
        taskManager.removeEpicTask(1);
        taskManager.clearSubTaskMap();
        taskManager.clearTaskMap();
        taskManager.clearEpicTaskMap();

        System.out.println(taskManager.getEpicTaskMap());
        System.out.println(taskManager.getSubTaskMap());
        System.out.println(taskManager.getTaskMap());
        System.out.println(taskManager.getSubTaskMapByEpic(3));
        System.out.println(taskManager.getEpicTaskById(1));

    }
}
