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
import java.util.HashMap;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import org.json.simple.JSONObject;

import com.telemetry.custom.Tools;
import com.telemetry.strategy.EnergyModelFunctions;
import com.telemetry.strategy.DataContainer;

public class AuxFrame extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8271261737440319731L;
	private static final Font LABEL_FONT = new Font("Arial Black", Font.BOLD, 35); // 40
	private static final Font FIELD_FONT = new Font("Consolas", Font.PLAIN, 115); // 120
	
	private JPanel text_fields;

	private JLabel time_f            = new JLabel("00:00:00");
	private JLabel speed_f           = new JLabel("00.000");
	private JLabel bat_volt_min_f    = new JLabel("00.000");
	private JLabel bat_volt_max_f    = new JLabel("00.000");
	private JLabel bat_temp_max_f    = new JLabel("00.000");
	private JLabel motor_power_f     = new JLabel("00.000");
	private JLabel bat_power_f       = new JLabel("00.000");
	private JLabel array_power_f     = new JLabel("00.000");
	private JLabel soc_f             = new JLabel("00.000");
	private JLabel runtime_indicator   = new JLabel("Telemetry Operation");
	private JLabel zero_batt_indicator = new JLabel("Non-zero Values");
	private JTextArea notification_area = new JTextArea();
	private JScrollPane scroll_pane;
	private DateFormat date_format = new SimpleDateFormat("HH:mm:ss");
	
	// Temp until Motor BC is transmitted
	private boolean flip_flop = true;
	
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
		gbc.insets = new Insets(0, 5, 0, 5);
		gbc.anchor = GridBagConstraints.CENTER;
		
		// Status Lights
		gbc.gridx = 0;
		gbc.gridy = 0;
		runtime_indicator.setOpaque(true);
		runtime_indicator.setBackground(Color.ORANGE);
		text_fields.add(runtime_indicator, gbc);
		
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
						   "Max Bat Volt",
						   "S.O.C"};
		
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
		
		gbc.gridy++;
		soc_f = initFieldLabel(soc_f);
		text_fields.add(soc_f, gbc);

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
		gbc.fill   = GridBagConstraints.BOTH;
		notification_area.setFont(Tools.FIELD_FONT);
		notification_area.setEditable(false);
		notification_area.setOpaque(true);
		notification_area.setMaximumSize(notification_area.getMinimumSize());
		scroll_pane = new JScrollPane(notification_area);
		text_fields.add(scroll_pane, gbc);
		
		add(text_fields, BorderLayout.NORTH);
	}
	
	private JLabel initFieldLabel(JLabel label) {
		label.setOpaque(true);
		label.setFont(FIELD_FONT);
		return label;
	}
	
	public void updatePanel(DataContainer data) {
		HashMap<String, Double> motor_data = data.getMotorData();
		HashMap<String, Double> battery_data = data.getBatteryData();
		HashMap<String, Double> calculation_data = data.getCalculationData();
		
		speed_f.setText(Tools.roundDouble(motor_data.get("motor_speed")));

		motor_power_f.setText(Tools.roundDouble(calculation_data.get("motor_power")));
		bat_power_f.setText(Tools.roundDouble(calculation_data.get("battery_power")));
		array_power_f.setText(Tools.roundDouble(calculation_data.get("array_power")));
		soc_f.setText(Tools.roundDouble(calculation_data.get("state_of_charge")));

		bat_volt_min_f.setText(Tools.roundDouble(battery_data.get("batt_volt_min")));
		bat_volt_max_f.setText(Tools.roundDouble(battery_data.get("batt_volt_max")));
		bat_temp_max_f.setText(Tools.roundDouble(battery_data.get("batt_temp_max")));
	}
	
	public void updatePanel(HashMap<String, Double> dataset) {
		if(flip_flop) {
			runtime_indicator.setBackground(Color.RED);
			flip_flop = false;
		}
		else {
			runtime_indicator.setBackground(Color.GREEN);
			flip_flop = true;
		}
//		String type = (String) obj.get("message_id");
//		switch(type) {
//		case "motor": {
//			Double speed = Double.parseDouble((String) obj.get("S"));
//			motor_current = Double.parseDouble((String) obj.get("I"));
//			Double voltage = Double.parseDouble((String) obj.get("V"));
////			Double motor_power = motor_current * voltage;
//			Double motor_power = EnergyModelFunctions.getMotorPower(motor_current, voltage);
//			
//			speed_f.setText(Tools.roundDouble(speed));
//			motor_power_f.setText(Tools.roundDouble(motor_power));
//			Tools.thresholdCheck(motor_power_f, motor_power, 0D, Tools.RED, Tools.GREEN);
//			break;
//		}
//		case "bat_volt": {
//			Double bus_current = Tools.getJSONDouble(obj, "BC") / 1000;
//			Double v_min = Tools.getJSONDouble(obj, "Vmin") / 10000;
//			Double v_max = Tools.getJSONDouble(obj, "Vmax") / 10000;
//			Double v_avg = Tools.getJSONDouble(obj, "Vavg") / 10000;
//			
//			if(bus_current + v_min + v_max == 0) {
//				zero_batt_indicator.setBackground(Tools.RED);
//				return;
//			}
//
//			zero_batt_indicator.setBackground(Tools.GREEN);
////			Double bat_power = bus_current * v_avg * 35;
//			Double bat_power = EnergyModelFunctions.getBatteryPower(v_avg, bus_current);
//			Double array_power = EnergyModelFunctions.getArrayPower(motor_current, bus_current, v_avg);
////			Double array_power = 35 * (motor_current - bus_current) * v_avg;
//			if(array_power < 0D)
//				array_power = 0D;
//			
//			array_power_f.setText(Tools.roundDouble(array_power));
//			bat_power_f.setText(Tools.roundDouble(bat_power));
//			Tools.thresholdCheck(bat_power_f, bus_current, 0D, Tools.RED, Tools.GREEN);
//			bat_volt_min_f.setText(Tools.roundDouble(v_min));
//			bat_volt_max_f.setText(Tools.roundDouble(v_max));
//			break;
//		}
//		case "bat_temp": {
//			Double max_temp = Tools.getJSONDouble(obj, "Tmax");
//			
//			if(max_temp == 0) {
//				zero_batt_indicator.setBackground(Tools.RED);
//				return;
//			}
//			else
//				zero_batt_indicator.setBackground(Tools.GREEN);
//			
//			bat_temp_max_f.setText(Tools.roundDouble(max_temp));
//			Tools.thresholdCheck(bat_temp_max_f, max_temp, 45D, Tools.RED, Tools.GREEN);
//			break;
//		}
//		case "messages": {
//			String[] messages = (String[]) obj.get("Messages");
//			for(String message : messages)
//				notification_area.append(message + "\n");
//		}
//		default:
//			break;
//		}
		double motor_power = dataset.get("motor_power");
		double batt_power = dataset.get("batt_power");
		double array_power = dataset.get("array_power");
		double state_of_charge = dataset.get("state_of_charge");
		double bat_volt_min = dataset.get("batt_v_min");
		double bat_volt_max = dataset.get("batt_v_max");
		double bat_temp_max = dataset.get("batt_temp_max");
		double speed        = dataset.get("average_speed");
		
		motor_power_f.setText(Tools.roundDouble(motor_power));
		bat_power_f.setText(Tools.roundDouble(batt_power));
		array_power_f.setText(Tools.roundDouble(array_power));
		bat_volt_min_f.setText(Tools.roundDouble(bat_volt_min));
		bat_volt_max_f.setText(Tools.roundDouble(bat_volt_max));
		bat_temp_max_f.setText(Tools.roundDouble(bat_temp_max));
		speed_f.setText(Tools.roundDouble(speed));
		soc_f.setText(Tools.roundDouble(state_of_charge));
		
		Tools.thresholdCheck(motor_power_f, motor_power, 0D, Tools.RED, Tools.GREEN);
		Tools.thresholdCheck(bat_power_f, motor_power, 0D, Tools.RED, Tools.GREEN);
		Tools.thresholdCheck(array_power_f, motor_power, 0D, Tools.RED, Tools.GREEN);
	}
	
	public void processMessages(String[] messages) {
		
	}
	
	public void updateRunTime() {
		Date date = new Date();
		time_f.setText(date_format.format(date));
	}
}
