package com.telemetry.gui.weather;

import java.awt.Dimension;
import java.text.SimpleDateFormat;

import javax.swing.JPanel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.time.Minute;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.telemetry.util.Tools;

public class MinutelyPrecipGraph extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 462407074568164071L;
	private JFreeChart precip_chart;
	private ChartPanel precip_chart_panel;
	private TimeSeriesCollection probability_dataset;
	private TimeSeriesCollection intensity_dataset;
	private TimeSeries precip_intensity;
	private TimeSeries precip_probability;
	
	private static final int NUM_MINUTE_FORECASTS = 60;
	
	public MinutelyPrecipGraph() {
		initDataset();
		setMinimumSize(new Dimension(400,400));
		
		precip_chart = ChartFactory.createTimeSeriesChart(
				"Minutely Precipitation", 
				"Time (Minute)", 
				"Precip Intensity", 
				intensity_dataset,
				true,
				true,
				false);
		
		XYPlot probability_plot = precip_chart.getXYPlot();
		NumberAxis axis2 = new NumberAxis("Precip Probability");
		axis2.setAutoRangeIncludesZero(false);
		probability_plot.setRangeAxis(1, axis2);
		probability_plot.setDataset(1, probability_dataset);
		probability_plot.mapDatasetToRangeAxis(1, 1);
		
		final DateAxis axis = (DateAxis) probability_plot.getDomainAxis();
		axis.setDateFormatOverride(new SimpleDateFormat("mm"));
		
		precip_chart_panel = new ChartPanel(precip_chart);
		add(precip_chart_panel);
	}
	
	private void initDataset() {
		intensity_dataset = new TimeSeriesCollection();
		probability_dataset = new TimeSeriesCollection();
		
		precip_intensity = new TimeSeries("Minutely Precipitation Intensity");
		intensity_dataset.addSeries(precip_intensity);

		precip_probability = new TimeSeries("Minutely Precipitation Probability");
		probability_dataset.addSeries(precip_probability);
	}
	
	public void updateDataset(JSONArray minutely_data) {
		precip_intensity.clear();
		precip_probability.clear();
		
		for(int i = 0; i < NUM_MINUTE_FORECASTS; i++) {
			JSONObject data_point = (JSONObject) minutely_data.get(i);
			precip_intensity.add(new Minute(i,0,0,0,0), 
								 Tools.getJSONDouble(data_point, 
								 "precipIntensity"));
			precip_probability.add(new Minute(i,0,0,0,0), 
								 Tools.getJSONDouble(data_point, 
								 "precipProbability"));
		}
		
		validate();
		repaint();
	}
}