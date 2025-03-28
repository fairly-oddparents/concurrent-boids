package pcd.ass01.api;

import pcd.ass01.Boid;

import java.util.List;

public interface BoidsModel {
    List<Boid> getBoids();
    double getMinX();
    double getMaxX();
    double getMinY();
    double getMaxY();
    double getWidth();
    double getHeight();
    void setSeparationWeight(double value);
    void setAlignmentWeight(double value);
    void setCohesionWeight(double value);
    double getSeparationWeight();
    double getCohesionWeight();
    double getAlignmentWeight();
    double getMaxSpeed();
    double getAvoidRadius();
    double getPerceptionRadius();
    void clearBoids();
    void setNumberBoids(int amount);
    boolean isPaused();
    void setPaused(boolean isPaused);
}
