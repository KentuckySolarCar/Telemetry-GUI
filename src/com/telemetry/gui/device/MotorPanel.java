package com.telemetry.gui.device;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;

import org.json.simple.JSONObject;

import com.telemetry.custom.SizedQueue;
import com.telemetry.custom.Tools;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class MotorPanel extends JPanel {

	private static final long serialVersionUID = -4958513623339300406L;
	private JLabel speed_label          = new JLabel("VALUE");
	private JLabel current_label        = new JLabel("VALUE");
	private JLabel voltage_label        = new JLabel("VALUE");
	private JLabel energy_label         = new JLabel("VALUE");
	private JLabel average_speed_label  = new JLabel("VALUE");
	private JButton energy_reset        = new JButton("Energy Reset");
	private JButton average_speed_reset = new JButton("Speed Reset");

	private SizedQueue<Double> speed;
	private SizedQueue<Double> current;
	private double amp_sec;
	private double watt_sec;
	private double odometer;
	private double energy;
	private double voltage;
	private double instant_current;
	private double instant_speed;
	
	// Threshold for fields
	private double speed_threshold = 50;
	private double current_threshold = 0;
	private double watt_sec_threshold = 0;
	
	private BatteryPanel battery_panel;
	
	public MotorPanel() {
		speed = new SizedQueue<Double>(60);
		current = new SizedQueue<Double>(60);
		
		insertComponents();
	}
	
	@SuppressWarnings("unchecked")
	public JSONObject getData() {
		JSONObject data = new JSONObject();
		data.put("ave_speed", new Double(calculateAveSpeed()));
		data.put("instant_current", instant_current);
		data.put("instant_speed", instant_speed);
		data.put("amp_sec", amp_sec);
		data.put("watt_sec", watt_sec);
		data.put("odometer", odometer);
		data.put("voltage", voltage);
		return data;
	}
	
	private void insertComponents() {
		setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();

		// Constant attributes
		gbc.insets = new Insets(5, 5, 5, 5);
		
		//----------------------Title------------------------//
		gbc.gridx = 1;
		gbc.gridy = 0;
		gbc.anchor = GridBagConstraints.CENTER;
		JLabel motor_controller = new JLabel("Motor Controller");
		motor_controller.setFont(Tools.TITLE_FONT);
		add(motor_controller, gbc);
		
		//----------------------Labels------------------------//
		gbc.gridx = 0;
		gbc.gridy = 1;
		JLabel speed = new JLabel("Speed:");
		speed.setFont(Tools.FIELD_FONT);
		add(speed, gbc);

		gbc.gridx = 0;
		gbc.gridy = 2;
		JLabel current = new JLabel("Current:");
		current.setFont(Tools.FIELD_FONT);
		add(current, gbc);
		
		gbc.gridx = 0;
		gbc.gridy = 3;
		JLabel voltage_l = new JLabel("Voltage:");
		voltage_l.setFont(Tools.FIELD_FONT);
		add(voltage_l, gbc);

		gbc.gridx = 0;
		gbc.gridy = 4;
		JLabel energy = new JLabel("Energy:");
		energy.setFont(Tools.FIELD_FONT);
		add(energy, gbc);

		gbc.gridx = 0;
		gbc.gridy = 5;
		JLabel av_speed = new JLabel("Av. Speed:");
		av_speed.setFont(Tools.FIELD_FONT);
		add(av_speed, gbc);
			
		//----------------------Fields------------------------//
		gbc.gridx = 1;
		gbc.gridy = 1;
		speed_label.setFont(Tools.FIELD_FONT);
		speed_label.setOpaque(true);
		speed_label.setBackground(Color.ORANGE);
		add(speed_label, gbc);

		gbc.gridx = 1;
		gbc.gridy = 2;
		current_label.setFont(Tools.FIELD_FONT);
		current_label.setOpaque(true);
		current_label.setBackground(Color.ORANGE);
		add(current_label, gbc);
		
		gbc.gridx = 1;
		gbc.gridy = 3;
		voltage_label.setFont(Tools.FIELD_FONT);
		voltage_label.setOpaque(true);
		voltage_label.setBackground(Color.ORANGE);
		add(voltage_label, gbc);

		gbc.gridx = 1;
		gbc.gridy = 4;
		energy_label.setFont(Tools.FIELD_FONT);
		energy_label.setOpaque(true);
		energy_label.setBackground(Color.ORANGE);
		add(energy_label, gbc);

		gbc.gridx = 1;
		gbc.gridy = 5;
		average_speed_label.setFont(Tools.FIELD_FONT);
		average_speed_label.setOpaque(true);
		average_speed_label.setBackground(Color.ORANGE);
		add(average_speed_label, gbc);

		//----------------------Buttons------------------------//
		gbc.insets = new Insets(0, 10, 0, 0);

		gbc.anchor = GridBagConstraints.CENTER;

		gbc.gridx = 2;
		gbc.gridy = 3;
		energy_reset.setBackground(Color.GRAY);
		energy_reset.addActionListener(new EnergyReset());
		add(energy_reset, gbc);

		gbc.gridx = 2;
		gbc.gridy = 4;
		average_speed_reset.setBackground(Color.GRAY);
		average_speed_reset.addActionListener(new AverageSpeedReset());
		add(average_speed_reset, gbc);
	}
	
	public void updatePanel(JSONObject obj) {
		double speed_instant = Double.parseDouble((String) obj.get("S"));
		double current_instant = Double.parseDouble((String) obj.get("I"));
		double voltage_instant = Double.parseDouble((String) obj.get("V"));
		
		if(speed_instant + current_instant + voltage_instant == 0) {
			speed_label.setBackground(Color.CYAN);
			current_label.setBackground(Color.CYAN);
			return;
		}
		
		speed.add(speed_instant);
		instant_speed = speed_instant;
		current.add(current_instant);
		instant_current = current_instant;
		voltage = voltage_instant;

		Tools.thresholdCheck(speed_label, speed_instant, speed_threshold, Tools.RED, Tools.GREEN);
		Tools.thresholdCheck(current_label, current_instant, current_threshold, Tools.RED, Tools.GREEN);
		
		speed_label.setText(Tools.roundDouble(speed_instant));
		current_label.setText(Tools.roundDouble(current_instant));
		voltage_label.setText(Tools.roundDouble(voltage_instant));
		
		average_speed_label.setText(Tools.roundDouble(Double.toString(calculateAveSpeed())));

		validate();
		repaint();
	}
	
	public void updatePanel(HashMap<String, Double> data, int dummy) {
		speed_label.setText(Tools.roundDouble(data.get("motor_speed")));
		current_label.setText(Tools.roundDouble(data.get("motor_current")));
		voltage_label.setText(Tools.roundDouble(data.get("motor_voltage")));
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
	
	public void setDependentPanel(BatteryPanel battery_panel) {
		this.battery_panel = battery_panel;
	}
	
	class EnergyReset implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent arg0) {
			energy = 0;
			energy_label.setText(Double.toString(energy));
		}
	}
	
	class AverageSpeedReset implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			average_speed_label.setText("0.0");
		}
	}
}