package org.example;

public class Main {
    public static void main(String[] args) {
        SharedFile sharedFile = new SharedFile("data.txt");
        Thread writerThread = new Thread(new Writer(sharedFile));
        Thread readerThread = new Thread(new Reader(sharedFile));

        writerThread.start();
        readerThread.start();
    }
}