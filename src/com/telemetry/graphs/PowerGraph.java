package com.telemetry.graphs;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.util.HashMap;

import javax.swing.JPanel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.title.LegendTitle;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.RectangleEdge;

public class PowerGraph extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3219450170241833378L;
	private JFreeChart power_chart;
	private ChartPanel power_panel;
	private XYSeriesCollection power_dataset;
	private XYSeries motor;
	private XYSeries array;
	private XYSeries battery;
//	private XYSeries tracker_1;
//	private XYSeries tracker_2;
//	private XYSeries tracker_3;
//	private XYSeries tracker_4;
	
	public PowerGraph() {
		power_dataset = createPowerDataSet();
		
		power_chart = ChartFactory.createXYLineChart("Power", "Time (sec)", "Power (Watts)", power_dataset);
		LegendTitle legend = power_chart.getLegend();
		legend.setPosition(RectangleEdge.RIGHT);
		
		power_panel = new ChartPanel(power_chart);
		
		this.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 0.5;
		c.weighty = 0.5;
		c.gridx = 0;
		c.gridy = 0;
		//c.ipadx = 200;
		c.ipady = 300;
		this.add(power_panel, c);
	}
	
	private XYSeriesCollection createPowerDataSet() {
		motor = new XYSeries("Motor");
		array = new XYSeries("Array");
		battery = new XYSeries("Battery");
//		tracker_1 = new XYSeries("Tracker 1");
//		tracker_2 = new XYSeries("Tracker 2");
//		tracker_3 = new XYSeries("Tracker 3");
//		tracker_4 = new XYSeries("Tracker 4");
		
		XYSeriesCollection power_dataset = new XYSeriesCollection();
		power_dataset.addSeries(motor);
		power_dataset.addSeries(array);
		power_dataset.addSeries(battery);
//		power_dataset.addSeries(tracker_1);
//		power_dataset.addSeries(tracker_2);
//		power_dataset.addSeries(tracker_3);
//		power_dataset.addSeries(tracker_4);
		
		return power_dataset;
	}
	
	public void updateDataSet(HashMap<String, Double> calculation_data) {
		double time_seconds = calculation_data.get("time_seconds");
		double motor_power  = calculation_data.get("motor_power");
		double array_power  = calculation_data.get("array_power");
		double batt_power  = calculation_data.get("battery_power");
	
		motor.add(time_seconds, motor_power);
		if(array_power != 0)
			array.add(time_seconds, array_power);
		if(batt_power != 0)
			battery.add(time_seconds, batt_power);

		validate();
		repaint();
	}
}
