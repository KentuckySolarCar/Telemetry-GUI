 package com.telemetry.graphs;

import java.awt.GridLayout;

import javax.swing.JPanel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

public class VoltageGraph extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5352260033600698308L;
	private JFreeChart voltage_chart;
	private ChartPanel voltage_panel;
	private XYDataset voltage_dataset;
	private XYSeries b_cell_max;
	private XYSeries b_cell_min;
	private XYSeries b_cell_mean;
	private XYSeries b_std_dev;
	private XYSeries r_cell_max;
	private XYSeries r_cell_min;
	private XYSeries r_cell_mean;
	private XYSeries r_std_dev;
	
	public VoltageGraph() {
		voltage_dataset = createVoltageDataSet();
		
		voltage_chart = ChartFactory.createXYLineChart("Voltage", "Time (min)", "Volts (V)", voltage_dataset);
	
		voltage_panel = new ChartPanel(voltage_chart);
		
		setLayout(new GridLayout(1, 1));
		
		add(voltage_panel);
	}
	
	private XYDataset createVoltageDataSet() {
		b_cell_max = new XYSeries("Batman Max");
		b_cell_min = new XYSeries("Batman Min");
		b_cell_mean = new XYSeries("Batman Mean");
		b_std_dev = new XYSeries("Batman Std. Dev");
		r_cell_max = new XYSeries("Robin Max");
		r_cell_min = new XYSeries("Robin Min");
		r_cell_mean = new XYSeries("Robin Mean");
		r_std_dev = new XYSeries("Robin Std. Dev");
		
		XYSeriesCollection voltage_dataset = new XYSeriesCollection();
		voltage_dataset.addSeries(b_cell_max);
		voltage_dataset.addSeries(b_cell_min);
		voltage_dataset.addSeries(b_cell_mean);
		voltage_dataset.addSeries(b_std_dev);
		voltage_dataset.addSeries(r_cell_max);
		voltage_dataset.addSeries(r_cell_min);
		voltage_dataset.addSeries(r_cell_mean);
		voltage_dataset.addSeries(r_std_dev);
		
		return voltage_dataset;
	}
	
	/* Double array of voltages format (indexes):
	 * 0: time in seconds
	 * 1: b_cell_max
	 * 2: b_cell_min
	 * 3: b_cell_mean
	 * 4: b_std_dev
	 * 5: r_cell_max
	 * 6: r_cell_min
	 * 7: r_cell_mean
	 * 8: r_std_dev
	 */
	public void updateDataSet(double[] volts) {
		b_cell_max.add(volts[0], volts[1]);
		b_cell_min.add(volts[0], volts[2]);
		b_cell_mean.add(volts[0], volts[3]);
		b_std_dev.add(volts[0], volts[4]);
		r_cell_max.add(volts[0], volts[5]);
		r_cell_min.add(volts[0], volts[6]);
		r_cell_mean.add(volts[0], volts[7]);
		r_std_dev.add(volts[0], volts[8]);
		voltage_panel.removeAll();
		voltage_panel.revalidate();
		voltage_chart = ChartFactory.createXYLineChart("Voltage", "Time (min)", "Volts (V)", voltage_dataset);
		voltage_panel = new ChartPanel(voltage_chart);
		validate();
		repaint();
	}
}
