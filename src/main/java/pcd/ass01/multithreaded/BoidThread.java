package pcd.ass01.multithreaded;

import pcd.ass01.Boid;
import pcd.ass01.BoidsModel;
import pcd.ass01.BoidsSimulator;
import pcd.ass01.multithreaded.api.Barrier;

public class BoidThread extends Thread{

    private final Barrier barrier;
    private final BoidsSimulator sim;
    private final BoidsModel model;
    private final Boid boid;


    public BoidThread(BoidsSimulator sim, BoidsModel model, Barrier barrier, Boid boid){
        this.sim = sim;
        this.barrier = barrier;
        this.model = model;
        this.boid = boid;
    }

    @Override
    public void run() {
        while(true){
            if (this.sim.isRunning()){ //TODO: da controllare (la pause dovrebbe essere una wait?)
                this.boid.updateVelocity(model);
                try {
                    barrier.await();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                boid.updatePos(model);
                try {
                    barrier.await();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                try {
                    Thread.sleep(16); //TODO: da controllare
                } catch (InterruptedException e) {
                    break;
                }
            }
        }
    }
}
