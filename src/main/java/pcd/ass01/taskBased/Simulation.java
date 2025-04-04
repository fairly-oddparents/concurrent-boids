package pcd.ass01.taskBased;

import pcd.ass01.BoidsController;
import pcd.ass01.BoidsModel;
import pcd.ass01.BoidsView;

public class Simulation {

    private static final int DEFAULT_BOIDS = 0;
    public static final int SCREEN_WIDTH = 800;
    public static final int SCREEN_HEIGHT = 800;

    public static void main(String[] args) {
        BoidsModel model = new BoidsModel(DEFAULT_BOIDS);
        BoidsController controller = new TaskController(model);
        BoidsView view = new BoidsView(controller, SCREEN_WIDTH, SCREEN_HEIGHT, model.getWidth(), model.getHeight());
        controller.attachView(view);
        controller.run();
    }
}
