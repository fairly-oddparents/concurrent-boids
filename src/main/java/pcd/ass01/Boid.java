package pcd.ass01;

import java.util.ArrayList;
import java.util.List;

/**
 * Class representing a boid in the simulation.
 */
public class Boid {

    private P2d pos, updatedPos;
    private V2d vel, updatedVel;

    /**
     * Constructor for the Boid class.
     * @param pos the position of the boid
     * @param vel the velocity of the boid
     */
    public Boid(P2d pos, V2d vel) {
        this.pos = pos;
        this.vel = vel;
    }

    /**
     * Gets the position of the boid.
     * @return the position of the boid
     */
    public P2d getPos() {
        return this.pos;
    }

    /**
     * Gets the velocity of the boid.
     * @return the velocity of the boid
     */
    public V2d getVel() {
        return this.vel;
    }

    /**
     * Calculates the new velocity of the boid based on its neighbors.
     * @param model the model
     */
    public void calculateVelocity(BoidsModel model) {
        List<Boid> nearbyBoids = getNearbyBoids(model);
        V2d separation = calculateSeparation(nearbyBoids, model.getAvoidRadius());
        V2d alignment = calculateAlignment(nearbyBoids);
        V2d cohesion = calculateCohesion(nearbyBoids);
        V2d vel = this.vel;
        vel = vel.sum(alignment.mul(model.getAlignmentWeight()))
                .sum(separation.mul(model.getSeparationWeight()))
                .sum(cohesion.mul(model.getCohesionWeight()));
        if (vel.abs() > model.getMaxSpeed()) {
            vel = vel.getNormalized().mul(model.getMaxSpeed());
        }
        this.updatedVel = vel;
    }

    /**
     * Updates the velocity with the new calculated velocity.
     */
    public void updateVelocity() {
        this.vel = this.updatedVel;
    }

    /**
     * Calculates the new position of the boid based on its velocity.
     * @param model the model
     */
    public void calculatePosition(BoidsModel model) {
        P2d pos = this.pos.sum(this.vel);
        if (pos.x() < model.getMinX()) pos = pos.sum(new V2d(model.getWidth(), 0));
        if (pos.x() >= model.getMaxX()) pos = pos.sum(new V2d(-model.getWidth(), 0));
        if (pos.y() < model.getMinY()) pos = pos.sum(new V2d(0, model.getHeight()));
        if (pos.y() >= model.getMaxY()) pos = pos.sum(new V2d(0, -model.getHeight()));
        this.updatedPos = pos;
    }

    /**
     * Updates the position with the new calculated position.
     */
    public void updatePosition() {
        this.pos = this.updatedPos;
    }

    private List<Boid> getNearbyBoids(BoidsModel model) {
        var list = new ArrayList<Boid>();
        for (Boid other : model.getBoids()) {
            if (other != this) {
                P2d otherPos = other.getPos();
                double distance = this.pos.distance(otherPos);
                if (distance < model.getPerceptionRadius()) {
                    list.add(other);
                }
            }
        }
        return list;
    }

    private V2d calculateAlignment(List<Boid> nearbyBoids) {
        double avgVx = 0;
        double avgVy = 0;
        if (!nearbyBoids.isEmpty()) {
            for (Boid other : nearbyBoids) {
                V2d otherVel = other.getVel();
                avgVx += otherVel.x();
                avgVy += otherVel.y();
            }
            avgVx /= nearbyBoids.size();
            avgVy /= nearbyBoids.size();
            return new V2d(avgVx - this.vel.x(), avgVy - this.vel.y()).getNormalized();
        } else {
            return new V2d(0, 0);
        }
    }

    private V2d calculateCohesion(List<Boid> nearbyBoids) {
        double centerX = 0;
        double centerY = 0;
        if (!nearbyBoids.isEmpty()) {
            for (Boid other: nearbyBoids) {
                P2d otherPos = other.getPos();
                centerX += otherPos.x();
                centerY += otherPos.y();
            }
            centerX /= nearbyBoids.size();
            centerY /= nearbyBoids.size();
            return new V2d(centerX - this.pos.x(), centerY - this.pos.y()).getNormalized();
        } else {
            return new V2d(0, 0);
        }
    }

    private V2d calculateSeparation(List<Boid> nearbyBoids, Double avoidRadius) {
        double dx = 0;
        double dy = 0;
        int count = 0;
        for (Boid other: nearbyBoids) {
            P2d otherPos = other.getPos();
            double distance = this.pos.distance(otherPos);
            if (distance < avoidRadius) {
                dx += this.pos.x() - otherPos.x();
                dy += this.pos.y() - otherPos.y();
                count++;
            }
        }
        if (count > 0) {
            dx /= count;
            dy /= count;
            return new V2d(dx, dy).getNormalized();
        } else {
            return new V2d(0, 0);
        }
    }
}
