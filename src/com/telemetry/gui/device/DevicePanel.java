package com.telemetry.gui.device;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;

import javax.swing.*;

import org.json.simple.JSONObject;

public class DevicePanel extends JPanel{
	private static final long serialVersionUID = -7422627351609719543L;
	
	private MotorPanel motor_panel = new MotorPanel();
	private MpptPanel mppt_panel = new MpptPanel();
	private TimePanel time_panel = new TimePanel();
	private SpeedDialPanel speed_dial_panel = new SpeedDialPanel();
	private BatteryPanel battery_panel = new BatteryPanel();
	
	public DevicePanel(int tab_panel_x, int tab_panel_y) {
		super();
		
		setSize(tab_panel_x, tab_panel_y);
//		setBackground(new Color(191, 239, 255));
		
		JPanel time_speed_panel = new JPanel(new GridLayout(1, 3));
		time_speed_panel.add(time_panel);
		time_speed_panel.add(speed_dial_panel);
		
//		setLayout(new GridLayout(4, 1));
//		add(time_panel);
//		add(motor_panel);
//		add(mppt_panel);
//		add(battery_panel);

		setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		
		gbc.gridx = 0;
		gbc.gridy = 0;
		add(time_panel, gbc);
		
		gbc.gridx = 0;
		gbc.gridy = 1;
		add(motor_panel, gbc);
		
		gbc.gridx = 1;
		gbc.gridy = 0;
		gbc.gridheight = 2;
		gbc.gridwidth = 2;
		add(speed_dial_panel, gbc);
		
		gbc.fill = GridBagConstraints.HORIZONTAL;
//		gbc.ipady = 10;
		gbc.gridx = 0;
		gbc.gridy = 2;
		gbc.gridwidth = 3;
		add(new JSeparator(SwingConstants.HORIZONTAL), gbc);
		
		gbc.gridwidth = 3;
		gbc.gridx = 0;
		gbc.gridy = 3;
		add(mppt_panel, gbc);
		
		gbc.ipady = 10;
		gbc.gridwidth = 3;
		gbc.gridx = 0;
		gbc.gridy = 4;
//		add(new JSeparator(SwingConstants.HORIZONTAL), gbc);
		add(new JLabel(""));

		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.gridwidth = 3;
		gbc.gridx = 0;
		gbc.gridy = 5;
		add(battery_panel, gbc);
	}
	
	public void updatePanel(JSONObject obj, String type) {
		time_panel.updatePanel((String) obj.get("Time"));
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
	
	public int[] getTime() {
		return time_panel.getTime();
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
