package pcd.ass01.multithreaded;

import pcd.ass01.multithreaded.api.Barrier;

public class BarrierImpl implements Barrier {
    private int count = 0;
    private final int totalThreads;

    public BarrierImpl(int totalThreads) {
        this.totalThreads = totalThreads;
    }

    @Override
    public synchronized void await() {
        count++;
        if (count == totalThreads) {
            count = 0;
            notifyAll();
        } else {
            try {
                wait();
            } catch (InterruptedException ignored) {}
        }
    }

}
