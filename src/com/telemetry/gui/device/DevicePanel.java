package com.telemetry.gui.device;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.util.HashMap;

import javax.swing.*;

import com.telemetry.data.CarData;

public class DevicePanel extends JPanel{
	private static final long serialVersionUID = -7422627351609719543L;
	
	// MPPT panel not used right now
	private TimePanel time_panel;
	private SpeedDialPanel speed_dial_panel;
	private BatteryPanel battery_panel;
	private MotorPanel motor_panel;
	
	public DevicePanel() {
		setLayout(new GridBagLayout());

		// Panel initialization
		time_panel = new TimePanel();
		speed_dial_panel = new SpeedDialPanel();
		battery_panel = new BatteryPanel();
		motor_panel = new MotorPanel();

		// Speed dial and time panel will be within same sub-panel
		JPanel time_speed_panel = new JPanel(new GridLayout(1, 2));
		time_speed_panel.add(time_panel);
		time_speed_panel.add(speed_dial_panel);
		
		// Insert panels
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(10, 0, 10, 0);
		
		gbc.gridx = 0;
		gbc.gridy = 0;
		add(time_panel, gbc);
		
		gbc.gridx = 0;
		gbc.gridy = 1;
		add(motor_panel, gbc);
		
		gbc.gridx = 1;
		gbc.gridy = 0;
		gbc.gridheight = 2;
		add(speed_dial_panel, gbc);
		gbc.gridheight = 1;
		
		gbc.gridx = 0;
		gbc.gridy = 2;
		gbc.gridwidth = 2;
		add(battery_panel, gbc);
	}
	
	/**
	 * Update each panel with input car data. Currently only passing necessary data
	 * to each panel.
	 * @param data Most recent car data
	 */
	public void updatePanel(CarData data) {
		HashMap<String, Double> motor_data = data.getMotorData();
		HashMap<String, Double> batt_data = data.getBatteryData();
		HashMap<String, Integer[]> time_data = data.getTimeData();
		motor_panel.updatePanel(motor_data);
		battery_panel.updatePanel(batt_data);
		time_panel.updatePanel(time_data);
		speed_dial_panel.updateDial(motor_data);
	}
	
	/**
	 * Updates the run time of time_panel, call interval controlled by main thread
	 */
	public void updateRunTime() {
		time_panel.updateRunTime();
	}
}
