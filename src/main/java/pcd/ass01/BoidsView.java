package pcd.ass01;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.List;

import java.awt.*;
import java.util.Hashtable;

public class BoidsView implements ChangeListener {

	private final JFrame frame;
	private final BoidsPanel boidsPanel;
    private final JButton pauseButton, stopButton;
	private final JSlider cohesionSlider, separationSlider, alignmentSlider;
	private final BoidsController controller;
	private int width, height;
	private boolean isPaused;

	public BoidsView(
			BoidsController controller,
			int screenWidth,
			int screenHeight,
			double logicalWidth,
			double logicalHeight
	) {
		this.controller = controller;
		this.width = screenWidth;
		this.height = screenHeight;

		frame = new JFrame("Boids Simulation");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(true);

		frame.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent e) {
				int w = frame.getWidth();
				int h = w * getHeight() / getWidth();
				frame.setSize(w, h);
				width = w;
				height = h;
			}
		});

		JPanel cp = new JPanel();
		LayoutManager layout = new BorderLayout();
		cp.setLayout(layout);

		JPanel buttonsPanel = new JPanel();

		this.stopButton = new JButton("Stop");
		this.stopButton.addActionListener(e -> this.controller.stop());
		pauseButton = new JButton("Pause");
		pauseButton.addActionListener(e -> {
			if (this.isPaused)
				this.controller.resume();
			else
				this.controller.pause();
		});

		buttonsPanel.add(stopButton);
		buttonsPanel.add(pauseButton);

		cp.add(BorderLayout.NORTH, buttonsPanel);

        boidsPanel = new BoidsPanel(this, logicalWidth, logicalHeight);
		cp.add(BorderLayout.CENTER, boidsPanel);

        JPanel slidersPanel = new JPanel();

        cohesionSlider = makeSlider();
        separationSlider = makeSlider();
        alignmentSlider = makeSlider();

        slidersPanel.add(new JLabel("Separation"));
        slidersPanel.add(separationSlider);
        slidersPanel.add(new JLabel("Alignment"));
        slidersPanel.add(alignmentSlider);
        slidersPanel.add(new JLabel("Cohesion"));
        slidersPanel.add(cohesionSlider);

		cp.add(BorderLayout.SOUTH, slidersPanel);

        frame.setContentPane(cp);
		frame.pack();
        frame.setVisible(true);
    }

	public void setPauseButtonState(boolean state) {
		this.isPaused = state;
        if (this.isPaused) {
			pauseButton.setText("Resume");
			this.stopButton.setEnabled(false);

        } else {
			pauseButton.setText("Pause");
			this.stopButton.setEnabled(true);
        }
    }

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
	
	public void update(int frameRate, List<Boid> boids) {
		boidsPanel.setFrameRate(frameRate);
		boidsPanel.setBoids(boids);
		boidsPanel.repaint();
	}

	@Override
	public void stateChanged(ChangeEvent e) {
		if (e.getSource() == separationSlider) {
			var val = separationSlider.getValue();
			this.controller.setSeparationWeight(0.1*val);
		} else if (e.getSource() == cohesionSlider) {
			var val = cohesionSlider.getValue();
			this.controller.setCohesionWeight(0.1*val);
		} else {
			var val = alignmentSlider.getValue();
			this.controller.setAlignmentWeight(0.1*val);
		}
	}
	
	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

}
