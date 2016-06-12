package com.telemetry.graphs;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;

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
	private XYSeries tracker_1;
	private XYSeries tracker_2;
	private XYSeries tracker_3;
	private XYSeries tracker_4;
	
	public PowerGraph(int width, int height) {
		setSize(width, height);

		power_dataset = createPowerDataSet();
		
		power_chart = ChartFactory.createXYLineChart("Power", "Time (min)", "Power (Watts)", power_dataset);
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
		tracker_1 = new XYSeries("Tracker 1");
		tracker_2 = new XYSeries("Tracker 2");
		tracker_3 = new XYSeries("Tracker 3");
		tracker_4 = new XYSeries("Tracker 4");
		
		XYSeriesCollection power_dataset = new XYSeriesCollection();
		power_dataset.addSeries(motor);
		power_dataset.addSeries(array);
		power_dataset.addSeries(tracker_1);
		power_dataset.addSeries(tracker_2);
		power_dataset.addSeries(tracker_3);
		power_dataset.addSeries(tracker_4);
		
		return power_dataset;
	}
	
	/* Double array of power format (indexes):
	 * 0: time in seconds
	 * 1: power of motor
	 * 2: power of array
	 * 3: power of tracker_1
	 * 4: power of tracker_2
	 * 5: power of tracker_3
	 * 6: power of tracker_4
	 */
	public void updateDataSet(double[] power) {
		motor.add(power[0], power[1]);
		array.add(power[0], power[2]);
		tracker_1.add(power[0], power[3]);
		tracker_2.add(power[0], power[4]);
		tracker_3.add(power[0], power[5]);
		tracker_4.add(power[0], power[6]);
		power_panel.removeAll();
		power_panel.revalidate();
		power_chart = ChartFactory.createXYLineChart("Power", "Time (min)", "Power (Watts)", power_dataset);
		power_panel = new ChartPanel(power_chart);
		validate();
		repaint();
	}
}
