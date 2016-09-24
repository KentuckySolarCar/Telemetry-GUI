package com.telemetry.gui.device;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.*;

import com.telemetry.util.Tools;

public class BatteryPanel extends JPanel {
	private static final long serialVersionUID = 3458289157169322167L;
	
	private JLabel v_average_l = new JLabel("0000.000");
	private JLabel v_max_l     = new JLabel("0000.000");
	private JLabel v_min_l     = new JLabel("0000.000");
	private JLabel current_l   = new JLabel("0000.000");
	private JLabel t_average_l = new JLabel("0000.000");
	private JLabel t_max_l     = new JLabel("0000.000");
	private JLabel t_min_l     = new JLabel("0000.000");
	
	// Temp. thresholds for fields
	private double v_max_threshold   = 9999999;
	private double v_min_threshold   = 0;
	private double v_avg_threshold   = 9999999;
	private double t_max_threshold   = 9999999;
	private double t_min_threshold   = 0;
	private double t_avg_threshold   = 9999999;
	private double current_threshold = 9999999;
	
	public BatteryPanel() {
		setLayout(new GridBagLayout());
		insertComponents();
	}
	
	public void updatePanel(HashMap<String, Double> data) {
		v_average_l.setText(Tools.roundDouble(data.get("batt_volt_avg")));
		v_max_l.setText(Tools.roundDouble(data.get("batt_volt_max")));
		v_min_l.setText(Tools.roundDouble(data.get("batt_volt_min")));
		t_average_l.setText(Tools.roundDouble(data.get("batt_temp_avg")));
		t_max_l.setText(Tools.roundDouble(data.get("batt_temp_max")));
		t_min_l.setText(Tools.roundDouble(data.get("batt_temp_min")));
		current_l.setText(Tools.roundDouble(data.get("batt_current")));
		
		// Change label color if threshold reached
		Tools.thresholdCheck(v_average_l, data.get("batt_volt_avg"), 
							 v_avg_threshold, Tools.GREEN, Tools.RED);
		Tools.thresholdCheck(v_max_l, data.get("batt_volt_max"), 
							 v_max_threshold, Tools.GREEN, Tools.RED);
		Tools.thresholdCheck(v_min_l, data.get("batt_volt_min"), 
							 v_min_threshold, Tools.GREEN, Tools.RED);
		Tools.thresholdCheck(t_average_l, data.get("batt_temp_avg"), 
							 t_avg_threshold, Tools.RED, Tools.GREEN);
		Tools.thresholdCheck(t_max_l, data.get("batt_temp_max"), 
							 t_max_threshold, Tools.RED, Tools.GREEN);
		Tools.thresholdCheck(t_min_l, data.get("batt_temp_min"), 
							 t_min_threshold, Tools.RED, Tools.GREEN);
		Tools.thresholdCheck(current_l, data.get("batt_current"), 
							 current_threshold, Tools.RED, Tools.GREEN);
		validate();
		repaint();
	}
	
	private void insertComponents() {
		GridBagConstraints gbc = new GridBagConstraints();
		
		// GBC constants
		gbc.insets = new Insets(3, 3, 3, 3);
		gbc.anchor = GridBagConstraints.CENTER;

		//--------------------------Title--------------------------//
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.gridwidth = 3;
		JLabel title = new JLabel("Battery");
		title.setFont(Tools.TITLE_FONT);
		add(title, gbc);

		// Set the grid width back to 1
		gbc.gridwidth = 1;

		//----------------------Labels----------------------//
		ArrayList<JLabel> labels = new ArrayList<JLabel>();
		
		labels.add(new JLabel("Voltage Average:"));
		labels.add(new JLabel("Voltage Max:"));
		labels.add(new JLabel("Voltage Min:"));
		labels.add(new JLabel("Current:"));
		labels.add(new JLabel("Temp. Average:"));
		labels.add(new JLabel("Temp. Max:"));
		labels.add(new JLabel("Temp. Min:"));
		
		// Initial position
		gbc.gridx = 0;
		gbc.gridy = 2;
		
		for(JLabel label : labels) {
			label.setFont(Tools.FIELD_FONT);
			add(label, gbc);
			gbc.gridy++;
		}

		//----------------------Unit Labels----------------------//
		ArrayList<JLabel> units = new ArrayList<JLabel>();

		units.add(new JLabel(" V"));
		units.add(new JLabel(" V (#)"));
		units.add(new JLabel(" V (#)"));
		units.add(new JLabel(" A"));
		units.add(new JLabel(" C"));
		units.add(new JLabel(" C"));
		units.add(new JLabel(" C"));
		
		gbc.gridx = 2;
		gbc.gridy = 2;
		
		for(JLabel unit_label : units) {
			unit_label.setFont(Tools.FIELD_FONT);
			add(unit_label, gbc);
			gbc.gridy++;
		}

		//-------------------Fields--------------------//
		gbc.insets = new Insets(3, 30, 3, 30);
		
		gbc.gridx = 1;
		gbc.gridy = 2;
		v_average_l.setFont(Tools.FIELD_FONT);
		v_average_l.setOpaque(true);
		v_average_l.setBackground(Color.ORANGE);
		add(v_average_l, gbc);
		
		gbc.gridx = 1;
		gbc.gridy = 3;
		v_max_l.setFont(Tools.FIELD_FONT);
		v_max_l.setOpaque(true);
		v_max_l.setBackground(Color.ORANGE);
		add(v_max_l, gbc);
		
		gbc.gridx = 1;
		gbc.gridy = 4;
		v_min_l.setFont(Tools.FIELD_FONT);
		v_min_l.setOpaque(true);
		v_min_l.setBackground(Color.ORANGE);
		add(v_min_l, gbc);
		
		gbc.gridx = 1;
		gbc.gridy = 5;
		current_l.setFont(Tools.FIELD_FONT);
		current_l.setOpaque(true);
		current_l.setBackground(Color.ORANGE);
		add(current_l, gbc);
		
		gbc.gridx = 1;
		gbc.gridy = 6;
		t_average_l.setFont(Tools.FIELD_FONT);
		t_average_l.setOpaque(true);
		t_average_l.setBackground(Color.ORANGE);
		add(t_average_l, gbc);
		
		gbc.gridx = 1;
		gbc.gridy = 7;
		t_max_l.setFont(Tools.FIELD_FONT);
		t_max_l.setOpaque(true);
		t_max_l.setBackground(Color.ORANGE);
		add(t_max_l, gbc);
		
		gbc.gridx = 1;
		gbc.gridy = 8;
		t_min_l.setFont(Tools.FIELD_FONT);
		t_min_l.setOpaque(true);
		t_min_l.setBackground(Color.ORANGE);
		add(t_min_l, gbc);
	}
}