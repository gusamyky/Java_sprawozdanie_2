package org.example;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class HistoryManager {

    /** Wyświetla ostatnie n linii z pliku results.txt */
    public static void displayLastLines(int n) {
        File resultsFile = new File("results.txt");
        if (!resultsFile.exists()) {
            System.out.println("Brak pliku results.txt – najpierw oblicz równania z zapisem do pliku.");
            return;
        }

        try (RandomAccessFile raf = new RandomAccessFile(resultsFile, "r")) {
            // Wczytaj wszystkie linie z pliku
            List<String> all = new ArrayList<>();
            String line;
            while ((line = raf.readLine()) != null) {
                all.add(line);
            }
            
            int start = Math.max(0, all.size() - n);
            for (int i = start; i < all.size(); i++) {
                System.out.println(all.get(i));
            }
        } catch (IOException e) {
            System.err.println("Błąd odczytu historii: " + e.getMessage());
        }
    }
}
