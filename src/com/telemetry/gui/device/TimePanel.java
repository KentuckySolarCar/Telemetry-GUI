package com.telemetry.gui.device;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
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
	
	private DateFormat date_format = new SimpleDateFormat("HH:mm:ss");
	
	public TimePanel() {
		// Initialize time data
		Date date = new Date();
		time_blank.setText(" ");
		initial_time = System.currentTimeMillis()/1000;
		time_counter.setText(hour + " H " + minute + " M " + second + " S ");
		time_computer.setText(date_format.format(date));

		setLayout(new GridBagLayout());
		insertComponents();
	}
	
	private void insertComponents() {
		GridBagConstraints gbc = new GridBagConstraints();
		
		// GBC constants
		gbc.insets = new Insets(3, 3, 3, 3);
		gbc.anchor = GridBagConstraints.CENTER;

		//---------------------------------Title-----------------------------------//
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.gridwidth = 2;
		JLabel time_title = new JLabel("Time");
		time_title.setFont(DevicePanel.TITLE_FONT);
		add(time_title, gbc);
		
		// Reset gridwidth
		gbc.gridwidth = 1;
		
		//---------------------------------Labels--------------------------------------//
		gbc.gridx = 0;
		gbc.gridy = 1;
		JLabel run_time_l = new JLabel("Run Time:");
		run_time_l.setFont(DevicePanel.FIELD_FONT);
		add(run_time_l, gbc);

		gbc.gridx = 0;
		gbc.gridy = 2;
		JLabel pc_time_l = new JLabel("Computer Time:");
		pc_time_l.setFont(DevicePanel.FIELD_FONT);
		add(pc_time_l, gbc);

		gbc.gridx = 0;
		gbc.gridy = 3;
		JLabel pi_time_l = new JLabel("Pi Time:");
		pi_time_l.setFont(DevicePanel.FIELD_FONT);
		add(pi_time_l, gbc);

		//---------------------------------Fields--------------------------------------//
		gbc.gridx = 1;
		gbc.gridy = 1;
		time_counter.setOpaque(true);
		time_counter.setBackground(Color.ORANGE);
		time_counter.setFont(DevicePanel.FIELD_FONT);
		add(time_counter, gbc);

		gbc.gridx = 1;
		gbc.gridy = 1;
		time_computer.setOpaque(true);
		time_computer.setBackground(Color.ORANGE);
		time_computer.setFont(DevicePanel.FIELD_FONT);
		add(time_computer, gbc);

		gbc.gridx = 1;
		gbc.gridy = 1;
		time_pi.setOpaque(true);
		time_pi.setBackground(Color.ORANGE);
		time_pi.setFont(DevicePanel.FIELD_FONT);
		add(time_pi, gbc);
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
