package com.telemetry.gui.device;

import java.awt.GridLayout;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.swing.*;

public class TimePanel extends JPanel {
	private static final long serialVersionUID = -8596634937772865283L;
	private JPanel time_label_panel = new JPanel();
	private JPanel time_data_panel = new JPanel();
	private JLabel time_counter = new JLabel();
	private JLabel time_computer = new JLabel();
	private JLabel time_pi = new JLabel("VALUE");
	private JLabel time_blank = new JLabel();
	private long initial_time;
	private int hour = 0;
	private int minute = 0;
	private int second = 0;
	
	private DateFormat date_format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
	
	public TimePanel() {
		setLayout(new GridLayout(1, 2));
		time_label_panel.setLayout(new GridLayout(4, 1));
		time_data_panel.setLayout(new GridLayout(4, 1));
		
		// Initialize time labels
		time_label_panel.add(new JLabel("Time"));
		time_label_panel.add(new JLabel("    Run time: "));
		time_label_panel.add(new JLabel("    Computer time: "));
		time_label_panel.add(new JLabel("    PI time: "));
		
		add(time_label_panel);
		
		// Initialize time data
		Date date = new Date();
		time_blank.setText(" ");
		initial_time = System.currentTimeMillis()/1000;
		time_counter.setText(hour + " H " + minute + " M " + second + " S ");
		time_computer.setText(date_format.format(date));
		
		time_data_panel.add(time_blank);
		time_data_panel.add(time_counter);
		time_data_panel.add(time_computer);
		time_data_panel.add(time_pi);
		
		add(time_data_panel);
	}
	
	public void updateRunTime() {
		Date date = new Date();
		
		long elapsed_time = System.currentTimeMillis()/1000 - initial_time;
		hour = (int) (elapsed_time / 3600);
		minute = (int) ((elapsed_time % 3600) / 60);
		second = (int) ((elapsed_time) % 60);
		
		time_computer.setText(date_format.format(date));
		time_counter.setText(hour + " H " + minute + " M " + second + " S ");

		validate();
		repaint();
	}
	
	public void updatePanel(String pi_time) {
		List<String> parsed_string = Arrays.asList(pi_time.split(":"));

		time_pi.setText(parsed_string.get(0) + " H " + parsed_string.get(1) + " M " + parsed_string.get(2) + " S ");
		
		validate();
		repaint();
	}
	
	public int[] getTime() {
		int time[] = {hour, minute, second};
		return time;
	}
}
