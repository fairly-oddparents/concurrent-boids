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
    private ExecutorService executor;
    private final List<Future<Void>> futures;

    /**
     * Constructor for the TaskController.
     * @param model the model
     */
    public TaskController(BoidsModel model) {
        super(model);
        this.futures = new ArrayList<>();
    }

    @Override
    public void run() {
        super.model.setNumberBoids(super.getNumberOfBoids());
        while (true) {
            super.awaitRun();
            this.executor = Executors.newFixedThreadPool(NUM_THREADS);
            List<Boid> boids = this.model.getBoids();
            List<UpdateVelocityTask> velocityTasks = new ArrayList<>();
            List<UpdatePositionTask> positionTasks = new ArrayList<>();
            boids.forEach(boid -> velocityTasks.add(new UpdateVelocityTask(boid, this.model)));
            boids.forEach(boid -> positionTasks.add(new UpdatePositionTask(boid, this.model)));

            while (!super.isStopped()) {
                super.awaitRun();
                velocityTasks.forEach(task -> this.futures.add(executor.submit(task)));
                var t0 = System.currentTimeMillis();
                waitFutures(futures);
                futures.clear();
                positionTasks.forEach(task -> this.futures.add(executor.submit(task)));
                waitFutures(futures);
                futures.clear();
                updateView(t0);
            }
            executor.shutdown();
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
