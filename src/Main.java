import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        TaskManager taskManager = new TaskManager();
        Scanner scanner = new Scanner(System.in);

        System.out.println("Поехали!");
        System.out.println("Что вы хотите сделать?");
        System.out.println("1-создать задачу; 2 - создать подзадачу; 3 -создать эпик");

        taskManager.newEpicTask(new Epic("эпик1", "111"));
        taskManager.newSubTask(new SubTask("саб1", "100", 1));
        taskManager.newEpicTask(new Epic("эпик2", "222"));
        taskManager.newSubTask(new SubTask("саб2", "200", 3));
        taskManager.newSubTask(new SubTask("саб3", "300", 3));
        taskManager.newTask(new Task("таск1", "1"));
        taskManager.newTask(new Task("таск2", "2"));

        System.out.println(taskManager.epicTaskMap);
        System.out.println(taskManager.subTaskMap);
        System.out.println(taskManager.taskMap);

        System.out.println(taskManager.epicTaskMap.get(1));
        System.out.println(taskManager.subTaskMap.get(2));
        System.out.println(taskManager.epicTaskMap.get(1).getSubTaskMap());

        System.out.println(taskManager.epicTaskMap.get(3));
        System.out.println(taskManager.subTaskMap.get(4));
        System.out.println(taskManager.subTaskMap.get(5));
        System.out.println(taskManager.epicTaskMap.get(3).getSubTaskMap());
    }
}
