package pcd.ass01.multithreaded;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * A simple monitor class that allows one thread to set a value and another thread to get it.
 * This implementation is from the course slides (lab 1.6).
 * @param <T>
 */
public class MyMonitor<T> {
    private Lock mutex;
    private Condition available;
    private T value;
    private boolean isSet = false;

    /**
     * Constructor for MyMonitor.
     */
    public MyMonitor() {
        this.mutex = new ReentrantLock();
        this.available = this.mutex.newCondition();
    }

    /**
     * Constructor for MyMonitor with an initial value.
     * @param value the initial value to set
     */
    public MyMonitor(T value) {
        this();
        this.set(value);
    }

    /**
     * Sets the value of the monitor.
     * @param value the value to set
     */
    public synchronized void set(T value) {
        try {
            this.mutex.lock();
            this.value = value;
            this.isSet = true;
            this.available.signalAll();
        } finally {
            this.mutex.unlock();
        }
    }

    /**
     * Gets the value of the monitor.
     * @return the value of the monitor
     */
    public synchronized T get() {
        try {
            mutex.lock();
            if (!isSet){
                try {
                    available.await();
                } catch (InterruptedException ignored) {}
            }
            return value;
        } finally {
            mutex.unlock();
        }
    }
}
