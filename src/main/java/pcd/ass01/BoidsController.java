package pcd.ass01;

/**
 * Abstract class representing the controller (from MVC architecture) for the Boids simulation.
 */
public abstract class BoidsController {

    private static final int FRAMERATE = 25;
    public static final int FRAMERATE_PERIOD = 1000 / FRAMERATE;
    private final State state;
    private BoidsView view;
    private int framerate;

    protected final BoidsModel model;

    /**
     * Constructor for the BoidsController.
     * @param model the model
     */
    public BoidsController(BoidsModel model) {
        this.model = model;
        this.state = new State();
    }

    /**
     * Attaches the view to the controller.
     * @param view the view to attach
     */
    public void attachView(BoidsView view) {
        this.view = view;
    }

    /**
     * Updates the view with the current framerate.
     * @param t0 the start time of the update
     */
    protected void updateView(long t0) {
        view.update(framerate, this.model.getBoids());
        var t1 = System.currentTimeMillis();
        var dtElapsed = t1 - t0;
        if (dtElapsed < FRAMERATE_PERIOD) {
            try {
                Thread.sleep(FRAMERATE_PERIOD - dtElapsed);
            } catch (Exception e) {
                Thread.currentThread().interrupt();
            }
            framerate = FRAMERATE;
        } else {
            framerate = (int) (1000/dtElapsed);
        }
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
     * Waits for the simulation to run.
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
     * Gets the number of boids from the user.
     * @return the number of boids
     */
    public int getNumberOfBoids() {
        return this.view.inputDialog();
    }

    /**
     * Stops the simulation.
     */
    public void stop() {
        this.state.stop();
        this.model.setNumberBoids(this.getNumberOfBoids());
        this.resume();
    }

    /**
     * Sets the separation weight.
     * @param weight the separation weight
     */
    public void setSeparationWeight(double weight) {
        this.model.setSeparationWeight(weight);
    }

    /**
     * Sets the cohesion weight.
     * @param weight the cohesion weight
     */
    public void setCohesionWeight(double weight) {
        this.model.setCohesionWeight(weight);
    }

    /**
     * Sets the alignment weight.
     * @param weight the alignment weight
     */
    public void setAlignmentWeight(double weight) {
        this.model.setAlignmentWeight(weight);
    }

}