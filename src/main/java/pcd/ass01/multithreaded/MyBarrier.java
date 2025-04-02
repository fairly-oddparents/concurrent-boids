package pcd.ass01.multithreaded;

import pcd.ass01.multithreaded.api.Barrier;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * An implementation of a barrier for synchronizing threads.
 */
public class MyBarrier implements Barrier {

    private final Lock mutex;
    private final Condition broken;
    private final int parties;
    private int count;

    /**
     * Creates a new MyBarrier that will be triggered when the given number of threads (parties) are waiting for it.
     * @param parties the number of threads that need to reach the barrier
     */
    public MyBarrier(int parties) {
        this.mutex = new ReentrantLock();
        this.broken = this.mutex.newCondition();
        this.parties = parties;
        this.count = 0;
    }

    @Override
    public void await() {
        try {
            this.mutex.lock();
            if (++this.count < this.parties)
                broken.await();
            else {
                this.count = 0;
                broken.signalAll();
            }
        } catch (InterruptedException ignored) {}
        finally {
            this.mutex.unlock();
        }
    }

}
