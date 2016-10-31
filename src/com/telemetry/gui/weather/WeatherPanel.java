package com.telemetry.gui.weather;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.telemetry.util.Tools;

public class WeatherPanel extends JPanel {
	// Misc. Stuff
	private static final long serialVersionUID = -3880688547665804873L;
	private SimpleDateFormat sdf = new SimpleDateFormat("YY-MM-dd HH:mm:ss");

	//---------------------------Currently components---------------------------//

	// Weather Icon
	// TODO Find a placeholder icon to use when GUI is not active
	private JLabel current_weather_icon = new JLabel("Place holder");
	
	// Current time in HH:MM:SS
	private JLabel current_time = new JLabel("00-00-00 00:00:00");

	// Will not be defined if nearestStormDistance is 0
	// 0 degree at true north and progress clockwise
	private JLabel current_nearest_storm_bearing = new JLabel("000.000");

	// Nearest storm in miles
	private JLabel current_nearest_storm_distance = new JLabel("0000.000");

	// 0 - 1 inclusive
	private JLabel current_precip_probability = new JLabel("000");

	// In inches of liquid water per hour
	private JLabel current_precip_intensity = new JLabel("00");

	// 0 - 1 inclusive
	private JLabel current_cloud_cover = new JLabel("000");

	// 0 - 1 inclusive
	private JLabel current_humidity = new JLabel("000");

	// Sea-level air pressure in millibars
	private JLabel current_pressure = new JLabel("0000");

	// visibility in miles, capped at 10
	private JLabel current_visibility = new JLabel("00.00");

	// Direction wind is coming FROM in degrees, with true north = 0 and clockwise
	// If windSpeed is 0, this will not be defined
	private JLabel current_wind_bearing = new JLabel("000");

	// In miles per hour
	private JLabel current_wind_speed = new JLabel("000.000");

	//---------------------------Minutely components---------------------------//
	MinutelyPrecipGraph minutely_graph = new MinutelyPrecipGraph();

	//---------------------------Hourly components---------------------------//
	HourlyPrecipGraph hourly_graph = new HourlyPrecipGraph();
	
	//---------------------------End Variable Declaration---------------------------//
	
	public WeatherPanel() {
		setLayout(new GridBagLayout());
		
		GridBagConstraints gbc = new GridBagConstraints();
		
		gbc.insets = new Insets(3,3,3,3);
		
		gbc.gridx = 0;
		gbc.gridy = 1;
		

	}
	
	public void updateDailyPanel(JSONArray current_json) {
		
	}

	public void updateHourlyPanel(JSONArray hourly_json) {
		// Graph does its own updating, so push all data directly to object
		hourly_graph.updateDataset(hourly_json);
	}
	
	public void updateMinutelyPanel(JSONArray minutely_json) {
		// Graph does its own updating, so push all data directly to object
		minutely_graph.updateDataset(minutely_json);
	}
	
	/**
	 * TODO sdf needs to have timezone set dynamically
	 * 
	 * @param currently_json Latest currently json
	 */
	public void updateCurrentlyPanel(JSONObject currently_json) {
		// Grab everything we need from the json
		// Note that some values are not fetched immediately as they might
		// not be defined
		long unix_seconds = Tools.getJSONLong(currently_json, "time");
		String weather_description = (String) currently_json.get("icon");
		double nearest_storm_dist = Tools.getJSONDouble(currently_json, "nearestStormDistance");
		double precip_intensity = Tools.getJSONDouble(currently_json, "precipIntensity");
		double precip_prob = Tools.getJSONDouble(currently_json, "precipProbability");
		double cloud_coverage = Tools.getJSONDouble(currently_json, "cloudCover");
		double humidity = Tools.getJSONDouble(currently_json, "humidity");
		double pressure = Tools.getJSONDouble(currently_json, "pressure");
		double visibility = Tools.getJSONDouble(currently_json, "visibility");
		double wind_speed = Tools.getJSONDouble(currently_json, "windSpeed");
		
		// Placeholder values for things that might not be defined
		double nearest_storm_bearing = -1D;
		double wind_bearing = -1D;
		
		// Check if some values should be defined
		if(nearest_storm_dist != 0)
			nearest_storm_bearing = Tools.getJSONDouble(currently_json, "nearestStormBearing");
		if(wind_speed != 0)
			wind_bearing = Tools.getJSONDouble(currently_json, "windBearing");
		
		// Update current time
		Date date = new Date(unix_seconds * 1000L);
		current_time.setText(sdf.format(date));

		// Update weather icon
		ImageIcon weather_icon = getImageIcon(weather_description);
		current_weather_icon = new JLabel(weather_icon);
		
		// Update all currently values
		current_nearest_storm_distance.setText(Tools.roundDouble(nearest_storm_dist));
		current_precip_intensity.setText(Tools.roundDouble(precip_intensity));
		current_precip_probability.setText(Tools.roundDouble(precip_prob));
		current_cloud_cover.setText(Tools.roundDouble(cloud_coverage));
		current_humidity.setText(Tools.roundDouble(humidity));
		current_pressure.setText(Tools.roundDouble(pressure));
		current_visibility.setText(Tools.roundDouble(visibility));
		current_wind_speed.setText(Tools.roundDouble(wind_speed));
		
		// Update all currently values that might not be defined
		if(nearest_storm_bearing != -1)
			current_nearest_storm_bearing.setText(Tools.roundDouble(nearest_storm_bearing));
		if(wind_bearing != -1)
			current_wind_bearing.setText(Tools.roundDouble(wind_speed));
	}

	private ImageIcon getImageIcon(String weather_description) {
		String path = weather_description + ".jpg";
		java.net.URL imgURL = getClass().getResource(path);
		if(imgURL != null)
			return new ImageIcon(imgURL, weather_description);
		else {
			System.out.println("Image icon not found at " + path);
			return null;
		}
	}
}
