package pcd.ass01;

import java.util.Optional;

/**
 * Abstract class representing a Boids simulator.
 */
public abstract class BoidsSimulator {
    private static final int FRAMERATE = 25;
    private Optional<BoidsView> view = Optional.empty();
    private int framerate;

    protected final BoidsModel model;

    public BoidsSimulator(BoidsModel model) {
        this.model = model;
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
     * Runs the simulation.
     */
    protected abstract void runSimulation();
}