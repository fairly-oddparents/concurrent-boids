package pcd.ass01.taskBased;

import pcd.ass01.Boid;
import pcd.ass01.BoidsModel;

import java.util.concurrent.Callable;

/**
 * Task for the read-only operations of the boids.
 */
public class ReadTask implements Callable<Void> {

    private final Boid boid;
    private final BoidsModel model;

    /**
     * Constructor for the ReadTask.
     * @param boid the boid to update
     * @param model the model
     */
    public ReadTask(Boid boid, BoidsModel model) {
        this.boid = boid;
        this.model = model;
    }

    @Override
    public Void call(){
        boid.calculateVelocity(this.model);
        boid.calculatePosition(this.model);
        return null;
    }
}
