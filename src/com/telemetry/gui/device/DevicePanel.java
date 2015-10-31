package com.telemetry.gui.device;

import java.awt.*;

public class DevicePanel extends Panel{
	
	private Panel device_panel = new Panel();
	private String separator = "----------------------------------------------------------------------------------------------------------------";
	
	MotorPanel motor_panel = new MotorPanel();
	MpptPanel mppt_panel = new MpptPanel();
	TimePanel time_panel = new TimePanel();
	BatteryPanel battery_panel = new BatteryPanel();
	
	GridBagConstraints gbc = new GridBagConstraints();
	
	public DevicePanel(int tab_panel_x, int tab_panel_y) {
		super();
		
		device_panel.setSize(tab_panel_x, tab_panel_y);
		device_panel.setLayout(new GridBagLayout());
		
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.gridx = 0;
		gbc.gridy = 0;
		device_panel.add(time_panel.getPanel(), gbc);
		
		gbc.gridx = 0;
		gbc.gridy = 1;
		device_panel.add(new Label(separator), gbc);
		
		gbc.gridx = 0;
		gbc.gridy = 2;
		device_panel.add(motor_panel.getPanel(), gbc);
		
		gbc.gridx = 0;
		gbc.gridy = 3;
		device_panel.add(new Label(separator), gbc);
	
		gbc.gridx = 0;
		gbc.gridy = 4;
		device_panel.add(mppt_panel.getPanel(), gbc);
		
		gbc.gridx = 0;
		gbc.gridy = 5;
		device_panel.add(new Label(separator), gbc);
		
		gbc.gridx = 0;
		gbc.gridy = 6;
		device_panel.add(battery_panel.getPanel(), gbc);
	}
	
	public Panel getPanel() {
		return device_panel;
	}
	
	public void updatePanel() {
		time_panel.updatePanel();
		motor_panel.updatePanel();
		mppt_panel.updatePanel();
		battery_panel.updatePanel();
	}
}
