package com.telemetry.gui;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import javax.swing.*; 
import org.jfree.chart.*;
import org.jfree.data.*;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.chart.util.*;

public class GraphPanel extends JPanel {
	private static JLabel speed_label = new JLabel("Speed (mph)");
	private static JLabel motor_current_label = new JLabel("Motor Current (A)");
	private static JLabel array_current_label = new JLabel("Array Current (A)");
	private static JLabel battery_voltage_label = new JLabel("Total Battery Voltage (V)");
	private static JFreeChart speed_chart;
	private static JFreeChart motor_current_chart;
	private static JFreeChart array_current_chart;
	private static JFreeChart battery_voltage_chart;
	
	public GraphPanel() {
		speed_chart = ChartFactory.createLineChart("Speed (mph)", "Time", "Miles Per Hour", createDataSet());
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
