package org.example;

import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;


public class Main {
    public static void main(String[] args) {
        ExecutorService executor = Executors.newFixedThreadPool(3);
        Map<Integer, CustomFutureTask<String>> tasksMap = new ConcurrentHashMap<>();

        for (int i = 1; i <= 5; i++) {
            CustomFutureTask<String> task = new CustomFutureTask<>(new LongRunningTask(i), i);
            tasksMap.put(i, task);
            executor.submit(task);
        }

        Scanner scanner = new Scanner(System.in);
        boolean exit = false;
        while (!exit) {
            System.out.println("\nMenu:");
            System.out.println("1. Show status of tasks");
            System.out.println("2. Cancel task");
            System.out.println("3. Show task result");
            System.out.println("4. Exit");
            System.out.print("Your choice: ");
            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    showTaskStatus(tasksMap);
                    break;
                case "2":
                    System.out.print("Enter ID of task to cancel: ");
                    String idStr = scanner.nextLine();
                    cancelTask(tasksMap, idStr);
                    break;
                case "3":
                    System.out.print("Enter ID of task to see result: ");
                    String idStr2 = scanner.nextLine();
                    showTaskResult(tasksMap, idStr2);
                    break;
                case "4":
                    exit = true;
                    break;
                default:
                    System.out.println("Wrong option, try again.");
            }
        }

        System.out.println("Exiting...");
        executor.shutdownNow();
        scanner.close();
    }

    private static void showTaskStatus(Map<Integer, CustomFutureTask<String>> tasksMap) {
        System.out.println("\nTasks Status:");
        for (Map.Entry<Integer, CustomFutureTask<String>> entry : tasksMap.entrySet()) {
            int id = entry.getKey();
            Future<String> task = entry.getValue();
            String status;
            if (task.isCancelled()) {
                status = "Cancelled";
            } else if (task.isDone()) {
                status = "Successfully completed";
            } else {
                status = "Currently running";
            }
            System.out.println("Task " + id + ": " + status);
        }
    }

    private static void cancelTask(Map<Integer, CustomFutureTask<String>> tasksMap, String idStr) {
        try {
            int id = Integer.parseInt(idStr);
            CustomFutureTask<String> task = tasksMap.get(id);
            if (task != null) {
                boolean canceled = task.cancel(true);
                if (canceled) {
                    System.out.println("Attempt to cancel task " + id + " was sent.");
                } else {
                    System.out.println("Unable to cancel task with ID " + id + ".");
                }
            } else {
                System.out.println("Unable to find task with ID "+id+".");
            }
        } catch (NumberFormatException e) {
            System.out.println("Wrong task ID format.");
        }
    }

    private static void showTaskResult(Map<Integer, CustomFutureTask<String>> tasksMap, String idStr) {
        try {
            int id = Integer.parseInt(idStr);
            CustomFutureTask<String> task = tasksMap.get(id);
            if (task != null) {
                if (task.isDone() && !task.isCancelled()) {
                    try {
                        String result = task.get();
                        System.out.println("Result of task " + id + ": " + result);
                    } catch (Exception ex) {
                        System.out.println("Error occurred during result fetching: " + ex.getMessage());
                    }
                } else if (task.isCancelled()) {
                    System.out.println("Task with ID " + id + " was cancelled.");
                } else {
                    System.out.println("Task with ID " + id + " still in progress.");
                }
            } else {
                System.out.println("Unable to find task with given ID.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Wrong task ID format .");
        }
    }
}