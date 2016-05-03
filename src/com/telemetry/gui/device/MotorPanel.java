package com.telemetry.gui.device;

import java.awt.GridLayout;

import org.json.simple.JSONObject;

import com.telemetry.custom.SizedQueue;

import java.util.Queue;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class MotorPanel extends JPanel {

	private static final long serialVersionUID = -4958513623339300406L;
	private JPanel motor_label_panel    = new JPanel();
	private JPanel motor_data_panel     = new JPanel();
	private JPanel motor_button_panel   = new JPanel();
	private JLabel speed_label          = new JLabel("VALUE");
	private JLabel current_label        = new JLabel("VALUE");
	private JLabel energy_label         = new JLabel("VALUE");
	private JLabel average_speed_label  = new JLabel("VALUE");
	private Queue<Double> speed;
	private Queue<Double> current;
	private double amp_sec;
	private double watt_sec;
	private double odometer;
	private JButton energy_reset        = new JButton("Energy Reset");
	private JButton average_speed_reset = new JButton("Speed Reset");
	
	private static final double speed_conversion = 0.223693629;
	
	public MotorPanel() {
		setLayout(new GridLayout(1, 3));
		motor_label_panel.setLayout(new GridLayout(5, 1));
		motor_data_panel.setLayout(new GridLayout(5, 1));
		motor_button_panel.setLayout(new GridLayout(5, 1));
		speed = new SizedQueue<Double>(60);
		current = new SizedQueue<Double>(60);
		
		insertLabelPanel();
		insertDataPanel();
		insertButtonPanel();
	}
	
	@SuppressWarnings("unchecked")
	public JSONObject getData() {
		JSONObject data = new JSONObject();
		data.put("ave_speed", new Double(calculateAveSpeed()));
		data.put("ave_current", new Double(calculateAveCurrent()));
		data.put("amp_sec", new Double(amp_sec));
		data.put("watt_sec", new Double(watt_sec));
		data.put("odometer", new Double(odometer));
		return data;
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
		
		speed.add(Double.parseDouble((String) obj.get("S")) * speed_conversion);
		current.add(Double.parseDouble((String) obj.get("I")));
		
		average_speed_label.setText(Double.toString(calculateAveSpeed()));
		validate();
		repaint();
	}
	
	private double calculateAveSpeed() {
		double sum = 0;
		for(Double d : speed)
			sum += d;
		return sum/speed.size();
	}
	
	private double calculateAveCurrent() {
		double sum = 0;
		for(Double d : current)
			sum += d;
		return sum/current.size();
	}
}
