package pcd.ass01.multithreaded;

import pcd.ass01.Boid;
import pcd.ass01.BoidsModel;
import pcd.ass01.BoidsSimulator;
import pcd.ass01.multithreaded.api.Barrier;

import java.util.List;

public class BoidThread extends Thread{

    private final Barrier velBarrier, posBarrier;
    private final BoidsSimulator sim;
    private final BoidsModel model;
    private final List<Boid> boids;


    public BoidThread(BoidsSimulator sim, BoidsModel model, Barrier velBarrier, Barrier posBarrier, List<Boid> boids) {
        this.sim = sim;
        this.model = model;
        this.velBarrier = velBarrier;
        this.posBarrier = posBarrier;
        this.boids = boids;
    }

    @Override
    public void run() {
        while (!Thread.interrupted()) {
            this.sim.waitForSimulation();
            for (Boid boid : this.boids) {
                boid.updateVelocity(model);
            }
            velBarrier.await();
            for (Boid boid : this.boids) {
                boid.updatePos(model);
            }
            posBarrier.await();
        }
    }
}
