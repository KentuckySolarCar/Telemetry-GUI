package com.telemetry.serial;

import java.awt.Color;
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
	private TelemetryFrame telem_frame;
	
	public SerialPortReader (InputStream input_stream, TelemetryFrame telem_frame) throws UnsupportedEncodingException {
		status = false;
		this.input_stream = new BufferedReader(new InputStreamReader(input_stream));
		this.telem_frame = telem_frame;
		parser = new JSONParser();
	}
	
	public void stopThread() throws IOException {
		status = false;
		input_stream.close();
	}
	
	public boolean getThreadStatus() {
		return status;
	}

	@Override
	public synchronized void run() {
		status = true;
		try {
			String line;
			while(status) {
				if((line = input_stream.readLine()) != null) {
					telem_frame.updateSerialBar(line);
					JSONObject obj = (JSONObject) parser.parse(line);
					telem_frame.updateAllPanels(obj, (String) obj.get("message_id"));
				}
				else
					telem_frame.updateSerialBar("Waiting on Serial Port");
					Thread.sleep(1*60*1000);
			}
		} catch (IOException | InterruptedException | ParseException e) {
			e.printStackTrace();
		}
	}
}
