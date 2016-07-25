package com.telemetry.gui.device;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.util.ArrayList;

import javax.swing.*;

import org.json.simple.JSONObject;

public class DevicePanel extends JPanel{
	private static final long serialVersionUID = -7422627351609719543L;
	
	private MotorPanel motor_panel = new MotorPanel();
	private MpptPanel mppt_panel = new MpptPanel();
	private TimePanel time_panel = new TimePanel();
	private SpeedDialPanel speed_dial_panel = new SpeedDialPanel();
	private BatteryPanel battery_panel = new BatteryPanel(motor_panel);
	
	public DevicePanel() {
		super();
		
		JPanel time_speed_panel = new JPanel(new GridLayout(1, 3));
		time_speed_panel.add(time_panel);
		time_speed_panel.add(speed_dial_panel);
		
		setLayout(new GridBagLayout());
		
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
	
	public void updatePanel(JSONObject obj) {
		time_panel.updatePanel((String) obj.get("Time"));
		String type = (String) obj.get("message_id");
		switch(type) {
		case "motor": {
			speed_dial_panel.updateDial((String) obj.get("S"));
			motor_panel.updatePanel(obj);
			break;
		}
		case "bat_volt": 
		case "bat_temp": {
			battery_panel.updatePanel(obj, type);
			break;
		}
		default:
			break;
		}
		
		validate();
		repaint();
	}
	
	public int[] getRunTime() {
		return time_panel.getRunTime();
	}
	
	public String getSystemTime() {
		return time_panel.getSystemTime();
	}
	
	@SuppressWarnings("unchecked")
	public JSONObject getDeviceData() {
		JSONObject device_data = new JSONObject();
		JSONObject battery_data = battery_panel.getData();
		JSONObject motor_data = motor_panel.getData();
		device_data.put("battery_data", battery_data);
		device_data.put("motor_data", motor_data);
		return device_data;
	}

	public void updateRunTime() {
		time_panel.updateRunTime();
	}
}
