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
	
	public WeatherData() {
		weather_handler = new WeatherHandler();
	}
	
	public void updateWeatherData(Double latitude, Double longitude) {
		JSONObject weather_data = weather_handler.getWeatherData(latitude, longitude);
		this.current_json.add((JSONObject) weather_data.get("currently"));
		JSONObject minutely_temp = (JSONObject) weather_data.get("minutely");
		JSONObject hourly_temp = (JSONObject) weather_data.get("hourly");
		minutely_json = (JSONArray) minutely_temp.get("data");
		hourly_json   = (JSONArray) hourly_temp.get("data");
	}
	
	public JSONObject getSingleCurrentData() {
		return current_json.get(current_json.size()-1);
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
}
