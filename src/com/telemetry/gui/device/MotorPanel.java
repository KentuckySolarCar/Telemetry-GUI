package com.telemetry.gui.device;

import java.awt.GridLayout;
import javax.swing.*;

import org.json.simple.JSONObject;

public class MotorPanel extends JPanel {
/**
	 * 
	 */
	private static final long serialVersionUID = -4958513623339300406L;
	//	private Panel motor_panel = new Panel();
	private JPanel motor_label_panel = new JPanel();
	private JPanel motor_data_panel = new JPanel();
	private JPanel motor_button_panel = new JPanel();
	private JLabel speed                = new JLabel("VALUE");
	private JLabel current              = new JLabel("VALUE");
	private JLabel energy               = new JLabel("VALUE");
	private JLabel average_speed        = new JLabel("VALUE");
	private JButton energy_reset        = new JButton("Reset");
	private JButton average_speed_reset = new JButton("Reset");
	
	public MotorPanel() {
		setLayout(new GridLayout(1, 3));
		motor_label_panel.setLayout(new GridLayout(5, 1, 10, 10));
		motor_data_panel.setLayout(new GridLayout(5, 1, 10, 10));
		motor_button_panel.setLayout(new GridLayout(5, 1, 10, 10));
		
		insertLabelPanel();
		insertDataPanel();
		insertButtonPanel();
	}
	
	private void insertLabelPanel() {
		// Adding labels to motor_label_panel
		motor_label_panel.add(new JLabel("Motor Controller"));
		motor_label_panel.add(new JLabel("    Speed: "));
		motor_label_panel.add(new JLabel("    Current: "));
		motor_label_panel.add(new JLabel("    Energy: "));
		motor_label_panel.add(new JLabel("    Av. Speed: "));
		add(motor_label_panel);
	}
	
	private void insertDataPanel() {
		motor_data_panel.add(new JLabel(" "));
		motor_data_panel.add(speed);
		motor_data_panel.add(current);
		motor_data_panel.add(energy);
		motor_data_panel.add(average_speed);
		add(motor_data_panel);
	}
	
	private void insertButtonPanel() {
		motor_button_panel.add(new JLabel(" "));
		motor_button_panel.add(new JLabel(" "));
		motor_button_panel.add(new JLabel(" "));
		motor_button_panel.add(energy_reset);
		motor_button_panel.add(average_speed_reset);
		add(motor_button_panel);
	}
	
	public void updatePanel(JSONObject data) {
		speed.setText("Needs Implementing!");
		current.setText("Need Implementing!");
		energy.setText("Need Implementing!");
		average_speed.setText("Need Implementing");
		
		validate();
		repaint();
	}
	
/*	public Panel getPanel() {
		return motor_panel;
	} */
}
