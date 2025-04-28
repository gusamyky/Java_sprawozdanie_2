package org.example;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class SharedFile {
    private final Lock lock = new ReentrantLock();
    private final Condition dataAvailable = lock.newCondition();
    private final Condition spaceAvailable = lock.newCondition();
    private final String filePath;
    private boolean hasNewData = false;

    public SharedFile(String filePath) {
        this.filePath = filePath;
    }

    public void lockWrite() throws InterruptedException {
        lock.lock();
        try {
            while (hasNewData) {
                spaceAvailable.await();
            }
        } finally {
            lock.unlock();
        }
    }

    public void unlockWrite() {
        lock.lock();
        try {
            hasNewData = true;
            dataAvailable.signal();
        } finally {
            lock.unlock();
        }
    }

    public void lockRead() throws InterruptedException {
        lock.lock();
        try {
            while (!hasNewData) {
                dataAvailable.await();
            }
        } finally {
            lock.unlock();
        }
    }

    public void unlockRead() {
        lock.lock();
        try {
            hasNewData = false;
            spaceAvailable.signal();
        } finally {
            lock.unlock();
        }
    }

    public void write(String line) throws InterruptedException {
        lockWrite();
        try (RandomAccessFile file = new RandomAccessFile(filePath, "rw")) {
            file.seek(file.length());
            file.writeBytes(line + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            unlockWrite();
        }
    }

    public String read() throws InterruptedException {
        lockRead();
        String lastLine = null;
        try (RandomAccessFile file = new RandomAccessFile(filePath, "r")) {
            String line;
            while ((line = file.readLine()) != null) {
                lastLine = line;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            unlockRead();
        }
        return lastLine;
    }
}
