package org.example;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        TaskManager mgr = new TaskManager();
        boolean exit = false;

        while (!exit) {
            printMenu();
            int choice = sc.nextInt();
            switch (choice) {
                case 1 -> {
                    int id = mgr.submitTask();
                    System.out.println("Uruchomiono zadanie o ID = " + id);
                }
                case 2 -> {
                    System.out.printf("%-4s %-10s %-8s %-8s%n", "ID", "Thread", "State", "Working");
                    for (Task t : mgr.listTasks()) {
                        Thread th = mgr.getThread(t.getId());
                        System.out.printf("%-4d %-10s %-8s %-8s%n",
                                t.getId(),
                                th.getName(),
                                t.getState(),
                                t.isWorking());
                    }
                }
                case 3 -> {
                    System.out.print("Podaj ID zadania: ");
                    int id = sc.nextInt();
                    Task t = mgr.getTask(id);
                    if (t != null) {
                        System.out.println("Zadanie " + id + " ma stan: " + t.getState());
                    } else {
                        System.out.println("Brak zadania o ID = " + id);
                    }
                }
                case 4 -> {
                    System.out.print("Podaj ID zadania do anulowania: ");
                    int id = sc.nextInt();
                    boolean ok = mgr.cancelTask(id);
                    System.out.println(ok
                            ? "Wysłano przerwanie do zadania " + id
                            : "Nie można przerwać zadania " + id);
                }
                case 5 -> {
                    System.out.print("Podaj ID zadania: ");
                    int id = sc.nextInt();
                    Task t = mgr.getTask(id);
                    if (t != null) {
                        if (t.getState() == Task.State.COMPLETED || t.getState() == Task.State.ERROR) {
                            System.out.println("Wynik zadania " + id + ": " + t.getResult());
                        } else {
                            System.out.println("Zadanie " + id + " jeszcze nie zakończone (" + t.getState() + ")");
                        }
                    } else {
                        System.out.println("Brak zadania o ID = " + id);
                    }
                }
                case 0 -> exit = true;
                default -> System.out.println("Nieprawidłowa opcja, spróbuj ponownie.");
            }
        }
        sc.close();
        System.out.println("Koniec programu.");
    }

    private static void printMenu() {
        System.out.println("\n=== MENU ===");
        System.out.println("1. Uruchom nowe zadanie");
        System.out.println("2. Pokaż listę zadań");
        System.out.println("3. Sprawdź stan zadania");
        System.out.println("4. Anuluj zadanie");
        System.out.println("5. Pokaż wynik zadania");
        System.out.println("0. Wyjście");
        System.out.print("Wybierz opcję: ");
    }
}
