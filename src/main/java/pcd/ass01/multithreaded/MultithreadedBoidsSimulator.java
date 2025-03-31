package pcd.ass01.multithreaded;

import pcd.ass01.Boid;
import pcd.ass01.BoidsModel;
import pcd.ass01.BoidsSimulator;
import pcd.ass01.multithreaded.api.Barrier;

import java.util.LinkedList;
import java.util.List;

public class MultithreadedBoidsSimulator extends BoidsSimulator {
    private final LinkedList<Thread> threads = new LinkedList<>();

    public MultithreadedBoidsSimulator(BoidsModel model) {
        super(model);
    }

    @Override
    public void runSimulation() {
        final int numThreads = Runtime.getRuntime().availableProcessors() + 1;
        while(true) {
            var nBoids = this.model.getBoids().size();
            Barrier velBarrier = new BarrierImpl(numThreads);
            Barrier posBarrier = new BarrierImpl(numThreads + 1);
            List<Boid> boids = this.model.getBoids();
            int boidsPerWorker = boids.size() / numThreads;
            for (int i = 0; i < numThreads; i++) {
                int start = i * boidsPerWorker;
                List<Boid> subList = boids.subList(start, (i == numThreads - 1) ? boids.size() : start + boidsPerWorker);
                threads.add(new BoidThread(this, this.model, velBarrier, posBarrier, subList));
            }
            threads.forEach(Thread::start);
            while (!this.model.getBoids().isEmpty()) {
                var t0 = System.currentTimeMillis();
                try {
                    posBarrier.await();
                } catch (InterruptedException ignored) {}
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
