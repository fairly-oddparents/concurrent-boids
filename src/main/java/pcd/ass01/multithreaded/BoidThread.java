package pcd.ass01.multithreaded;

import pcd.ass01.Boid;
import pcd.ass01.multithreaded.api.Barrier;

public class BoidThread extends Thread{

    private final Barrier barrier;
    private final MultithreadedBoidsModel model;
    private final Boid boid;


    public BoidThread(MultithreadedBoidsModel model, Barrier barrier, Boid boid){
        this.barrier = barrier;
        this.model = model;
        this.boid = boid;
    }

    @Override
    public void run() {
        while(true){
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

                try {
                    Thread.sleep(16); //TODO: da controllare
                } catch (InterruptedException e) {
                    break;
                }
            }
        }

    }
}
