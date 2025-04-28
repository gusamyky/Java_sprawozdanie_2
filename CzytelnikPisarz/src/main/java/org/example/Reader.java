package org.example;

class Reader implements Runnable {
    private final SharedFile sharedFile;

    public Reader(SharedFile sharedFile) {
        this.sharedFile = sharedFile;
    }

    @Override
    public void run() {
        while (true) {
            try {
                String line = sharedFile.read();
                System.out.println("Read line: " + line);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }
}