package com.telemetry.templates;

import java.awt.Color;
import java.awt.Dimension;
import java.text.SimpleDateFormat;

import javax.swing.JPanel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.labels.StandardXYToolTipGenerator;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.StandardXYItemRenderer;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.data.time.Hour;
import org.jfree.data.time.Minute;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.telemetry.util.Tools;

public class TwoSeriesTimeGraph extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8686289643412242742L;
	private JFreeChart chart;
	private ChartPanel chart_panel;
	private TimeSeriesCollection left_dataset;
	private TimeSeriesCollection right_dataset;
	private TimeSeries left_series;
	private TimeSeries right_series;
	private String date_format;
	private int num_updates;
	
	public TwoSeriesTimeGraph(String title, 
						  	  String time_axis_title, 
						      String left_axix_title,
						      String right_axis_title,
						      String date_format,
						      int num_updates) {
		setMinimumSize(new Dimension(400,400));
		this.date_format = date_format;
		this.num_updates = num_updates;

		left_dataset = new TimeSeriesCollection();
		left_series = new TimeSeries(left_axix_title);
		left_dataset.addSeries(left_series);
		
		right_dataset = new TimeSeriesCollection();
		right_series = new TimeSeries(right_axis_title);
		right_dataset.addSeries(right_series);
		
		chart = ChartFactory.createTimeSeriesChart(title, 
				time_axis_title, 
				left_axix_title, 
				left_dataset,
				true,
				true,
				false);
		XYPlot plot = chart.getXYPlot();
		NumberAxis axis2 = new NumberAxis(right_axis_title);
		axis2.setAutoRangeIncludesZero(false);
		plot.setRangeAxis(1, axis2);
		plot.setDataset(1, right_dataset);
		plot.mapDatasetToRangeAxis(1, 1);
		
		final XYItemRenderer renderer2 = new StandardXYItemRenderer();
		renderer2.setSeriesPaint(1, Color.black);
		plot.setRenderer(1, renderer2);
		
		final DateAxis axis = (DateAxis) plot.getDomainAxis();
		axis.setDateFormatOverride(new SimpleDateFormat(date_format));
		
		chart_panel = new ChartPanel(chart);
		add(chart_panel);
	}
	
	public void updateDataset(JSONArray data, String left_key, String right_key) {
		left_series.clear();
		right_series.clear();

		for(int i = 0; i < num_updates; i++) {
			JSONObject data_point = (JSONObject) data.get(i);
			switch(date_format) {
			case "HH":
				left_series.add(new Hour(i,1,1,2000),
						   Tools.getJSONDouble(data_point, left_key));
				right_series.add(new Hour(i,1,1,2000),
						   Tools.getJSONDouble(data_point, right_key));
				break;
			case "mm":
				left_series.add(new Minute(i,1,1,1,2000),
						   Tools.getJSONDouble(data_point, left_key));
				right_series.add(new Minute(i,1,1,1,2000),
						   Tools.getJSONDouble(data_point, right_key));
			}
		}
	}
}