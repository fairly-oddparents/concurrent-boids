package pcd.ass01.taskBased;

import pcd.ass01.BoidsController;
import pcd.ass01.BoidsModel;
import pcd.ass01.BoidsView;
import pcd.ass01.api.View;

/**
 * Entry-point for the task-based version of the boids simulation.
 */
public class TaskBasedSimulation {

    public static void main(String[] args) {
        BoidsModel model = new BoidsModel();
        BoidsController controller = new TaskBasedController(model);
        View view = new BoidsView(controller, model.getWidth(), model.getHeight());
        controller.attachView(view);
        controller.run();
    }
}
