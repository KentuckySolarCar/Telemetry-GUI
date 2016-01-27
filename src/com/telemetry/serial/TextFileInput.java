package com.telemetry.serial;

import org.json.simple.JSONObject;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import org.json.simple.JSONArray;
import org.json.simple.parser.ParseException;

import com.telemetry.gui.TelemetryFrame;
import com.telemetry.gui.device.MotorPanel;

import org.json.simple.parser.JSONParser;

public class TextFileInput {
	private JSONParser parser;
	private BufferedReader buffer_reader;
	
	public TextFileInput() throws IOException, ParseException {
		parser = new JSONParser();
		try {
			buffer_reader = new BufferedReader(new FileReader("pi_data.txt"));
		
			String line;
			while((line = buffer_reader.readLine()) != null) {
				Object obj = parser.parse(line);
				JSONArray array = (JSONArray)obj;
				JSONObject message_id = (JSONObject) array.get(1);
				String message_type = (String) message_id.get("message_id");
				TelemetryFrame.updateTelemetryFrame(array, message_type);
			}
			
		} finally {
			if(buffer_reader != null)
				buffer_reader.close();
		}
	}
}
