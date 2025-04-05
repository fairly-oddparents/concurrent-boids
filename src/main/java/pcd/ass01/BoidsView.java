package pcd.ass01;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.util.List;
import java.awt.*;
import java.util.Hashtable;

/**
 * The BoidsView class is responsible for creating the graphical user interface (GUI) for the Boids simulation.
 * It allows users to interact with the simulation, including starting, pausing, and stopping it.
 * The view also provides sliders to adjust the weights of the boids behaviors (cohesion, separation, and alignment).
 */
public class BoidsView implements ChangeListener {

	private final static Dimension SCREEN_SIZE = Toolkit.getDefaultToolkit().getScreenSize();
	private final static int SIDE_SIZE = Math.min(SCREEN_SIZE.width, SCREEN_SIZE.height) * 4 / 5;

	private final JFrame frame;
	private final BoidsPanel boidsPanel;
    private final JButton pauseButton, stopButton;
	private final JSlider cohesionSlider, separationSlider, alignmentSlider;
	private final BoidsController controller;
	private boolean isPaused;

	/**
	 * Constructor for the BoidsView class.
	 * @param controller the BoidsController instance that manages the simulation
	 * @param logicalWidth the logical width of the simulation area
	 * @param logicalHeight the logical height of the simulation area
	 */
	public BoidsView(BoidsController controller, double logicalWidth, double logicalHeight) {
		this.controller = controller;
		this.frame = setFrame();

		JPanel cp = new JPanel();
		cp.setLayout(new BorderLayout());

		// Create a panel for the buttons (stop and pause/resume)
		JPanel buttonsPanel = new JPanel();
		this.stopButton = new JButton("Stop");
		this.stopButton.addActionListener(e -> this.controller.stop());
		this.pauseButton = new JButton("Pause");
		this.pauseButton.addActionListener(e -> {
			if (this.isPaused)
				this.controller.resume();
			else
				this.controller.pause();
		});
		buttonsPanel.add(this.stopButton);
		buttonsPanel.add(this.pauseButton);
		cp.add(BorderLayout.NORTH, buttonsPanel);

		// Create a panel for the boids
		this.boidsPanel = new BoidsPanel(this, logicalWidth, logicalHeight);
		cp.add(BorderLayout.CENTER, boidsPanel);

		// Create a panel for the sliders
        JPanel slidersPanel = new JPanel();
		this.cohesionSlider = makeSlider();
		this.separationSlider = makeSlider();
		this.alignmentSlider = makeSlider();
        slidersPanel.add(new JLabel("Separation"));
		slidersPanel.add(this.separationSlider);
        slidersPanel.add(new JLabel("Alignment"));
		slidersPanel.add(this.alignmentSlider);
        slidersPanel.add(new JLabel("Cohesion"));
		slidersPanel.add(this.cohesionSlider);
		cp.add(BorderLayout.SOUTH, slidersPanel);

        this.frame.setContentPane(cp);
        this.frame.setVisible(true);
    }

	private JFrame setFrame() {
		final JFrame frame;
		frame = new JFrame("Boids Simulation");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(SIDE_SIZE, SIDE_SIZE);
		frame.setResizable(true);
		return frame;
	}

	/**
	 * Sets the state of the pause button and updates its text accordingly.
	 * @param state true if the simulation is paused, false otherwise
	 */
	public void setPauseButtonState(boolean state) {
		this.isPaused = state;
        if (this.isPaused) {
			this.pauseButton.setText("Resume");
			this.stopButton.setEnabled(false);

        } else {
			this.pauseButton.setText("Pause");
			this.stopButton.setEnabled(true);
        }
    }

	/**
	 * Displays an input dialog to the user to enter the number of boids.
	 * @return the number of boids entered by the user
	 */
	public Integer inputDialog() {
		String input;
		do {
			input = JOptionPane.showInputDialog(
					this.frame,
					"Insert number of boids:",
					"Input",
					JOptionPane.QUESTION_MESSAGE
			);
			if (input == null) {	// Handles the "cancel" button
				this.frame.dispose();
				System.exit(0);
			}
		} while (input.isEmpty());
		return Integer.parseInt(input);
	}

	private JSlider makeSlider() {
		var slider = new JSlider(JSlider.HORIZONTAL, 0, 20, 10);        
		slider.setMajorTickSpacing(10);
		slider.setMinorTickSpacing(1);
		slider.setPaintTicks(true);
		slider.setPaintLabels(true);
		Hashtable<Integer, JLabel> labelTable = new Hashtable<>();
		labelTable.put( 0, new JLabel("0") );
		labelTable.put( 10, new JLabel("1") );
		labelTable.put( 20, new JLabel("2") );
		slider.setLabelTable( labelTable );
		slider.setPaintLabels(true);
        slider.addChangeListener(this);
		return slider;
	}

	/**
	 * Updates the boids panel with the current frame rate and the list of boids.
	 * @param frameRate the current frame rate of the simulation
	 * @param boids the list of boids to be displayed
	 */
	public void update(int frameRate, List<Boid> boids) {
		this.boidsPanel.setFrameRate(frameRate);
		this.boidsPanel.setBoids(boids);
		this.boidsPanel.repaint();
	}

	@Override
	public void stateChanged(ChangeEvent e) {
		if (e.getSource() == this.separationSlider) {
			var val = this.separationSlider.getValue();
			this.controller.setSeparationWeight(0.1*val);
		} else if (e.getSource() == this.cohesionSlider) {
			var val = this.cohesionSlider.getValue();
			this.controller.setCohesionWeight(0.1*val);
		} else {
			var val = this.alignmentSlider.getValue();
			this.controller.setAlignmentWeight(0.1*val);
		}
	}

	/**
	 * Returns the current width of the frame.
	 * @return the width of the frame
	 */
	public int getWidth() {
		return this.frame.getWidth();
	}

	/**
	 * Returns the current height of the frame.
	 * @return the height of the frame
	 */
	public int getHeight() {
		return this.frame.getHeight();
	}

}
