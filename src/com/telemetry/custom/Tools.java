package com.telemetry.custom;

import java.awt.Color;

import javax.swing.JLabel;

import org.json.simple.JSONObject;

public class Tools {
	public static String roundDouble(String double_str) {
		String delimit = "[.]";
		String[] tokens = double_str.split(delimit);
		if(tokens.length > 1) {
			if(tokens[1].length() >= 2)
				return tokens[0] + "." + tokens[1].substring(0, 3);
			else
				return tokens[0] + "." + tokens[1];
		}
		else
			return double_str;
	}
	
	public static String roundDouble(Double d) {
		String double_str = d.toString();
		String delimit = "[.]";
		String[] tokens = double_str.split(delimit);
		if(tokens.length > 1) {
			if(tokens[1].length() >= 3)
				return tokens[0] + "." + tokens[1].substring(0, 3);
			else
				return tokens[0] + "." + tokens[1];
		}
		else
			return double_str;
	}
	
	public static double getLabelDouble(JLabel label) {
		try {
			return Double.parseDouble(label.getText());
		} catch(NumberFormatException e) {
			return 0D;
		}
	}
	
	public static double getJSONDouble(JSONObject obj, String key) {
		return Double.parseDouble((String) obj.get(key));
	}

	public static void thresholdCheck(JLabel label, Double value, Double threshold, Color positive, Color negative) {
		if(value > threshold)
			label.setBackground(positive);
		else if (value < threshold)
			label.setBackground(negative);
	}
}
