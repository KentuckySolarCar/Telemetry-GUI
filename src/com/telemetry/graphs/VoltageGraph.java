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
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.RectangleEdge;

public class VoltageGraph extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5352260033600698308L;
	private JFreeChart voltage_chart;
	private ChartPanel voltage_panel;
	private XYDataset voltage_dataset;
	private XYSeries v_max;
	private XYSeries v_min;
	private XYSeries v_avg;
	//private XYSeries b_std_dev;
	
	public VoltageGraph() {
		voltage_dataset = createVoltageDataSet();
		
		voltage_chart = ChartFactory.createXYLineChart("Voltage", "Time (sec)", "Volts (V)", voltage_dataset);
		LegendTitle legend = voltage_chart.getLegend();
		legend.setPosition(RectangleEdge.RIGHT);
		
		voltage_panel = new ChartPanel(voltage_chart);
		
		this.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 0.5;
		c.weighty = 0.5;
		c.gridx = 0;
		c.gridy = 0;
		//c.ipadx = 200;
		c.ipady = 300;
		this.add(voltage_panel, c);
	}
	
	private XYDataset createVoltageDataSet() {
		v_max = new XYSeries("Battery Max");
		v_min = new XYSeries("Battery Min");
		v_avg = new XYSeries("Battery Mean");
		
		XYSeriesCollection voltage_dataset = new XYSeriesCollection();
		voltage_dataset.addSeries(v_max);
		voltage_dataset.addSeries(v_min);
		voltage_dataset.addSeries(v_avg);
		
		return voltage_dataset;
	}
	
	public void updateDataSet(HashMap<String, Double> calculation_data) {
		double time_seconds = calculation_data.get("time_seconds");
		double batt_v_avg   = calculation_data.get("batt_v_avg");
		double batt_v_max   = calculation_data.get("batt_v_max");
		double batt_v_min   = calculation_data.get("batt_v_min");
		if(batt_v_avg + batt_v_max + batt_v_min == 0)
			return;
		v_max.add(time_seconds, batt_v_max);
		v_min.add(time_seconds, batt_v_min);
		v_avg.add(time_seconds, batt_v_avg);
//		voltage_panel.removeAll();
//		voltage_panel.revalidate();
//		voltage_chart = ChartFactory.createXYLineChart("Voltage", "Time (min)", "Volts (V)", voltage_dataset);
//		voltage_panel = new ChartPanel(voltage_chart);
		validate();
		repaint();
	}
}
