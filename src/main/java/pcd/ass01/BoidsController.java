package pcd.ass01;

import java.util.Optional;

/**
 * Abstract class representing a Boids simulator.
 */
public abstract class BoidsController {
    private static final int FRAMERATE = 25;
    private final State state;
    private Optional<BoidsView> view = Optional.empty();
    private int framerate;

    protected final BoidsModel model;

    public BoidsController(BoidsModel model) {
        this.model = model;
        this.state = new State();
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
    public boolean isRunning() {
        return this.state.isRunning();
    }

    /**
     * Runs the simulation.
     */
    public abstract void runSimulation();

    /**
     * Pauses the simulation.
     */
    public void pauseSimulation() {
        System.out.println("Pausing simulation");   //TODO: remove logs
        this.state.pauseSimulation();
    }

    /**
     * Resumes the simulation.
     */
    public void resumeSimulation() {
        System.out.println("Resuming simulation");  //TODO: remove logs
        this.state.resumeSimulation();
    }

    /**
     * Waits for the simulation to start.
     */
    public void waitForSimulation() {
        this.state.waitForSimulation();
    }

    /**
     * Checks if the simulation is stopped.
     * @return true if the simulation is stopped, false otherwise
     */
    public boolean isStopped() {
        return this.state.isStopped();
    }

    /**
     * Stops the simulation.
     */
    public void stopSimulation() {
        this.state.stopSimulation();
    }

}