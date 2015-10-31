package com.telemetry.gui.device;

import java.awt.*;

public class MotorPanel extends Panel {
	private Panel motor_panel = new Panel();
	private Panel motor_label_panel = new Panel();
	private Panel motor_data_panel = new Panel();
	private Panel motor_button_panel = new Panel();
	private Label speed = new Label("VALUE");
	private Label current = new Label("VALUE");
	private Label energy = new Label("VALUE");
	private Label average_speed = new Label("VALUE");
	private Button energy_reset = new Button("Reset");
	private Button average_speed_reset = new Button("Reset");
	
	public MotorPanel() {
		motor_panel.setLayout(new GridLayout(1, 3));
		motor_label_panel.setLayout(new GridLayout(5, 1, 10, 10));
		motor_data_panel.setLayout(new GridLayout(5, 1, 10, 10));
		motor_button_panel.setLayout(new GridLayout(5, 1, 10, 10));
		
		insertLabelPanel();
		insertDataPanel();
		insertButtonPanel();
	}
	
	private void insertLabelPanel() {
		// Adding labels to motor_label_panel
		motor_label_panel.add(new Label("Motor Controller"));
		motor_label_panel.add(new Label("    Speed: "));
		motor_label_panel.add(new Label("    Current: "));
		motor_label_panel.add(new Label("    Energy: "));
		motor_label_panel.add(new Label("    Av. Speed: "));
		motor_panel.add(motor_label_panel);
	}
	
	private void insertDataPanel() {
		motor_data_panel.add(new Label(" "));
		motor_data_panel.add(speed);
		motor_data_panel.add(current);
		motor_data_panel.add(energy);
		motor_data_panel.add(average_speed);
		motor_panel.add(motor_data_panel);
	}
	
	private void insertButtonPanel() {
		motor_button_panel.add(new Label(" "));
		motor_button_panel.add(new Label(" "));
		motor_button_panel.add(new Label(" "));
		motor_button_panel.add(energy_reset);
		motor_button_panel.add(average_speed_reset);
		motor_panel.add(motor_button_panel);
	}
	
	@SuppressWarnings("unused")
	private void updatePanel() {
		speed.setText("Needs Implementing!");
		current.setText("Need Implementing!");
		energy.setText("Need Implementing!");
		average_speed.setText("Need Implementing");
	}
	
	public Panel getPanel() {
		return motor_panel;
	}
}
