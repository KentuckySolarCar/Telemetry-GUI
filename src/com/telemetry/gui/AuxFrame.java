package com.telemetry.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GraphicsConfiguration;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.json.simple.JSONObject;

import com.telemetry.custom.Tools;

public class AuxFrame extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8271261737440319731L;
	private static final double speed_conversion = 0.223693629;
	private static final Font LABEL_FONT = new Font("Arial Black", Font.BOLD, 20); // 60
	private static final Font FIELD_FONT = new Font("Consolas", Font.PLAIN, 20); // 140
	
	private JPanel text_fields;

	private JLabel time_f            = new JLabel("00:00:00");
	private JLabel speed_f           = new JLabel("00.000");
	private JLabel motor_current_f   = new JLabel("00.000");
	private JLabel battery_current_f = new JLabel("00.000");
	private JLabel bat_volt_min_f    = new JLabel("00.000");
	private JLabel bat_volt_max_f    = new JLabel("00.000");
	private JLabel bat_temp_max_f    = new JLabel("00.000");
	private JLabel input_indicator   = new JLabel("Continuous Input");
	private JLabel zero_batt_indicator = new JLabel("Non-zero Values");
	
	public AuxFrame(GraphicsConfiguration target_screen_id) {
		super(target_screen_id);
		setLayout(new BorderLayout());
		
		insertComponents();
	}
	
	private void insertComponents() {
		text_fields = new JPanel();
		text_fields.setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		
		// GBC static properties
		gbc.insets = new Insets(9, 5, 9, 5);
		gbc.anchor = GridBagConstraints.CENTER;
		
		// Status Lights
		gbc.gridx = 0;
		gbc.gridy = 0;
		input_indicator.setOpaque(true);
		input_indicator.setBackground(Color.ORANGE);
		text_fields.add(input_indicator, gbc);
		
		gbc.gridx = 1;
		gbc.gridy = 0;
		zero_batt_indicator.setOpaque(true);
		zero_batt_indicator.setBackground(Color.ORANGE);
		text_fields.add(zero_batt_indicator, gbc);
		
		// Labels
		gbc.gridx = 1;
		gbc.gridy = 1;
		gbc.anchor = GridBagConstraints.WEST;
		String[] labels = {"Car Speed", 
						   "Bat Current", 
						   "Motor Current", 
						   "Min Bat Volt", 
						   "Max Bat Temp",
						   "Max Bat Volt"};
		
		for(String label : labels) {
			JLabel l = new JLabel(label);
			l.setFont(LABEL_FONT);
			text_fields.add(l, gbc);
			gbc.gridy++;
		}
		
		// Time Label needs to be on different line
		gbc.gridx = 0;
		gbc.anchor = GridBagConstraints.CENTER;
		gbc.gridwidth = 2;
		JLabel time_l = new JLabel("Current Time");
		time_l.setFont(LABEL_FONT);
		text_fields.add(time_l, gbc);
		gbc.gridwidth = 1;
		
		// Fields
		gbc.anchor = GridBagConstraints.WEST;
		
		gbc.gridx = 0;
		gbc.gridy = 1;
		speed_f = initFieldLabel(speed_f);
		text_fields.add(speed_f, gbc);

		gbc.gridy++;
		battery_current_f = initFieldLabel(battery_current_f);
		text_fields.add(battery_current_f, gbc);

		gbc.gridy++;
		motor_current_f = initFieldLabel(motor_current_f);
		text_fields.add(motor_current_f, gbc);

		gbc.gridy++;
		bat_volt_min_f = initFieldLabel(bat_volt_min_f);
		text_fields.add(bat_volt_min_f, gbc);

		gbc.gridy++;
		bat_temp_max_f = initFieldLabel(bat_temp_max_f);
		text_fields.add(bat_temp_max_f, gbc);

		gbc.gridy++;
		bat_volt_max_f = initFieldLabel(bat_volt_max_f);
		text_fields.add(bat_volt_max_f, gbc);

		// Time field needs to be on different line
		gbc.gridy+= 2;
		gbc.gridwidth = 2;
		gbc.anchor = GridBagConstraints.CENTER;
		time_f = initFieldLabel(time_f);
		text_fields.add(time_f, gbc);
		gbc.gridwidth = 1;
		
		add(text_fields, BorderLayout.NORTH);
	}
	
	private JLabel initFieldLabel(JLabel label) {
		label.setOpaque(true);
		label.setFont(FIELD_FONT);
		return label;
	}
	
	public void updatePanel(JSONObject obj) {
		String type = (String) obj.get("message_id");
		switch(type) {
		case "motor": {
			Double speed = Double.parseDouble((String) obj.get("S")) * speed_conversion;
			Double motor_current = Double.parseDouble((String) obj.get("I")) / 1000;
			
			speed_f.setText(Tools.roundDouble(speed));
			motor_current_f.setText(Tools.roundDouble(motor_current));
			Tools.thresholdCheck(motor_current_f, motor_current, 0D, Tools.RED, Tools.GREEN);
			break;
		}
		case "bat_volt": {
			Double bus_current = Tools.getJSONDouble(obj, "BC") / 1000;
			Double v_min = Tools.getJSONDouble(obj, "Vmin") / 10000;
			Double v_max = Tools.getJSONDouble(obj, "Vmax") / 10000;
			Double v_avg = Tools.getJSONDouble(obj, "Vavg") / 10000;
			
			if(bus_current + v_min + v_max == 0) {
				zero_batt_indicator.setBackground(Tools.RED);
				return;
			}
			else
				zero_batt_indicator.setBackground(Tools.GREEN);
			
			battery_current_f.setText(Tools.roundDouble(bus_current));
			Tools.thresholdCheck(battery_current_f, bus_current, 0D, Tools.RED, Tools.GREEN);
			bat_volt_min_f.setText(Tools.roundDouble(v_min));
			bat_volt_max_f.setText(Tools.roundDouble(v_max));
			break;
		}
		case "bat_temp": {
			Double max_temp = Tools.getJSONDouble(obj, "Tmax");
			
			if(max_temp == 0) {
				zero_batt_indicator.setBackground(Tools.RED);
				return;
			}
			else
				zero_batt_indicator.setBackground(Tools.GREEN);
			
			bat_temp_max_f.setText(Tools.roundDouble(max_temp));
			Tools.thresholdCheck(bat_temp_max_f, max_temp, 45D, Tools.RED, Tools.GREEN);
			break;
		}
		default:
			break;
		}
		
	}
	
	public void setInputIndicator(Color color) {
		input_indicator.setBackground(color);
	}
}
