package pcd.ass01.multithreaded;

import pcd.ass01.BoidsModel;
import pcd.ass01.BoidsSimulator;
import pcd.ass01.multithreaded.api.Barrier;

import java.util.LinkedList;

public class MultithreadedBoidsSimulator extends BoidsSimulator {
    private final LinkedList<Thread> threads = new LinkedList<>();

    public MultithreadedBoidsSimulator(BoidsModel model) {
        super(model);
    }

    @Override
    public void runSimulation() {
        while(true) {
            var nBoids = this.model.getBoids().size();
            Barrier barrier = new BarrierImpl(nBoids);
            for (int i = 0; i < nBoids; i++) {
                threads.add(new BoidThread(this, this.model, barrier, this.model.getBoids().get(i)));
            }
            threads.forEach(Thread::start);
            while (!this.model.getBoids().isEmpty()) {
                var t0 = System.currentTimeMillis();
                updateView(t0);
            }
            this.stopSimulation();
        }
    }

    public synchronized void stopSimulation() {
        for (Thread thread : threads) {
            thread.interrupt();
        }
        threads.clear();
    }
}
