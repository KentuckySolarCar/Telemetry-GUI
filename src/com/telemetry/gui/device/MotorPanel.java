package com.telemetry.gui.device;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.swing.*;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class MotorPanel extends JPanel {

	private static final long serialVersionUID = -4958513623339300406L;
	private JPanel motor_label_panel    = new JPanel();
	private JPanel motor_data_panel     = new JPanel();
	private JPanel motor_button_panel   = new JPanel();
	private JLabel speed_label          = new JLabel("VALUE");
	private JLabel current_label        = new JLabel("VALUE");
	private JLabel energy_label         = new JLabel("VALUE");
	private JLabel average_speed_label  = new JLabel("VALUE");
	private ArrayList<Double> speed;
	private ArrayList<Double> current;
	private double amp_sec;
	private double watt_sec;
	private double odometer;
	private JButton energy_reset        = new JButton("Energy Reset");
	private JButton average_speed_reset = new JButton("Speed Reset");
	
	public MotorPanel() {
		setLayout(new GridLayout(1, 3));
		motor_label_panel.setLayout(new GridLayout(5, 1, 10, 10));
		motor_data_panel.setLayout(new GridLayout(5, 1, 10, 10));
		motor_button_panel.setLayout(new GridLayout(5, 1, 10, 10));
		speed = new ArrayList<Double>();
		current = new ArrayList<Double>();
		
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
		motor_data_panel.add(speed_label);
		motor_data_panel.add(current_label);
		motor_data_panel.add(energy_label);
		motor_data_panel.add(average_speed_label);
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
	
	public void updatePanel(JSONObject obj) {
		speed_label.setText((String) obj.get("S"));
		current_label.setText((String) obj.get("I"));
		
		speed.add(Double.parseDouble((String) obj.get("S")));
		current.add(Double.parseDouble((String) obj.get("I")));
		
		average_speed_label.setText(calculateAveSpeed());
		validate();
		repaint();
	}
	
	private String calculateAveSpeed() {
		double sum = 0;
		for(Double d : speed)
			sum += d;
		return Double.toString(sum/speed.size());
	}
}
