package org.example;

import java.io.*;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    private static final String INPUT_FILE = "input.txt";

    public static void main(String[] args) {
        System.out.println("Katalog roboczy: " + Paths.get("").toAbsolutePath());
        List<String> equations = new ArrayList<>();
        try (RandomAccessFile raf = new RandomAccessFile(INPUT_FILE, "r")) {
            String line;
            while ((line = raf.readLine()) != null) {
                equations.add(line);
            }
        } catch (IOException e) {
            System.err.println("Nie można wczytać " + INPUT_FILE + ": " + e.getMessage());
            return;
        }

        Scanner sc = new Scanner(System.in);
        boolean exit = false;

        while (!exit) {
            printMenu();
            int choice = sc.nextInt();
            sc.nextLine();  // konsumuj \n

            switch (choice) {
                case 1 -> {
                    try {
                        EquationCalculator.calculateAndPrint(equations);
                    } catch (InterruptedException e) {
                        System.err.println("Przerwano obliczenia.");
                    }
                }
                case 2 -> {
                    try {
                        EquationCalculator.calculateAndWriteFile(equations);
                    } catch (IOException | InterruptedException e) {
                        System.err.println("Błąd zapisu do pliku: " + e.getMessage());
                    }
                }
                case 3 -> HistoryManager.displayLastLines(10);
                case 4 -> {
                    System.out.print("Podaj liczbę ostatnich wierszy do wyświetlenia: ");
                    int n = sc.nextInt();
                    HistoryManager.displayLastLines(n);
                }
                case 5 -> exit = true;
                default -> System.out.println("Nieprawidłowa opcja, spróbuj ponownie.");
            }
        }

        System.out.println("Koniec programu.");
        sc.close();
    }

    private static void printMenu() {
        System.out.println("\n=== MENU ===");
        System.out.println("1. oblicz równania");
        System.out.println("2. oblicz równania z zapisem do pliku");
        System.out.println("3. historia (ostatnie 10)");
        System.out.println("4. historia z parametrem (ostatnie n)");
        System.out.println("5. wyjscie");
        System.out.print("Wybierz opcję: ");
    }
}
