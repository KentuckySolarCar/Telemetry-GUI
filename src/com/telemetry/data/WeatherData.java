package com.telemetry.data;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.telemetry.util.Tools;
import com.telemetry.util.WeatherHandler;
import java.util.ArrayList;
import java.util.HashMap;

public class WeatherData {
	public static final int NUM_MINUTELY_DATA_POINTS = 60;
	public static final int NUM_HOURLY_DATA_POINTS = 48;
	
	private WeatherHandler weather_handler;
	private ArrayList<JSONObject> current_json;
	private JSONArray minutely_json;
	private JSONArray hourly_json;
	private JSONArray daily_json;
	
	public WeatherData() {
		weather_handler = new WeatherHandler();
		current_json = new ArrayList<JSONObject>();
		minutely_json = new JSONArray();
		hourly_json = new JSONArray();
		daily_json = new JSONArray();
	}
	
	public void updateWeatherData(Double latitude, Double longitude) {
		JSONObject weather_data = weather_handler.getWeatherData(latitude, longitude);
		this.current_json.add((JSONObject) weather_data.get("currently"));
		JSONObject minutely_temp = (JSONObject) weather_data.get("minutely");
		JSONObject hourly_temp = (JSONObject) weather_data.get("hourly");
		JSONObject daily_temp = (JSONObject) weather_data.get("daily");
		minutely_json = (JSONArray) minutely_temp.get("data");
		hourly_json   = (JSONArray) hourly_temp.get("data");
		daily_json    = (JSONArray) daily_temp.get("data");
	}
	
	/**
	 * Check to make sure that there's at least one data point available to grab
	 * 
	 * @return latest current weather data
	 */
	public JSONObject getLatestCurrentData() {
		if(current_json.size() > 0)
			return current_json.get(current_json.size()-1);
		else {
			System.err.println("No currently data points available yet");
			return null;
		}
	}
	
	public ArrayList<JSONObject> getAllCurrentData() {
		return current_json;
	}
	
	public JSONArray getMinutelyData() {
		return minutely_json;
	}
	
	public JSONArray getHourlyData() {
		return hourly_json;
	}
	
	public JSONArray getDailyData() {
		return daily_json;
	}
}
