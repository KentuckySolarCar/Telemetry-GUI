package com.telemetry.graphs;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.util.ArrayList;
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

public class EnergyGraph extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 8698643579672784275L;
	private JFreeChart energy_chart;
	private ChartPanel energy_panel;
//	private JPanel button_panel;
	private XYSeriesCollection energy_dataset;
	private ArrayList<XYSeries> laps;
	private int lap_counts = 0;
	
	public EnergyGraph(int width, int height ) {
		setSize(width, height);

		energy_dataset = createEnergyDataSet();

		energy_chart = ChartFactory.createXYLineChart("Energy", "Distance (miles)", "Energy This Interval (watt*hours)", energy_dataset);
		LegendTitle legend = energy_chart.getLegend();
		legend.setPosition(RectangleEdge.RIGHT);
		
		energy_panel = new ChartPanel(energy_chart);
		
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
		this.add(energy_panel, c);
		
		/*c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 0;
		c.weighty = 0;
		c.gridx = 1;
		c.gridy = 0;
		this.add(button_panel, c);*/
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
	
	public void updateDataSet(HashMap<String, Double> calculation_data) {
		/*for (int i = 0; i < lap_counts; i++) {
			laps.get(i).add(calculation_data.get("time_elapsed"), calculation_data[i]);
		}*/
		energy_panel.removeAll();
		energy_panel.revalidate();
		energy_chart = ChartFactory.createXYLineChart("Energy", "Distance (miles)", "Energy This Interval (watt*hours)", energy_dataset);
		energy_panel = new ChartPanel(energy_chart);
		validate();
		repaint();
	}
}
