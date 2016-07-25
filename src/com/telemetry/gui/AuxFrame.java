package com.telemetry.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GraphicsConfiguration;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import org.json.simple.JSONObject;

import com.telemetry.custom.Tools;

public class AuxFrame extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8271261737440319731L;
	private static final Font LABEL_FONT = new Font("Arial Black", Font.BOLD, 40); // 60
	private static final Font FIELD_FONT = new Font("Consolas", Font.PLAIN, 120); // 140
	
	private JPanel text_fields;

	private JLabel time_f            = new JLabel("00:00:00");
	private JLabel speed_f           = new JLabel("00.000");
	private JLabel bat_volt_min_f    = new JLabel("00.000");
	private JLabel bat_volt_max_f    = new JLabel("00.000");
	private JLabel bat_temp_max_f    = new JLabel("00.000");
	private JLabel motor_power_f     = new JLabel("00.000");
	private JLabel bat_power_f       = new JLabel("00.000");
	private JLabel array_power_f     = new JLabel("00.000");
	private JLabel input_indicator   = new JLabel("Continuous Input");
	private JLabel zero_batt_indicator = new JLabel("Non-zero Values");
	private JTextArea notification_area = new JTextArea();
	private DateFormat date_format = new SimpleDateFormat("HH:mm:ss");
	
	// Temp until Motor BC is transmitted
	private Double bat_v_avg = 0D;
	private Double motor_current = 0D;
	
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
						   "Bat Power", 
						   "Motor Power", 
						   "Array Power",
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
		bat_power_f = initFieldLabel(bat_power_f);
		text_fields.add(bat_power_f, gbc);

		gbc.gridy++;
		motor_power_f = initFieldLabel(motor_power_f);
		text_fields.add(motor_power_f, gbc);
		
		gbc.gridy++;
		array_power_f = initFieldLabel(array_power_f);
		text_fields.add(array_power_f, gbc);

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
		gbc.gridy += 2;
		gbc.gridwidth = 2;
		gbc.anchor = GridBagConstraints.CENTER;
		time_f = initFieldLabel(time_f);
		text_fields.add(time_f, gbc);
		gbc.gridwidth = 1;
		
		// Notification area
		gbc.gridy += 2;
		gbc.gridwidth = 2;
		gbc.anchor = GridBagConstraints.CENTER;
		
		add(text_fields, BorderLayout.NORTH);
		
	}
	
	private JLabel initFieldLabel(JLabel label) {
		label.setOpaque(true);
		label.setFont(FIELD_FONT);
		return label;
	}
	
	public void updatePanel(JSONObject obj) {
		Date date = new Date();
		time_f.setText(date_format.format(date));
		String type = (String) obj.get("message_id");
		switch(type) {
		case "motor": {
			Double speed = Double.parseDouble((String) obj.get("S"));
			motor_current = Double.parseDouble((String) obj.get("I"));
			Double motor_power = motor_current * bat_v_avg * 35;
			
			speed_f.setText(Tools.roundDouble(speed));
			motor_power_f.setText(Tools.roundDouble(motor_power));
			Tools.thresholdCheck(motor_power_f, motor_power, 0D, Tools.RED, Tools.GREEN);
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

			zero_batt_indicator.setBackground(Tools.GREEN);
			bat_v_avg = v_avg;
			Double bat_power = bus_current * bat_v_avg * 35;
			Double array_power = 35 * (motor_current - bus_current) * v_avg;
			if(array_power < 0D)
				array_power = 0D;
			
			array_power_f.setText(Tools.roundDouble(array_power));
			bat_power_f.setText(Tools.roundDouble(bat_power));
			Tools.thresholdCheck(bat_power_f, bus_current, 0D, Tools.RED, Tools.GREEN);
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
