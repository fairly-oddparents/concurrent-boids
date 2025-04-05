package pcd.ass01;

import pcd.ass01.api.View;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Panel for displaying the boids simulation.
 */
public class BoidsPanel extends JPanel {

    private final View view;
    private final List<Boid> boids;
    private final double logicalWidth, logicalHeight;
    private int framerate;

    /**
     * Constructor for the BoidsPanel.
     * @param view the view
     * @param logicalWidth the logical width
     * @param logicalHeight the logical height
     */
    public BoidsPanel(View view, double logicalWidth, double logicalHeight) {
        this.boids = new ArrayList<>();
        this.view = view;
        this.logicalWidth = logicalWidth;
        this.logicalHeight = logicalHeight;
    }

    /**
     * Sets the framerate.
     * @param framerate the framerate
     */
    public void setFrameRate(int framerate) {
        this.framerate = framerate;
    }

    /**
     * Sets the boids.
     * @param boids the list of boids
     */
    public void setBoids (List<Boid> boids) {
        this.boids.clear();
        this.boids.addAll(boids);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        setBackground(Color.WHITE);

        var width = this.view.getWidth();
        var height = this.view.getHeight();
        var xScale = width / this.logicalWidth;
        var yScale = height / this.logicalHeight;

        g.setColor(Color.BLUE);
        List<Boid> boids = new ArrayList<>(this.boids);
        for (Boid boid : boids) {
            P2d pos = boid.getPos();
            int px = (int)(width / 2.0 + pos.x() * xScale);
            int py = (int)(height / 2.0 - pos.y() * yScale);
            g.fillOval(px,py, 5, 5);
        }

        g.setColor(Color.BLACK);
        g.drawString("Num. Boids: " + boids.size(), 10, 25);
        g.drawString("Framerate: " + framerate, 10, 40);
    }
}

