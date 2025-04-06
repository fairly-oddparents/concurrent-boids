package pcd.ass01.taskBased;

import pcd.ass01.Boid;

import java.util.concurrent.Callable;

/**
 * Task for updating the position and velocity (write operations) of a boid.
 */
public class WriteTask implements Callable<Void> {

    private final Boid boid;

    /**
     * Constructor for the WriteTask.
     * @param boid the boid to update
     */
    public WriteTask(Boid boid) {
        this.boid = boid;
    }

    @Override
    public Void call(){
        boid.updateVelocity();
        boid.updatePosition();
        return null;
    }
}
