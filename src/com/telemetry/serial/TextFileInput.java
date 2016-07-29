package com.telemetry.serial;

import org.json.simple.JSONObject;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map;
import java.util.Arrays;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.parser.ParseException;

import com.telemetry.gui.TelemetryFrame;
import com.telemetry.gui.device.MotorPanel;

import org.json.simple.parser.JSONParser;

public class TextFileInput extends Thread {
	private JSONParser parser;
	private BufferedReader buffer_reader;
	private TelemetryFrame telem_frame;
	
	public TextFileInput(String directory, TelemetryFrame telem_frame) throws FileNotFoundException {
		parser = new JSONParser();
		buffer_reader = new BufferedReader(new FileReader(directory));
		this.telem_frame = telem_frame;
	}

	@Override
	public void run() {
		try {
			String line;
			while((line = buffer_reader.readLine()) != null) {
				String msg = line.split(" ")[0];
				if(isValidMessage(msg)) {
					JSONObject obj = (JSONObject) parser.parse(msg);
					telem_frame.updateAllPanels(obj);
				}
				else {
					telem_frame.processInvalidData(msg);
				}
				Thread.sleep(100);
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
	
	private void processLogLine(String line) {
		String[] splits = line.split(" ");
		String processed = "";
		if(splits[0] != "*ERROR*") {
			
		}
	}
}

