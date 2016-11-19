package com.telemetry.templates;

import java.awt.Dimension;
import java.text.SimpleDateFormat;

import javax.swing.JPanel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.time.Hour;
import org.jfree.data.time.Minute;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.telemetry.util.Tools;

public class OneSeriesTimeGraph extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8686289643412242742L;
	private JFreeChart chart;
	private ChartPanel chart_panel;
	private TimeSeriesCollection dataset;
	private TimeSeries series;
	private String date_format;
	private int num_updates;
	
	public OneSeriesTimeGraph(String title, 
						  	  String time_axis_title, 
						      String value_axix_title,
						      String date_format,
						      int num_updates) {
		setMinimumSize(new Dimension(400,400));
		this.date_format = date_format;
		this.num_updates = num_updates;

		dataset = new TimeSeriesCollection();
		series = new TimeSeries("Time Series");
		dataset.addSeries(series);
		
		chart = ChartFactory.createTimeSeriesChart(title, 
				time_axis_title, 
				value_axix_title, 
				dataset,
				true,
				true,
				false);
		XYPlot plot = chart.getXYPlot();
		final DateAxis axis = (DateAxis) plot.getDomainAxis();
		axis.setDateFormatOverride(new SimpleDateFormat(date_format));
		
		chart_panel = new ChartPanel(chart);
		add(chart_panel);
	}
	
	public void updateDataset(JSONArray data, String key) {
		series.clear();
		for(int i = 0; i < num_updates; i++) {
			JSONObject data_point = (JSONObject) data.get(i);
			switch(date_format) {
			case "HH":
				series.add(new Hour(i,1,1,2000),
						   Tools.getJSONDouble(data_point, key));
				break;
			case "mm":
				series.add(new Minute(i,1,1,1,2000),
						   Tools.getJSONDouble(data_point, key));
			}
		}
	}
}
