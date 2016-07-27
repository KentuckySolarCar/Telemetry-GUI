package com.telemetry.serial;

import java.util.List;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.telemetry.custom.Tools;
import com.telemetry.gui.TelemetryFrame;

public class SerialPortReader extends Thread {
	
	private BufferedReader input_stream;
	private JSONParser parser;
	private boolean status;
	private TelemetryFrame telem_frame;
	private boolean logging = false;
	private File log_file;
	private BufferedWriter writer;
	
	public SerialPortReader (InputStream input_stream, TelemetryFrame telem_frame) throws UnsupportedEncodingException {
		status = false;
		this.input_stream = new BufferedReader(new InputStreamReader(input_stream));
		this.telem_frame = telem_frame;
		parser = new JSONParser();
	}
	
	public void stopThread() throws IOException {
		status = false;
		logging = false;
		input_stream.close();
	}
	
	public boolean getThreadStatus() {
		return status;
	}
	
	public void enableLogging(String log_filename) throws IOException {
		log_file = new File(log_filename);
		if(!log_file.exists())
			log_file.createNewFile();
		writer = new BufferedWriter(new FileWriter(log_file));
		logging = true;
	}

	@Override
	public synchronized void run() {
		status = true;
		while(status) {
			String line;
			try {
				if((line = input_stream.readLine()) != null) {
					telem_frame.updateStatus(line);
					if(isValidMessage(line)) {
						JSONObject obj = (JSONObject) parser.parse(line);
						telem_frame.updateAllPanels(obj);
					}
					else {
						telem_frame.processInvalidData(line);
						line = "*ERROR* " + line;
					}
					if(logging)
						writer.write(line + "\n");
				}
				else
					input_stream.wait(3*60*1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (IOException e) {
				telem_frame.updateStatus("Waiting on Serial Port");
				try {
					Thread.sleep(50);
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			} catch (ParseException e) {
				// Do Nothing...
				System.out.println("Parse Error");
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
