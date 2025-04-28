package org.example;

import java.util.*;
import java.util.concurrent.*;

public class Main {
    public static void main(String[] args) {
        TicketPool pool = new TicketPool(3);
        ExecutorService executor = Executors.newFixedThreadPool(5);

        Map<Integer, TicketWorker> workerMap = new HashMap<>();
        Scanner scanner = new Scanner(System.in);

        for (int i = 0; i < 10; i++) {
            TicketWorker worker = new TicketWorker(i, pool);
            workerMap.put(i, worker);
            executor.submit(worker);
        }

        boolean running = true;
        while (running) {
            System.out.println("\n--- MENU ---");
            System.out.println("1. Przegląd wątków");
            System.out.println("2. Przerwij wątek");
            System.out.println("3. Przerwij wszystkie wątki");
            System.out.println("4. Wyjście");

            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    workerMap.forEach((id, w) ->
                            System.out.println("Wątek nr " + id + " | Status: " + w.getStatus().toString()));
                    break;
                case "2":
                    System.out.print("Podaj numer wątku do zatrzymania: ");
                    int id = Integer.parseInt(scanner.nextLine());
                    TicketWorker worker = workerMap.get(id);
                    if (worker != null) {
                        worker.stop();
                    }
                    break;
                case "3":
                    workerMap.values().forEach(TicketWorker::stop);
                    break;
                case "4":
                    running = false;
                    workerMap.values().forEach(TicketWorker::stop);
                    executor.shutdownNow();
                    break;
                default:
                    System.out.println("Nieznana opcja.");
            }
        }

        System.out.println("Program zakończony.");
    }
}