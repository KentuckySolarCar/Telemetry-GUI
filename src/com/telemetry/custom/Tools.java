package com.telemetry.custom;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.json.simple.JSONObject;

public class Tools {
	public static final Color GREEN = new Color(198, 224, 180);
	public static final Color RED = new Color(224, 176, 132);
	public static final Font TITLE_FONT = new Font("Consolas", Font.BOLD, 18);
	public static final Font FIELD_FONT = new Font("Consolas", Font.PLAIN, 16);

	public static int stringToInt(String str_num) {
		String delimit = "[.]";
		String[] tokens = str_num.split(delimit);
		return Integer.parseInt(tokens[0]);
	}

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
	
	public static void changeFrameFonts(JFrame frame, Font font) {
		Component[] components = frame.getComponents();
		for(Component component : components) {
			if(component instanceof JLabel) {
				((JLabel) component).setFont(font);
			}
			else if(component instanceof JPanel) {
				System.out.println("TEST");
				changePanelFonts((JPanel) component, font);
			}
			else{
				// Do Nothing...
			}
		}
	}
	
	public static void changePanelFonts(JPanel panel, Font font) {	
		Component[] components = panel.getComponents();
		for(Component component : components) {
			if(component instanceof JLabel) {
				((JLabel) component).setFont(font);
			}
			else if(component instanceof JPanel) {
				changePanelFonts((JPanel) component, font);
			}
			else{
				// Do Nothing...
			}
		}
	}
}
