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
        var nBoids = this.model.getBoids().size();
        Barrier barrier = new BarrierImpl(nBoids);
        for (int i = 0; i < nBoids; i++) {
            threads.add(new BoidThread(this, this.model, barrier, this.model.getBoids().get(i)));
        }
        threads.forEach(Thread::start);
    	while (true) {
            var t0 = System.currentTimeMillis();
            if (this.model.getBoids().isEmpty()) {
                this.stopSimulation(); //TODO: e quando deve ripartire?
            }
    		updateView(t0);
    	}
    }

    public synchronized void stopSimulation() {
        for (Thread thread : threads) {
            thread.interrupt();
        }
        threads.clear();
    }
}
