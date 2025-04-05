package pcd.ass01.taskBased;

import pcd.ass01.Boid;
import pcd.ass01.BoidsController;
import pcd.ass01.BoidsModel;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Controller for the task-based version of the boids simulation.
 */
public class TaskController extends BoidsController {

    private static final int NUM_THREADS = Runtime.getRuntime().availableProcessors() + 1;
    private final ExecutorService executor;
    private final List<Future<Void>> futures;

    /**
     * Constructor for the TaskController.
     * @param model the model
     */
    public TaskController(BoidsModel model) {
        super(model);
        this.executor = Executors.newFixedThreadPool(NUM_THREADS);
        this.futures = new ArrayList<>();
    }

    @Override
    public void run() {
        int iteration = 0;
        super.model.setNumberBoids(super.getNumberOfBoids());
        while (iteration < 1000) {
            super.awaitRun();
            List<Boid> boids = this.model.getBoids();
            boids.forEach(boid -> this.futures.add(executor.submit(new UpdateVelocityTask(boid, this.model))));
            var t0 = System.currentTimeMillis();
            waitFutures(futures);
            futures.clear();

            boids.forEach(boid -> this.futures.add(executor.submit(new UpdatePositionTask(boid, this.model))));
            waitFutures(futures);
            futures.clear();
            updateView(t0);

            iteration++;
            //TODO: executor.shutdown();
        }
    }

    private void waitFutures(List<Future<Void>> futures){
        futures.forEach(future -> {
            try {
                future.get();
            } catch (InterruptedException | ExecutionException e) {
                throw new RuntimeException(e); //TODO:
            }
        });
    }

}
