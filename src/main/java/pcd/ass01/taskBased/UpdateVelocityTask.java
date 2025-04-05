package pcd.ass01.taskBased;

import pcd.ass01.Boid;
import pcd.ass01.BoidsModel;

import java.util.concurrent.Callable;

/**
 * Task for updating the velocity of a boid.
 */
public class UpdateVelocityTask implements Callable<Void> {

    private final Boid boid;
    private final BoidsModel model;

    /**
     * Constructor for the UpdateVelocityTask.
     * @param boid the boid to update
     * @param model the model
     */
    public UpdateVelocityTask(Boid boid, BoidsModel model) {
        this.boid = boid;
        this.model = model;
    }

    @Override
    public Void call(){
        boid.readVelocity(this.model);
        boid.updateVelocity();
        return null;
    }
}
