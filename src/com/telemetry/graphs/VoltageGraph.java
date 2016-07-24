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
	private XYSeries batt_v_max;
	private XYSeries batt_v_min;
	private XYSeries batt_v_avg;
	//private XYSeries b_std_dev;
	
	public VoltageGraph(int width, int height) {
		setSize(width, height);

		voltage_dataset = createVoltageDataSet();
		
		voltage_chart = ChartFactory.createXYLineChart("Voltage", "Time (min)", "Volts (V)", voltage_dataset);
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
		batt_v_max = new XYSeries("Batman Max");
		batt_v_min = new XYSeries("Batman Min");
		batt_v_avg = new XYSeries("Batman Mean");
		//b_std_dev = new XYSeries("Batman Std. Dev");
		
		XYSeriesCollection voltage_dataset = new XYSeriesCollection();
		voltage_dataset.addSeries(batt_v_max);
		voltage_dataset.addSeries(batt_v_min);
		voltage_dataset.addSeries(batt_v_avg);
		//voltage_dataset.addSeries(b_std_dev);
		
		return voltage_dataset;
	}
	
	public void updateDataSet(HashMap<String, Double> calculation_data) {
		//double batt_v_max = calculation_data.get("batt_v_avg") - calculation_data.get("batt_v_min") + calculation_data.get("batt_v_avg");
		batt_v_max.add(calculation_data.get("time_elapsed"), (Number) (calculation_data.get("batt_v_avg") - calculation_data.get("batt_v_min") + calculation_data.get("batt_v_avg")));
		batt_v_min.add(calculation_data.get("time_elapsed"), calculation_data.get("batt_v_min"));
		batt_v_avg.add(calculation_data.get("time_elapsed"), calculation_data.get("batt_v_avg"));
		//b_std_dev.add(calculation_data.get("time_elapsed"), calculation_data[4]);
		voltage_panel.removeAll();
		voltage_panel.revalidate();
		voltage_chart = ChartFactory.createXYLineChart("Voltage", "Time (min)", "Volts (V)", voltage_dataset);
		voltage_panel = new ChartPanel(voltage_chart);
		validate();
		repaint();
	}
}
