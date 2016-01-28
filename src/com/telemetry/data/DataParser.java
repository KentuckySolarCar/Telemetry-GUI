package com.telemetry.data;

import java.io.IOException;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class DataParser {
	private JSONParser parser;
	private MotorData motor_data;
	private BatteryData batt_data;
	private Calculations calc_data;
	private MpptData mppt_data;
	
	public DataParser() {
		parser = new JSONParser();
		motor_data = new MotorData();
		batt_data = new BatteryData();
		calc_data = new Calculations();
		mppt_data = new MpptData();
	}
	
	public void parseData(String raw) throws IOException, ParseException {
		JSONObject data = (JSONObject) parser.parse(raw);
		String message_type = (String) data.get("message_id");
		
		switch(message_type) {
		case "motor": {
			motor_data.inputData(data);
			break;
		}
		case "bat_volt": 
		case "bat_temp": {
			break;
		}
		default:
			System.out.println("Unknown Message Type");
			break;
		}
	}
}
