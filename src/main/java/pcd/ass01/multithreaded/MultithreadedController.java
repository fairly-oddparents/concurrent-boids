package pcd.ass01.multithreaded;

import pcd.ass01.Boid;
import pcd.ass01.BoidsModel;
import pcd.ass01.BoidsController;
import pcd.ass01.MyBarrier;
import pcd.ass01.api.Barrier;

import java.util.LinkedList;
import java.util.List;

/**
 * Controller for the multithreaded boids simulation.
 */
public class MultithreadedController extends BoidsController {
    private static final int NUM_THREADS = Runtime.getRuntime().availableProcessors() + 1;
    private final LinkedList<Thread> workers = new LinkedList<>();
    private Barrier boidsUpdated;

    /**
     * Constructor for the MultithreadedController.
     * @param model the model
     */
    public MultithreadedController(BoidsModel model) {
        super(model);
    }

    @Override
    public void run() {
        super.model.setNumberBoids(super.getNumberOfBoids());
        while (true) {
            super.awaitRun();
            this.SetUpBoids();
            while (!super.isStopped()) {
                var t0 = System.currentTimeMillis();
                this.boidsUpdated.await();
                updateView(t0);
            }
            this.removeThreads();
        }
    }

    private void SetUpBoids() {
        List<Boid> boids = this.model.getBoids();
        int numThreads = Math.min(NUM_THREADS, boids.size());
        Barrier readToWrite = new MyBarrier(numThreads);
        Barrier boidsUpdated = new MyBarrier(numThreads + 1);
        int boidsPerWorker = boids.size() / numThreads;
        for (int i = 0; i < numThreads; i++) {
            int start = i * boidsPerWorker;
            int end = (i == numThreads - 1) ? boids.size() : start + boidsPerWorker;
            List<Boid> subList = boids.subList(start, end);
            Thread worker = new BoidWorker(this, this.model, readToWrite, boidsUpdated, subList);
            this.workers.add(worker);
            worker.start();
        }
        this.boidsUpdated = boidsUpdated;
    }

    private synchronized void removeThreads() {
        for (Thread thread : workers) {
            thread.interrupt();
        }
        workers.clear();
    }
}
