package pcd.ass01.multithreaded;

import pcd.ass01.Boid;
import pcd.ass01.BoidsModel;
import pcd.ass01.BoidsController;
import pcd.ass01.api.Barrier;

import java.util.List;

/**
 * Worker for updating the boids positions and velocities.
 */
public class BoidWorker extends Thread {

    private final Barrier readToWrite, boidsUpdated;
    private final BoidsController controller;
    private final BoidsModel model;
    private final List<Boid> boids;

    /**
     * Constructor for the BoidWorker.
     * @param controller the controller
     * @param model the model
     * @param readToWrite the barrier for waiting all boids have finished reading before writing
     * @param boidsUpdated the barrier for waiting all boids have finished updating
     * @param boids the list of boids
     */
    public BoidWorker(
            BoidsController controller,
            BoidsModel model,
            Barrier readToWrite,
            Barrier boidsUpdated,
            List<Boid> boids
    ) {
        this.controller = controller;
        this.model = model;
        this.readToWrite = readToWrite;
        this.boidsUpdated = boidsUpdated;
        this.boids = boids;
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            this.controller.awaitRun();
            for (Boid boid : this.boids) {
                boid.calculateVelocity(model);
                boid.calculatePosition(model);
            }
            readToWrite.await();
            for (Boid boid : this.boids) {
                boid.updateVelocity();
                boid.updatePosition();
            }
            boidsUpdated.await();
        }
    }
}
