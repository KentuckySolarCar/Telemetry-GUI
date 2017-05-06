package com.telemetry.serial;

import java.util.List;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.telemetry.gui.TelemetryFrame;

public class SerialPortReader extends Thread {
	
	private BufferedReader input_stream;
	private JSONParser parser;
	private boolean thread_status;
	private TelemetryFrame telem_frame;
	
	public SerialPortReader (InputStream input_stream, TelemetryFrame telem_frame) throws UnsupportedEncodingException {
		thread_status = false;
		this.input_stream = new BufferedReader(new InputStreamReader(input_stream));
		this.telem_frame = telem_frame;
		parser = new JSONParser();
	}
	
	public void stopThread() throws IOException {
		thread_status = false;
	}
	
	public boolean getThreadStatus() {
		return thread_status;
	}

	@Override
	public synchronized void run() {
		thread_status = true;
		while(thread_status) {
			String line = "";
			try {
				line = input_stream.readLine();
				// Always print out the latest telemetry msg to telemetry frame's status bar
				telem_frame.updateStatus(line);
				// Check to see if message is valid
				if(isValidMessage(line)) {
					JSONObject obj = (JSONObject) parser.parse(line);
					telem_frame.updateAllPanels(obj);
				}
				// Do something speical if message is invalid, as in don't update panel and just add
				// received message to the log panel, with an ERROR prefix
				else {
					telem_frame.processInvalidData(line);
					line = "*ERROR* " + line;
				}
				// Let telem_frame's logger handle the logging of raw telemetry message to a file
				if(telem_frame.getLogger().getState() == true) {
					telem_frame.getLogger().writeJSON(line);
				}
			} catch (IOException e) {
				// If this catch is reached, that means we are getting no bytes through telemetry,
				// which the thread will wait indefinitely until new data is sent over.
				telem_frame.updateStatus("Waiting on Serial Port");
				try {
					Thread.sleep(50);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
			} catch (ParseException e) {
				// This does nothing, since we already manually validated the JSON string
			}
		}
	}
	
	/**
	 * This function literally tries to read every possible field for a given type of message, and only 
	 * returns true if all fields can be read for that certain message type
	 * @param line
	 * @return validity of JSON message
	 */
	@SuppressWarnings("unused")
	private boolean isValidMessage(String line) {
		try {
			JSONObject obj = (JSONObject) parser.parse(line);
			String time = (String) obj.get("Time");
			List<String> parsed_string = Arrays.asList(time.split(":"));
			if(parsed_string.size() != 3)
				throw new ParseException(0);
			String type = (String) obj.get("message_id");
			switch(type) {
			case "motor":
				Double speed_instant = Double.parseDouble((String) obj.get("S"));
				Double current_instant = Double.parseDouble((String) obj.get("I"));
				Double voltage_instant = Double.parseDouble((String) obj.get("V"));
				break;
			case "bat_volt":
				Double v_average       = Double.parseDouble((String) obj.get("Vavg"));
				Double v_max           = Double.parseDouble((String) obj.get("Vmax"));
				Double v_min           = Double.parseDouble((String) obj.get("Vmin"));
				Double current_average = Double.parseDouble((String) obj.get("BC"));
				break;
			case "bat_temp":
				Double ave_temp = Double.parseDouble((String) obj.get("Tavg"));
				Double max_temp = Double.parseDouble((String) obj.get("Tmax"));
				Double min_temp = Double.parseDouble((String) obj.get("Tmin"));
				break;
			case "messages":
				String[] messages = (String[]) obj.get("Messages");
			}
			return true;
		} catch (ParseException e) {
			telem_frame.updateStatus("Parse Error");
			return false;
		} catch (NullPointerException e) {
			telem_frame.updateStatus("Parse Error");
			return false;
		} catch (Exception e) {
			telem_frame.updateStatus("Unknown Error");
			return false;
		}
	}
}
