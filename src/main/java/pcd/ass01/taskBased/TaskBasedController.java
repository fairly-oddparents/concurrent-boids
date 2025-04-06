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
public class TaskBasedController extends BoidsController {

    private static final int NUM_THREADS = Runtime.getRuntime().availableProcessors() + 1;
    private ExecutorService executor;
    private final List<Future<Void>> futures;

    /**
     * Constructor for the TaskBasedController.
     * @param model the model
     */
    public TaskBasedController(BoidsModel model) {
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
            List<ReadTask> readTasks = new ArrayList<>();
            List<WriteTask> writeTasks = new ArrayList<>();
            boids.forEach(boid -> readTasks.add(new ReadTask(boid, this.model)));
            boids.forEach(boid -> writeTasks.add(new WriteTask(boid)));

            while (!super.isStopped()) {
                super.awaitRun();
                var t0 = System.currentTimeMillis();
                readTasks.forEach(task -> this.futures.add(executor.submit(task)));
                waitFutures(futures);
                futures.clear();
                writeTasks.forEach(task -> this.futures.add(executor.submit(task)));
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
