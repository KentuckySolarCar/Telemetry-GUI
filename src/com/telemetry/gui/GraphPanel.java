package com.telemetry.gui;

import java.awt.GridLayout;
import javax.swing.*; 
import org.jfree.chart.*;
import org.jfree.data.category.DefaultCategoryDataset;

public class GraphPanel extends JPanel {
	private static JFreeChart speed_chart;
	private static JFreeChart motor_current_chart;
	private static JFreeChart array_current_chart;
	private static JFreeChart battery_voltage_chart;
	private static ChartPanel speed_panel;
	private static ChartPanel motor_current_panel;
	private static ChartPanel array_current_panel;
	private static ChartPanel battery_voltage_panel;
	
	public GraphPanel(int tab_panel_x, int tab_panel_y) {
		setSize(tab_panel_x, tab_panel_y);
		
		speed_chart           = ChartFactory.createLineChart("Speed (mph)", "Time", "Miles Per Hour", createSpeedDataSet());
		motor_current_chart   = ChartFactory.createLineChart("Motor Current (A)", "Time", "Amperage", createMotorCurrentDataSet());
		array_current_chart   = ChartFactory.createLineChart("Array Current (A)", "Time", "Amperage", createArrayCurrentDataSet());
		battery_voltage_chart = ChartFactory.createLineChart("Total Battery Voltage (V)", "Time", "Voltage", createBatteryVoltageDataSet());
		
		speed_panel           = new ChartPanel(speed_chart);
		motor_current_panel   = new ChartPanel(motor_current_chart);
		array_current_panel   = new ChartPanel(array_current_chart);
		battery_voltage_panel = new ChartPanel(battery_voltage_chart);
		
		setLayout(new GridLayout(2, 2));
		//add(new JLabel("Speed (mph)"));
		//add(new JLabel("Motor Current (A)"));
		add(speed_panel);
		add(motor_current_panel);
		//add(new JLabel("Array Current (A)"));
		//add(new JLabel("Total Battery Voltage (V)"));
		add(array_current_panel);
		add(battery_voltage_panel);
	}
	
	private DefaultCategoryDataset createSpeedDataSet() {
		DefaultCategoryDataset dataset = new DefaultCategoryDataset();
		dataset.addValue(15, "Miles Per Hour", "00:00:00");
		dataset.addValue(30, "Miles Per Hour", "00:00:10");
		dataset.addValue(60, "Miles Per Hour", "00:00:20");
		dataset.addValue(120, "Miles Per Hour", "00:00:30");
		dataset.addValue(240, "Miles Per Hour", "00:00:40");
		dataset.addValue(300, "Miles Per Hour", "00:00:50");
		dataset.addValue(400, "Miles Per Hour", "00:01:00");
		dataset.addValue(500, "Miles Per Hour", "00:01:10");
		return dataset;
	}
	
	private DefaultCategoryDataset createMotorCurrentDataSet() {
		DefaultCategoryDataset dataset = new DefaultCategoryDataset();
		dataset.addValue(15, "Amperage", "00:00:00");
		dataset.addValue(30, "Amperage", "00:00:10");
		dataset.addValue(60, "Amperage", "00:00:20");
		dataset.addValue(120, "Amperage", "00:00:30");
		dataset.addValue(240, "Amperage", "00:00:40");
		dataset.addValue(300, "Amperage", "00:00:50");
		dataset.addValue(400, "Amperage", "00:01:00");
		dataset.addValue(500, "Amperage", "00:01:10");
		return dataset;
	}
	
	private DefaultCategoryDataset createArrayCurrentDataSet() {
		DefaultCategoryDataset dataset = new DefaultCategoryDataset();
		dataset.addValue(15, "Amperage", "00:00:00");
		dataset.addValue(30, "Amperage", "00:00:10");
		dataset.addValue(60, "Amperage", "00:00:20");
		dataset.addValue(120, "Amperage", "00:00:30");
		dataset.addValue(240, "Amperage", "00:00:40");
		dataset.addValue(300, "Amperage", "00:00:50");
		dataset.addValue(400, "Amperage", "00:01:00");
		dataset.addValue(500, "Amperage", "00:01:10");
		return dataset;
	}
	
	private DefaultCategoryDataset createBatteryVoltageDataSet() {
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
