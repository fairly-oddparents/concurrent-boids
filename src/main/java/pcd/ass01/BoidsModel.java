package pcd.ass01;

import java.util.ArrayList;
import java.util.List;

/**
 * Class representing the model of the boids simulation.
 */
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

    /**
     * Constructor for the BoidsModel class.
     * @param numBoids the number of boids to create
     */
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

    /**
     * Gets the list of boids.
     * @return the list of boids
     */
    public List<Boid> getBoids(){
    	return this.boids.get();
    }

    /**
     * Gets the minimum x coordinate of the simulation area.
     * @return the minimum x coordinate
     */
    public double getMinX() {
    	return -WIDTH/2;
    }

    /**
     * Gets the maximum x coordinate of the simulation area.
     * @return the maximum x coordinate
     */
    public double getMaxX() {
    	return WIDTH/2;
    }

    /**
     * Gets the minimum y coordinate of the simulation area.
     * @return the minimum y coordinate
     */
    public double getMinY() {
    	return -HEIGHT/2;
    }

    /**
     * Gets the maximum y coordinate of the simulation area.
     * @return the maximum y coordinate
     */
    public double getMaxY() {
    	return HEIGHT/2;
    }

    /**
     * Gets the width of the simulation area.
     * @return the width
     */
    public double getWidth() {
    	return WIDTH;
    }

    /**
     * Gets the height of the simulation area.
     * @return the height
     */
    public double getHeight() {
    	return HEIGHT;
    }

    /**
     * Sets the separation weight.
     * @param value the separation weight
     */
    public void setSeparationWeight(double value) {
    	this.separationWeight.set(value);
    }

    /**
     * Sets the alignment weight.
     * @param value the alignment weight
     */
    public void setAlignmentWeight(double value) {
    	this.alignmentWeight.set(value);
    }

    /**
     * Sets the cohesion weight.
     * @param value the cohesion weight
     */
    public void setCohesionWeight(double value) {
    	this.cohesionWeight.set(value);
    }

    /**
     * Gets the separation weight.
     * @return the separation weight
     */
    public double getSeparationWeight() {
    	return this.separationWeight.get();
    }

    /**
     * Gets the cohesion weight.
     * @return the cohesion weight
     */
    public double getCohesionWeight() {
    	return this.cohesionWeight.get();
    }

    /**
     * Gets the alignment weight.
     * @return the alignment weight
     */
    public double getAlignmentWeight() {
    	return this.alignmentWeight.get();
    }

    /**
     * Gets the maximum speed of the boids.
     * @return the maximum speed
     */
    public double getMaxSpeed() {
    	return MAX_SPEED;
    }

    /**
     * Gets the perception radius of the boids.
     * @return the perception radius
     */
    public double getAvoidRadius() {
    	return AVOID_RADIUS;
    }

    /**
     * Gets the perception radius of the boids.
     * @return the perception radius
     */
    public double getPerceptionRadius() {
    	return PERCEPTION_RADIUS;
    }

    /**
     * Sets the number of boids.
     * @param amount the number of boids
     */
    public void setNumberBoids(int amount) {
        this.createBoids(amount);
    }

}
