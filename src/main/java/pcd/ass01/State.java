package pcd.ass01;

/**
 * This class is a monitor that manage the state of the simulation.
 */
public class State {

    private enum StateType {
        RUNNING,
        PAUSED,
        STOPPED
    }

    private StateType state;

    private void setState(StateType state) {
        this.state = state;
    }

    public State() {
        this.setState(StateType.RUNNING);
    }

    /**
     * Check if the simulation is running.
     * @return true if the simulation is running, false otherwise
     */
    public synchronized boolean isRunning() {
        return this.state.equals(StateType.RUNNING);
    }

    /**
     * Check if the simulation is stopped.
     * @return true if the simulation is stopped, false otherwise
     */
    public synchronized boolean isStopped() {
        return this.state.equals(StateType.STOPPED);
    }

    /**
     * Stop the simulation.
     */
    public synchronized void stop() {
        this.setState(StateType.STOPPED);
    }

    /**
     * Suspend the simulation.
     */
    public synchronized void pause() {
        this.setState(StateType.PAUSED);
    }

    /**
     * Resume the simulation.
     */
    public synchronized void resume() {
        this.setState(StateType.RUNNING);
        notifyAll();
    }

    /**
     * Wait that the simulation is running.
     */
    public synchronized void awaitRun() {
        try {
            while (!this.isRunning()) {
                wait();
            }
        } catch (InterruptedException ignored) { }
    }

}
