package com.telemetry.gui.device;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;

import com.telemetry.util.Tools;

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
	
	// Threshold for fields
	private double current_threshold = 0;
	private double voltage_threshold = 0;
	
	public MotorPanel() {
		insertComponents();
	}

	public void updatePanel(HashMap<String, Double> data) {
		speed_label.setText(Tools.roundDouble(data.get("motor_speed")));
		current_label.setText(Tools.roundDouble(data.get("motor_current")));
		voltage_label.setText(Tools.roundDouble(data.get("motor_voltage")));
		
		// Change label color if threshold reached
		Tools.thresholdCheck(current_label, data.get("motor_current"), 
							 current_threshold, Tools.NOT_RED, Tools.NOT_GREEN);
		Tools.thresholdCheck(voltage_label, data.get("motor_voltage"), 
							 voltage_threshold, Tools.NOT_RED, Tools.NOT_GREEN);
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
		gbc.anchor = GridBagConstraints.WEST;
		
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
	
	class EnergyReset implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent arg0) {
			// This listener will edit value in data container
			// But for now this won't be used
		}
	}
	
	class AverageSpeedReset implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			// This listener will edit value in data container
			// But for now this won't be used
		}
	}
}