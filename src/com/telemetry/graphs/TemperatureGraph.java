package com.telemetry.graphs;

import java.awt.GridLayout;

import javax.swing.JPanel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

public class TemperatureGraph extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7951816934572096963L;
	private JFreeChart temperature_chart;
	private ChartPanel temperature_panel;
	private XYSeriesCollection temperature_dataset;
	private XYSeries motor;
	private XYSeries motor_controller;
	private XYSeries b_temp_max;
	private XYSeries b_temp_min;
	private XYSeries r_temp_max;
	private XYSeries r_temp_min;
	
	public TemperatureGraph() {
		temperature_dataset = createTemperatureDataSet();
		
		temperature_chart = ChartFactory.createXYLineChart("Temperature", "Time (min)", "Degree (C)", temperature_dataset);
	
		temperature_panel = new ChartPanel(temperature_chart);
		
		setLayout(new GridLayout(1, 1));
		
		add(temperature_panel);
	}
	
	private XYSeriesCollection createTemperatureDataSet() {
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
	
	/* Double array of temperatures format (indexes):
	 * 0: time in seconds
	 * 1: temp of motor
	 * 2: temp of motor_controller
	 * 3: temp of b_temp_max
	 * 4: temp of b_temp_min
	 * 5: temp of r_temp_max
	 * 6: temp of r_temp_min
	 */
	public void updateDataSet(double[] temps) {
		motor.add(temps[0], temps[1]);
		motor_controller.add(temps[0], temps[2]);
		b_temp_max.add(temps[0], temps[3]);
		b_temp_min.add(temps[0], temps[4]);
		r_temp_max.add(temps[0], temps[5]);
		r_temp_min.add(temps[0], temps[6]);
		temperature_panel.removeAll();
		temperature_panel.revalidate();
		temperature_chart = ChartFactory.createXYLineChart("Temperature", "Time (Min)", "Degree (C)", temperature_dataset);
		temperature_panel = new ChartPanel(temperature_chart);
		validate();
		repaint();
	}
}
