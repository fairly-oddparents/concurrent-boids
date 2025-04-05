package pcd.ass01.taskBased;

import pcd.ass01.BoidsController;
import pcd.ass01.BoidsModel;
import pcd.ass01.BoidsView;
import pcd.ass01.api.View;

public class Simulation {

    private static final int DEFAULT_BOIDS = 0;

    public static void main(String[] args) {
        BoidsModel model = new BoidsModel(DEFAULT_BOIDS);
        BoidsController controller = new TaskController(model);
        View view = new BoidsView(controller, model.getWidth(), model.getHeight());
        controller.attachView(view);
        controller.run();
    }
}
