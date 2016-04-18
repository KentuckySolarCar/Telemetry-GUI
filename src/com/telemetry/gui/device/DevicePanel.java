package com.telemetry.gui.device;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;

import javax.swing.*;

import org.json.simple.JSONObject;

public class DevicePanel extends JPanel{
	private static final long serialVersionUID = -7422627351609719543L;
	private String separator = "----------------------------------------------------------------------------------------------------------------";
	
	MotorPanel motor_panel = new MotorPanel();
	MpptPanel mppt_panel = new MpptPanel();
	TimePanel time_panel = new TimePanel();
	BatteryPanel battery_panel = new BatteryPanel();
	
	public DevicePanel(int tab_panel_x, int tab_panel_y) {
		super();
		
		setSize(tab_panel_x, tab_panel_y);
		setLayout(new GridLayout(7, 1, 10, 10));
		
		add(time_panel);
		add(new JSeparator(SwingConstants.HORIZONTAL));
		add(motor_panel);
		add(new JSeparator(SwingConstants.HORIZONTAL));
		add(mppt_panel);
		add(new JSeparator(SwingConstants.HORIZONTAL));
		add(battery_panel);
	}
	
	public void updatePanel(JSONObject obj, String type) {
		time_panel.updatePanel((String) obj.get("Time"));
		switch(type) {
		case "motor": {
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
