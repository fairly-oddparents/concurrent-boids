package pcd.ass01.multithreaded;

import pcd.ass01.Boid;
import pcd.ass01.multithreaded.api.Barrier;

public class BoidThread extends Thread{

    private final Barrier barrier;
    private final BoidsModel model;
    private final Boid boid;


    public BoidThread(BoidsModel model, Barrier barrier, Boid boid){
        this.barrier = barrier;
        this.model = model;
        this.boid = boid;
    }

    @Override
    public void run() {
        if (!this.model.isPaused()){ //TODO: da controllare (la pause dovrebbe essere una wait?)
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
        }
    }
}
