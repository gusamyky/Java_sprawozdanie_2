package org.example;

import java.util.Scanner;

import static java.lang.System.exit;

class Writer implements Runnable {
    private final SharedFile sharedFile;

    public Writer(SharedFile sharedFile) {
        this.sharedFile = sharedFile;
    }

    @Override
    public void run() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.print("Enter a line to write: ");
            String line = scanner.nextLine();
            if (line.equalsIgnoreCase("/exit")) {
                exit(0);
            }
            try {
                sharedFile.write(line);

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
        scanner.close();
    }
}