package pcd.ass01.taskBased;


import pcd.ass01.BoidsController;
import pcd.ass01.BoidsModel;
import pcd.ass01.BoidsView;

public class Simulation {
    private static final int DEFAULT_BOIDS = 0;
    public static final int SCREEN_WIDTH = 800;
    public static final int SCREEN_HEIGHT = 800;

    public static void main(String[] args) {
        BoidsController controller = new TaskController(new BoidsModel(DEFAULT_BOIDS));
        BoidsView view = new BoidsView(controller, SCREEN_WIDTH, SCREEN_HEIGHT);
        controller.attachView(view);
        controller.run();
    }
}
