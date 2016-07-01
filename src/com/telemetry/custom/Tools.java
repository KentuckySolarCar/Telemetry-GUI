package com.telemetry.custom;

import javax.swing.JLabel;

public class Tools {
	public static String roundDouble(String double_str) {
		String delimit = "[.]";
		String[] tokens = double_str.split(delimit);
		if(tokens.length > 1) {
			if(tokens[1].length() >= 2)
				return tokens[0] + "." + tokens[1].substring(0, 2);
			else
				return tokens[0] + "." + tokens[1];
		}
		else
			return double_str;
	}
	
	public static double getLabelDouble(JLabel label) {
		return Double.parseDouble(label.getText());
	}
}
