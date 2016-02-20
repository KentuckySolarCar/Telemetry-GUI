package com.telemetry.gui;

import java.awt.GridLayout;
import javax.swing.*; 
import org.jfree.chart.*;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

public class GraphPanel extends JPanel {
	private static final long serialVersionUID = 6054350621681751868L;
	private JFreeChart speed_chart;
	private JFreeChart motor_current_chart;
	private JFreeChart array_current_chart;
	private JFreeChart battery_voltage_chart;
	private JFreeChart temperature_chart;
	private ChartPanel speed_panel;
	private ChartPanel motor_current_panel;
	private ChartPanel array_current_panel;
	private ChartPanel battery_voltage_panel;
	private ChartPanel temperature_panel;
	private XYDataset speed_dataset;
	private XYDataset motor_current_dataset;
	private XYDataset array_current_dataset;
	private XYDataset battery_voltage_dataset;
	private XYDataset temperature_dataset;
	
	private XYSeries motor;
	private XYSeries motor_controller;
	private XYSeries b_temp_max;
	private XYSeries b_temp_min;
	private XYSeries r_temp_max;
	private XYSeries r_temp_min;
	
	public GraphPanel(int tab_panel_x, int tab_panel_y) {
		setSize(tab_panel_x, tab_panel_y);
		
		temperature_dataset = createTemperatureDataSet();
		
//		speed_chart           = ChartFactory.createXYLineChart("Speed (mph)", "Time", "Miles Per Hour", speed_dataset);
//		motor_current_chart   = ChartFactory.createXYLineChart("Motor Current (A)", "Time", "Amperage", motor_current_dataset);
//		array_current_chart   = ChartFactory.createXYLineChart("Array Current (A)", "Time", "Amperage", array_current_dataset);
//		battery_voltage_chart = ChartFactory.createXYLineChart("Total Battery Voltage (V)", "Time", "Voltage", battery_voltage_dataset);
		
		temperature_chart = ChartFactory.createXYLineChart("Temperature", "Time(min)", "Degree(C)", temperature_dataset);
		
//		speed_panel           = new ChartPanel(speed_chart);
//		motor_current_panel   = new ChartPanel(motor_current_chart);
//		array_current_panel   = new ChartPanel(array_current_chart);
//		battery_voltage_panel = new ChartPanel(battery_voltage_chart);
		
		temperature_panel = new ChartPanel(temperature_chart);
		
		setLayout(new GridLayout(1, 1));
		
//		add(speed_panel);
//		add(motor_current_panel);
//		
//		add(array_current_panel);
//		add(battery_voltage_panel);
		
		add(temperature_panel);
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
	
	private XYDataset createTemperatureDataSet() {
		motor = new XYSeries("Motor");
		motor_controller = new XYSeries("Motor Controller");
		b_temp_max = new XYSeries("Batman Max");
		b_temp_min = new XYSeries("Batman Min");
		r_temp_max = new XYSeries("Robin Max");
		r_temp_min = new XYSeries("Robin Min");
		
		XYSeriesCollection temperature_dataset = new XYSeriesCollection();
		temperature_dataset.addSeries(motor);
		temperature_dataset.addSeries(motor_controller);
		temperature_dataset.addSeries(b_temp_max);
		temperature_dataset.addSeries(b_temp_min);
		temperature_dataset.addSeries(r_temp_max);
		temperature_dataset.addSeries(r_temp_min);
		
		return temperature_dataset;
	}
	
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
