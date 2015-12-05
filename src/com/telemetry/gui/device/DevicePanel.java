package com.telemetry.gui.device;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import javax.swing.*;

import org.json.simple.JSONObject;

public class DevicePanel extends JPanel{
	private static final long serialVersionUID = -7422627351609719543L;
	private String separator = "----------------------------------------------------------------------------------------------------------------";
	
	MotorPanel motor_panel = new MotorPanel();
	MpptPanel mppt_panel = new MpptPanel();
	TimePanel time_panel = new TimePanel();
	BatteryPanel battery_panel = new BatteryPanel();
	
	GridBagConstraints gbc = new GridBagConstraints();
	
	public DevicePanel(int tab_panel_x, int tab_panel_y) {
		super();
		
		setSize(tab_panel_x, tab_panel_y);
		setLayout(new GridBagLayout());
		
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.gridx = 0;
		gbc.gridy = 0;
		add(time_panel, gbc);
		
		gbc.gridx = 0;
		gbc.gridy = 1;
		add(new JLabel(separator), gbc);
		
		gbc.gridx = 0;
		gbc.gridy = 2;
		add(motor_panel, gbc);
		
		gbc.gridx = 0;
		gbc.gridy = 3;
		add(new JLabel(separator), gbc);
	
		gbc.gridx = 0;
		gbc.gridy = 4;
		add(mppt_panel, gbc);
		
		gbc.gridx = 0;
		gbc.gridy = 5;
		add(new JLabel(separator), gbc);
		
		gbc.gridx = 0;
		gbc.gridy = 6;
		add(battery_panel, gbc);
	}
	
	public void updatePanel(JSONObject data, String type) {
		time_panel.updatePanel();
		switch(type) {
		case "motor": {
			motor_panel.updatePanel(data);
			break;
		}
		case "bat_volt": 
		case "bat_temp": {
			battery_panel.updatePanel(data);
			break;
		}
		default:
			break;
		}
		
		validate();
		repaint();
	}
}
