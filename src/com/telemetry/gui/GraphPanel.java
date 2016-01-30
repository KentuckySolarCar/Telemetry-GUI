package com.telemetry.gui;

import java.awt.GridLayout;
import javax.swing.*; 
import org.jfree.chart.*;
import org.jfree.data.category.DefaultCategoryDataset;

public class GraphPanel extends JPanel {
	private static final long serialVersionUID = 6054350621681751868L;
	private JFreeChart speed_chart;
	private JFreeChart motor_current_chart;
	private JFreeChart array_current_chart;
	private JFreeChart battery_voltage_chart;
	private ChartPanel speed_panel;
	private ChartPanel motor_current_panel;
	private ChartPanel array_current_panel;
	private ChartPanel battery_voltage_panel;
	private DefaultCategoryDataset speed_dataset;
	private DefaultCategoryDataset motor_current_dataset;
	private DefaultCategoryDataset array_current_dataset;
	private DefaultCategoryDataset battery_voltage_dataset;
	
	public GraphPanel(int tab_panel_x, int tab_panel_y) {
		setSize(tab_panel_x, tab_panel_y);
		
		createSpeedDataSet();
		createMotorCurrentDataSet();
		createArrayCurrentDataSet();
		createBatteryVoltageDataSet();
		speed_chart           = ChartFactory.createLineChart("Speed (mph)", "Time", "Miles Per Hour", speed_dataset);
		motor_current_chart   = ChartFactory.createLineChart("Motor Current (A)", "Time", "Amperage", motor_current_dataset);
		array_current_chart   = ChartFactory.createLineChart("Array Current (A)", "Time", "Amperage", array_current_dataset);
		battery_voltage_chart = ChartFactory.createLineChart("Total Battery Voltage (V)", "Time", "Voltage", battery_voltage_dataset);
		
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
	
	public void updateSpeedDataSet(double mph, int h, int m, int s) {
		speed_dataset.addValue(mph, "Miles Per Hour", h + ":" + m + ":" + s);
		speed_panel.removeAll();
		speed_panel.revalidate();
		speed_chart = ChartFactory.createLineChart("Speed (mph)", "Time", "Miles Per Hour", speed_dataset);
		speed_panel = new ChartPanel(speed_chart);
		validate();
		repaint();
	}

	public void updateMotorCurrentDataSet(double amp, int h, int m, int s) {
		
	}
	
	public void updateArrayCurrentDataSet(double amp, int h, int m, int s) {
		
	}

	public void updateBatteryVoltageDataSet(double v, int h, int m, int s) {
	
	}
	
	private void createSpeedDataSet() {
		speed_dataset = new DefaultCategoryDataset();
		speed_dataset.addValue(15, "Miles Per Hour", "00:00:00");
	}
	
	private void createMotorCurrentDataSet() {
		motor_current_dataset = new DefaultCategoryDataset();
		motor_current_dataset.addValue(15, "Amperage", "00:00:00");
	}
	
	private void createArrayCurrentDataSet() {
		array_current_dataset = new DefaultCategoryDataset();
		array_current_dataset.addValue(15, "Amperage", "00:00:00");
	}
	
	private void createBatteryVoltageDataSet() {
		battery_voltage_dataset = new DefaultCategoryDataset();
		battery_voltage_dataset.addValue(15, "Voltage", "00:00:00");
	}
}
