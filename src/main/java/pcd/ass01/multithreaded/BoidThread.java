package pcd.ass01.multithreaded;

import pcd.ass01.Boid;
import pcd.ass01.BoidsModel;
import pcd.ass01.BoidsSimulator;
import pcd.ass01.multithreaded.api.Barrier;

public class BoidThread extends Thread{

    private final Barrier velBarrier, posBarrier;
    private final BoidsSimulator sim;
    private final BoidsModel model;
    private final Boid boid;


    public BoidThread(BoidsSimulator sim, BoidsModel model, Barrier velBarrier, Barrier posBarrier, Boid boid){
        this.sim = sim;
        this.model = model;
        this.velBarrier = velBarrier;
        this.posBarrier = posBarrier;
        this.boid = boid;
    }

    @Override
    public void run() {
        while(true){
            if (this.sim.isRunning()){ //TODO: da controllare (la pause dovrebbe essere una wait?)
                this.boid.updateVelocity(model);
                try {
                    velBarrier.await();
                } catch (InterruptedException ignored) {}
                boid.updatePos(model);
                try {
                    posBarrier.await();
                } catch (InterruptedException ignored) {}
            }
        }
    }
}
