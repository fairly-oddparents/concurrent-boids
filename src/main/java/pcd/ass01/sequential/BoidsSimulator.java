package pcd.ass01.sequential;

import pcd.ass01.Boid;
import pcd.ass01.BoidsView;

import java.util.Optional;

public class BoidsSimulator {

    private final SequentialBoidsModel model;
    private Optional<BoidsView> view;
    
    private static final int FRAMERATE = 25;
    private int framerate;
    
    public BoidsSimulator(SequentialBoidsModel model) {
        this.model = model;
        view = Optional.empty();
    }

    public void attachView(BoidsView view) {
    	this.view = Optional.of(view);
    }
      
    public void runSimulation() {
    	while (true) {
            var t0 = System.currentTimeMillis();
    		var boids = model.getBoids();

            if (!model.isPaused()) {
                for (Boid boid : boids) {
                    boid.updateVelocity(model);
                }

                for (Boid boid : boids) {
                    boid.updatePos(model);
                }
            }

    		if (view.isPresent()) {
            	view.get().update(framerate);
            	var t1 = System.currentTimeMillis();
                var dtElapsed = t1 - t0;
                var framratePeriod = 1000/FRAMERATE;
                
                if (dtElapsed < framratePeriod) {		
                	try {
                		Thread.sleep(framratePeriod - dtElapsed);
                	} catch (Exception ignored) {}
                	framerate = FRAMERATE;
                } else {
                	framerate = (int) (1000/dtElapsed);
                }
    		}
            
    	}
    }
}
