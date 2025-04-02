package pcd.ass01.taskBased;

import pcd.ass01.BoidsController;
import pcd.ass01.BoidsModel;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TaskController extends BoidsController {

    private static final int NUM_THREADS = Runtime.getRuntime().availableProcessors() + 1;
    private ExecutorService executor;

    public TaskController(BoidsModel model) {
        super(model);
        this.executor = Executors.newFixedThreadPool(NUM_THREADS);
    }

    @Override
    public void run() {

    }
}
