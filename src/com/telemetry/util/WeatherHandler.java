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
	private String url_excludes = "exclude=[daily,flags]";
	private static double latitude = 38.043702D;
	private static double longitude = -84.504728D;
	private JSONParser parser;

	public static void main(String[] args) {
		WeatherHandler weather_handler = new WeatherHandler();
		JSONObject weather_data = weather_handler.getWeatherData(latitude, longitude);
		System.out.println(weather_data.toString());
	}
	
	public WeatherHandler() { 
		parser = new JSONParser();
	}
	
	public JSONObject getWeatherData(double latitude, double longitude) {
		String url_str = url_prefix + Double.toString(latitude) + "," 
									+ Double.toString(longitude) + ","
									+ url_excludes;
		try {
			String content = getHTML(url_str);
			System.out.println(content);
			JSONObject json_content = (JSONObject) parser.parse(content);
			return json_content;
		} catch (Exception e) {
			return null;
		}
	}
	
	// Done
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
