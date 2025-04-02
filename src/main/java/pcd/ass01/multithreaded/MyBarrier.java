package pcd.ass01.multithreaded;

import pcd.ass01.multithreaded.api.Barrier;

/**
 * An implementation of a barrier for synchronizing threads.
 */
public class MyBarrier implements Barrier {

    private final int parties;
    private int count;

    /**
     * Creates a new MyBarrier that will be triggered when the given number of threads (parties) are waiting for it.
     * @param parties the number of threads that need to reach the barrier
     */
    public MyBarrier(int parties) {
        this.parties = parties;
        this.count = 0;
    }

    @Override
    public synchronized void await() {
        if (++this.count == this.parties) {
            this.count = 0;
            notifyAll();
        } else {
            try {
                wait();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

}
