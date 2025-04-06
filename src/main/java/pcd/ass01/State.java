package pcd.ass01;

/**
 * This class is a monitor that manage the state of the simulation (running, paused, stopped).
 * When instantiated, the simulation is in running state.
 */
public class State {

    private enum StateType {
        RUNNING,
        PAUSED,
        STOPPED
    }

    private final MyMonitor<StateType> state;

    /**
     * Constructor for the State class.
     * This constructor initializes the state to running.
     */
    public State() {
        this.state = new MyMonitor<>(StateType.RUNNING);
    }

    private void setState(StateType state) {
        this.state.set(state);
    }

    /**
     * Check if the simulation is running.
     * @return true if the simulation is running, false otherwise
     */
    public boolean isRunning() {
        return this.state.get().equals(StateType.RUNNING);
    }

    /**
     * Check if the simulation is stopped.
     * @return true if the simulation is stopped, false otherwise
     */
    public boolean isStopped() {
        return this.state.get().equals(StateType.STOPPED);
    }

    /**
     * Stop the simulation.
     */
    public void stop() {
        this.setState(StateType.STOPPED);
    }

    /**
     * Suspend the simulation.
     */
    public void pause() {
        this.setState(StateType.PAUSED);
    }

    /**
     * Resume the simulation.
     */
    public void resume() {
        this.setState(StateType.RUNNING);
    }

    /**
     * Wait that the simulation is running.
     */
    public void awaitRun() {
        this.state.awaitUntil(s -> isRunning());
    }

}
