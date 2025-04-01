package pcd.ass01;

/**
 * Abstract class representing a Boids simulator.
 */
public abstract class BoidsController {
    private static final int FRAMERATE = 25;
    private final State state;
    private BoidsView view;
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
        this.view = view;
    }

    /**
     * Updates the view with the current framerate.
     * @param t0 the start time of the update
     */
    protected void updateView(long t0) {
        view.update(framerate);
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
    public abstract void run();

    /**
     * Pauses the simulation.
     */
    public void pause() {
        System.out.println("Pausing simulation");   //TODO: remove logs
        this.state.pause();
        this.view.setPauseButtonState(true);
    }

    /**
     * Resumes the simulation.
     */
    public void resume() {
        System.out.println("Resuming simulation");  //TODO: remove logs
        this.state.resume();
        this.view.setPauseButtonState(false);
    }

    /**
     * Waits for the simulation to start.
     */
    public void awaitRun() {
        this.state.awaitRun();
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
    public void stop() {
        this.state.stop();
    }

}