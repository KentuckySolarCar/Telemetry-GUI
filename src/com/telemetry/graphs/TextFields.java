package com.telemetry.graphs;

//import java.awt.LayoutManager;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
//import java.awt.GridLayout;

import javax.swing.*;

public class TextFields extends JPanel {
	private static final long serialVersionUID = 800851946880569758L;				
	private JPanel text_panel_powr;
	private JPanel text_panel_volt;
	private JPanel text_panel_temp;
	private JPanel text_panel_strat;
	private JTextArea average_motor_power_day			= new JTextArea("VALUE");
	private JTextArea average_speed_day					= new JTextArea("VALUE");
	private JTextArea battery_and_solar_range			= new JTextArea("VALUE");
	private JTextArea battery_and_solar_runtime_60_sec	= new JTextArea("VALUE");
	private JTextArea battery_charge_remaining			= new JTextArea("VALUE");
	private JTextArea battery_only_range_60_sec			= new JTextArea("VALUE");
	private JTextArea battery_only_runtime_60_sec		= new JTextArea("VALUE");
	private JTextArea battery_watt_hours				= new JTextArea("VALUE");
	private JTextArea distance_left_in_day				= new JTextArea("VALUE");
	private JTextArea motor_power_60_sec				= new JTextArea("VALUE");
	private JTextArea motor_watt_hours					= new JTextArea("VALUE");
	private JTextArea odometer							= new JTextArea("VALUE");
	private JTextArea solar_energy_remaining			= new JTextArea("VALUE");
	private JTextArea speed_60_sec						= new JTextArea("VALUE");
	private JTextArea target_speed						= new JTextArea("VALUE");
	private JTextArea target_watt_hour_per_mile			= new JTextArea("VALUE");
	private JTextArea target_watt_hour_per_mile_60_sec	= new JTextArea("VALUE");
	private JTextArea target_watt_hour_per_mile_day		= new JTextArea("VALUE");
	private JTextArea time_left_in_day					= new JTextArea("VALUE");
	
	public TextFields() {
		this.setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		
		text_panel_powr = new JPanel();
		text_panel_powr.setLayout(new GridBagLayout());
		text_panel_volt = new JPanel();
		text_panel_volt.setLayout(new GridBagLayout());
		text_panel_temp = new JPanel();
		text_panel_temp.setLayout(new GridBagLayout());
		text_panel_strat = new JPanel();
		text_panel_strat.setLayout(new GridBagLayout());
		
		/*
		 * POWER TEXT FIELDS
		 */

		gbc.fill = GridBagConstraints.BOTH;
		gbc.weightx = 0;
		gbc.weighty = 0;
		gbc.gridx = 0;
		gbc.gridy = 0;
		text_panel_powr.add(new JTextArea("Motor Watt Hours"), gbc);
		
		gbc.gridx = 0;
		gbc.gridy = 1;
		text_panel_powr.add(motor_watt_hours, gbc);
		
		gbc.gridheight = 2;
		gbc.gridx = 1;
		gbc.gridy = 0;
		text_panel_powr.add(new JButton("Reset"), gbc);
		
		gbc.gridheight = 1;
		gbc.gridx = 0;
		gbc.gridy = 2;
		text_panel_powr.add(new JTextArea("Battery Watt Hours"), gbc);
		
		gbc.gridx = 0;
		gbc.gridy = 3;
		text_panel_powr.add(battery_watt_hours, gbc);
		
		gbc.gridheight = 2;
		gbc.gridx = 1;
		gbc.gridy = 2;
		text_panel_powr.add(new JButton("Reset"), gbc);
		
		gbc.gridheight = 1;
		gbc.weightx = 0.5;
		gbc.weighty = 0.5;
		gbc.gridx = 0;
		gbc.gridy = 4;
		text_panel_powr.add(new JLabel("\n"), gbc);
		
		gbc.weightx = 0;
		gbc.weighty = 0;
		gbc.gridx = 0;
		gbc.gridy = 5;
		text_panel_powr.add(new JTextArea("60 Second Motor Power"), gbc);
		
		gbc.gridx = 0;
		gbc.gridy = 6;
		text_panel_powr.add(motor_power_60_sec, gbc);
		
		gbc.gridx = 0;
		gbc.gridy = 7;
		text_panel_powr.add(new JTextArea("Day's Avg Motor Power"), gbc);
		
		gbc.gridx = 0;
		gbc.gridy = 8;
		text_panel_powr.add(average_motor_power_day, gbc);
		
		gbc.weightx = 0.5;
		gbc.weighty = 0.5;
		gbc.gridx = 0;
		gbc.gridy = 9;
		text_panel_powr.add(new JLabel("\n"), gbc);
		
		gbc.weightx = 0;
		gbc.weighty = 0;
		gbc.gridx = 0;
		gbc.gridy = 10;
		text_panel_powr.add(new JTextArea("Target Speed"), gbc);
		
		gbc.gridx = 0;
		gbc.gridy = 11;
		text_panel_powr.add(target_speed, gbc);
		
		gbc.gridx = 0;
		gbc.gridy = 12;
		text_panel_powr.add(new JTextArea("60 Second Speed"), gbc);
		
		gbc.gridx = 0;
		gbc.gridy = 13;
		text_panel_powr.add(speed_60_sec, gbc);
		
		gbc.gridx = 0;
		gbc.gridy = 14;
		text_panel_powr.add(new JTextArea("Day's Average Speed"), gbc);
		
		gbc.gridx = 0;
		gbc.gridy = 15;
		text_panel_powr.add(average_speed_day, gbc);

		gbc.gridheight = 2;
		gbc.gridx = 1;
		gbc.gridy = 14;
		text_panel_powr.add(new JButton("Reset"), gbc);

		gbc.gridheight = 1;
		gbc.gridx = 0;
		gbc.gridy = 0;
		this.add(text_panel_powr, gbc);
		
		/*
		 * VOLTAGE TEXT FIELDS
		 */
		
		gbc.gridx = 0;
		gbc.gridy = 0;
		text_panel_volt.add(new JTextArea("Target WH/mile"), gbc);
		
		gbc.gridx = 0;
		gbc.gridy = 1;
		text_panel_volt.add(target_watt_hour_per_mile, gbc);
		
		gbc.gridx = 0;
		gbc.gridy = 2;
		text_panel_volt.add(new JTextArea("60 Sec Motor WH/mile"), gbc);
		
		gbc.gridx = 0;
		gbc.gridy = 3;
		text_panel_volt.add(target_watt_hour_per_mile_60_sec, gbc);
		
		gbc.gridx = 0;
		gbc.gridy = 4;
		text_panel_volt.add(new JTextArea("Days WH/mile"), gbc);
		
		gbc.gridx = 0;
		gbc.gridy = 5;
		text_panel_volt.add(target_watt_hour_per_mile_day, gbc);
		
		gbc.gridheight = 2;
		gbc.gridx = 1;
		gbc.gridy = 4;
		text_panel_volt.add(new JButton("Reset"), gbc);
		
		gbc.gridheight = 1;
		gbc.weightx = 0.5;
		gbc.weighty = 0.5;
		gbc.gridx = 0;
		gbc.gridy = 6;
		text_panel_volt.add(new JLabel("\n"), gbc);
		
		gbc.weightx = 0;
		gbc.weighty = 0;
		gbc.gridx = 0;
		gbc.gridy = 7;
		text_panel_volt.add(new JTextArea("Based on 60 sec motor power:"), gbc);
		
		gbc.gridx = 0;
		gbc.gridy = 8;
		text_panel_volt.add(new JTextArea("Battery Runtime"), gbc);
		
		gbc.gridx = 0;
		gbc.gridy = 9;
		text_panel_volt.add(battery_only_runtime_60_sec, gbc);
		
		gbc.gridx = 0;
		gbc.gridy = 10;
		text_panel_volt.add(new JTextArea("Battery Range"), gbc);
		
		gbc.gridx = 0;
		gbc.gridy = 11;
		text_panel_volt.add(battery_only_range_60_sec, gbc);
		
		gbc.gridx = 0;
		gbc.gridy = 12;
		text_panel_volt.add(new JTextArea("Battery + Solar Runtime"), gbc);
		
		gbc.gridx = 0;
		gbc.gridy = 13;
		text_panel_volt.add(battery_and_solar_runtime_60_sec, gbc);
		
		gbc.gridx = 0;
		gbc.gridy = 14;
		text_panel_volt.add(new JTextArea("Battery + Solar Range"), gbc);
		
		gbc.gridx = 0;
		gbc.gridy = 15;
		text_panel_volt.add(battery_and_solar_range, gbc);
		
		gbc.gridx = 1;
		gbc.gridy = 0;
		this.add(text_panel_volt, gbc);
		
		/*
		 * TEMPERATURE TEXT FIELDS
		 */
		
		gbc.gridx = 0;
		gbc.gridy = 0;
		text_panel_temp.add(new JTextArea("Odometer"), gbc);
		
		gbc.gridx = 0;
		gbc.gridy = 1;
		text_panel_temp.add(odometer, gbc);
		
		gbc.gridheight = 2;
		gbc.gridx = 1;
		gbc.gridy = 0;
		text_panel_temp.add(new JButton("Reset"), gbc);
		
		gbc.gridheight = 1;
		gbc.weightx = 0.5;
		gbc.weighty = 0.5;
		gbc.gridx = 0;
		gbc.gridy = 2;
		text_panel_temp.add(new JLabel("\n"), gbc);
		
		gbc.weightx = 0;
		gbc.weighty = 0;
		gbc.gridx = 0;
		gbc.gridy = 3;
		text_panel_temp.add(new JTextArea("Distance Left in Day"), gbc);
		
		gbc.gridx = 0;
		gbc.gridy = 4;
		text_panel_temp.add(distance_left_in_day, gbc);
		
		gbc.gridx = 0;
		gbc.gridy = 5;
		text_panel_temp.add(new JTextArea("Time Left in Day"), gbc);
		
		gbc.gridx = 0;
		gbc.gridy = 6;
		text_panel_temp.add(time_left_in_day, gbc);
		
		gbc.weightx = 0.5;
		gbc.weighty = 0.5;
		gbc.gridx = 0;
		gbc.gridy = 7;
		text_panel_temp.add(new JLabel("\n"), gbc);
		
		gbc.weightx = 0;
		gbc.weighty = 0;
		gbc.gridx = 0;
		gbc.gridy = 8;
		text_panel_temp.add(new JTextArea("Battery Charge Remaining"), gbc);
		
		gbc.gridx = 0;
		gbc.gridy = 9;
		text_panel_temp.add(battery_charge_remaining, gbc);
		
		gbc.gridx = 0;
		gbc.gridy = 10;
		text_panel_temp.add(new JTextArea("Energy Remaining in Day"), gbc);
		
		gbc.gridx = 0;
		gbc.gridy = 11;
		text_panel_temp.add(solar_energy_remaining, gbc);
		
		gbc.gridx = 2;
		gbc.gridy = 0;
		this.add(text_panel_temp, gbc);
		
		/*
		 * STRATEGY TEXT FIELDS
		 */
		
		gbc.gridx = 0;
		gbc.gridy = 0;
		text_panel_strat.add(new JTextArea("Input Solar Const A"), gbc);
		
		gbc.gridx = 0;
		gbc.gridy = 1;
		text_panel_strat.add(new JTextField(""), gbc);
		
		gbc.gridx = 0;
		gbc.gridy = 2;
		text_panel_strat.add(new JTextArea("Input Solar Const B"), gbc);
		
		gbc.gridx = 0;
		gbc.gridy = 3;
		text_panel_strat.add(new JTextField(""), gbc);
		
		gbc.gridx = 0;
		gbc.gridy = 4;
		text_panel_strat.add(new JTextArea("Input Solar Const C"), gbc);
		
		gbc.gridx = 0;
		gbc.gridy = 5;
		text_panel_strat.add(new JTextField(""), gbc);
		
		gbc.gridx = 0;
		gbc.gridy = 6;
		text_panel_strat.add(new JTextArea("Input Solar Const D"), gbc);
		
		gbc.gridx = 0;
		gbc.gridy = 7;
		text_panel_strat.add(new JTextField(""), gbc);
		
		gbc.gridx = 1;
		gbc.gridy = 1;
		text_panel_strat.add(new JTextArea("\"60 Second\" Interval (sec)"), gbc);
		
		gbc.gridx = 1;
		gbc.gridy = 2;
		text_panel_strat.add(new JButton("Recalculate Strategy"), gbc);
		
		gbc.gridx = 3;
		gbc.gridy = 0;
		this.add(text_panel_strat, gbc);	
	}
	
	public void updateDataSet(double[] data) {
//		average_motor_power_day				.setText(String.valueOf(data[0]));
		average_speed_day					.setText(String.valueOf(data[1]));
		battery_and_solar_range				.setText(String.valueOf(data[2]));
		battery_and_solar_runtime_60_sec	.setText(String.valueOf(data[3]));
		battery_charge_remaining			.setText(String.valueOf(data[4]));
		battery_only_range_60_sec			.setText(String.valueOf(data[5]));
		battery_only_runtime_60_sec			.setText(String.valueOf(data[6]));
		battery_watt_hours					.setText(String.valueOf(data[7]));
		distance_left_in_day				.setText(String.valueOf(data[8]));
		motor_power_60_sec					.setText(String.valueOf(data[9]));
		motor_watt_hours					.setText(String.valueOf(data[10]));
		odometer							.setText(String.valueOf(data[11]));
		solar_energy_remaining				.setText(String.valueOf(data[12]));
		speed_60_sec						.setText(String.valueOf(data[13]));
		target_speed						.setText(String.valueOf(data[14]));
		target_watt_hour_per_mile			.setText(String.valueOf(data[15]));
		target_watt_hour_per_mile_60_sec	.setText(String.valueOf(data[16]));
		target_watt_hour_per_mile_day		.setText(String.valueOf(data[17]));
		time_left_in_day					.setText(String.valueOf(data[18]));
	}
}