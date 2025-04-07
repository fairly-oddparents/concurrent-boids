package pcd.ass01;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Predicate;

/**
 * A simple monitor class that allows one thread to set a value and another thread to get it.
 * This implementation is from the course slides (lab 1.6).
 * @param <T>
 */
public class MyMonitor<T> {
    private final Lock mutex;
    private final Condition available;
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
    public void set(T value) {
        this.mutex.lock();
        try {
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
    public T get() {
        try {
            mutex.lock();
            while (!isSet){
                try {
                    available.await();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return null;
                }
            }
            return value;
        } finally {
            mutex.unlock();
        }
    }

    /**
     * Waits until the predicate is true.
     * @param predicate the predicate that checks the condition on the value
     */
    public void awaitUntil(Predicate<T> predicate) {
        this.mutex.lock();
        try {
            while (!predicate.test(this.value)) {
                try {
                    this.available.await();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        } finally {
            this.mutex.unlock();
        }
    }
}
