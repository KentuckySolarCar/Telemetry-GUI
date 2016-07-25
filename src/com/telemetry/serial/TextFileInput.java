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
				if(isValidMessage(line)) {
					JSONObject obj = (JSONObject) parser.parse(line);
					telem_frame.updateAllPanels(obj);
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
			if(parsed_string.size() != 3) {
				System.out.println("Wrong Time");
				throw new ParseException(0);
			}
			String type = (String) obj.get("message_id");
			switch(type) {
			case "motor":
				String speed_instant = (String) obj.get("S");
				String current_instant = (String) obj.get("I");
				break;
			case "bat_volt":
				String v_average       = (String) obj.get("Vavg");
				String v_max           = (String) obj.get("Vmax");
				String v_min           = (String) obj.get("Vmin");
				String current_average = (String) obj.get("BC");
				break;
			case "bat_temp":
				String ave_temp = (String) obj.get("Tavg");
				String max_temp = (String) obj.get("Tmax");
				String min_temp = (String) obj.get("Tmin");
				break;
			}
			return true;
		} catch (ParseException e) {
			telem_frame.updateStatus("Parse Error");
			return false;
		} catch (NullPointerException e) {
			telem_frame.updateStatus("Parse Error");
			return false;
		}
	}
}

