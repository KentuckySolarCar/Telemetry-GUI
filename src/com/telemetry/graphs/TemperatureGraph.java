package com.telemetry.graphs;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.util.HashMap;

import javax.swing.JButton;
import javax.swing.JPanel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.title.LegendTitle;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.RectangleEdge;

public class TemperatureGraph extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7951816934572096963L;
	private JFreeChart temperature_chart;
	private ChartPanel temperature_panel;
//	private JPanel button_panel;
	private XYSeriesCollection temperature_dataset;
//	private XYSeries motor;
//	private XYSeries motor_controller;
	private XYSeries batt_temp_avg;
//	private XYSeries b_temp_max;
//	private XYSeries b_temp_min;
	
	public TemperatureGraph(int width, int height) {
		setSize(width, height);
		
		temperature_dataset = createTemperatureDataSet();
		
		temperature_chart = ChartFactory.createXYLineChart("Temperature", "Time (min)", "Degree (C)", temperature_dataset);
		LegendTitle legend = temperature_chart.getLegend();
		legend.setPosition(RectangleEdge.RIGHT);
		
		temperature_panel = new ChartPanel(temperature_chart);
		
		// Testing layouts
		/*button_panel = new JPanel();
		button_panel.setLayout(new GridLayout(5, 2));
		button_panel.add(new JButton("Bla"));
		button_panel.add(new JButton("Bla"));
		button_panel.add(new JButton("Bla"));
		button_panel.add(new JButton("Bla"));
		button_panel.add(new JButton("Bla"));*/
		
		// Use GridBagConstraints to be able to change size of grid elements
		this.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 0.5;
		c.weighty = 0.5;
		c.gridx = 0;
		c.gridy = 0;
		//c.ipadx = 200;
		c.ipady = 300;
		this.add(temperature_panel, c);
		
		/*c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 0;
		c.weighty = 0;
		c.gridx = 1;
		c.gridy = 0;
		this.add(button_panel, c);*/
	}
	
	private XYSeriesCollection createTemperatureDataSet() {
		//motor = new XYSeries("Motor");
		//motor_controller = new XYSeries("Motor Controller");
		batt_temp_avg = new XYSeries("Batman Ave");
		//b_temp_max = new XYSeries("Batman Max");
		//b_temp_min = new XYSeries("Batman Min");
		
		XYSeriesCollection temperature_dataset = new XYSeriesCollection();
		//temperature_dataset.addSeries(motor);
		//temperature_dataset.addSeries(motor_controller);
		temperature_dataset.addSeries(batt_temp_avg);
		//temperature_dataset.addSeries(b_temp_max);
		//temperature_dataset.addSeries(b_temp_min);
		
		return temperature_dataset;
	}
	
	public void updateDataSet(HashMap<String, Double> calculation_data) {
		//motor.add(calculation_data.get("time_elapsed"), calculation_data.get("motor_temp"));
		//motor_controller.add(calculation_data.get("time_elapsed"), calculation_data.get("motor_c_temp"));
		batt_temp_avg.add(calculation_data.get("time_elapsed"), calculation_data.get("batt_temp_avg"));
		//b_temp_max.add(calculation_data.get("time_elapsed"), calculation_data.get("b_temp_max"));
		//b_temp_min.add(calculation_data.get("time_elapsed"), calculation_data.get("b_temp_min"));
		temperature_panel.removeAll();
		temperature_panel.revalidate();
		temperature_chart = ChartFactory.createXYLineChart("Temperature", "Time (Min)", "Degree (C)", temperature_dataset);
		temperature_panel = new ChartPanel(temperature_chart);
		validate();
		repaint();
	}
}
