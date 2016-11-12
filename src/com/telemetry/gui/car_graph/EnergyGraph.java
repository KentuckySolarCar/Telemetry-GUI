package com.telemetry.gui.car_graph;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.HashMap;

import javax.swing.JPanel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.title.LegendTitle;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.RectangleEdge;

public class EnergyGraph extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 8698643579672784275L;
	private JFreeChart energy_chart;
	private ChartPanel energy_panel;
	private XYSeriesCollection energy_dataset;
	private XYSeries soc_series;
	
	public EnergyGraph() {
		energy_dataset = createEnergyDataSet();

		energy_chart = ChartFactory.createXYLineChart("Energy", "Time (Seconds)", "Energy This Interval (watt*hours)", energy_dataset);
		LegendTitle legend = energy_chart.getLegend();
		legend.setPosition(RectangleEdge.RIGHT);
		
		energy_panel = new ChartPanel(energy_chart);
		
		// Use GridBagConstraints to be able to change size of grid elements
		this.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 0.5;
		c.weighty = 0.5;
		c.gridx = 0;
		c.gridy = 0;
		c.ipady = 300;
		this.add(energy_panel, c);
	}
	
	private XYSeriesCollection createEnergyDataSet() {
		soc_series = new XYSeries("State of Charge");
		
		XYSeriesCollection energy_dataset = new XYSeriesCollection();
		energy_dataset.addSeries(soc_series);
		
		return energy_dataset;
	}
	
	public void updateDataSet(HashMap<String, Double> calculation_data) {
		double seconds_elapsed = calculation_data.get("time_seconds");
		double soc = calculation_data.get("state_of_charge");
		soc_series.add(seconds_elapsed, soc);
	}
}