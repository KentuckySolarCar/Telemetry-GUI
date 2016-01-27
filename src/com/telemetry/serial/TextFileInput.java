package com.telemetry.serial;

import org.json.simple.JSONObject;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.parser.ParseException;

import com.telemetry.gui.TelemetryFrame;
import com.telemetry.gui.device.MotorPanel;

import org.json.simple.parser.JSONParser;

public class TextFileInput {
	private JSONParser parser;
	private BufferedReader buffer_reader;
	
	public TextFileInput() {
		parser = new JSONParser();
	}
	
	public void Initiate() throws IOException, ParseException {
		try {
			buffer_reader = new BufferedReader(new FileReader("pi_data.txt"));
		
			String line;
			while((line = buffer_reader.readLine()) != null) {
				JSONObject obj = (JSONObject) parser.parse(line);
				String message_type = (String) obj.get("message_id");
				TelemetryFrame.updateTelemetryFrame(obj, message_type);
			}
			
		} finally {
			if(buffer_reader != null)
				buffer_reader.close();
		}
	}
}

