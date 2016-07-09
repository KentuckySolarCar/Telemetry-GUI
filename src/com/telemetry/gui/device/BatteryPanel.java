 package com.telemetry.gui.device;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.*;

import org.json.simple.JSONObject;

import com.telemetry.custom.Tools;

public class BatteryPanel extends JPanel {
	private static final long serialVersionUID = 3458289157169322167L;
	
	private JLabel v_average_l = new JLabel("0000.000");
	private JLabel v_max_l = new JLabel("0000.000");
	private JLabel v_global_max_l = new JLabel("0000.000");
	private JLabel v_min_l = new JLabel("0000.000");
	private JLabel v_global_min_l = new JLabel("0000.000");
	private JLabel current_l = new JLabel("0000.000");
	private JLabel t_average_l = new JLabel("0000.000");
	private JLabel t_max_l = new JLabel("0000.000");
	private JLabel t_min_l = new JLabel("0000.000");
	private double ave_temp = 0;
	private double v_average = 0;
	private double v_max = 0;
	private double v_global_max = 0;
	private double v_min = 0;
	private double v_global_min = 999999;
	private double current_average = 0;
	private JButton v_global_max_reset = new JButton("Reset");
	private JButton v_global_min_reset = new JButton("Reset");
	
	// Threshold for fields
	private double v_max_threshold = 9999999;
	private double v_min_threshold = 0;
	private double v_avg_threshold = 9999999;
	private double t_max_threshold = 9999999;
	private double t_min_threshold = 0;
	private double t_avg_threshold = 9999999;
	private double current_threshold = 9999999;
	
	public BatteryPanel() {
		setLayout(new GridBagLayout());

		insertComponents();
	}
	
	public void updatePanel(JSONObject obj, String type) {
		if(type.equals("bat_temp")) {
			ave_temp = Double.parseDouble((String) obj.get("Tavg"));
			if(ave_temp > t_avg_threshold)
				t_average_l.setBackground(Color.RED);
			else
				t_average_l.setBackground(Color.GREEN);
			t_average_l.setText(Tools.roundDouble((String) obj.get("Tavg")));
			
			if(Double.parseDouble((String) obj.get("Tmax")) > t_max_threshold)
				t_max_l.setBackground(Color.RED);
			else
				t_max_l.setBackground(Color.GREEN);
			t_max_l.setText(Tools.roundDouble((String) obj.get("Tmax")));
			
			if(Double.parseDouble((String) obj.get("Tmin")) < t_min_threshold)
				t_min_l.setBackground(Color.RED);
			else
				t_min_l.setBackground(Color.GREEN);
			t_min_l.setText(Tools.roundDouble((String) obj.get("Tmin")));
		}
		else if(type.equals("bat_volt")) {
			v_average       = Double.parseDouble((String) obj.get("Vavg"));
			v_max           = Double.parseDouble((String) obj.get("Vmax"));
			v_min           = Double.parseDouble((String) obj.get("Vmin"));
			current_average = Double.parseDouble((String) obj.get("BC"));
		
			if(v_global_max < v_max ) {
				v_global_max = v_max;
				if(v_global_max > v_max_threshold)
					v_global_max_l.setBackground(Color.RED);
				else
					v_global_max_l.setBackground(Color.GREEN);
				v_global_max_l.setText(Tools.roundDouble((String) obj.get("Vmax")));
			}

			if(v_global_min > v_min ) {
				v_global_min = v_min;
				if(v_global_min < v_min_threshold)
					v_global_min_l.setBackground(Color.RED);
				else
					v_global_min_l.setBackground(Color.GREEN);
				v_global_min_l.setText(Tools.roundDouble((String) obj.get("Vmin")));
			}

			if(v_average > v_avg_threshold)
				v_average_l.setBackground(Color.RED);
			else
				v_average_l.setBackground(Color.GREEN);
			v_average_l.setText(Tools.roundDouble((String) obj.get("Vavg")));

			if(v_max > v_max_threshold)
				v_max_l.setBackground(Color.RED);
			else
				v_max_l.setBackground(Color.GREEN);
			v_max_l.setText(Tools.roundDouble((String) obj.get("Vmax")));

			if(v_min < v_min_threshold)
				v_min_l.setBackground(Color.RED);
			else
				v_min_l.setBackground(Color.GREEN);
			v_min_l.setText(Tools.roundDouble((String) obj.get("Vmin")));

			if(current_average > current_threshold)
				current_l.setBackground(Color.RED);
			else
				current_l.setBackground(Color.GREEN);
			current_l.setText(Tools.roundDouble((String) obj.get("BC")));
		}
		
		validate();
		repaint();
	}
	
	@SuppressWarnings("unchecked")
	public JSONObject getData() {
		JSONObject output_data = new JSONObject();
		
		output_data.put("ave_temp", new Double(ave_temp));
		output_data.put("v_average", new Double(v_average));
		output_data.put("v_min", new Double(v_min));
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
		title.setFont(DevicePanel.TITLE_FONT);
		add(title, gbc);

		// Set the grid width back to 1
		gbc.gridwidth = 1;

		//----------------------Labels----------------------//
		ArrayList<JLabel> labels = new ArrayList<JLabel>();
		
		labels.add(new JLabel("Voltage Average:"));
		labels.add(new JLabel("Voltage Max:"));
		labels.add(new JLabel("Voltage Min:"));
		labels.add(new JLabel("Global Voltage Max:"));
		labels.add(new JLabel("Global Voltage Min:"));
		labels.add(new JLabel("Current:"));
		labels.add(new JLabel("Temp. Average:"));
		labels.add(new JLabel("Temp. Max:"));
		labels.add(new JLabel("Temp. Min:"));
		
		// Initial position
		gbc.gridx = 0;
		gbc.gridy = 2;
		
		for(JLabel label : labels) {
			label.setFont(DevicePanel.FIELD_FONT);
			add(label, gbc);
			gbc.gridy++;
		}

		//----------------------Unit Labels----------------------//
		ArrayList<JLabel> units = new ArrayList<JLabel>();

		units.add(new JLabel(" V"));
		units.add(new JLabel(" V (#)"));
		units.add(new JLabel(" V (#)"));
		units.add(new JLabel(" V (#)"));
		units.add(new JLabel(" V (#)"));
		units.add(new JLabel(" A"));
		units.add(new JLabel(" C"));
		units.add(new JLabel(" C"));
		units.add(new JLabel(" C"));
		
		gbc.gridx = 2;
		gbc.gridy = 2;
		
		for(JLabel unit_label : units) {
			unit_label.setFont(DevicePanel.FIELD_FONT);
			add(unit_label, gbc);
			gbc.gridy++;
		}

		//-------------------Fields--------------------//
		gbc.insets = new Insets(3, 30, 3, 30);
		
		gbc.gridx = 1;
		gbc.gridy = 2;
		v_average_l.setFont(DevicePanel.FIELD_FONT);
		v_average_l.setOpaque(true);
		v_average_l.setBackground(Color.ORANGE);
		add(v_average_l, gbc);
		
		gbc.gridx = 1;
		gbc.gridy = 3;
		v_max_l.setFont(DevicePanel.FIELD_FONT);
		v_max_l.setOpaque(true);
		v_max_l.setBackground(Color.ORANGE);
		add(v_max_l, gbc);
		
		gbc.gridx = 1;
		gbc.gridy = 4;
		v_min_l.setFont(DevicePanel.FIELD_FONT);
		v_min_l.setOpaque(true);
		v_min_l.setBackground(Color.ORANGE);
		add(v_min_l, gbc);
		
		gbc.gridx = 1;
		gbc.gridy = 5;
		v_global_max_l.setFont(DevicePanel.FIELD_FONT);
		v_global_max_l.setOpaque(true);
		v_global_max_l.setBackground(Color.ORANGE);
		v_global_max_l.setPreferredSize(v_global_max_l.getMinimumSize());
		v_global_max_l.setMaximumSize(v_global_max_l.getMinimumSize());
		add(v_global_max_l, gbc);
		
		gbc.gridx = 1;
		gbc.gridy = 6;
		v_global_min_l.setFont(DevicePanel.FIELD_FONT);
		v_global_min_l.setOpaque(true);
		v_global_min_l.setBackground(Color.ORANGE);
		v_global_min_l.setPreferredSize(v_global_min_l.getMinimumSize());
		v_global_min_l.setMaximumSize(v_global_min_l.getMinimumSize());
		add(v_global_min_l, gbc);
		
		gbc.gridx = 1;
		gbc.gridy = 7;
		current_l.setFont(DevicePanel.FIELD_FONT);
		current_l.setOpaque(true);
		current_l.setBackground(Color.ORANGE);
		add(current_l, gbc);
		
		gbc.gridx = 1;
		gbc.gridy = 8;
		t_average_l.setFont(DevicePanel.FIELD_FONT);
		t_average_l.setOpaque(true);
		t_average_l.setBackground(Color.ORANGE);
		add(t_average_l, gbc);
		
		gbc.gridx = 1;
		gbc.gridy = 9;
		t_max_l.setFont(DevicePanel.FIELD_FONT);
		t_max_l.setOpaque(true);
		t_max_l.setBackground(Color.ORANGE);
		add(t_max_l, gbc);
		
		gbc.gridx = 1;
		gbc.gridy = 10;
		t_min_l.setFont(DevicePanel.FIELD_FONT);
		t_min_l.setOpaque(true);
		t_min_l.setBackground(Color.ORANGE);
		add(t_min_l, gbc);
		
		//-------------------Reset Buttons--------------------//
		gbc.insets = new Insets(3, 10, 3, 10);
		
		gbc.gridx = 3;
		gbc.gridy = 5;
		v_global_max_reset.setBackground(Color.GRAY);
		v_global_max_reset.addActionListener(new GlobalMaxVoltageReset());
		add(v_global_max_reset, gbc);
		
		gbc.gridx = 3;
		gbc.gridy = 6;
		v_global_min_reset.setBackground(Color.GRAY);
		v_global_min_reset.addActionListener(new GlobalMinVoltageReset());
		add(v_global_min_reset, gbc);
		
		// Reset Insets
		gbc.insets = new Insets(3, 3, 3, 3);
	}

	class GlobalMaxVoltageReset implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			v_global_max = 0;
			v_global_max_l.setText(Double.toString(v_global_max));
		}
	}

	class GlobalMinVoltageReset implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			v_global_min = 9999;
			v_global_min_l.setText(Double.toString(v_global_min));
		}
	}
}