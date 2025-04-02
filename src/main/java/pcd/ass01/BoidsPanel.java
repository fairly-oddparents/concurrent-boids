package pcd.ass01;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class BoidsPanel extends JPanel {

	private final BoidsView view;
	private final List<Boid> boids;
    private int framerate;

    public BoidsPanel(BoidsView view) {
    	this.boids = new ArrayList<>();
    	this.view = view;
    }

    public void setFrameRate(int framerate) {
    	this.framerate = framerate;
    }

    public void setBoids (List<Boid> boids) {
    	this.boids.clear();
    	this.boids.addAll(boids);
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        setBackground(Color.WHITE);
        
        var w = this.view.getWidth();
        var h = this.view.getHeight();
        var envWidth = this.view.getWidth();
        var xScale = w/envWidth;

        g.setColor(Color.BLUE);
        List<Boid> boids = new ArrayList<>(this.boids); // TODO: copy the array to avoid concurrent modification
        for (Boid boid : boids) {
        	P2d pos = boid.getPos();
        	int px = (int)((double) w / 2 + pos.x() * xScale);
        	int py = (int)((double) h / 2 - pos.y() * xScale);
            g.fillOval(px,py, 5, 5);
        }
        
        g.setColor(Color.BLACK);
        g.drawString("Num. Boids: " + boids.size(), 10, 25);
        g.drawString("Framerate: " + framerate, 10, 40);
   }
}
