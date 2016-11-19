package com.telemetry.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class WeatherHandler {
	private String url_prefix = "https://api.darksky.net/forecast/8b535574cdf206319b5df5d257b1929e/";
	// Excludes some data blocks from HTML response
	private String url_excludes = "exclude=daily,flags";
	private JSONParser parser;

	public WeatherHandler() { parser = new JSONParser(); }
	
	/**
	 * Get weather data with specified lat, long, and time
	 * @param latitude
	 * @param longitude
	 * @param time Unix time
	 * @return
	 */
	public JSONObject getWeatherData(double latitude, double longitude) {
		long time = System.currentTimeMillis() / 1000L;
		String url_str = url_prefix + Double.toString(latitude) + "," 
									+ Double.toString(longitude) + ","
									+ Long.toString(time) + "?"
									+ url_excludes;
		try {
			String content = getHTML(url_str);
			System.out.println(content);
			JSONObject json_content = (JSONObject) parser.parse(content);
			return json_content;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static String getHTML(String url_str) throws Exception {
		StringBuilder content = new StringBuilder();
		URL url = new URL(url_str);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod("GET");
		BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
		String line;
		while((line = br.readLine()) != null) {
			content.append(line);
		}
		br.close();
		return content.toString();
	}
}
