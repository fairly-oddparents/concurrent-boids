package pcd.ass01.taskBased;

import pcd.ass01.Boid;
import pcd.ass01.BoidsModel;

import java.util.concurrent.Callable;

public class UpdateVelocityTask implements Callable<Void> {

    private final Boid boid;
    private final BoidsModel model;

    public UpdateVelocityTask(Boid boid, BoidsModel model) {
        this.boid = boid;
        this.model = model;
    }

    @Override
    public Void call(){
        boid.readVelocity(this.model);
        boid.updateVelocity();
        return null;
    }
}
