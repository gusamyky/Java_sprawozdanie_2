package org.example;

import java.io.*;
import java.nio.file.*;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.locks.ReentrantLock;

public class EquationCalculator {
    private static final ReentrantLock fileLock = new ReentrantLock();

    /** Oblicza wszystkie równania i wypisuje je na konsolę */
    public static void calculateAndPrint(List<String> equations) throws InterruptedException {
        ExecutorService exec = Executors.newFixedThreadPool(
                Runtime.getRuntime().availableProcessors()
        );

        for (String eq : equations) {
            FutureTask<String> task = new FutureTask<>(() -> RPN.formatResult(eq)) {
                @Override
                protected void done() {
                    try {
                        System.out.println(get());
                    } catch (InterruptedException | ExecutionException e) {
                        System.err.println("Błąd obliczenia dla: " + eq);
                    }
                }
            };
            exec.submit(task);
        }

        exec.shutdown();
        exec.awaitTermination(1, TimeUnit.HOURS);
    }

    /**
     * Oblicza wszystkie równania i dopisuje wyniki do pliku results.txt.
     * Tworzy plik, jeśli nie istnieje, lub czyści go, jeśli jest już obecny.
     */
    public static void calculateAndWriteFile(List<String> equations)
            throws IOException, InterruptedException {

        // Usuń stary lub stwórz nowy plik
        File resultsFile = new File("results.txt");
        if (resultsFile.exists()) {
            resultsFile.delete();
        }
        resultsFile.createNewFile();

        ExecutorService exec = Executors.newFixedThreadPool(
                Runtime.getRuntime().availableProcessors()
        );

        for (String eq : equations) {
            FutureTask<String> task = new FutureTask<>(() -> RPN.formatResult(eq)) {
                @Override
                protected void done() {
                    String line;
                    try {
                        line = (String) get();
                    } catch (InterruptedException | ExecutionException e) {
                        line = eq + " ERROR";
                    }
                    // Bezpieczne dopisanie do pliku
                    fileLock.lock();
                    try (RandomAccessFile raf = new RandomAccessFile(resultsFile, "rw")) {
                        raf.seek(raf.length()); // Przejdź na koniec pliku
                        raf.writeBytes(line + System.lineSeparator());
                    } catch (IOException io) {
                        io.printStackTrace();
                    } finally {
                        fileLock.unlock();
                    }
                }
            };
            exec.submit(task);
        }

        exec.shutdown();
        exec.awaitTermination(1, TimeUnit.HOURS);
        System.out.println("Wszystkie wyniki zapisano do results.txt");
    }
}
