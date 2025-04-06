package pcd.ass01.virtualThreads;

import pcd.ass01.Boid;
import pcd.ass01.BoidsController;
import pcd.ass01.BoidsModel;
import pcd.ass01.MyBarrier;
import pcd.ass01.api.Barrier;

import java.util.List;

/**
 * Controller for the virtual threads boids simulation.
 */
public class VirtualThreadsController extends BoidsController {

    /**
     * Constructor for the BoidsController.
     * @param model the model
     */
    public VirtualThreadsController(BoidsModel model) {
        super(model);
    }

    @Override
    public void run() {
        super.model.setNumberBoids(super.getNumberOfBoids());
        while (true) {
            super.awaitRun();
            List<Boid> boids = super.model.getBoids();
            int numBoids = boids.size();
            Barrier velComputed = new MyBarrier(numBoids);
            Barrier velUpdated = new MyBarrier(numBoids);
            Barrier boidsUpdated = new MyBarrier(numBoids + 1);
            for (Boid boid : boids) {
                Thread.ofVirtual().start(() -> {
                    while (true) {
                        awaitRun();
                        boid.readVelocity(model);
                        velComputed.await();
                        boid.updateVelocity();
                        velUpdated.await();
                        boid.updatePos(model);
                        boidsUpdated.await();
                    }
                });
            }
            while (!super.isStopped()) {
                var t0 = System.currentTimeMillis();
                boidsUpdated.await();
                updateView(t0);
            }
        }
    }

}
