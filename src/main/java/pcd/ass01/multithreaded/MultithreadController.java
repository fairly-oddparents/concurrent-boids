package pcd.ass01.multithreaded;

import pcd.ass01.Boid;
import pcd.ass01.BoidsModel;
import pcd.ass01.BoidsController;
import pcd.ass01.multithreaded.api.Barrier;

import java.util.LinkedList;
import java.util.List;

public class MultithreadController extends BoidsController {
    private static final int NUM_THREADS = Runtime.getRuntime().availableProcessors() + 1;
    private final LinkedList<Thread> workers = new LinkedList<>();

    public MultithreadController(BoidsModel model) {
        super(model);
    }

    @Override
    public void run() {
        super.model.setNumberBoids(super.getNumberOfBoids());
        while (true) {
            super.awaitRun();
            System.out.println("Starting simulation");  //TODO: remove logs
            Barrier velBarrier = new MyBarrier(NUM_THREADS);
            Barrier posBarrier = new MyBarrier(NUM_THREADS + 1);
            List<Boid> boids = this.model.getBoids();
            int boidsPerWorker = boids.size() / NUM_THREADS;
            for (int i = 0; i < NUM_THREADS; i++) {
                int start = i * boidsPerWorker;
                int end = (i == NUM_THREADS - 1) ? boids.size() : start + boidsPerWorker;
                List<Boid> subList = boids.subList(start, end);
                this.workers.add(new BoidWorker(this, this.model, velBarrier, posBarrier, subList));
            }
            workers.forEach(Thread::start);
            while (!super.isStopped()) {
                var t0 = System.currentTimeMillis();
                updateView(t0);
                posBarrier.await();
            }
            this.removeThreads();
        }
    }

    public synchronized void removeThreads() {
        for (Thread thread : workers) {
            thread.interrupt();
        }
        System.out.println("All threads killed");   //TODO: remove logs
        workers.clear();
    }
}
