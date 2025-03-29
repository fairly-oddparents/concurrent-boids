package pcd.ass01.multithreaded;

import pcd.ass01.Boid;
import pcd.ass01.P2d;
import pcd.ass01.V2d;

import java.util.ArrayList;
import java.util.List;

public class MultithreadedBoidsModel implements pcd.ass01.api.BoidsModel {

    private final List<Boid> boids;
    private double separationWeight;
    private double alignmentWeight;
    private double cohesionWeight;
    private final double width;
    private final double height;
    private final double maxSpeed;
    private final double perceptionRadius;
    private final double avoidRadius;
    private boolean isPaused;
    private int nBoids;

    public MultithreadedBoidsModel(int nboids,
                                   double initialSeparationWeight,
                                   double initialAlignmentWeight,
                                   double initialCohesionWeight,
                                   double width,
                                   double height,
                                   double maxSpeed,
                                   double perceptionRadius,
                                   double avoidRadius) {
        this.separationWeight = initialSeparationWeight;
        this.alignmentWeight = initialAlignmentWeight;
        this.cohesionWeight = initialCohesionWeight;
        this.width = width;
        this.height = height;
        this.maxSpeed = maxSpeed;
        this.perceptionRadius = perceptionRadius;
        this.avoidRadius = avoidRadius;
        this.boids = new ArrayList<>();
        this.nBoids = nboids;
        this.createBoids(nboids);
    }

    private synchronized void createBoids(int amount) {
        for (int i = 0; i < amount; i++) {
            P2d pos = new P2d(-width/2 + Math.random() * width, -height/2 + Math.random() * height);
            V2d vel = new V2d(Math.random() * maxSpeed/2 - maxSpeed/4, Math.random() * maxSpeed/2 - maxSpeed/4);
            boids.add(new Boid(pos, vel));
        }
    }

    @Override
    public synchronized List<Boid> getBoids(){
        return boids;
    }

    @Override
    public synchronized double getMinX() {
        return -width/2;
    }

    @Override
    public synchronized double getMaxX() {
        return width/2;
    }

    @Override
    public synchronized double getMinY() {
        return -height/2;
    }

    @Override
    public synchronized double getMaxY() {
        return height/2;
    }

    @Override
    public synchronized double getWidth() {
        return width;
    }

    @Override
    public synchronized double getHeight() {
        return height;
    }

    @Override
    public synchronized void setSeparationWeight(double value) {
        this.separationWeight = value;
    }

    @Override
    public synchronized void setAlignmentWeight(double value) {
        this.alignmentWeight = value;
    }

    @Override
    public synchronized void setCohesionWeight(double value) {
        this.cohesionWeight = value;
    }

    @Override
    public synchronized double getSeparationWeight() {
        return separationWeight;
    }

    @Override
    public synchronized double getCohesionWeight() {
        return cohesionWeight;
    }

    @Override
    public synchronized double getAlignmentWeight() {
        return alignmentWeight;
    }

    @Override
    public synchronized double getMaxSpeed() {
        return maxSpeed;
    }

    @Override
    public synchronized double getAvoidRadius() {
        return avoidRadius;
    }

    @Override
    public synchronized double getPerceptionRadius() {
        return perceptionRadius;
    }

    @Override
    public synchronized void clearBoids() {
        this.boids.clear();
        this.nBoids = 0;
    }

    @Override
    public synchronized void setNumberBoids(int amount) {
        this.clearBoids();
        this.nBoids = amount;
        this.createBoids(amount);
    }

    public synchronized int getNBoids(){
        return this.nBoids;
    }

    @Override
    public synchronized boolean isPaused() {
        return this.isPaused;
    }

    @Override
    public synchronized void setPaused(boolean isPaused) {
        this.isPaused = isPaused;
    }
}
