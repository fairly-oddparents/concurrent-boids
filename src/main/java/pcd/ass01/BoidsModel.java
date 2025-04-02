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

    private final List<Boid> boids;
    private double separationWeight; 
    private double alignmentWeight; 
    private double cohesionWeight; 

    public BoidsModel(int numBoids) {
        this.separationWeight = SEPARATION_WEIGHT;
        this.alignmentWeight = ALIGNMENT_WEIGHT;
        this.cohesionWeight = COHESION_WEIGHT;
        this.boids = new ArrayList<>();
        this.createBoids(numBoids);
    }

    private void createBoids(int amount) {
        for (int i = 0; i < amount; i++) {
            P2d pos = new P2d(-width/2 + Math.random() * width, -height/2 + Math.random() * height);
            V2d vel = new V2d(Math.random() * maxSpeed/2 - maxSpeed/4, Math.random() * maxSpeed/2 - maxSpeed/4);
            boids.add(new Boid(pos, vel));
        }
    }

    public synchronized List<Boid> getBoids(){
    	return boids;
    }

    public synchronized double getMinX() {
    	return -WIDTH/2;
    }

    public synchronized double getMaxX() {
    	return WIDTH/2;
    }

    public synchronized double getMinY() {
    	return -HEIGHT/2;
    }

    public synchronized double getMaxY() {
    	return HEIGHT/2;
    }

    public synchronized double getWidth() {
    	return WIDTH;
    }

    public synchronized double getHeight() {
    	return HEIGHT;
    }

    public synchronized void setSeparationWeight(double value) {
    	this.separationWeight = value;
    }

    public synchronized void setAlignmentWeight(double value) {
    	this.alignmentWeight = value;
    }

    public synchronized void setCohesionWeight(double value) {
    	this.cohesionWeight = value;
    }

    public synchronized double getSeparationWeight() {
    	return separationWeight;
    }

    public synchronized double getCohesionWeight() {
    	return cohesionWeight;
    }

    public synchronized double getAlignmentWeight() {
    	return alignmentWeight;
    }

    public synchronized double getMaxSpeed() {
    	return MAX_SPEED;
    }

    public synchronized double getAvoidRadius() {
    	return AVOID_RADIUS;
    }

    public synchronized double getPerceptionRadius() {
    	return PERCEPTION_RADIUS;
    }

    public synchronized void setNumberBoids(int amount) {
        this.boids.clear();
        this.createBoids(amount);
    }
}
