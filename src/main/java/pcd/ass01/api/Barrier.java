package pcd.ass01.api;

/**
 * Barrier interface for synchronizing threads.
 */
public interface Barrier {

    /**
     * Waits for all threads to reach this barrier.
     * This method blocks the current thread until all threads have called this method.
     */
    void await();

}
