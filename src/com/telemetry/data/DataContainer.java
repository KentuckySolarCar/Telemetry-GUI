package com.telemetry.data;

import org.json.simple.JSONObject;

/**
 * Container for all specific data containers
 * @author Weilian Song
 */
public class DataContainer {
	private CarData car_data;
	private RaceData race_data;
	private WeatherData weather_data;
	
	public DataContainer() {
		car_data = new CarData();
		race_data = new RaceData();
		weather_data = new WeatherData();
	}
	
	public CarData getCarData() {
		return car_data;
	}
	
	public RaceData getRaceData() {
		return race_data;
	}
	
	public WeatherData getWeatherData() {
		return weather_data;
	}
	
	public void updateCarData(JSONObject obj) {
		car_data.updateData(obj);
	}

	public void updateRaceData() {
		return;
	}

	public void updateWeatherData() {
		return;
	}
}