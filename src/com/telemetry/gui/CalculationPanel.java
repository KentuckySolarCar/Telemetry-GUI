package com.telemetry.gui;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.time.LocalDateTime;

import javax.swing.*;

import org.json.simple.JSONObject;

import com.telemetry.equations.EnergyModelFunctions; 

public class CalculationPanel extends JPanel{
	private static final long serialVersionUID = -2305051374349231050L;
	private JLabel array_power                     = new JLabel("VALUE");
//	private JLabel gross_watt_hours                = new JLabel("VALUE");
//	private JLabel net_watt_hours                  = new JLabel("VALUE");
	private JLabel average_speed                   = new JLabel("VALUE");
//	private JLabel average_gross_power             = new JLabel("VALUE");
//	private JLabel average_net_power               = new JLabel("VALUE");
//	private JLabel gross_average_power             = new JLabel("VALUE");
//	private JLabel gross_average_watt_hour         = new JLabel("VALUE");
	private JLabel battery_only_run_time_60_sec    = new JLabel("VALUE");
	private JLabel battery_only_range_60_sec              = new JLabel("VALUE");
	private JLabel battery_and_solar_run_time_60_sec      = new JLabel("VALUE");
	private JLabel battery_and_solar_range         = new JLabel("VALUE");
	private JLabel battery_charge_remaining        = new JLabel("VALUE");
	private JLabel solar_energy_remaining          = new JLabel("VALUE");
	private JLabel motor_power                     = new JLabel("VALUE");
	private JLabel motor_watt_hours                = new JLabel("VALUE");
	private JLabel battery_watt_hours              = new JLabel("VALUE");
	private JLabel motor_power_60_sec              = new JLabel("VALUE");
	private JLabel target_speed                    = new JLabel("VALUE");
	private JLabel speed_60_sec                    = new JLabel("VALUE");
	private JLabel target_watt_hour_per_mile       = new JLabel("VALUE");
	private JLabel target_watt_hour_per_mile_60_sec = new JLabel("VALUE");
	private JLabel target_watt_hour_per_mile_day   = new JLabel("VALUE");
	private JLabel distance_left_in_day            = new JLabel("VALUE");
	private JLabel time_left_in_day                = new JLabel("VALUE");
	private JLabel target_average_motor_power      = new JLabel("VALUE");
	private JLabel target_motor_energy             = new JLabel("VALUE");
	private JLabel target_battery_state_of_charge  = new JLabel("VALUE");
	private JLabel predicted_array_power           = new JLabel("VALUE");
	private JLabel time_elapsed                    = new JLabel("VALUE");
	
	
	public CalculationPanel(int tab_panel_x, int tab_panel_y) {
		super();
		
		setSize(tab_panel_x, tab_panel_y);
	    setLayout(new GridLayout(24, 5, 10, 15));
		
		insertFields();
	}
	
	public void updatePanel(JSONObject device_data) {
		// Motor data includes:
		//	ave_speed, ave_current, amp_sec, watt_sec, odometer
		JSONObject motor_data   = (JSONObject) device_data.get("motor_data");
		// Battery data includes:
		//	batman: ave_temp, v_average, v_min, current_average
		//	robin : ave_temo, v_average, v_min, current_average
		JSONObject battery_data = (JSONObject) device_data.get("battery_data");
		JSONObject batman_data  = (JSONObject) battery_data.get("batman");
		JSONObject robin_data   = (JSONObject) battery_data.get("robin");
		
		double ave_speed   = (Double) motor_data.get("ave_speed");
		double ave_current = (Double) motor_data.get("ave_current");
		double amp_sec     = (Double) motor_data.get("amp_sec");
		double watt_sec    = (Double) motor_data.get("watt_sec");
		double odometer    = (Double) motor_data.get("odometer");
		
		double b_ave_temp        = (Double) batman_data.get("ave");
		double b_v_average       = (Double) batman_data.get("v_average");
		double b_v_min           = (Double) batman_data.get("v_min");
		double b_current_average = (Double) batman_data.get("current_average");
		
		double r_ave_temp        = (Double) robin_data.get("ave");
		double r_v_average       = (Double) robin_data.get("v_average");
		double r_v_min           = (Double) robin_data.get("v_min");
		double r_current_average = (Double) robin_data.get("current_average");
		
		double total_batt_v = EnergyModelFunctions.getTotalBatteryVoltage(b_v_average, r_v_average);
		
		LocalDateTime localTime 	= LocalDateTime.now();
		int GMTOffset           	= -5;
		double latitude         	= 38.035831;
		double longitude        	= -84.506800;
		int dayTypeCode 			= 0;
		double startChargeInMorning = 7;
		double startRacingInMorning = 9;
		double chargeInEveTime 		= 17;
		double stopChargingInEve 	= 20;
		
		array_power                    .setText(Double.toString(EnergyModelFunctions.getArrayPower(ave_current, b_current_average, r_current_average, b_v_average, r_v_average)));
//		gross_watt_hours               .setText("Needs Implementing");
//		net_watt_hours                 .setText("Needs Implementing");
		average_speed                  .setText(Double.toString(ave_speed));
//		average_gross_power            .setText("Needs Implementing");
//		average_net_power              .setText("Needs Implementing");
//		gross_average_power            .setText("Needs Implementing");
//		gross_average_watt_hour        .setText("Needs Implementing");
		battery_only_run_time_60_sec.setText("Needs Implementing");
		battery_only_range_60_sec             .setText("Needs Implementing");
		battery_and_solar_run_time_60_sec     .setText("Needs Implementing");
		battery_and_solar_range        .setText("Needs Implementing");
		battery_charge_remaining       .setText("Needs Implementing");
		solar_energy_remaining         .setText(Double.toString(EnergyModelFunctions.getEnergyLeftInDay(localTime, GMTOffset, latitude, longitude, dayTypeCode, startChargeInMorning, startRacingInMorning, chargeInEveTime, stopChargingInEve)));
		motor_power                    .setText(Double.toString(EnergyModelFunctions.getMotorPower(ave_current, total_batt_v)));
		
		validate();
		repaint();
	}

	private void insertFields() {
		add(new JLabel("Array Power")); add(array_power); add(new JSeparator(SwingConstants.VERTICAL));
		add(new JLabel("Average Speed")); add(average_speed); add(new JSeparator(SwingConstants.HORIZONTAL));
		add(new JLabel("Battery Only Run Time (60 secs)")); add(battery_only_run_time_60_sec);add(new JSeparator(SwingConstants.VERTICAL));
		add(new JLabel("Battery Only Range (60 secs)")); add(battery_only_range_60_sec);add(new JSeparator(SwingConstants.HORIZONTAL));
		add(new JLabel("Battery and Solar Run Time ")); add(battery_and_solar_run_time_60_sec);add(new JSeparator(SwingConstants.VERTICAL));
		add(new JLabel("Battery and Solar Range")); add(battery_and_solar_range);add(new JSeparator(SwingConstants.HORIZONTAL));
		add(new JLabel("Battery Charge Remaining")); add(battery_charge_remaining);add(new JSeparator(SwingConstants.VERTICAL));
		add(new JLabel("Solar Energy Remaining")); add(solar_energy_remaining);add(new JSeparator(SwingConstants.HORIZONTAL));
		add(new JLabel("Motor Power")); add(motor_power);add(new JSeparator(SwingConstants.VERTICAL));
		add(new JLabel("Motor Watt Hours")); add(motor_watt_hours);add(new JSeparator(SwingConstants.HORIZONTAL));
		add(new JLabel("Battery Watt Hours")); add(battery_watt_hours);add(new JSeparator(SwingConstants.VERTICAL));
		add(new JLabel("Motor Power (60 secs)")); add(motor_power_60_sec);add(new JSeparator(SwingConstants.HORIZONTAL));
		add(new JLabel("Target Speed")); add(target_speed);add(new JSeparator(SwingConstants.VERTICAL));
		add(new JLabel("Speed (60 sec)")); add(speed_60_sec);add(new JSeparator(SwingConstants.HORIZONTAL));
		add(new JLabel("Target Watt Hour Per Mile")); add(target_watt_hour_per_mile);add(new JSeparator(SwingConstants.VERTICAL));
		add(new JLabel("Target Watt Hour Per Mile (60 sec)")); add(target_watt_hour_per_mile_60_sec);add(new JSeparator(SwingConstants.HORIZONTAL));
		add(new JLabel("Target Watt Hour Per Mile (Day)")); add(target_watt_hour_per_mile_day);add(new JSeparator(SwingConstants.VERTICAL));
		add(new JLabel("Distance Left in Day")); add(distance_left_in_day);add(new JSeparator(SwingConstants.HORIZONTAL));
		add(new JLabel("Time Left in Day")); add(time_left_in_day);add(new JSeparator(SwingConstants.VERTICAL));
		add(new JLabel("Target Average Motor Power")); add(target_average_motor_power);add(new JSeparator(SwingConstants.HORIZONTAL));
		add(new JLabel("Target Motor Energy")); add(target_motor_energy);add(new JSeparator(SwingConstants.VERTICAL));
		add(new JLabel("Target Battery State of Charge")); add(target_battery_state_of_charge);add(new JSeparator(SwingConstants.HORIZONTAL));
		add(new JLabel("Predicted Array Power")); add(predicted_array_power);add(new JSeparator(SwingConstants.VERTICAL));
		add(new JLabel("Time Elapsed")); add(time_elapsed);add(new JSeparator(SwingConstants.HORIZONTAL));
	}
}
