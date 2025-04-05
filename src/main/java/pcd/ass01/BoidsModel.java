package pcd.ass01;

import java.util.ArrayList;
import java.util.List;

public class BoidsModel {

    private static final double SEPARATION_WEIGHT = 1.0;
    private static final double ALIGNMENT_WEIGHT = 1.0;
    private static final double COHESION_WEIGHT = 1.0;
    private static final double WIDTH = 1000;
    private static final double HEIGHT = 1000;
    private static final double MAX_SPEED = 4.0;
    private static final double AVOID_RADIUS = 20.0;
    private static final double PERCEPTION_RADIUS = 50.0;

    private final MyMonitor<List<Boid>> boids;
    private final MyMonitor<Double> separationWeight;
    private final MyMonitor<Double> alignmentWeight;
    private final MyMonitor<Double> cohesionWeight;

    public BoidsModel(int numBoids) {
        this.boids = new MyMonitor<>(new ArrayList<>());
        this.separationWeight = new MyMonitor<>(SEPARATION_WEIGHT);
        this.alignmentWeight = new MyMonitor<>(ALIGNMENT_WEIGHT);
        this.cohesionWeight = new MyMonitor<>(COHESION_WEIGHT);
        this.createBoids(numBoids);
    }

    private void createBoids(int amount) {
        List<Boid> boids = new ArrayList<>();
        for (int i = 0; i < amount; i++) {
            P2d pos = new P2d(
                    -WIDTH / 2 + Math.random() * WIDTH,
                    -HEIGHT / 2 + Math.random() * HEIGHT
            );
            V2d vel = new V2d(
                    Math.random() * MAX_SPEED / 2 - MAX_SPEED / 4,
                    Math.random() * MAX_SPEED / 2 - MAX_SPEED / 4
            );
            boids.add(new Boid(pos, vel));
        }
        this.boids.set(boids);
    }

    public List<Boid> getBoids(){
    	return this.boids.get();
    }

    public double getMinX() {
    	return -WIDTH/2;
    }

    public double getMaxX() {
    	return WIDTH/2;
    }

    public double getMinY() {
    	return -HEIGHT/2;
    }

    public double getMaxY() {
    	return HEIGHT/2;
    }

    public double getWidth() {
    	return WIDTH;
    }

    public double getHeight() {
    	return HEIGHT;
    }

    public void setSeparationWeight(double value) {
    	this.separationWeight.set(value);
    }

    public void setAlignmentWeight(double value) {
    	this.alignmentWeight.set(value);
    }

    public void setCohesionWeight(double value) {
    	this.cohesionWeight.set(value);
    }

    public double getSeparationWeight() {
    	return this.separationWeight.get();
    }

    public double getCohesionWeight() {
    	return this.cohesionWeight.get();
    }

    public double getAlignmentWeight() {
    	return this.alignmentWeight.get();
    }

    public double getMaxSpeed() {
    	return MAX_SPEED;
    }

    public double getAvoidRadius() {
    	return AVOID_RADIUS;
    }

    public double getPerceptionRadius() {
    	return PERCEPTION_RADIUS;
    }

    public void setNumberBoids(int amount) {
        this.createBoids(amount);
    }
}
