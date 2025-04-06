package pcd.ass01.multithreaded;

import pcd.ass01.BoidsController;
import pcd.ass01.BoidsModel;
import pcd.ass01.BoidsView;
import pcd.ass01.api.View;

/**
 * Entry-point for the multithreaded version of the boids simulation.
 */
public class MultithreadedSimulation {

	public static void main(String[] args) {
		BoidsModel model = new BoidsModel();
		BoidsController controller = new MultithreadedController(model);
		View view = new BoidsView(controller, model.getWidth(), model.getHeight());
		controller.attachView(view);
		controller.run();
	}
}
