package pcd.ass01.sequential;

import pcd.ass01.BoidsView;

import static pcd.ass01.Constants.*;

public class SequentialSimulation {

    public static void main(String[] args) {      
    	var model = new SequentialBoidsModel(
				DEFAULT_BOIDS,
				SEPARATION_WEIGHT, ALIGNMENT_WEIGHT, COHESION_WEIGHT,
				ENVIRONMENT_WIDTH, ENVIRONMENT_HEIGHT,
				MAX_SPEED,
				PERCEPTION_RADIUS,
				AVOID_RADIUS);
    	var sim = new BoidsSimulator(model);
    	var view = new BoidsView(model, SCREEN_WIDTH, SCREEN_HEIGHT);
		model.setNumberBoids(view.inputDialog());
    	sim.attachView(view);
    	sim.runSimulation();
    }

}
