package com.telemetry.serial;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
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
		writer.close();
	}
	
	public boolean getThreadStatus() {
		return status;
	}
	
	public void enableLogging(String log_filename) throws IOException {
		log_file = new File(log_filename);
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
					telem_frame.updateSerialBar(line);
					try {
						JSONObject obj = (JSONObject) parser.parse(line);
						telem_frame.updateAllPanels(obj, (String) obj.get("message_id"));
						if(logging) {
							writer.write(line + "\n");
						}
					} catch(Exception e) {}
				}
				else {
					telem_frame.updateSerialBar("Waiting on Serial Port");
					input_stream.wait(3*60*1000);
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (IOException e) {
				telem_frame.updateSerialBar("Waiting on Serial Port");
				try {
					Thread.sleep(50);
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
//				char input;
//				if((input = (char) input_stream.read()) != '\n') {
//					line += input;
//				}
//				telem_frame.updateSerialBar(line);
//				JSONObject obj = (JSONObject) parser.parse(line);
//				telem_frame.updateAllPanels(obj, (String) obj.get("message_id"));
		}
	}
}
