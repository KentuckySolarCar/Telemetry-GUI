package com.telemetry.gui.device;

import java.awt.GridLayout;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.*;

public class TimePanel extends JPanel {
	private static final long serialVersionUID = -8596634937772865283L;
	private JPanel time_label_panel = new JPanel();
	private JPanel time_data_panel = new JPanel();
	private JLabel time_counter = new JLabel();
	private JLabel time_current = new JLabel();
	private JLabel time_blank = new JLabel();
	private long initial_time;
	private int hour = 0;
	private int minute = 0;
	private int second = 0;
	
	private DateFormat date_format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
	
	public TimePanel() {
		setLayout(new GridLayout(1, 2));
		time_label_panel.setLayout(new GridLayout(3, 1, 10, 10));
		time_data_panel.setLayout(new GridLayout(3, 1, 10, 10));
		
		// Initialize time labels
		time_label_panel.add(new JLabel("Time"));
		time_label_panel.add(new JLabel("    Current time: "));
		time_label_panel.add(new JLabel("    Run time: "));
		
		add(time_label_panel);
		
		// Initialize time data
		Date date = new Date();
		time_blank.setText(" ");
		initial_time = System.currentTimeMillis()/1000;
		time_counter.setText(hour + " H " + minute + " M " + second + " S ");
		time_current.setText(date_format.format(date));
		
		time_data_panel.add(time_blank);
		time_data_panel.add(time_current);
		time_data_panel.add(time_counter);
		
		add(time_data_panel);
	}
	
	public void updatePanel() {
		Date date = new Date();
		long elapsed_time = System.currentTimeMillis()/1000 - initial_time;
		hour = (int) (elapsed_time / 3600);
		minute = (int) ((elapsed_time % 3600) / 60);
		second = (int) ((elapsed_time) % 60);
		time_counter.setText(hour + " H " + minute + " M " + second + " S ");
		time_current.setText(date_format.format(date));
		
		validate();
		repaint();
	}
	
	public int[] getTime() {
		int time[] = {hour, minute, second};
		return time;
	}
}
