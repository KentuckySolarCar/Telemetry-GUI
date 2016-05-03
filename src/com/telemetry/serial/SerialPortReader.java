package com.telemetry.serial;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.telemetry.gui.TelemetryFrame;

public class SerialPortReader extends Thread {
	
	private BufferedReader input_stream;
	private JSONParser parser;
	private boolean status;
	
	public SerialPortReader (InputStream input_stream) throws UnsupportedEncodingException {
		status = false;
		this.input_stream = new BufferedReader(new InputStreamReader(input_stream));
	}
	
	public void stopThread() {
		status = false;
	}
	
	public boolean getThreadStatus() {
		return status;
	}

	@Override
	public void run() {
		status = true;
		try {
			String line;

			while(status) {
				line = input_stream.readLine();
				try {
					System.out.println(line);
					JSONObject obj = (JSONObject) parser.parse(line);
					TelemetryFrame.updateDevicePanel(obj, (String) obj.get("message_id"));
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					System.out.println("Incomplete Data, ignored...");
				}
				Thread.sleep(1000);
			}
		} catch (InterruptedException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
