package pcd.ass01.multithreaded;

import pcd.ass01.BoidsView;
import pcd.ass01.multithreaded.api.Barrier;

import java.util.LinkedList;
import java.util.Optional;

public class MultithreadedBoidsSimulator {

    private final MultithreadedBoidsModel model;
    private final LinkedList<Thread> threads;
    private Barrier barrier;
    private Optional<BoidsView> view;
    private static final int FRAMERATE = 25;
    private int framerate;


    public MultithreadedBoidsSimulator(MultithreadedBoidsModel model) {
        this.model = model;
        view = Optional.empty();
        this.threads = new LinkedList<>();
    }

    public void attachView(BoidsView view) {
    	this.view = Optional.of(view);
    }
      
    public void runSimulation() {

        var nBoids = this.model.getNBoids();
        this.barrier = new BarrierImpl(nBoids);
        for (int i = 0; i < nBoids; i++) {
            threads.add(new BoidThread(this.model, this.barrier, this.model.getBoids().get(i)));
        }
        for (Thread thread : threads) {
            thread.start();
        }

    	while (true) {
            var t0 = System.currentTimeMillis();

            /*
            if (!model.isPaused()) {
                for (Boid boid : boids) {
                    boid.updateVelocity(model);
                }

                for (Boid boid : boids) {
                    boid.updatePos(model);
                }
            }
            */

            if(model.getNBoids() == 0) {
                this.stopSimulation(); //TODO: e quando deve ripartire?
            }

    		if (view.isPresent()) {
            	view.get().update(framerate);
            	var t1 = System.currentTimeMillis();
                var dtElapsed = t1 - t0;
                var frameratePeriod = 1000/FRAMERATE;
                
                if (dtElapsed < frameratePeriod) {		
                	try {
                		Thread.sleep(frameratePeriod - dtElapsed);
                	} catch (Exception ignored) {}
                	framerate = FRAMERATE;
                } else {
                	framerate = (int) (1000/dtElapsed);
                }
    		}
            
    	}
    }

    public synchronized void stopSimulation() {
        for (Thread thread : threads) {
            thread.interrupt();
        }
        threads.clear();
    }
}
