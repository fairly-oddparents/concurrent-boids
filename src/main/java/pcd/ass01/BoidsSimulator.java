package pcd.ass01;

import java.util.Optional;

/**
 * Abstract class representing a Boids simulator.
 */
public abstract class BoidsSimulator {
    private static final int FRAMERATE = 25;
    private Optional<BoidsView> view = Optional.empty();
    private int framerate;
    private boolean isRunning;

    protected final BoidsModel model;

    public BoidsSimulator(BoidsModel model) {
        this.model = model;
        this.isRunning = true;
    }

    /**
     * Attaches the view to the simulation.
     * @param view the view
     */
    public void attachView(BoidsView view) {
        this.view = Optional.of(view);
    }

    /**
     * Updates the view with the current framerate.
     * @param t0 the start time of the update
     */
    protected void updateView(long t0) {
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

    /**
     * Checks whether the simulation is running or not.
     * @return true if the simulation is running, false otherwise
     */
    public synchronized boolean isRunning() {
        return this.isRunning;
    }

    /**
     * Runs the simulation.
     */
    protected abstract void runSimulation();

    /**
     * Pauses the simulation.
     */
    public synchronized void pauseSimulation() {
        this.isRunning = false;
    }

    /**
     * Resumes the simulation.
     */
    public synchronized void resumeSimulation() {
        this.isRunning = true;
    }

}