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

    private final Barrier velComputed, boidsUpdated, velUpdated;
    private final BoidsController controller;
    private final BoidsModel model;
    private final List<Boid> boids;

    /**
     * Constructor for the BoidWorker.
     * @param controller the controller
     * @param model the model
     * @param velComputed the barrier for velocity computation
     * @param velUpdated the barrier for velocity update
     * @param boidsUpdated the barrier for boids update
     * @param boids the list of boids
     */
    public BoidWorker(
            BoidsController controller,
            BoidsModel model,
            Barrier velComputed,
            Barrier velUpdated,
            Barrier boidsUpdated,
            List<Boid> boids
    ) {
        this.controller = controller;
        this.model = model;
        this.velComputed = velComputed;
        this.velUpdated = velUpdated;
        this.boidsUpdated = boidsUpdated;
        this.boids = boids;
    }

    @Override
    public void run() {
        while (true) {
            this.controller.awaitRun();
            for (Boid boid : this.boids) {
                boid.readVelocity(model);
            }
            velComputed.await();
            for (Boid boid : this.boids) {
                boid.updateVelocity();
            }
            velUpdated.await();
            for (Boid boid : this.boids) {
                boid.updatePos(model);
            }
            boidsUpdated.await();
        }
    }
}
