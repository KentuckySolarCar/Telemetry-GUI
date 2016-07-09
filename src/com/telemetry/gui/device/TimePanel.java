package com.telemetry.gui.device;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.swing.*;

public class TimePanel extends JPanel {
	private static final long serialVersionUID = -8596634937772865283L;
	private JLabel time_counter = new JLabel();
	private JLabel time_computer = new JLabel();
	private JLabel time_pi = new JLabel("VALUE");
	private JLabel time_stop_watch = new JLabel();
	private long initial_time;
	private int hour = 0;
	private int minute = 0;
	private int second = 0;
	private int stop_watch_hour = 0;
	private int stop_watch_minute = 0;
	private int stop_watch_second = 0;
	private boolean stop_watch_on = false;
	
	private DateFormat date_format = new SimpleDateFormat("HH:mm:ss");
	
	public TimePanel() {
		setLayout(new GridBagLayout());

		// Initialize time data
		Date date = new Date();
		initial_time = System.currentTimeMillis()/1000;
		time_counter.setText(hour + " H " + minute + " M " + second + " S ");
		time_stop_watch.setText(stop_watch_hour + " H " 
								+ stop_watch_minute + " M " 
								+ stop_watch_second + " S ");
		time_computer.setText(date_format.format(date));

		setLayout(new GridBagLayout());
		insertComponents();
	}
	
	private void insertComponents() {
		GridBagConstraints gbc = new GridBagConstraints();
	
		// GBC Constants
		gbc.insets = new Insets(3, 3, 3, 3);
		gbc.anchor = GridBagConstraints.CENTER;
		
		//-------------------------Title---------------------------//
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.gridwidth = 2;
		JLabel title = new JLabel("Time");
		title.setFont(DevicePanel.TITLE_FONT);
		add(title, gbc);
		
		// Resets gridwidth
		gbc.gridwidth = 1;
		
		//------------------------Labels----------------------------//
		gbc.gridx = 0;
		gbc.gridy = 1;
		JLabel run_time = new JLabel("Run Time:");
		run_time.setFont(DevicePanel.FIELD_FONT);
		add(run_time, gbc);
		
		gbc.gridx = 0;
		gbc.gridy = 2;
		JLabel computer_time = new JLabel("Computer Time:");
		computer_time.setFont(DevicePanel.FIELD_FONT);
		add(computer_time, gbc);
		
		gbc.gridx = 0;
		gbc.gridy = 3;
		JLabel pi_time = new JLabel("PI Time:");
		pi_time.setFont(DevicePanel.FIELD_FONT);
		add(pi_time, gbc);
		
		//-----------------------Fields-------------------------//
		gbc.gridx = 1;
		gbc.gridy = 1;
		time_counter.setFont(DevicePanel.FIELD_FONT);
		time_counter.setOpaque(true);
		time_counter.setBackground(Color.GREEN);
		add(time_counter, gbc);

		gbc.gridx = 1;
		gbc.gridy = 2;
		time_computer.setFont(DevicePanel.FIELD_FONT);
		time_computer.setOpaque(true);
		add(time_computer, gbc);

		gbc.gridx = 1;
		gbc.gridy = 3;
		time_pi.setFont(DevicePanel.FIELD_FONT);
		time_pi.setOpaque(true);
		time_pi.setBackground(Color.ORANGE);
	}
	
	public void updateRunTime() {
		Date date = new Date();
		
		long elapsed_time = System.currentTimeMillis()/1000 - initial_time;
		hour = (int) (elapsed_time / 3600);
		minute = (int) ((elapsed_time % 3600) / 60);
		second = (int) ((elapsed_time) % 60);
		
		time_computer.setText(date_format.format(date));
		time_counter.setText(hour + " H " + minute + " M " + second + " S ");
		
//		if(stop_watch_on) {
//			if(stop_watch_hour + stop_watch_minute + stop_watch_second == 0) {
//				time_stop_watch.setBackground(Color.ORANGE);
//			}
//			else {
//				time_stop_watch.setBackground(Color.RED);
//				second--;
//			}
//		}

		validate();
		repaint();
	}
	
	public void updatePanel(String pi_time) {
		List<String> parsed_string = Arrays.asList(pi_time.split(":"));

//		time_pi.setText(parsed_string.get(0) + " H " + parsed_string.get(1) + " M " + parsed_string.get(2) + " S ");
		
		validate();
		repaint();
	}
	
	public int[] getTime() {
		int time[] = {hour, minute, second};
		return time;
	}
	
//	public void setTimer(int seconds) {
//		time_computer.setBackground(color);
//	}
}
