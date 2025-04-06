package pcd.ass01.virtualThreads;

import pcd.ass01.BoidsController;
import pcd.ass01.BoidsModel;
import pcd.ass01.BoidsView;
import pcd.ass01.api.View;

/**
 * Entry-point for the virtual threads version of the boids simulation.
 */
public class VirtualThreadsSimulation {

    public static void main(String[] args) {
        BoidsModel model = new BoidsModel();
        BoidsController controller = new VirtualThreadsController(model);
        View view = new BoidsView(controller, model.getWidth(), model.getHeight());
        controller.attachView(view);
        controller.run();
    }

}
