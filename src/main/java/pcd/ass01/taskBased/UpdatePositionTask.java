package pcd.ass01.taskBased;

import pcd.ass01.Boid;
import pcd.ass01.BoidsModel;

import java.util.concurrent.Callable;

/**
 * Task for updating the position of a boid.
 */
public class UpdatePositionTask implements Callable<Void> {

    private final Boid boid;
    private final BoidsModel model;

    /**
     * Constructor for the UpdatePositionTask.
     * @param boid the boid to update
     * @param model the model
     */
    public UpdatePositionTask(Boid boid, BoidsModel model) {
        this.boid = boid;
        this.model = model;
    }

    @Override
    public Void call(){
        boid.calculatePosition(this.model);
        boid.updatePosition();
        return null;
    }
}
