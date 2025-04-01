package pcd.ass01.multithreaded;

import pcd.ass01.multithreaded.api.Barrier;

public class BarrierImpl implements Barrier {
    private final int totalThreads;
    private int count = 0;

    public BarrierImpl(int totalThreads) {
        this.totalThreads = totalThreads;
    }

    @Override
    public synchronized void await() {
        if (++this.count == this.totalThreads) {
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
