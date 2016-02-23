package com.telemetry.graphs;

import java.awt.GridLayout;
import java.util.ArrayList;

import javax.swing.JPanel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

public class EnergyGraph extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 8698643579672784275L;
	private JFreeChart energy_chart;
	private ChartPanel energy_panel;
	private XYSeriesCollection energy_dataset;
	private ArrayList<XYSeries> laps;
	private int lap_counts = 0;
	
	public EnergyGraph() {
		energy_dataset = createEnergyDataSet();
		
		energy_chart = ChartFactory.createXYLineChart("Energy", "Distance (miles)", "Energy This Interval (watt*hours)", energy_dataset);
	
		energy_panel = new ChartPanel(energy_chart);
		
		setLayout(new GridLayout(1, 1));
		
		add(energy_panel);
	}
	
	private XYSeriesCollection createEnergyDataSet() {
		lap_counts++;
		laps = new ArrayList<XYSeries>();
		laps.add(new XYSeries("Lap " + lap_counts));
		
		XYSeriesCollection energy_dataset = new XYSeriesCollection();
		
		for(XYSeries lap : laps) 
			energy_dataset.addSeries(lap);
		
		return energy_dataset;
	}
	
	/* Double array of energy format (indexes):
	 * 0: time in seconds
	 * 1: energy of lap_1
	 * 2: energy of lap_2
	 * ... (N = lap_counts)
	 * N: energy of lap_N
	 */
	private void updateDataSet(double[] energies) {
		for (int i = 0; i < lap_counts; i++) {
			laps.get(i).add(energies[0], energies[i]);
		}
	}
}
