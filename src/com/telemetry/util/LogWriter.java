package com.telemetry.util;


import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.telemetry.data.CarData;
import com.telemetry.data.RaceData;
import com.telemetry.data.WeatherData;
import com.telemetry.gui.TelemetryFrame;

/**
 * Handles all the logging needs, including telemetry car data, race information, and weather
 * data at the time of logging
 * 
 * @author Weilian Song
 *
 */
public class LogWriter {
	private File json_file;
	private File car_data_file;
	private File race_data_file;
	private File weather_data_file;

	private BufferedWriter json_writer;
	private BufferedWriter car_data_writer;
	private BufferedWriter race_data_writer;
	private BufferedWriter weather_data_writer;

	private DateFormat date_format = new SimpleDateFormat("yyyy-mm-dd_HH:mm:ss");
	private boolean initialized_state = false;
	private TelemetryFrame telemetry_frame;
	
	/**
	 * Logger won't be active until writer is initialized
	 */
	public LogWriter(TelemetryFrame telemetry_frame) {
		this.telemetry_frame = telemetry_frame;
	}
	

	/**
	 * Initialize the writer to the specified folder
	 * @param dest_folder
	 * @return
	 */
	public boolean initializeWriter(String dest_folder) {
		// Make root directory to store logs in, naming scheme will be date of logging
		Date date = new Date();
		File root_folder = new File(dest_folder + File.separator + date_format.format(date));
		root_folder.mkdir();
		
		// 4 different log writer, one for raw json, one for car telemetry data, one for race data
		// (laps, penalties, etc.), and weather data at time of logging (if enabled)
		json_file         = new File(root_folder.getAbsolutePath() + File.separator + "json.txt");
		car_data_file     = new File(root_folder.getAbsolutePath() + File.separator + "data.csv");
		race_data_file    = new File(root_folder.getAbsolutePath() + File.separator + "data.csv");
		weather_data_file = new File(root_folder.getAbsolutePath() + File.separator + "data.csv");
		try {
			json_file.createNewFile();
			car_data_file.createNewFile();
			race_data_file.createNewFile();
			weather_data_file.createNewFile();
			json_writer = new BufferedWriter(new FileWriter(json_file));
			car_data_writer  = new BufferedWriter(new FileWriter(car_data_file));
			race_data_writer  = new BufferedWriter(new FileWriter(race_data_file));
			weather_data_writer  = new BufferedWriter(new FileWriter(weather_data_file));
			initialized_state = true;
			return true;
		} catch (IOException e) {
			return false;
		}
	}
	
	public boolean getState() {
		return initialized_state;
	}
	
	/**
	 * Close up all writers for safe exit
	 * @throws IOException
	 */
	public void closeWriter() throws IOException {
		if(initialized_state) {
			json_writer.close();
			car_data_writer.close();
			race_data_writer.close();
			weather_data_writer.close();
			initialized_state = false;
		}
	}
	
	/**
	 * Writes one line of json message to the json text file
	 * @param obj JSON object wanting to be written
	 * @throws IOException
	 */
	public void writeJSON(String json_str) throws IOException {
		if(!initialized_state)
			telemetry_frame.updateStatus("No writer is initialized!");
		else
			json_writer.write(json_str + "\n");
	}
	
	/**
	 * Not yet implemented
	 * @param data
	 */
	public void writeCarData(CarData data) {}
	
	/**
	 * Not yet implemented
	 * @param data
	 */
	public void writeRaceData(RaceData data) {}
	
	/**
	 * Not yet implemented
	 * @param data
	 */
	public void writeWeatherData(WeatherData data) {}
}