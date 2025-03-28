package pcd.ass01.multithreaded.api;

public interface Barrier {

    void await() throws InterruptedException;

}
