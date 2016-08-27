 package com.telemetry.graphs;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.HashMap;

import javax.swing.*;

import com.telemetry.data.CarData;

public class GraphPanel extends JPanel {
	private static final long serialVersionUID = 6054350621681751868L;
	private TemperatureGraph temperature_graph;
	private VoltageGraph voltage_graph;
	private PowerGraph power_graph;
	private EnergyGraph energy_graph;
	
	public GraphPanel() {
		setLayout(new GridBagLayout());
		
		GridBagConstraints gbc = new GridBagConstraints();
		
		temperature_graph = new TemperatureGraph();
		voltage_graph = new VoltageGraph();
		power_graph = new PowerGraph();
		energy_graph = new EnergyGraph();
		

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

		gbc.gridx = 1;
		gbc.gridy = 0;
		add(temperature_graph, gbc);

		gbc.gridx = 1;
		gbc.gridy = 1;
		add(energy_graph, gbc);
	}
	
	public void updatePanel(CarData data) {
		HashMap<String, Double> calculation_data = data.getCalculationData();
		HashMap<String, Double> battery_data = data.getBatteryData();

		energy_graph		.updateDataSet(calculation_data);
		power_graph			.updateDataSet(calculation_data);
		temperature_graph	.updateDataSet(battery_data);
		voltage_graph		.updateDataSet(battery_data);

		validate();
		repaint();
	}
}
