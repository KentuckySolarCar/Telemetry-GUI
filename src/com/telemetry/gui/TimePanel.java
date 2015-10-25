package com.telemetry.gui;

import java.awt.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TimePanel extends Panel {
	private Panel time_panel = new Panel();
	private Panel time_label_panel = new Panel();
	private Panel time_data_panel = new Panel();
	private Label time_counter = new Label();
	private Label time_current = new Label();
	private Label time_blank = new Label();
	
	DateFormat date_format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
	
	public TimePanel() {
		time_panel.setLayout(new GridLayout(1, 2));
		time_label_panel.setLayout(new GridLayout(3, 1, 10, 10));
		time_data_panel.setLayout(new GridLayout(3, 1, 10, 10));
		
		// Initialize time labels
		time_label_panel.add(new Label("Time"));
		time_label_panel.add(new Label("    Current time: "));
		time_label_panel.add(new Label("    Run time: "));
		
		time_panel.add(time_label_panel);
		
		// Initialize time data
		Date date = new Date();
		time_blank.setText(" ");
		time_counter.setText("0000");
		time_current.setText(date_format.format(date));
		
		time_data_panel.add(time_blank);
		time_data_panel.add(time_current);
		time_data_panel.add(time_counter);
		
		time_panel.add(time_data_panel);
	}
	
	public Panel getPanel() {
	//	time_panel.remove(time_data_panel);
		
		Date date = new Date();
		time_counter.setText("0000");
		time_current.setText(date_format.format(date));
		
	//	time_panel.add(time_data_panel);
		
		return time_panel;
	}
}
