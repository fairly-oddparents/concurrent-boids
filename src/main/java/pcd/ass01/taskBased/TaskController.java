package pcd.ass01.taskBased;

import pcd.ass01.Boid;
import pcd.ass01.BoidsController;
import pcd.ass01.BoidsModel;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class TaskController extends BoidsController {

    private static final int NUM_THREADS = Runtime.getRuntime().availableProcessors() + 1;
    private final ExecutorService executor;
    private List<Future> futures;

    public TaskController(BoidsModel model) {
        super(model);
        this.executor = Executors.newFixedThreadPool(NUM_THREADS);
        this.futures = new ArrayList<>();
    }

    @Override
    public void run() {
        super.model.setNumberBoids(super.getNumberOfBoids());
        while (true) {
            super.awaitRun();
            System.out.println("Simulation...");  //TODO: remove logs
            List<Boid> boids = this.model.getBoids();
            for (Boid boid : boids) {
                this.futures.add(executor.submit(new UpdateVelocityTask(boid, this.model)));
            }
            for (Future future : futures) {
                try {
                    future.get();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            futures.clear();
            for (Boid boid : boids) {
                this.futures.add(executor.submit(new UpdatePositionTask(boid, this.model)));
            }
            for (Future future : futures) {
                try {
                    future.get();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            var t0 = System.currentTimeMillis();
            updateView(t0);

            //TODO: executor.shutdown();
        }
    }
}
