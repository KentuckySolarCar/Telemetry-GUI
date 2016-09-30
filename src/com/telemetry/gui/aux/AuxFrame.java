package com.telemetry.gui.aux;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
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


import com.telemetry.data.CarData;
import com.telemetry.util.Tools;

public class AuxFrame extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8271261737440319731L;
	// Special fonts for aux frame
	private static final Font LABEL_FONT = new Font("Arial Black", Font.BOLD, 35); // 35
	private static final Font FIELD_FONT = new Font("Consolas", Font.PLAIN, 115); // 115
	
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
	
	private boolean flip_flop = true;
	
	public AuxFrame() {
		setLayout(new BorderLayout());
		insertComponents();
	}
	
	public void updatePanel(CarData data) {
		// Kind of a weird way to indicate status of telemetry...
		if(flip_flop) {
			runtime_indicator.setBackground(Color.RED);
			flip_flop = false;
		}
		else {
			runtime_indicator.setBackground(Color.GREEN);
			flip_flop = true;
		}

		// Retrieve specific data containers
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
	
	/**
	 * Called by a ticking thread to update the current system time
	 */
	public void updateRunTime() {
		Date date = new Date();
		time_f.setText(date_format.format(date));
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

	/**
	 * Helper function to set properties of labels
	 * @param label
	 * @return
	 */
	private JLabel initFieldLabel(JLabel label) {
		label.setOpaque(true);
		label.setFont(FIELD_FONT);
		return label;
	}
}
