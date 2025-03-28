package pcd.ass01;

import pcd.ass01.sequential.BoidsModel;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import java.awt.*;
import java.util.Hashtable;

public class BoidsView implements ChangeListener {

	private final JFrame frame;
	private final BoidsPanel boidsPanel;
    private final JButton pauseResumeButton;
	private final JSlider cohesionSlider, separationSlider, alignmentSlider;
	private final BoidsModel model;
	private final int width, height;
	private boolean isPaused;
	
	public BoidsView(BoidsModel model, int width, int height) {
		this.model = model;
		this.width = width;
		this.height = height;
        this.isPaused = false;

		frame = new JFrame("Boids Simulation");
        frame.setSize(width, height);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(true);

		JPanel cp = new JPanel();
		LayoutManager layout = new BorderLayout();
		cp.setLayout(layout);

		JPanel buttonsPanel = new JPanel();

		JButton stopButton = new JButton("Stop");
		stopButton.addActionListener(e -> {
			this.model.clearBoids();
			this.model.setNumberBoids(inputDialog());
		});
		pauseResumeButton = new JButton("Pause");
		pauseResumeButton.addActionListener(e -> {
			this.isPaused = !this.isPaused;
			pauseResumeButton.setText(this.isPaused ? "Resume" : "Pause");
			this.model.setPaused(this.isPaused);
		});

		buttonsPanel.add(stopButton);
		buttonsPanel.add(pauseResumeButton);

		cp.add(BorderLayout.NORTH, buttonsPanel);

        boidsPanel = new BoidsPanel(this, model);
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

        frame.setVisible(true);
    }

	public Integer inputDialog() {
		String input;
		do {
			input = JOptionPane.showInputDialog(
					this.frame,
					"Insert number of voids:",
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
	
	public void update(int frameRate) {
		boidsPanel.setFrameRate(frameRate);
		boidsPanel.repaint();
	}

	@Override
	public void stateChanged(ChangeEvent e) {
		if (e.getSource() == separationSlider) {
			var val = separationSlider.getValue();
			model.setSeparationWeight(0.1*val);
		} else if (e.getSource() == cohesionSlider) {
			var val = cohesionSlider.getValue();
			model.setCohesionWeight(0.1*val);
		} else {
			var val = alignmentSlider.getValue();
			model.setAlignmentWeight(0.1*val);
		}
	}
	
	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

}
