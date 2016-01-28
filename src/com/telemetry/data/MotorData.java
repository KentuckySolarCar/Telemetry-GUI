package com.telemetry.data;

import java.util.HashMap;
import java.util.Map;

import org.json.simple.JSONObject;

public class MotorData {
	private double speed;
	private double current;
	private double energy;
	private double average_speed;
	
	public MotorData() {
		speed = 0;
		current = 0;
		energy = 0;
		average_speed = 0;
	}
	
	public void inputData(JSONObject data) {
		speed = ((Number)data.get("S")).doubleValue();
	}
	
	public Map<String, Double> getData() {
		HashMap<String, Double> data = new HashMap<String, Double>();
		data.put("speed", speed);
		data.put("current", current);
		data.put("energy", energy);
		data.put("average_speed", average_speed);
		return data;
	}
}
