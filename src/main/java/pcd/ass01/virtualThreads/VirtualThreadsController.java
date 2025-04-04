package pcd.ass01.virtualThreads;

import pcd.ass01.Boid;
import pcd.ass01.BoidsController;
import pcd.ass01.BoidsModel;
import pcd.ass01.multithreaded.MyBarrier;
import pcd.ass01.multithreaded.api.Barrier;

import java.util.List;

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
            Barrier velBarrier = new MyBarrier(numBoids);
            Barrier posBarrier = new MyBarrier(numBoids + 1);
            Barrier viewBarrier = new MyBarrier(numBoids + 1);
            for (Boid boid : boids) {
                Thread.ofVirtual().start(() -> {
                    while (true) {
                        awaitRun();
                        boid.readVelocity(super.model);
                        boid.updateVelocity();
                        velBarrier.await();
                        boid.updatePos(super.model);
                        posBarrier.await();
                        viewBarrier.await();
                    }
                });
            }
            while (!super.isStopped()) {
                var t0 = System.currentTimeMillis();
                posBarrier.await();
                updateView(t0);
                viewBarrier.await();
            }
        }
    }

}
