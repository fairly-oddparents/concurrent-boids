package pcd.ass01.sequential;

import pcd.ass01.BoidsController;
import pcd.ass01.BoidsModel;
import pcd.ass01.BoidsView;

import static pcd.ass01.Constants.*;

public class Simulation {

    public static void main(String[] args) {
    	BoidsController controller = new SequentialController(new BoidsModel(
				DEFAULT_BOIDS,
				SEPARATION_WEIGHT, ALIGNMENT_WEIGHT, COHESION_WEIGHT,
				ENVIRONMENT_WIDTH, ENVIRONMENT_HEIGHT,
				MAX_SPEED,
				PERCEPTION_RADIUS,
				AVOID_RADIUS));
    	var view = new BoidsView(controller, SCREEN_WIDTH, SCREEN_HEIGHT);
		controller.attachView(view);
		controller.run();
    }

}
