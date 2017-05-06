package com.telemetry.gui.device;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import javax.swing.*;

import com.telemetry.util.Tools;

public class TimePanel extends JPanel {
	private static final long serialVersionUID = -8596634937772865283L;
	private JLabel time_counter_l = new JLabel();
	private JLabel time_computer_l = new JLabel();
	private JLabel time_pi_l = new JLabel("VALUE");
	private long initial_time;
	private int hour = 0;
	private int minute = 0;
	private int second = 0;
	
	private DateFormat date_format = new SimpleDateFormat("HH:mm:ss");
	
	public TimePanel() {
		setLayout(new GridBagLayout());

		// Initialize time data
		Date date = new Date();
		initial_time = System.currentTimeMillis()/1000;
		time_counter_l.setText(hour + " H " + minute + " M " + second + " S ");
		time_computer_l.setText(date_format.format(date));
		time_pi_l.setText("VALUE");

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
		title.setFont(Tools.TITLE_FONT);
		add(title, gbc);
		
		// Resets gridwidth
		gbc.gridwidth = 1;
		
		//------------------------Labels----------------------------//
		gbc.anchor = GridBagConstraints.WEST;
		gbc.gridx = 0;
		gbc.gridy = 1;
		JLabel run_time = new JLabel("Run Time:");
		run_time.setFont(Tools.FIELD_FONT);
		add(run_time, gbc);
		
		gbc.gridx = 0;
		gbc.gridy = 2;
		JLabel computer_time = new JLabel("Computer Time:");
		computer_time.setFont(Tools.FIELD_FONT);
		add(computer_time, gbc);
		
		gbc.gridx = 0;
		gbc.gridy = 3;
		JLabel pi_time = new JLabel("PI Time:");
		pi_time.setFont(Tools.FIELD_FONT);
		add(pi_time, gbc);
		
		//-----------------------Fields-------------------------//
		gbc.anchor = GridBagConstraints.CENTER;
		gbc.gridx = 1;
		gbc.gridy = 1;
		time_counter_l.setFont(Tools.FIELD_FONT);
		add(time_counter_l, gbc);

		gbc.gridx = 1;
		gbc.gridy = 2;
		time_computer_l.setFont(Tools.FIELD_FONT);
		add(time_computer_l, gbc);

		gbc.gridx = 1;
		gbc.gridy = 3;
		time_pi_l.setFont(Tools.FIELD_FONT);
		add(time_pi_l, gbc);
	}
	
	/**
	 * Called by a ticking thread to update system and elapsed time
	 */
	public void updateRunTime() {
		Date date = new Date();
		
		long elapsed_time = System.currentTimeMillis()/1000 - initial_time;
		hour = (int) (elapsed_time / 3600);
		minute = (int) ((elapsed_time % 3600) / 60);
		second = (int) ((elapsed_time) % 60);
		
		time_computer_l.setText(date_format.format(date));
		time_counter_l.setText(hour + " H " + minute + " M " + second + " S ");
	}
	
	/**
	 * This only update the time received from the pi
	 * @param time_data
	 */
	public void updatePanel(HashMap<String, Integer[]> time_data) {
		Integer[] pi_time = time_data.get("pi_time");
		time_pi_l.setText(pi_time[0] + ":" + pi_time[1] + ":" + pi_time[2]);
	}
}
