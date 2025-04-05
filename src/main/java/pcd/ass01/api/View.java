package pcd.ass01.api;

import pcd.ass01.Boid;

import java.util.List;

public interface View {

    /**
     * Sets the state of the pause.
     * @param state true if the simulation is paused, false otherwise
     */
    void setPauseState(boolean state);

    /**
     * Asks the user to enter the number of boids.
     * @return the number of boids entered by the user
     */
    Integer getBoidCountFromUser();

    /**
     * Updates the view with the current frame rate and the list of boids.
     * @param frameRate the current frame rate of the simulation
     * @param boids the list of boids to be displayed
     */
    void update(int frameRate, List<Boid> boids);

    /**
     * Returns the current width of the frame.
     * @return the width of the frame
     */
    int getWidth();

    /**
     * Returns the current height of the frame.
     * @return the height of the frame
     */
    int getHeight();
}
