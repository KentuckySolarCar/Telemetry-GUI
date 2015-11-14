package com.telemetry.gui;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import javax.swing.*; 
import org.jfree.chart.*;
import org.jfree.data.*;
import org.jfree.data.category.DefaultCategoryDataset;

public class GraphPanel extends JPanel {
	private static JFreeChart speed_chart;
	private static JFreeChart motor_current_chart;
	private static JFreeChart array_current_chart;
	private static JFreeChart battery_voltage_chart;
	
	public GraphPanel() {
		speed_chart = ChartFactory.createLineChart("Speed (mph)", "Time", "Miles Per Hour", createDataSet());
		motor_current_chart = ChartFactory.createLineChart("Motor Current (A)", "Time", "Amperage", createDataSet());
		array_current_chart = ChartFactory.createLineChart("Array Current (A)", "Time", "Amperage", createDataSet());
		battery_voltage_chart = ChartFactory.createLineChart("Total Battery Voltage (V)", "Time", "Voltage", createDataSet());
		
		new ChartPanel(speed_chart);
		new ChartPanel(motor_current_chart);
		new ChartPanel(array_current_chart);
		new ChartPanel(battery_voltage_chart);
	}
	
	private DefaultCategoryDataset createDataSet() {
		DefaultCategoryDataset dataset = new DefaultCategoryDataset();
		dataset.addValue(15, "Voltage", "00:00:00");
		dataset.addValue(30, "Voltage", "00:00:10");
		dataset.addValue(60, "Voltage", "00:00:20");
		dataset.addValue(120, "Voltage", "00:00:30");
		dataset.addValue(240, "Voltage", "00:00:40");
		dataset.addValue(300, "Voltage", "00:00:50");
		dataset.addValue(400, "Voltage", "00:01:00");
		dataset.addValue(500, "Voltage", "00:01:10");
		return dataset;
	}
}
