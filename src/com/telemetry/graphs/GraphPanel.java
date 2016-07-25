 package com.telemetry.graphs;

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.HashMap;

import javax.swing.*;
import org.json.simple.JSONObject;

import com.telemetry.gui.CalculationPanel;

public class GraphPanel extends JPanel {
	private static final long serialVersionUID = 6054350621681751868L;
	private TemperatureGraph temperature_graph;
	private VoltageGraph voltage_graph;
	private PowerGraph power_graph;
	private EnergyGraph energy_graph;
	private TextFields text_field;
	
	static final Font TITLE_FONT = new Font("Consolas", Font.BOLD, 16);
	static final Font FIELD_FONT = new Font("Consolas", Font.PLAIN, 14);
	
	public GraphPanel(CalculationPanel calculation_panel) {
		setLayout(new GridBagLayout());
		
		GridBagConstraints gbc = new GridBagConstraints();
		
		temperature_graph = new TemperatureGraph(getWidth()/2, getHeight()/3*2);
		voltage_graph = new VoltageGraph(getWidth()/2, getHeight()/3*2);
		power_graph = new PowerGraph(getWidth()/2, getHeight()/3*2);
		energy_graph = new EnergyGraph(getWidth()/2, getHeight()/3*2);
		text_field = new TextFields(getWidth()/2, getHeight()/3*2);
		

		gbc.fill = GridBagConstraints.BOTH;
		gbc.weightx = 1;
		gbc.weighty = 1;
		gbc.gridwidth = 1;
		gbc.gridheight = 1;
		
		gbc.gridx = 0;
		gbc.gridy = 0;
		add(power_graph, gbc);

		gbc.gridx = 0;
		gbc.gridy = 1;
		add(voltage_graph, gbc);

		gbc.gridx = 0;
		gbc.gridy = 2;
		add(temperature_graph, gbc);

		gbc.gridx = 1;
		gbc.gridy = 0;
		add(energy_graph, gbc);
		
		gbc.gridx = 1;
		gbc.gridy = 1;
		gbc.gridheight = 2;
		add(text_field, gbc);
		gbc.gridheight = 1;
	}
	
	/**
	 * TODO @John This function will be automatically called when calculation receives
	 * 		new data, just make use of the input hash table to update any values necessary.
	 * 
	 * Keys for the hash table:
	 * 		array_power
	 *  	average_speed
	 *  	battery_and_solar_range
	 *  	battery_and_solar_runtime_60_sec
	 *  	battery_charge_remaining
	 *  	battery_only_range_60_sec
	 *  	battery_only_runtime_60_sec
	 *  	battery_watt_hours
	 *  	distance_left_in_day
	 *  	motor_power_60_sec
	 *  	motor_watt_hours
	 *  	predicted_array_power
	 *  	solar_energy_remaining
	 *  	speed_60_sec
	 *  	target_average_motor_power
	 *  	target_battery_state_of_charge
	 *  	target_motor_energy
	 *  	target_speed
	 *  	target_watt_hour_per_mile
	 *  	target_watt_hour_per_mile_60_sec
	 *  	target_watt_hour_per_mile_day
	 *  	time_elapsed
	 *  	time_left_in_day
	 *   	motor_current
	 *   	batt_current
	 *   	batt_temp_avg
	 *   	batt_v_avg
	 *   	batt_v_min
	 *  
	 * @see CalculationPanel -> getData() for more info on input data
	 *  	
	 * @param calculation_data
	 */
	public void updatePanel(HashMap<String, Double> calculation_data) {
		energy_graph		.updateDataSet(calculation_data);
		power_graph			.updateDataSet(calculation_data);
		temperature_graph	.updateDataSet(calculation_data);
		voltage_graph		.updateDataSet(calculation_data);
		text_field			.updateDataSet(calculation_data);

		validate();
		repaint();
	}
}
