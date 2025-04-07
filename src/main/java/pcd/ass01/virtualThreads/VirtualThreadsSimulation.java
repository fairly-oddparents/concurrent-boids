package pcd.ass01.virtualThreads;

import pcd.ass01.BoidsController;
import pcd.ass01.BoidsModel;
import pcd.ass01.BoidsView;
import pcd.ass01.api.View;

/**
 * Entry-point for the virtual threads version of the boids simulation.
 */
public class VirtualThreadsSimulation {

    private static long startTime = 0;
    private static long endTime = 0;
    private static long totalElapsed = 0;

    public static void main(String[] args) {
        BoidsModel model = new BoidsModel();
        BoidsController controller = new VirtualThreadsController(model);
        View view = new BoidsView(controller, model.getWidth(), model.getHeight());
        controller.attachView(view);

        startTime = System.currentTimeMillis();
        controller.run();
        endTime = System.currentTimeMillis();
        totalElapsed += endTime - startTime;

        System.out.println("Simulazione completata");
        System.out.println("Tempo totale per eseguire 1000 iterazioni (ms): " + totalElapsed);
        System.out.println("Tempo totale per eseguire 1000 iterazioni (s): " + totalElapsed / (1000.0));
    }

}
