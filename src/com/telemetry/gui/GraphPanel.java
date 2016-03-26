 package com.telemetry.gui;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import javax.swing.*; 
import org.jfree.chart.*;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.json.simple.JSONObject;

import com.telemetry.graphs.EnergyGraph;
import com.telemetry.graphs.PowerGraph;
import com.telemetry.graphs.TemperatureGraph;
import com.telemetry.graphs.VoltageGraph;

public class GraphPanel extends JPanel {
	private static final long serialVersionUID = 6054350621681751868L;
//	private JFreeChart speed_chart;
//	private JFreeChart motor_current_chart;
//	private JFreeChart array_current_chart;
//	private JFreeChart battery_voltage_chart;
//	private ChartPanel speed_panel;
//	private ChartPanel motor_current_panel;
//	private ChartPanel array_current_panel;
//	private ChartPanel battery_voltage_panel;
//	private XYDataset speed_dataset;
//	private XYDataset motor_current_dataset;
//	private XYDataset array_current_dataset;
//	private XYDataset battery_voltage_dataset;
//	private JTabbedPane tab_panel;
	private TemperatureGraph temperature_graph;
	private VoltageGraph voltage_graph;
	private PowerGraph power_graph;
	private EnergyGraph energy_graph;
	private JPanel button_panel_enrg;
	private JPanel button_panel_powr;
	private JPanel button_panel_temp;
	private JPanel button_panel_volt;
	
	public GraphPanel(int grid_panel_x, int grid_panel_y) {
		//this.setLayout(new GridLayout(3, 2));
		//this.setSize(grid_panel_x, grid_panel_y);
		this.setLayout(new GridBagLayout());;
		GridBagConstraints gbc = new GridBagConstraints();
		
		temperature_graph = new TemperatureGraph();
		voltage_graph = new VoltageGraph();
		power_graph = new PowerGraph();
		energy_graph = new EnergyGraph();
		
		button_panel_enrg = new JPanel();
		button_panel_enrg.setLayout(new GridLayout(5, 2));
		button_panel_enrg.add(new JButton("Bla"));
		button_panel_enrg.add(new JButton("Bla"));
		button_panel_enrg.add(new JButton("Bla"));
		button_panel_enrg.add(new JButton("Bla"));
		button_panel_enrg.add(new JButton("Bla"));
		
		button_panel_powr = new JPanel();
		button_panel_powr.setLayout(new GridLayout(5, 2));
		button_panel_powr.add(new JButton("Bla"));
		button_panel_powr.add(new JButton("Bla"));
		button_panel_powr.add(new JButton("Bla"));
		button_panel_powr.add(new JButton("Bla"));
		button_panel_powr.add(new JButton("Bla"));
		
		button_panel_temp = new JPanel();
		button_panel_temp.setLayout(new GridLayout(5, 10));
		button_panel_temp.add(new JButton("Bla"));
		button_panel_temp.add(new JButton("Bla"));
		button_panel_temp.add(new JButton("Bla"));
		button_panel_temp.add(new JButton("Bla"));
		button_panel_temp.add(new JButton("Bla"));
		button_panel_temp.add(new JButton("Bla"));
		button_panel_temp.add(new JButton("Bla"));
		button_panel_temp.add(new JButton("Bla"));
		button_panel_temp.add(new JButton("Bla"));
		button_panel_temp.add(new JButton("Bla"));
		
		button_panel_volt = new JPanel();
		button_panel_volt.setLayout(new GridLayout(5, 2));
		button_panel_volt.add(new JButton("Bla"));
		button_panel_volt.add(new JButton("Bla"));
		button_panel_volt.add(new JButton("Bla"));
		button_panel_volt.add(new JButton("Bla"));
		button_panel_volt.add(new JButton("Bla"));
		
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.weightx = 0.5;
		gbc.weighty = 0;
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.ipady = 0;
		this.add(temperature_graph, gbc);
		
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.weightx = 0;
		gbc.weighty = 0.5;
		gbc.gridx = 1;
		gbc.gridy = 0;
		gbc.ipady = 200;
		this.add(button_panel_temp, gbc);
		
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.weightx = 0.5;
		gbc.weighty = 0;
		gbc.gridx = 2;
		gbc.gridy = 0;
		gbc.ipady = 0;
		this.add(energy_graph, gbc);
		
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.weightx = 0;
		gbc.weighty = 0.5;
		gbc.gridx = 3;
		gbc.gridy = 0;
		gbc.ipady = 200;
		this.add(button_panel_enrg, gbc);
		
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.weightx = 0.5;
		gbc.weighty = 0;
		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.ipady = 0;
		this.add(voltage_graph, gbc);
		
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.weightx = 0;
		gbc.weighty = 0.5;
		gbc.gridx = 1;
		gbc.gridy = 1;
		gbc.ipady = 200;
		this.add(button_panel_volt, gbc);
		
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.weightx = 0.5;
		gbc.weighty = 0;
		gbc.gridx = 0;
		gbc.gridy = 2;
		gbc.ipady = 0;
		this.add(power_graph, gbc);
		
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.weightx = 0;
		gbc.weighty = 0.5;
		gbc.gridx = 1;
		gbc.gridy = 2;
		gbc.ipady = 200;
		this.add(button_panel_powr, gbc);
		
//		speed_chart           = ChartFactory.createXYLineChart("Speed (mph)", "Time", "Miles Per Hour", speed_dataset);
//		motor_current_chart   = ChartFactory.createXYLineChart("Motor Current (A)", "Time", "Amperage", motor_current_dataset);
//		array_current_chart   = ChartFactory.createXYLineChart("Array Current (A)", "Time", "Amperage", array_current_dataset);
//		battery_voltage_chart = ChartFactory.createXYLineChart("Total Battery Voltage (V)", "Time", "Voltage", battery_voltage_dataset);
		
//		speed_panel           = new ChartPanel(speed_chart);
//		motor_current_panel   = new ChartPanel(motor_current_chart);
//		array_current_panel   = new ChartPanel(array_current_chart);
//		battery_voltage_panel = new ChartPanel(battery_voltage_chart);
		
//		add(speed_panel);
//		add(motor_current_panel);
//		
//		add(array_current_panel);
//		add(battery_voltage_panel);
		
//		tab_panel = new JTabbedPane();
//		tab_panel.setPreferredSize(new Dimension(tab_panel_x, tab_panel_y));
		
//		tab_panel.add("Temperature", temperature_graph);
//		tab_panel.add("Voltage", voltage_graph);
//		tab_panel.add("Power", power_graph);
//		tab_panel.add("Energy", energy_graph);
		
		// Old layout with GridLayout and buttons inside each graph
		/*this.add(temperature_graph);
		this.add(energy_graph);
		this.add(voltage_graph);
		this.add(new JLabel(""));
		this.add(power_graph);*/
		
//		add(tab_panel);
	}
	
	public void updateGraphs(JSONObject data) {
		double[] energy = new double[10]; // needs to be expandable, number of laps
		double[] power = new double[7];
		double[] temps = new double[7];
		double[] volts = new double[9];
		
/*		dataset.put("array_power",                      array_power.getText());
		dataset.put("average_speed",                    average_speed.getText());
		dataset.put("battery_and_solar_range",          battery_and_solar_range.getText());
		dataset.put("battery_and_solar_runtime_60_sec", battery_and_solar_runtime_60_sec.getText());
		dataset.put("battery_charge_remaining",         battery_charge_remaining.getText());
		dataset.put("battery_only_range_60_sec",        battery_only_range_60_sec.getText());
		dataset.put("battery_only_runtime_60_sec",      battery_only_runtime_60_sec.getText());
		dataset.put("battery_watt_hours",               battery_watt_hours.getText());
		dataset.put("distance_left_in_day",             distance_left_in_day.getText());
		dataset.put("motor_power_60_sec",               motor_power_60_sec.getText());
		dataset.put("motor_watt_hours",                 motor_watt_hours.getText());
		dataset.put("predicted_array_power",            predicted_array_power.getText());
		dataset.put("solar_energy_remaining",           solar_energy_remaining.getText());
		dataset.put("speed_60_sec",                     speed_60_sec.getText());
		dataset.put("target_average_motor_power",       target_average_motor_power.getText());
		dataset.put("target_battery_state_of_charge",   target_battery_state_of_charge.getText());
		dataset.put("target_motor_energy",              target_motor_energy.getText());
		dataset.put("target_speed",                     target_speed.getText());
		dataset.put("target_watt_hour_per_mile",        target_watt_hour_per_mile.getText());
		dataset.put("target_watt_hour_per_mile_60_sec", target_watt_hour_per_mile_60_sec.getText());
		dataset.put("target_watt_hour_per_mile_day",    target_watt_hour_per_mile_day.getText());
		dataset.put("time_elapsed",                     time_elapsed.getText());
		dataset.put("time_left_in_day",                 time_left_in_day.getText());
*/

		temperature_graph.updateDataSet(temps);
		voltage_graph.updateDataSet(volts);
		power_graph.updateDataSet(power);
		energy_graph.updateDataSet(energy);
	}
	
//	public void updateSpeedDataSet(double mph, int h, int m, int s) {
//		speed_dataset.addValue(mph, "Miles Per Hour", h + ":" + m + ":" + s);
//		speed_panel.removeAll();
//		speed_panel.revalidate();
//		speed_chart = ChartFactory.createLineChart("Speed (mph)", "Time", "Miles Per Hour", speed_dataset);
//		speed_panel = new ChartPanel(speed_chart);
//		validate();
//		repaint();
//	}
//
//	public void updateMotorCurrentDataSet(double amp, int h, int m, int s) {
//		motor_current_dataset.addValue(amp, "Amperage", h + ":" + m + ":" + s);
//		motor_current_panel.removeAll();
//		motor_current_panel.revalidate();
//		motor_current_chart = ChartFactory.createLineChart("Amperage (stuff)", "Time", "Amperage", motor_current_dataset);
//		motor_current_panel = new ChartPanel(motor_current_chart);
//		validate();
//		repaint();
//	}
//	
//	public void updateArrayCurrentDataSet(double amp, int h, int m, int s) {
//		array_current_dataset.addValue(amp, "Amperage", h + ":" + m + ":" + s);
//		array_current_panel.removeAll();
//		array_current_panel.revalidate();
//		array_current_chart = ChartFactory.createLineChart("Amperage (stuff)", "Time", "Amperage", array_current_dataset);
//		array_current_panel = new ChartPanel(array_current_chart);
//		validate();
//		repaint();
//	}
//
//	public void updateBatteryVoltageDataSet(double v, int h, int m, int s) {
//		battery_voltage_dataset.addValue(v, "Voltage", h + ":" + m + ":" + s);
//		battery_voltage_panel.removeAll();
//		battery_voltage_panel.revalidate();
//		battery_voltage_chart = ChartFactory.createLineChart("Speed (mph)", "Time", "Miles Per Hour", speed_dataset);
//		battery_voltage_panel = new ChartPanel(battery_voltage_chart);
//		validate();
//		repaint();
//	}
	
//	private XYDataset createSpeedDataSet() {
//		speed_dataset = new XYDataset();
//		speed_dataset.addValue(15, "Miles Per Hour", "00:00:00");
//	}
//	
//	private XYDataset createMotorCurrentDataSet() {
//		motor_current_dataset = new DefaultCategoryDataset();
//		motor_current_dataset.addValue(15, "Amperage", "00:00:00");
//	}
//	
//	private XYDataset createArrayCurrentDataSet() {
//		array_current_dataset = new DefaultCategoryDataset();
//		array_current_dataset.addValue(15, "Amperage", "00:00:00");
//	}
//	
//	private XYDataset createBatteryVoltageDataSet() {
//		battery_voltage_dataset = new DefaultCategoryDataset();
//		battery_voltage_dataset.addValue(15, "Voltage", "00:00:00");
//	}
}
