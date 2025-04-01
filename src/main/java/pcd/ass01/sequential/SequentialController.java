package pcd.ass01.sequential;

import pcd.ass01.Boid;
import pcd.ass01.BoidsModel;
import pcd.ass01.BoidsController;

public class SequentialController extends BoidsController {

    public SequentialController(BoidsModel model) {
        super(model);
    }

    @Override
    public void run() {
        super.model.setNumberBoids(super.getNumberOfBoids());
    	while (true) {
            var t0 = System.currentTimeMillis();
    		var boids = model.getBoids();
            if (isRunning()) {
                for (Boid boid : boids) {
                    boid.updateVelocity(model);
                }
                for (Boid boid : boids) {
                    boid.updatePos(model);
                }
            }
            updateView(t0);
    	}
    }
}
