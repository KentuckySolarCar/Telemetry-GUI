package com.telemetry.data;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.json.simple.JSONObject;

import com.telemetry.strategy.EnergyModelFunctions;
import com.telemetry.strategy.StateOfCharge;
import com.telemetry.util.Tools;

/**
 * This class handles all data relating to the car itself, like battery, motor, and any strategy
 * calculations, like power, target watt hours, etc.
 * 
 * @author Weilian Song
 *
 */
public class CarData {
	// Battery Data
	private double batt_temp_avg = 0D;
	private double batt_temp_max = 0D;
	private double batt_temp_min = 0D;
	private double batt_volt_max = 0D;
	private double batt_volt_min = 0D;
	private double batt_volt_avg = 0D;
	private double batt_current  = 0D;
	
	// Motor Data
	private double motor_amp_hour = 0D;
	private double motor_watt_sec = 0D;
	private double motor_odometer = 0D;
	private double motor_voltage = 0D;
	private double motor_current = 0D;
	private double motor_speed = 0D;
	
	// Calculation Data
	private double array_power = 0D;
	private double motor_power = 0D;
	private double battery_power = 0D;
	private double time_seconds = 0D;
	private double state_of_charge = 0D;
	
	// Unimplemented Calculation Data
	private double average_speed = 0D;
	private double solar_energy_remaining = 0D;
	private double time_left_in_day = 0D;
	private double battery_only_runtime_60_sec = 0D;
	private double battery_only_range_60_sec = 0D;
	private double motor_power_60_sec = 0D;
	private double speed_60_sec = 0D;
	private double battery_and_solar_runtime_60_sec = 0D;
	private double battery_charge_remaining = 0D;
	private double motor_watt_hours = 0D;
	private double battery_watt_hours = 0D;
	private double battery_and_solar_range = 0D;
	private double distance_left_in_day = 0D;
	private double target_speed = 0D;
	private double target_watt_hour_per_mile = 0D;
	private double target_watt_hour_per_mile_60_sec = 0D;
	private double target_watt_hour_per_mile_day = 0D;
	private double target_average_motor_power = 0D;
	private double target_motor_energy = 0D;
	private double target_battery_state_of_charge = 0D;
	private double predicted_array_power = 0D;
	private double time_elapsed = 0D;
	
	// Battery Messages
	private List<String> batt_msg = new ArrayList<String>();
	
	// Time
	private Integer[] computer_time = new Integer[3];
	private Integer[] counter_time = new Integer[3];
	private Integer[] pi_time = new Integer[3];
	
	// Dynamic Calculation Helpers
	StateOfCharge soc;
	
	// Time
	private DateFormat date_format = new SimpleDateFormat("HH:mm:ss");
	private long initial_time = 0;
	
	public CarData() {
		// Initialize state of charge calculator
		soc = new StateOfCharge();
		// Initialize initial time so we know GUI's running time
		initial_time = System.currentTimeMillis()/1000;
	}
	
	public HashMap<String, Integer[]> getTimeData() {
		HashMap<String, Integer[]> time_data = new HashMap<String, Integer[]>();
		time_data.put("computer_time", computer_time);
		time_data.put("counter_time", counter_time);
		time_data.put("pi_time", pi_time);
		return time_data;
	}
	
	public HashMap<String, Double> getMotorData() {
		HashMap<String, Double> motor_data = new HashMap<String, Double>();
		motor_data.put("motor_amp_hour", motor_amp_hour);
		motor_data.put("motor_watt_sec", motor_watt_sec);
		motor_data.put("motor_odometer", motor_odometer);
		motor_data.put("motor_voltage", motor_voltage);
		motor_data.put("motor_current", motor_current);
		motor_data.put("motor_speed", motor_speed);
		motor_data.put("time_seconds", time_seconds);
		return motor_data;
	}
	
	public HashMap<String, Double> getBatteryData() {
		HashMap<String, Double> battery_data = new HashMap<String, Double>();
		battery_data.put("batt_volt_max", batt_volt_max);
		battery_data.put("batt_volt_min", batt_volt_min);
		battery_data.put("batt_volt_avg", batt_volt_avg);
		battery_data.put("batt_temp_max", batt_temp_max);
		battery_data.put("batt_temp_min", batt_temp_min);
		battery_data.put("batt_temp_avg", batt_temp_avg);
		battery_data.put("batt_current", batt_current);
		battery_data.put("time_seconds", time_seconds);
		return battery_data;
	}
	
	public HashMap<String, Double> getCalculationData() {
		HashMap<String, Double> calculation_data = new HashMap<String, Double>();
		calculation_data.put("array_power", array_power);
		calculation_data.put("motor_power", motor_power);
		calculation_data.put("battery_power", battery_power);
		calculation_data.put("state_of_charge", state_of_charge);
		calculation_data.put("time_seconds", time_seconds);
		return calculation_data;
	}
	
	public synchronized void updateData(JSONObject obj) {
		// Time Update
		Date date = new Date();
		String date_str = date_format.format(date);
		computer_time[0] = Tools.stringToInt(date_str.split(":")[0]);
		computer_time[1] = Tools.stringToInt(date_str.split(":")[1]);
		computer_time[2] = Tools.stringToInt(date_str.split(":")[2]);

		String pi_time_str = (String) obj.get("Time");
		pi_time[0] = Tools.stringToInt(pi_time_str.split(":")[0]);
		pi_time[1] = Tools.stringToInt(pi_time_str.split(":")[1]);
		pi_time[2] = Tools.stringToInt(pi_time_str.split(":")[2]);
		
		long elapsed_time = System.currentTimeMillis()/1000 - initial_time;
		counter_time[0] = (int) (elapsed_time / 3600);
		counter_time[1] = (int) ((elapsed_time % 3600) / 60);
		counter_time[2] = (int) (elapsed_time % 60);
				
		// Telemetry Data Update
		String message_id = (String) obj.get("message_id");
		switch(message_id) {
		case "motor": {
			motor_amp_hour = Tools.getJSONDouble(obj, "amp_hours");
			motor_odometer = Tools.getJSONDouble(obj, "odometer");
			motor_voltage  = Tools.getJSONDouble(obj, "V");
			motor_current  = Tools.getJSONDouble(obj, "I");
			motor_speed    = Tools.getJSONDouble(obj, "S");
			break;
		}
		case "bat_temp": {
			batt_temp_avg = Tools.getJSONDouble(obj, "Tavg");
			batt_temp_max = Tools.getJSONDouble(obj, "Tmax");
			batt_temp_min = Tools.getJSONDouble(obj, "Tmin");
			break;
		}
		case "bat_volt": {
			batt_volt_max = Tools.getJSONDouble(obj, "Vmax") / 10000;
			batt_volt_min = Tools.getJSONDouble(obj, "Vmin") / 10000;
			batt_volt_avg = Tools.getJSONDouble(obj, "Vavg") / 10000;
			batt_current  = Tools.getJSONDouble(obj, "BC") / 1000;
			break;
		}
		case "message": {
			batt_msg.add((String) obj.get("Messages"));
			break;
		}
		default:
			break;
		}
		
		// Calculation Data Updating
		array_power = EnergyModelFunctions.getArrayPower(motor_current, batt_current, batt_volt_avg);
		motor_power = EnergyModelFunctions.getMotorPower(motor_current, motor_voltage);
		battery_power  = EnergyModelFunctions.getBatteryPower(batt_volt_avg, batt_current);		
		state_of_charge = soc.calculateSOC(batt_volt_min);
		time_seconds = counter_time[0]*3600 + counter_time[1]*60 + counter_time[2];
	}
}
