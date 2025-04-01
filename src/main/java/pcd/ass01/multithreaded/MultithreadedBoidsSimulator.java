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
        while (true) {
            super.waitForSimulation();
            System.out.println("Starting simulation");  //TODO: remove logs
            Barrier velBarrier = new BarrierImpl(numThreads);
            Barrier posBarrier = new BarrierImpl(numThreads + 1);
            List<Boid> boids = this.model.getBoids();
            int boidsPerWorker = boids.size() / numThreads;
            for (int i = 0; i < numThreads; i++) {
                int start = i * boidsPerWorker;
                int end = (i == numThreads - 1) ? boids.size() : start + boidsPerWorker;
                List<Boid> subList = boids.subList(start, end);
                this.threads.add(new BoidThread(this, this.model, velBarrier, posBarrier, subList));
            }
            threads.forEach(Thread::start);
            while (!super.isStopped()) {
                var t0 = System.currentTimeMillis();
                updateView(t0);
                posBarrier.await();
            }
            this.removeThreads();
            while (this.model.getBoids().isEmpty()) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException ignored) { }
            }
        }
    }

    public synchronized void removeThreads() {
        for (Thread thread : threads) {
            thread.interrupt();
        }
        System.out.println("All threads killed");   //TODO: remove logs
        threads.clear();
    }
}
