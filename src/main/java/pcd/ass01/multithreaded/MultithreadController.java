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
public class MultithreadController extends BoidsController {
    private static final int NUM_THREADS = Runtime.getRuntime().availableProcessors() + 1;
    private final LinkedList<Thread> workers = new LinkedList<>();

    /**
     * Constructor for the MultithreadController.
     * @param model the model
     */
    public MultithreadController(BoidsModel model) {
        super(model);
    }

    @Override
    public void run() {
        int iteration = 0;
        super.model.setNumberBoids(super.getNumberOfBoids());
        while (true) {
            super.awaitRun();
            Barrier velComputed = new MyBarrier(NUM_THREADS);
            Barrier velUpdated = new MyBarrier(NUM_THREADS);
            Barrier boidsUpdated = new MyBarrier(NUM_THREADS + 1);
            List<Boid> boids = this.model.getBoids();
            int boidsPerWorker = boids.size() / NUM_THREADS;
            for (int i = 0; i < NUM_THREADS; i++) {
                int start = i * boidsPerWorker;
                int end = (i == NUM_THREADS - 1) ? boids.size() : start + boidsPerWorker;
                List<Boid> subList = boids.subList(start, end);
                this.workers.add(new BoidWorker(this, this.model, velComputed, velUpdated, boidsUpdated, subList));
            }
            workers.forEach(Thread::start);
            //while (!super.isStopped()) {
            while (iteration < 1000) {
                var t0 = System.currentTimeMillis();
                boidsUpdated.await();
                updateView(t0);
                iteration++;
            }
            this.removeThreads();
            break;
        }
    }

    private synchronized void removeThreads() {
        for (Thread thread : workers) {
            thread.interrupt();
        }
        workers.clear();
    }
}
