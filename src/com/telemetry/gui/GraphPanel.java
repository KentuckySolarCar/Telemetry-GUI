 package com.telemetry.gui;

import java.awt.Dimension;
import java.awt.GridLayout;
import javax.swing.*; 
import org.jfree.chart.*;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

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
	private JTabbedPane tab_panel;
	private TemperatureGraph temperature_graph;
	private VoltageGraph voltage_graph;
	private PowerGraph power_graph;
	private EnergyGraph energy_graph;
	
	public GraphPanel(int tab_panel_x, int tab_panel_y) {
		setSize(tab_panel_x, tab_panel_y);
		
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
		
		tab_panel = new JTabbedPane();
		tab_panel.setPreferredSize(new Dimension(tab_panel_x, tab_panel_y));
		temperature_graph = new TemperatureGraph();
		voltage_graph = new VoltageGraph();
		power_graph = new PowerGraph();
		energy_graph = new EnergyGraph();
		
		tab_panel.add("Temperature", temperature_graph);
		tab_panel.add("Voltage", voltage_graph);
		tab_panel.add("Power", power_graph);
		tab_panel.add("Energy", energy_graph);
		
		add(tab_panel);
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
