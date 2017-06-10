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
import org.jfree.data.time.Hour;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.telemetry.util.Tools;

public class HourlyTempGraph extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 392886343346179844L;
	private JFreeChart temp_chart;
	private ChartPanel temp_chart_panel;
	private TimeSeriesCollection temp_dataset;
	private TimeSeries temp_series;
	
	private static final int NUM_HOUR_FORECASTS = 24;
	
	public HourlyTempGraph() {
		initDataset();
		
		temp_chart = ChartFactory.createTimeSeriesChart(
				"Hourly Temperature", 
				"Time (Hour)", 
				"Temperature (F)", 
				temp_dataset,
				true,
				true,
				false);
		
		XYPlot plot = temp_chart.getXYPlot();
		
		final DateAxis axis = (DateAxis) plot.getDomainAxis();
		axis.setDateFormatOverride(new SimpleDateFormat("HH"));
		
		temp_chart_panel = new ChartPanel(temp_chart);
		add(temp_chart_panel);
	}
	
	private void initDataset() {
		temp_dataset = new TimeSeriesCollection();
		temp_series = new TimeSeries("Hourly Temperature");
		temp_dataset.addSeries(temp_series);
	}
	
	public void updateDataset(JSONArray hourly_data) {
		temp_series.clear();
		
		for(int i = 0; i < NUM_HOUR_FORECASTS; i++) {
			JSONObject data_point = (JSONObject) hourly_data.get(i);
			temp_series.add(new Hour(i,1,1,2000), 
							Tools.getJSONDouble(data_point, 
							"temperature"));
		}
	}
}