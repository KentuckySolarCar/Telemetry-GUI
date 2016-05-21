 package com.telemetry.graphs;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import javax.swing.*;
import org.jfree.chart.*;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.json.simple.JSONObject;

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
	private TextFields text_field;
//	private JPanel button_panel_enrg;
//	private JPanel button_panel_powr;
//	private JPanel button_panel_temp;
//	private JPanel button_panel_volt;

	static final Font TITLE_FONT = new Font("Consolas", Font.BOLD, 16);
	static final Font FIELD_FONT = new Font("Consolas", Font.PLAIN, 15);
	
	public GraphPanel(int grid_panel_x, int grid_panel_y) {
		//this.setLayout(new GridLayout(3, 2));
		//this.setSize(grid_panel_x, grid_panel_y);
		this.setLayout(new GridBagLayout());;
		GridBagConstraints gbc = new GridBagConstraints();
		
		temperature_graph = new TemperatureGraph();
		voltage_graph = new VoltageGraph();
		power_graph = new PowerGraph();
		energy_graph = new EnergyGraph();
		
		text_field = new TextFields();
		
		/*button_panel_enrg = new JPanel();
		button_panel_enrg.setLayout(new GridLayout(5, 5));
		button_panel_enrg.add(new JButton("Bla"));
		button_panel_enrg.add(new JButton("Bla"));
		button_panel_enrg.add(new JButton("Bla"));
		button_panel_enrg.add(new JButton("Bla"));
		button_panel_enrg.add(new JButton("Bla"));
		
		button_panel_powr = new JPanel();
		button_panel_powr.setLayout(new GridLayout(7, 14));
		button_panel_powr.add(new JTextArea("Motor Watt Hours\nTesting"));
		button_panel_powr.add(new JButton("Reset"));
		button_panel_powr.add(new JTextArea("Battery Watt Hours\nTesting"));
		button_panel_powr.add(new JButton("Reset"));
		button_panel_powr.add(new JTextArea("60 Second Motor Power\nTesting"));
		button_panel_powr.add(new JLabel(""));
		button_panel_powr.add(new JTextArea("Day's Avg Motor Power\nTesting"));
		button_panel_powr.add(new JLabel(""));
		button_panel_powr.add(new JTextArea("Target Speed\nTesting"));
		button_panel_powr.add(new JLabel(""));
		button_panel_powr.add(new JTextArea("60 Second Speed\nTesting"));
		button_panel_powr.add(new JLabel(""));
		button_panel_powr.add(new JTextArea("Day's Average Speed\nTesting"));
		button_panel_powr.add(new JButton("Reset"));
		
		button_panel_temp = new JPanel();
		button_panel_temp.setLayout(new GridLayout(6, 12));
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
		button_panel_temp.add(new JButton("Bla"));
		button_panel_temp.add(new JButton("Bla"));
		
		button_panel_volt = new JPanel();
		button_panel_volt.setLayout(new GridLayout(6, 12));
		button_panel_volt.add(new JButton("Bla"));
		button_panel_volt.add(new JButton("Bla"));
		button_panel_volt.add(new JButton("Bla"));
		button_panel_volt.add(new JButton("Bla"));
		button_panel_volt.add(new JButton("Bla"));
		button_panel_volt.add(new JButton("Bla"));
		button_panel_volt.add(new JButton("Bla"));
		button_panel_volt.add(new JButton("Bla"));
		button_panel_volt.add(new JButton("Bla"));
		button_panel_volt.add(new JButton("Bla"));
		button_panel_volt.add(new JButton("Bla"));
		button_panel_volt.add(new JButton("Bla"));*/
		
		gbc.fill = GridBagConstraints.BOTH;
		gbc.weightx = 2;
		gbc.weighty = 0;
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.ipadx = 0;
		gbc.ipady = 0;
		this.add(power_graph, gbc);
		
		/*gbc.fill = GridBagConstraints.BOTH;
		gbc.weightx = 0;
		gbc.weighty = 0.5;
		gbc.gridx = 1;
		gbc.gridy = 0;
		gbc.ipadx = 0;
		gbc.ipady = 90;
		this.add(button_panel_powr, gbc);*/
		
		gbc.fill = GridBagConstraints.BOTH;
		gbc.weightx = 0.1;
		gbc.weighty = 0;
		gbc.gridx = 2;
		gbc.gridy = 0;
		gbc.ipadx = 0;
		gbc.ipady = 0;
		this.add(energy_graph, gbc);
		
		/*gbc.fill = GridBagConstraints.BOTH;
		gbc.weightx = 0;
		gbc.weighty = 0.5;
		gbc.gridx = 3;
		gbc.gridy = 0;
		gbc.ipady = 200;
		this.add(button_panel_enrg, gbc);*/
		
		gbc.fill = GridBagConstraints.BOTH;
		gbc.weightx = 2;
		gbc.weighty = 0;
		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.ipady = 0;
		this.add(voltage_graph, gbc);
		
		/*gbc.fill = GridBagConstraints.BOTH;
		gbc.weightx = 0;
		gbc.weighty = 0.5;
		gbc.gridx = 1;
		gbc.gridy = 1;
		gbc.ipady = 90;
		this.add(button_panel_volt, gbc);*/
		
		gbc.fill = GridBagConstraints.BOTH;
		gbc.weightx = 0.1;
		gbc.weighty = 0;
		gbc.gridx = 2;
		gbc.gridy = 1;
		gbc.ipady = 0;
		this.add(text_field, gbc);
		
		gbc.fill = GridBagConstraints.BOTH;
		gbc.weightx = 2;
		gbc.weighty = 0;
		gbc.gridx = 0;
		gbc.gridy = 2;
		gbc.ipady = 0;
		this.add(temperature_graph, gbc);
		
		/*gbc.fill = GridBagConstraints.BOTH;
		gbc.weightx = 0;
		gbc.weighty = 0.5;
		gbc.gridx = 1;
		gbc.gridy = 2;
		gbc.ipady = 90;
		this.add(button_panel_temp, gbc);*/
		
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
		double[] text = new double[19];
		double[] volts = new double[9];
		
		/*
		 * ENERGY GRAPH DATA
		 */
		
		energy[0]	= (double) ((JSONObject) data.get("calculation_data")).get("time_elapsed");
//		energy[1]	= (double) ((JSONObject) ((JSONObject) data.get("device_data")).get("motor_data")).get("lap1");
		
		/*
		 * POWER GRAPH DATA
		 */
		
		power[0]	= (double) ((JSONObject) data.get("calculation_data")).get("time_elapsed");
//		power[1]	= (double) ((JSONObject) data.get("calculation_data")).get("motor_power");
//		power[2]	= (double) ((JSONObject) data.get("calculation_data")).get("array_power");
//		power[3]	= (double) ((JSONObject) data.get("calculation_data")).get("tracker1_power");
//		power[4]	= (double) ((JSONObject) data.get("calculation_data")).get("tracker2_power");
//		power[5]	= (double) ((JSONObject) data.get("calculation_data")).get("tracker3_power");
//		power[6]	= (double) ((JSONObject) data.get("calculation_data")).get("tracker4_power");		
		
		/*
		 * TEMPERATURE GRAPH DATA
		 */
		
		temps[0]	= (double) ((JSONObject) data.get("calculation_data")).get("time_elapsed");
//		temps[1]	= (double) ((JSONObject) ((JSONObject) data.get("device_data")).get("motor_data")).get("temperature");
//		temps[2]	= (double) ((JSONObject) ((JSONObject) data.get("device_data")).get("motor_data")).get("temperature");
//		temps[3]	= (double) ((JSONObject) ((JSONObject) ((JSONObject) data.get("device_data")).get("battery_data")).get("batman")).get("t_max");
//		temps[4]	= (double) ((JSONObject) ((JSONObject) ((JSONObject) data.get("device_data")).get("battery_data")).get("batman")).get("t_min");
//		temps[5]	= (double) ((JSONObject) ((JSONObject) ((JSONObject) data.get("device_data")).get("battery_data")).get("robin")).get("t_max");
//		temps[6]	= (double) ((JSONObject) ((JSONObject) ((JSONObject) data.get("device_data")).get("battery_data")).get("robin")).get("t_min");
		
		/*
		 * TEXT FIELD DATA
		 */
		
//		text[0]		= (double) ((JSONObject) data.get("calculation_data")).get("average_motor_power_day");
		text[1]		= (double) ((JSONObject) data.get("calculation_data")).get("average_speed");
		text[2]		= (double) ((JSONObject) data.get("calculation_data")).get("battery_and_solar_range");
		text[3]		= (double) ((JSONObject) data.get("calculation_data")).get("battery_and_solar_runtime_60_sec");
		text[4]		= (double) ((JSONObject) data.get("calculation_data")).get("battery_charge_remaining");
		text[5]		= (double) ((JSONObject) data.get("calculation_data")).get("battery_only_range_60_sec");
		text[6]		= (double) ((JSONObject) data.get("calculation_data")).get("battery_only_runtime_60_sec");
		text[7]		= (double) ((JSONObject) data.get("calculation_data")).get("battery_watt_hours");
		text[8]		= (double) ((JSONObject) data.get("calculation_data")).get("distance_left_in_day");
		text[9]		= (double) ((JSONObject) data.get("calculation_data")).get("motor_power_60_sec");
		text[10]	= (double) ((JSONObject) data.get("calculation_data")).get("motor_watt_hours");
		text[11]	= (double) ((JSONObject) ((JSONObject) data.get("device_data")).get("motor_data")).get("odometer");
		text[12]	= (double) ((JSONObject) data.get("calculation_data")).get("solar_energy_remaining");
		text[13]	= (double) ((JSONObject) data.get("calculation_data")).get("speed_60_sec");
		text[14]	= (double) ((JSONObject) data.get("calculation_data")).get("target_speed");
		text[15]	= (double) ((JSONObject) data.get("calculation_data")).get("target_watt_hour_per_mile");
		text[16]	= (double) ((JSONObject) data.get("calculation_data")).get("target_watt_hour_per_mile_60_sec");
		text[17]	= (double) ((JSONObject) data.get("calculation_data")).get("target_watt_hour_per_mile_day");
		text[18]	= (double) ((JSONObject) data.get("calculation_data")).get("time_left_in_day");
		
		/*
		 * VOLTAGE GRAPH DATA
		 */
		
		volts[0]	= (double) ((JSONObject) data.get("calculation_data")).get("time_elapsed");
//		volts[1]	= (double) ((JSONObject) (((JSONObject) ((JSONObject) data.get("device_data")).get("battery_data")).get("batman"))).get("v_max");
//		volts[2]	= (double) ((JSONObject) (((JSONObject) ((JSONObject) data.get("device_data")).get("battery_data")).get("batman"))).get("v_min");
//		volts[3]	= (double) ((JSONObject) (((JSONObject) ((JSONObject) data.get("device_data")).get("battery_data")).get("batman"))).get("v_average");
//		volts[4]	= (double) ((JSONObject) (((JSONObject) ((JSONObject) data.get("device_data")).get("battery_data")).get("batman"))).get("v_std_dev");
//		volts[5]	= (double) ((JSONObject) (((JSONObject) ((JSONObject) data.get("device_data")).get("battery_data")).get("robin"))).get("v_max");
//		volts[6]	= (double) ((JSONObject) (((JSONObject) ((JSONObject) data.get("device_data")).get("battery_data")).get("robin"))).get("v_min");
//		volts[7]	= (double) ((JSONObject) (((JSONObject) ((JSONObject) data.get("device_data")).get("battery_data")).get("robin"))).get("v_average");
//		volts[8]	= (double) ((JSONObject) (((JSONObject) ((JSONObject) data.get("device_data")).get("battery_data")).get("robin"))).get("v_std_dev");
		
		energy_graph		.updateDataSet(energy);
		power_graph			.updateDataSet(power);
		temperature_graph	.updateDataSet(temps);
		text_field			.updateDataSet(text);
		voltage_graph		.updateDataSet(volts);
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
