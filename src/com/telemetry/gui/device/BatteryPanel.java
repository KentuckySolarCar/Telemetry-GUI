 package com.telemetry.gui.device;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.*;

import org.json.simple.JSONObject;

import com.telemetry.custom.Tools;

public class BatteryPanel extends JPanel {
	private static final long serialVersionUID = 3458289157169322167L;
	
	private JLabel v_average_l = new JLabel("0000.000");
	private JLabel v_max_l = new JLabel("0000.000");
	private JLabel v_min_l = new JLabel("0000.000");
	private JLabel current_l = new JLabel("0000.000");
	private JLabel t_average_l = new JLabel("0000.000");
	private JLabel t_max_l = new JLabel("0000.000");
	private JLabel t_min_l = new JLabel("0000.000");
	private double ave_temp = 0D;
	private double max_temp = 0D;
	private double v_average = 0D;
	private double v_max = 0D;
	private double v_min = 0D;
	private double current_average = 0D;
	
	// Threshold for fields
	private double v_max_threshold = 9999999;
	private double v_min_threshold = 0;
	private double v_avg_threshold = 9999999;
	private double t_max_threshold = 9999999;
	private double t_min_threshold = 0;
	private double t_avg_threshold = 9999999;
	private double current_threshold = 9999999;
	
	private MotorPanel motor_panel;
	
	public BatteryPanel() {
		setLayout(new GridBagLayout());
		insertComponents();
	}
	
	public void updatePanel(HashMap<String, Double> data, int dummy) {
		v_average_l.setText(Tools.roundDouble(data.get("batt_volt_avg")));
		v_max_l.setText(Tools.roundDouble(data.get("batt_volt_max")));
		v_min_l.setText(Tools.roundDouble(data.get("batt_volt_min")));
		t_average_l.setText(Tools.roundDouble(data.get("batt_temp_avg")));
		t_max_l.setText(Tools.roundDouble(data.get("batt_temp_max")));
		t_min_l.setText(Tools.roundDouble(data.get("batt_temp_min")));
		current_l.setText(Tools.roundDouble(data.get("batt_current")));
	}
	
	public void updatePanel(JSONObject obj, String type) {
		if(type.equals("bat_temp")) {
			ave_temp = Double.parseDouble((String) obj.get("Tavg"));
			max_temp = Double.parseDouble((String) obj.get("Tmax"));
			double min_temp = Double.parseDouble((String) obj.get("Tmin"));
			
			if(ave_temp + max_temp + min_temp == 0) {
				t_average_l.setBackground(Color.CYAN);
				t_max_l.setBackground(Color.CYAN);
				t_min_l.setBackground(Color.CYAN);
				return;
			}

			Tools.thresholdCheck(t_average_l, ave_temp, t_avg_threshold, Tools.RED, Tools.GREEN);
			t_average_l.setText(Tools.roundDouble(ave_temp));
			
			Tools.thresholdCheck(t_max_l, max_temp, t_max_threshold, Tools.RED, Tools.GREEN);
			t_max_l.setText(Tools.roundDouble(max_temp));
			
			Tools.thresholdCheck(t_min_l, min_temp, t_min_threshold, Tools.GREEN, Tools.RED);
			t_min_l.setText(Tools.roundDouble(min_temp));
		}
		else if(type.equals("bat_volt")) {
			v_average       = Double.parseDouble((String) obj.get("Vavg")) / 10000;
			v_max           = Double.parseDouble((String) obj.get("Vmax")) / 10000;
			v_min           = Double.parseDouble((String) obj.get("Vmin")) / 10000;
			current_average = Double.parseDouble((String) obj.get("BC")) / 1000;
			
			if(v_average + v_max + v_min + current_average == 0) {
				v_average_l.setBackground(Color.CYAN);
				v_max_l.setBackground(Color.CYAN);
				v_min_l.setBackground(Color.CYAN);
				current_l.setBackground(Color.CYAN);
				return;
			}
		
			Tools.thresholdCheck(v_average_l, v_average, v_avg_threshold, Tools.RED, Tools.GREEN);
			v_average_l.setText(Tools.roundDouble(v_average));

			Tools.thresholdCheck(v_max_l, v_max, v_max_threshold, Tools.RED, Tools.GREEN);
			v_max_l.setText(Tools.roundDouble(v_max));

			Tools.thresholdCheck(v_min_l, v_min, v_min_threshold, Tools.GREEN, Tools.RED);
			v_min_l.setText(Tools.roundDouble(v_min));

			Tools.thresholdCheck(current_l, current_average, current_threshold, Tools.RED, Tools.GREEN);
			current_l.setText(Tools.roundDouble(current_average));
		}
		
		validate();
		repaint();
	}
	
	@SuppressWarnings("unchecked")
	public JSONObject getData() {
		JSONObject output_data = new JSONObject();
		
		output_data.put("ave_temp", new Double(ave_temp));
		output_data.put("max_temp", max_temp);
		output_data.put("v_average", new Double(v_average));
		output_data.put("v_min", new Double(v_min));
		output_data.put("v_max", new Double(v_max));
		output_data.put("current_average", new Double(current_average));
		
		return output_data;
	}
	
	private void insertComponents() {
		GridBagConstraints gbc = new GridBagConstraints();
		
		// GBC constants
		gbc.insets = new Insets(3, 3, 3, 3);
		gbc.anchor = GridBagConstraints.CENTER;

		//--------------------------Title--------------------------//
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.gridwidth = 8;
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
	
	public void setDependentPanel(MotorPanel motor_panel) {
		this.motor_panel = motor_panel;
	}
}