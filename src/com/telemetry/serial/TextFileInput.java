package com.telemetry.serial;

import org.json.simple.JSONObject;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.parser.ParseException;

import com.telemetry.gui.TelemetryFrame;
import com.telemetry.gui.device.MotorPanel;

import org.json.simple.parser.JSONParser;

public class TextFileInput implements Runnable {
	private JSONParser parser;
	private BufferedReader buffer_reader;
	
	public TextFileInput() throws FileNotFoundException {
		parser = new JSONParser();
		buffer_reader = new BufferedReader(new FileReader("pi_data.txt"));
	}
	
	public void Initiate() throws IOException, ParseException {
		Thread thread = new Thread(new TextFileInput());
		thread.start();
	}

	@Override
	public void run() {
		try {
			String line;
			while((line = buffer_reader.readLine()) != null) {
				JSONObject obj = (JSONObject) parser.parse(line);
				String message_type = (String) obj.get("message_id");
				TelemetryFrame.updateDevicePanel(obj, message_type);
				Thread.sleep(50);
			}
		} catch (IOException | ParseException | InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if(buffer_reader != null)
				try {
					buffer_reader.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
		
	}
}

