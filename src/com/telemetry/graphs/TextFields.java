package com.telemetry.graphs;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;

import javax.swing.*;

import com.telemetry.util.Tools;

/**
 * This class is currently unused, but might be brought back into GraphPanel if necessary
 * @author wsong
 *
 */
public class TextFields extends JPanel {
	private static final long serialVersionUID = 800851946880569758L;				
	private JPanel text_panel_powr  = new JPanel();
	private JPanel text_panel_speed = new JPanel();
	private JPanel text_panel_volt  = new JPanel();
	private JPanel text_panel_misc  = new JPanel();
	private JPanel text_panel_strat = new JPanel();
	private JLabel average_motor_power_day			= new JLabel("000.000");
	private JLabel average_speed_day				= new JLabel("000.000");
	private JLabel battery_and_solar_range			= new JLabel("000.000");
	private JLabel battery_and_solar_runtime_60_sec	= new JLabel("000.000");
	private JLabel battery_charge_remaining			= new JLabel("000.000");
	private JLabel battery_only_range_60_sec		= new JLabel("000.000");
	private JLabel battery_only_runtime_60_sec		= new JLabel("000.000");
	private JLabel battery_watt_hours				= new JLabel("000.000");
	private JLabel distance_left_in_day				= new JLabel("000.000");
	private JLabel motor_power_60_sec				= new JLabel("000.000");
	private JLabel motor_watt_hours					= new JLabel("000.000");
	private JLabel odometer							= new JLabel("000.000");
	private JLabel solar_energy_remaining			= new JLabel("000.000");
	private JLabel speed_60_sec						= new JLabel("000.000");
	private JLabel target_speed						= new JLabel("000.000");
	private JLabel target_watt_hour_per_mile		= new JLabel("000.000");
	private JLabel target_watt_hour_per_mile_60_sec	= new JLabel("000.000");
	private JLabel target_watt_hour_per_mile_day	= new JLabel("000.000");
	private JLabel time_left_in_day					= new JLabel("000.000");
	private JTextField solar_const_a = new JTextField(6);
	private JTextField solar_const_b = new JTextField(6);
	private JTextField solar_const_c = new JTextField(6);
	private JTextField solar_const_d = new JTextField(6);
	
	public TextFields(int width, int height) {
		setSize(width, height);
		setLayout(new GridBagLayout());
		
		text_panel_powr.setLayout(new GridBagLayout());
		text_panel_volt.setLayout(new GridBagLayout());
		text_panel_misc.setLayout(new GridBagLayout());
		text_panel_strat.setLayout(new GridBagLayout());
		text_panel_speed.setLayout(new GridBagLayout());
	
		insertComponents();
	}
	
	public void updateDataSet(HashMap<String, Double> calculation_data) {
//		average_motor_power_day				.setText(String.valueOf(data[0]));
		average_speed_day					.setText(String.valueOf(calculation_data.get("average_speed_day")));
		battery_and_solar_range				.setText(String.valueOf(calculation_data.get("battery_and_solar_range")));
		battery_and_solar_runtime_60_sec	.setText(String.valueOf(calculation_data.get("battery_and_solar_runtime_60_sec")));
		battery_charge_remaining			.setText(String.valueOf(calculation_data.get("battery_charge_remaining")));
		battery_only_range_60_sec			.setText(String.valueOf(calculation_data.get("battery_only_range_60_sec")));
		battery_only_runtime_60_sec			.setText(String.valueOf(calculation_data.get("battery_only_runtime_60_sec")));
		battery_watt_hours					.setText(String.valueOf(calculation_data.get("battery_watt_hours")));
		distance_left_in_day				.setText(String.valueOf(calculation_data.get("distance_left_in_day")));
		motor_power_60_sec					.setText(String.valueOf(calculation_data.get("motor_power_60_sec")));
		motor_watt_hours					.setText(String.valueOf(calculation_data.get("motor_watt_hours")));
		odometer							.setText(String.valueOf(calculation_data.get("odometer")));
		solar_energy_remaining				.setText(String.valueOf(calculation_data.get("solar_energy_remaining")));
		speed_60_sec						.setText(String.valueOf(calculation_data.get("speed_60_sec")));
		target_speed						.setText(String.valueOf(calculation_data.get("target_speed")));
		target_watt_hour_per_mile			.setText(String.valueOf(calculation_data.get("target_watt_hour_per_mile")));
		target_watt_hour_per_mile_60_sec	.setText(String.valueOf(calculation_data.get("target_watt_hour_per_mile_60_sec")));
		target_watt_hour_per_mile_day		.setText(String.valueOf(calculation_data.get("target_watt_hour_per_mile_day")));
		time_left_in_day					.setText(String.valueOf(calculation_data.get("time_left_in_day")));
	}

	private void insertComponents() {
		GridBagConstraints gbc = new GridBagConstraints();
		
		// GBC Constants
		gbc.anchor = GridBagConstraints.CENTER;
		gbc.insets = new Insets(3, 3, 3, 3);
		
		// Major Components
//		gbc.gridx = 0; 
//		gbc.gridy = 0;
//		gbc.gridheight = 2;
//		add(text_panel_volt, gbc);
//		gbc.gridheight = 1;
//		
//		gbc.gridx = 1;
//		add(text_panel_powr, gbc);
//		
//		gbc.gridy = 1;
//		add(text_panel_speed, gbc);
//		
//		gbc.gridx = 2;
//		gbc.gridy = 0;
//		add(text_panel_misc, gbc);
//		
//		gbc.gridy = 1;
//		add(text_panel_strat, gbc);
		
		gbc.gridx = 0;
		gbc.gridy = 0;
		add(text_panel_powr, gbc);
		
		gbc.gridx = 0;
		gbc.gridy = 1;
		add(text_panel_speed, gbc);
		
		gbc.gridx = 1;
		gbc.gridy = 0;
		add(text_panel_volt, gbc);

		gbc.gridx = 1;
		gbc.gridy = 1;
		add(text_panel_misc, gbc);

		//---------------------------------Power Fields-------------------------------------//
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.gridwidth = 3;
		JLabel power_title = new JLabel("Power");
		power_title.setFont(Tools.TITLE_FONT);
		text_panel_powr.add(power_title, gbc);
		gbc.gridwidth = 1;
		
		gbc.gridx = 0;
		gbc.gridy = 1;
		JLabel mwhl = new JLabel("Motor Watt Hours");
		mwhl.setFont(Tools.FIELD_FONT);
		text_panel_powr.add(mwhl, gbc);
		
		gbc.gridx = 1;
		gbc.gridy = 1;
		motor_watt_hours.setFont(Tools.FIELD_FONT);
		motor_watt_hours.setOpaque(true);
		motor_watt_hours.setBackground(Color.ORANGE);
		motor_watt_hours.setPreferredSize(motor_watt_hours.getMinimumSize());
		motor_watt_hours.setMaximumSize(motor_watt_hours.getMinimumSize());
		text_panel_powr.add(motor_watt_hours, gbc);
		
		gbc.gridx = 2;
		gbc.gridy = 1;
		JButton motor_wh_reset = new JButton("Reset");
		motor_wh_reset.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// Reset here or in calculation panel???
			}
		});
		text_panel_powr.add(new JButton("Reset"), gbc);
		
		gbc.gridx = 0;
		gbc.gridy = 2;
		JLabel batt_wh_l = new JLabel("Battery Watt Hours");
		batt_wh_l.setFont(Tools.FIELD_FONT);
		text_panel_powr.add(batt_wh_l, gbc);
		
		gbc.gridx = 1;
		gbc.gridy = 2;
		battery_watt_hours.setFont(Tools.FIELD_FONT);
		battery_watt_hours.setOpaque(true);
		battery_watt_hours.setBackground(Color.ORANGE);
		battery_watt_hours.setPreferredSize(battery_watt_hours.getMinimumSize());
		battery_watt_hours.setMaximumSize(battery_watt_hours.getMinimumSize());
		text_panel_powr.add(battery_watt_hours, gbc);
		
		gbc.gridx = 2;
		gbc.gridy = 2;
		JButton batt_wh_reset = new JButton("Reset");
		batt_wh_reset.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// Something here also...
			}
		});
		text_panel_powr.add(new JButton("Reset"), gbc);
		
		gbc.gridx = 0;
		gbc.gridy = 3;
		JLabel mtr_pwr_60_l = new JLabel("60 Second Motor Power");
		mtr_pwr_60_l.setFont(Tools.FIELD_FONT);
		text_panel_powr.add(mtr_pwr_60_l, gbc);
		
		gbc.gridx = 1;
		gbc.gridy = 3;
		motor_power_60_sec.setFont(Tools.FIELD_FONT);
		motor_power_60_sec.setOpaque(true);
		motor_power_60_sec.setBackground(Color.ORANGE);
		motor_power_60_sec.setPreferredSize(motor_power_60_sec.getMinimumSize());
		motor_power_60_sec.setMaximumSize(motor_power_60_sec.getMinimumSize());
		text_panel_powr.add(motor_power_60_sec, gbc);
		
		gbc.gridx = 0;
		gbc.gridy = 4;
		JLabel mtr_pwr_day_avg = new JLabel("Day's Avg Motor Power");
		mtr_pwr_day_avg.setFont(Tools.FIELD_FONT);
		text_panel_powr.add(mtr_pwr_day_avg, gbc);
		
		gbc.gridx = 1;
		gbc.gridy = 4;
		average_motor_power_day.setFont(Tools.FIELD_FONT);
		average_motor_power_day.setOpaque(true);
		average_motor_power_day.setBackground(Color.ORANGE);
		average_motor_power_day.setPreferredSize(average_motor_power_day.getMinimumSize());
		average_motor_power_day.setMaximumSize(average_motor_power_day.getMinimumSize());
		text_panel_powr.add(average_motor_power_day, gbc);
		
		//---------------------------------Speed Fields-------------------------------------//
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.gridwidth = 3;
		JLabel speed_title = new JLabel("Speed");
		speed_title.setFont(Tools.TITLE_FONT);
		text_panel_speed.add(speed_title, gbc);
		gbc.gridwidth = 1;
		
		gbc.gridx = 0;
		gbc.gridy = 1;
		JLabel tgt_speed_l = new JLabel("Target Speed");
		tgt_speed_l.setFont(Tools.FIELD_FONT);
		text_panel_speed.add(tgt_speed_l, gbc);
		
		gbc.gridx = 1;
		gbc.gridy = 1;
		target_speed.setFont(Tools.FIELD_FONT);
		target_speed.setOpaque(true);
		target_speed.setBackground(Color.ORANGE);
		target_speed.setPreferredSize(target_speed.getMinimumSize());
		target_speed.setMaximumSize(target_speed.getMinimumSize());
		text_panel_speed.add(target_speed, gbc);
		
		gbc.gridx = 0;
		gbc.gridy = 2;
		JLabel speed_60_l = new JLabel("60 Second Speed");
		speed_60_l.setFont(Tools.FIELD_FONT);
		text_panel_speed.add(speed_60_l, gbc);
		
		gbc.gridx = 1;
		gbc.gridy = 2;
		speed_60_sec.setFont(Tools.FIELD_FONT);
		speed_60_sec.setOpaque(true);
		speed_60_sec.setBackground(Color.ORANGE);
		speed_60_sec.setPreferredSize(speed_60_sec.getMinimumSize());
		speed_60_sec.setMaximumSize(speed_60_sec.getMinimumSize());
		text_panel_speed.add(speed_60_sec, gbc);
		
		gbc.gridx = 0;
		gbc.gridy = 3;
		JLabel speed_day_avg = new JLabel("Day's Average Speed");
		speed_day_avg.setFont(Tools.FIELD_FONT);
		text_panel_speed.add(speed_day_avg, gbc);
		
		gbc.gridx = 1;
		gbc.gridy = 3;
		average_speed_day.setFont(Tools.FIELD_FONT);
		average_speed_day.setOpaque(true);
		average_speed_day.setBackground(Color.ORANGE);
		average_speed_day.setPreferredSize(average_speed_day.getMinimumSize());
		average_speed_day.setMaximumSize(average_speed_day.getMinimumSize());
		text_panel_speed.add(average_speed_day, gbc);

		gbc.gridx = 2;
		gbc.gridy = 3;
		JButton avg_speed_day_reset = new JButton("Reset");
		avg_speed_day_reset.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Something here...
			}
		});
		text_panel_speed.add(new JButton("Reset"), gbc);
		
		//---------------------------------Battery/Watt Hour Fields-------------------------------------//
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.gridwidth = 3;
		JLabel wh_title = new JLabel("Watt Hour/Battery");
		wh_title.setFont(Tools.TITLE_FONT);
		text_panel_volt.add(wh_title, gbc);
		gbc.gridwidth = 1;

		gbc.gridx = 0;
		gbc.gridy = 1;
		JLabel tgt_wh_l = new JLabel("Target WH/mile");
		tgt_wh_l.setFont(Tools.FIELD_FONT);
		text_panel_volt.add(tgt_wh_l, gbc);
		
		gbc.gridx = 1;
		gbc.gridy = 1;
		target_watt_hour_per_mile.setFont(Tools.FIELD_FONT);
		target_watt_hour_per_mile.setOpaque(true);
		target_watt_hour_per_mile.setBackground(Color.ORANGE);
		target_watt_hour_per_mile.setPreferredSize(target_watt_hour_per_mile.getMinimumSize());
		target_watt_hour_per_mile.setMaximumSize(target_watt_hour_per_mile.getMinimumSize());
		text_panel_volt.add(target_watt_hour_per_mile, gbc);

		gbc.gridx = 0;
		gbc.gridy = 2;
		JLabel wh_60_l = new JLabel("60 Sec Motor WH/mile");
		wh_60_l.setFont(Tools.FIELD_FONT);
		text_panel_volt.add(wh_60_l, gbc);
		
		gbc.gridx = 1;
		gbc.gridy = 2;
		target_watt_hour_per_mile_60_sec.setFont(Tools.FIELD_FONT);
		target_watt_hour_per_mile_60_sec.setOpaque(true);
		target_watt_hour_per_mile_60_sec.setBackground(Color.ORANGE);
		target_watt_hour_per_mile_60_sec.setPreferredSize(target_watt_hour_per_mile_60_sec.getMinimumSize());
		target_watt_hour_per_mile_60_sec.setMaximumSize(target_watt_hour_per_mile_60_sec.getMinimumSize());
		text_panel_volt.add(target_watt_hour_per_mile_60_sec, gbc);

		gbc.gridx = 0;
		gbc.gridy = 3;
		JLabel wh_day_l = new JLabel("Day's WH/mile");
		wh_day_l.setFont(Tools.FIELD_FONT);
		text_panel_volt.add(wh_day_l, gbc);
		
		gbc.gridx = 1;
		gbc.gridy = 3;
		target_watt_hour_per_mile_day.setFont(Tools.FIELD_FONT);
		target_watt_hour_per_mile_day.setOpaque(true);
		target_watt_hour_per_mile_day.setBackground(Color.ORANGE);
		target_watt_hour_per_mile_day.setPreferredSize(target_watt_hour_per_mile_day.getMinimumSize());
		target_watt_hour_per_mile_day.setMaximumSize(target_watt_hour_per_mile_day.getMinimumSize());
		text_panel_volt.add(target_watt_hour_per_mile_day, gbc);
		
		gbc.gridx = 2;
		gbc.gridy = 3;
		JButton wh_day_reset = new JButton("Reset");
		wh_day_reset.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Something here...
			}
		});

		gbc.gridx = 0;
		gbc.gridy = 4;
		JLabel batt_runtime = new JLabel("Battery Runtime");
		batt_runtime.setFont(Tools.FIELD_FONT);
		text_panel_volt.add(batt_runtime, gbc);
		
		gbc.gridx = 1;
		gbc.gridy = 4;
		battery_only_runtime_60_sec.setFont(Tools.FIELD_FONT);
		battery_only_runtime_60_sec.setOpaque(true);
		battery_only_runtime_60_sec.setBackground(Color.ORANGE);
		battery_only_runtime_60_sec.setPreferredSize(battery_only_runtime_60_sec.getMinimumSize());
		battery_only_runtime_60_sec.setMaximumSize(battery_only_runtime_60_sec.getMinimumSize());
		text_panel_volt.add(battery_only_runtime_60_sec, gbc);

		gbc.gridx = 0;
		gbc.gridy = 5;
		JLabel batt_range = new JLabel("Battery Range");
		batt_range.setFont(Tools.FIELD_FONT);
		text_panel_volt.add(batt_range, gbc);
		
		gbc.gridx = 1;
		gbc.gridy = 5;
		battery_only_range_60_sec.setFont(Tools.FIELD_FONT);
		battery_only_range_60_sec.setOpaque(true);
		battery_only_range_60_sec.setBackground(Color.ORANGE);
		battery_only_range_60_sec.setPreferredSize(battery_only_range_60_sec.getMinimumSize());
		battery_only_range_60_sec.setMaximumSize(battery_only_range_60_sec.getMinimumSize());
		text_panel_volt.add(battery_only_range_60_sec, gbc);

		gbc.gridx = 0;
		gbc.gridy = 6;
		JLabel batt_solar_runtime = new JLabel("Battery + Solar Runtime");
		batt_solar_runtime.setFont(Tools.FIELD_FONT);
		text_panel_volt.add(batt_solar_runtime, gbc);
		
		gbc.gridx = 1;
		gbc.gridy = 6;
		battery_and_solar_runtime_60_sec.setFont(Tools.FIELD_FONT);
		battery_and_solar_runtime_60_sec.setOpaque(true);
		battery_and_solar_runtime_60_sec.setBackground(Color.ORANGE);
		battery_and_solar_runtime_60_sec.setPreferredSize(battery_and_solar_runtime_60_sec.getMinimumSize());
		battery_and_solar_runtime_60_sec.setMaximumSize(battery_and_solar_runtime_60_sec.getMinimumSize());
		text_panel_volt.add(battery_and_solar_runtime_60_sec, gbc);

		gbc.gridx = 0;
		gbc.gridy = 7;
		JLabel batt_solar_range = new JLabel("Battery + Solar Range");
		batt_solar_range.setFont(Tools.FIELD_FONT);
		text_panel_volt.add(batt_solar_range, gbc);
		
		gbc.gridx = 1;
		gbc.gridy = 7;
		battery_and_solar_range.setFont(Tools.FIELD_FONT);
		battery_and_solar_range.setOpaque(true);
		battery_and_solar_range.setBackground(Color.ORANGE);
		battery_and_solar_range.setPreferredSize(battery_and_solar_range.getMinimumSize());
		battery_and_solar_range.setMaximumSize(battery_and_solar_range.getMinimumSize());
		text_panel_volt.add(battery_and_solar_range, gbc);

		//---------------------------------Misc. Fields-------------------------------------//
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.gridwidth = 3;
		JLabel misc_title = new JLabel("Misc. Strategy");
		misc_title.setFont(Tools.TITLE_FONT);
		text_panel_misc.add(misc_title, gbc);
		gbc.gridwidth = 1;
		
		gbc.gridx = 0;
		gbc.gridy = 1;
		JLabel odometer_label = new JLabel("Odometer");
		odometer_label.setFont(Tools.FIELD_FONT);
		text_panel_misc.add(odometer_label, gbc);
		
		gbc.gridx = 1;
		gbc.gridy = 1;
		odometer.setFont(Tools.FIELD_FONT);
		odometer.setOpaque(true);
		odometer.setBackground(Color.ORANGE);
		text_panel_misc.add(odometer, gbc);
		
		gbc.gridx = 2;
		gbc.gridy = 1;
		JButton odometer_reset = new JButton("Reset");
		odometer_reset.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
			}
		});
		text_panel_misc.add(odometer_reset, gbc);
		
		gbc.gridx = 0;
		gbc.gridy = 2;
		JLabel dist_left_label = new JLabel("Distance Left in Day");
		dist_left_label.setFont(Tools.FIELD_FONT);
		text_panel_misc.add(dist_left_label, gbc);
		
		gbc.gridx = 1;
		gbc.gridy = 2;
		distance_left_in_day.setFont(Tools.FIELD_FONT);
		distance_left_in_day.setOpaque(true);
		distance_left_in_day.setBackground(Color.ORANGE);
		text_panel_misc.add(distance_left_in_day, gbc);
		
		gbc.gridx = 0;
		gbc.gridy = 3;
		JLabel time_left_label = new JLabel("Time Left in Day");
		time_left_label.setFont(Tools.FIELD_FONT);
		text_panel_misc.add(time_left_label, gbc);
		
		gbc.gridx = 1;
		gbc.gridy = 3;
		time_left_in_day.setFont(Tools.FIELD_FONT);
		time_left_in_day.setOpaque(true);
		time_left_in_day.setBackground(Color.ORANGE);
		text_panel_misc.add(time_left_in_day, gbc);
		
		gbc.gridx = 0;
		gbc.gridy = 4;
		JLabel battery_charge_remaining_label = new JLabel("Battery Charge Remaining");
		battery_charge_remaining_label.setFont(Tools.FIELD_FONT);
		text_panel_misc.add(battery_charge_remaining_label, gbc);
		
		gbc.gridx = 1;
		gbc.gridy = 4;
		battery_charge_remaining.setFont(Tools.FIELD_FONT);
		battery_charge_remaining.setOpaque(true);
		battery_charge_remaining.setBackground(Color.ORANGE);
		text_panel_misc.add(battery_charge_remaining, gbc);
		
		gbc.gridx = 0;
		gbc.gridy = 5;
		JLabel energy_remaining_label = new JLabel("Energy Remaining");
		energy_remaining_label.setFont(Tools.FIELD_FONT);
		text_panel_misc.add(energy_remaining_label, gbc);
		
		gbc.gridx = 1;
		gbc.gridy = 5;
		solar_energy_remaining.setFont(Tools.FIELD_FONT);
		solar_energy_remaining.setOpaque(true);
		solar_energy_remaining.setBackground(Color.ORANGE);
		text_panel_misc.add(solar_energy_remaining, gbc);
		
		//---------------------------------Strategy Fields-------------------------------------//
		gbc.gridx = 0;
		gbc.gridy = 0;
		JLabel strat_label = new JLabel("Strategy");
		strat_label.setFont(Tools.TITLE_FONT);
		text_panel_strat.add(strat_label, gbc);
		
		gbc.gridx = 1;
		gbc.gridy = 0;
		JButton calculate_button = new JButton("Recalculate Strategy");
		calculate_button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
			}
		});
		text_panel_strat.add(calculate_button, gbc);
		
		gbc.gridx = 0;
		gbc.gridy = 1;
		JLabel input_solar_const_a = new JLabel("Input Solar Const A");
		input_solar_const_a.setFont(Tools.FIELD_FONT);
		text_panel_strat.add(input_solar_const_a, gbc);
		
		gbc.gridx = 1;
		gbc.gridy = 1;
		text_panel_strat.add(solar_const_a, gbc);
		
		gbc.gridx = 0;
		gbc.gridy = 2;
		JLabel input_solar_const_b = new JLabel("Input Solar Const B");
		input_solar_const_b.setFont(Tools.FIELD_FONT);
		text_panel_strat.add(input_solar_const_b, gbc);
		
		gbc.gridx = 1;
		gbc.gridy = 2;
		text_panel_strat.add(solar_const_b, gbc);
		
		gbc.gridx = 0;
		gbc.gridy = 3;
		JLabel input_solar_const_c = new JLabel("Input Solar Const C");
		input_solar_const_c.setFont(Tools.FIELD_FONT);
		text_panel_strat.add(input_solar_const_c, gbc);
		
		gbc.gridx = 1;
		gbc.gridy = 3;
		text_panel_strat.add(solar_const_c, gbc);
		
		gbc.gridx = 0;
		gbc.gridy = 4;
		JLabel input_solar_const_d = new JLabel("Input Solar Const D");
		input_solar_const_d.setFont(Tools.FIELD_FONT);
		text_panel_strat.add(input_solar_const_d, gbc);
		
		gbc.gridx = 1;
		gbc.gridy = 4;
		text_panel_strat.add(solar_const_d, gbc);
	}
}