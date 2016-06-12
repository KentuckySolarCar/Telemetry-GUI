package com.telemetry.gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.time.LocalDateTime;
import java.util.ArrayList;

import javax.swing.*;

import org.json.simple.JSONObject;

import com.telemetry.custom.SizedQueue;
import com.telemetry.equations.EnergyModelFunctions;
import com.telemetry.graphs.GraphPanel; 

public class CalculationPanel extends JPanel{
	private static final long serialVersionUID = -2305051374349231050L;
	private JLabel array_power                     = new JLabel("000.000"); // Implemented
	private JLabel average_speed                   = new JLabel("000.000"); // Implemented
	private JLabel solar_energy_remaining          = new JLabel("000.000"); // Implemented
//	private JLabel motor_power                     = new JLabel("000.000"); // Implemented
	private JLabel time_left_in_day                = new JLabel("000.000"); // Implemented
	private JLabel battery_only_runtime_60_sec     = new JLabel("000.000"); // Pretty much implemented
	private JLabel battery_only_range_60_sec       = new JLabel("000.000"); // Pretty much implemented
	private JLabel motor_power_60_sec              = new JLabel("000.000"); // Implemented
	private JLabel speed_60_sec                    = new JLabel("000.000"); // Implemented
	private JLabel battery_and_solar_runtime_60_sec = new JLabel("000.000");
	private JLabel battery_charge_remaining        = new JLabel("000.000");
	private JLabel motor_watt_hours                = new JLabel("000.000");
	private JLabel battery_watt_hours              = new JLabel("000.000");
	private JLabel battery_and_solar_range         = new JLabel("000.000");
	private JLabel distance_left_in_day            = new JLabel("000.000"); // Standby
	private JLabel target_speed                    = new JLabel("000.000"); // Standby
	private JLabel target_watt_hour_per_mile       = new JLabel("000.000"); // Standby
	private JLabel target_watt_hour_per_mile_60_sec = new JLabel("000.000");// Standby
	private JLabel target_watt_hour_per_mile_day   = new JLabel("000.000"); // Standby
	private JLabel target_average_motor_power      = new JLabel("000.000"); // Standby
	private JLabel target_motor_energy             = new JLabel("000.000"); // Standby
	private JLabel target_battery_state_of_charge  = new JLabel("000.000"); // Standby
	private JLabel predicted_array_power           = new JLabel("000.000"); // Standby
	private JLabel time_elapsed                    = new JLabel("000.000"); // Standby
	private SizedQueue<Double> battery_runtime_q;
	private SizedQueue<Double> battery_range_q; 
	private SizedQueue<Double> battery_solar_runtime_q;
	private SizedQueue<Double> motor_power_q;
	private SizedQueue<Double> speed_q;
	
	private GraphPanel graph_panel;

	static final Font FIELD_FONT = new Font("Consolas", Font.PLAIN, 16);
	
	
	public CalculationPanel(GraphPanel graph_panel) {
		this.graph_panel = graph_panel;
		
	    setLayout(new GridBagLayout());
	    
	    battery_runtime_q        = new SizedQueue<Double>(60);
	    battery_range_q          = new SizedQueue<Double>(60);
	    battery_solar_runtime_q  = new SizedQueue<Double>(60);
	    motor_power_q            = new SizedQueue<Double>(60);
	    speed_q                  = new SizedQueue<Double>(60);
		
		insertFields();
	}
	
	@SuppressWarnings("unchecked")
	public JSONObject getData() {
		JSONObject dataset = new JSONObject();
		
		dataset.put("array_power",                      array_power.getText());
		dataset.put("average_speed",                    average_speed.getText());
		dataset.put("battery_and_solar_range",          battery_and_solar_range.getText());
		dataset.put("battery_and_solar_runtime_60_sec", battery_and_solar_runtime_60_sec.getText());
		dataset.put("battery_charge_remaining",         battery_charge_remaining.getText());
		dataset.put("battery_only_range_60_sec",        battery_only_range_60_sec.getText());
		dataset.put("battery_only_runtime_60_sec",      battery_only_runtime_60_sec.getText());
		dataset.put("battery_watt_hours",               battery_watt_hours.getText());
		dataset.put("distance_left_in_day",             distance_left_in_day.getText());
		dataset.put("motor_power_60_sec",               motor_power_60_sec.getText());
		dataset.put("motor_watt_hours",                 motor_watt_hours.getText());
		dataset.put("predicted_array_power",            predicted_array_power.getText());
		dataset.put("solar_energy_remaining",           solar_energy_remaining.getText());
		dataset.put("speed_60_sec",                     speed_60_sec.getText());
		dataset.put("target_average_motor_power",       target_average_motor_power.getText());
		dataset.put("target_battery_state_of_charge",   target_battery_state_of_charge.getText());
		dataset.put("target_motor_energy",              target_motor_energy.getText());
		dataset.put("target_speed",                     target_speed.getText());
		dataset.put("target_watt_hour_per_mile",        target_watt_hour_per_mile.getText());
		dataset.put("target_watt_hour_per_mile_60_sec", target_watt_hour_per_mile_60_sec.getText());
		dataset.put("target_watt_hour_per_mile_day",    target_watt_hour_per_mile_day.getText());
		dataset.put("time_elapsed",                     time_elapsed.getText());
		dataset.put("time_left_in_day",                 time_left_in_day.getText());
		
		return dataset;
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
		
		double b_ave_temp        = (Double) batman_data.get("ave_temp");
		double b_v_average       = (Double) batman_data.get("v_average");
		double b_v_min           = (Double) batman_data.get("v_min");
		double b_current_average = (Double) batman_data.get("current_average");
		
		double r_ave_temp        = (Double) robin_data.get("ave_temp");
		double r_v_average       = (Double) robin_data.get("v_average");
		double r_v_min           = (Double) robin_data.get("v_min");
		double r_current_average = (Double) robin_data.get("current_average");
		
		double total_batt_v = EnergyModelFunctions.getTotalBatteryVoltage(b_v_average, r_v_average);
		double total_batt_c = b_current_average + r_current_average;
		double battery_runtime_current = EnergyModelFunctions.getInstantaneousBatteryRuntime(0.0, total_batt_v, total_batt_c);
		double battery_range_current = EnergyModelFunctions.getBatteryRange(battery_runtime_current, ave_speed);
		double motor_power_current = EnergyModelFunctions.getMotorPower(ave_current, total_batt_v);
		
		LocalDateTime localTime 	= LocalDateTime.now();
		int GMTOffset           	= -5;
		double latitude         	= 38.035831;
		double longitude        	= -84.506800;
		int dayTypeCode 			= 0;
		double startChargeInMorning = 7;
		double startRacingInMorning = 9;
		double chargeInEveTime 		= 17;
		double stopChargingInEve 	= 20;
		
		battery_runtime_q.add(battery_runtime_current);
		if(battery_runtime_q.size() == 60) {
			double battery_runtime_average = getAverage(battery_runtime_q);
			battery_only_runtime_60_sec.setText(Double.toString(battery_runtime_average));
		}	
		
		battery_range_q.add(battery_range_current);
		if(battery_range_q.size() == 60) {
			double battery_range_average = getAverage(battery_range_q);
			battery_only_range_60_sec.setText(Double.toString(battery_range_average));
		}
		
		motor_power_q.add(motor_power_current);
		if(motor_power_q.size() == 60) {
			double motor_power_average = getAverage(motor_power_q);
			motor_power_60_sec.setText(Double.toString(motor_power_average));
		}
		
		speed_q.add(ave_speed);
		if(speed_q.size() == 60) {
			double ave_speed_60_sec = getAverage(speed_q);
			speed_60_sec.setText(Double.toString(ave_speed_60_sec));
		}
		
		array_power                    .setText(Double.toString(EnergyModelFunctions.getArrayPower(ave_current, b_current_average, r_current_average, b_v_average, r_v_average)));
		average_speed                  .setText(Double.toString(ave_speed));
		battery_only_range_60_sec         .setText("Needs Implementing");
		battery_and_solar_runtime_60_sec .setText("Needs Implementing");
		battery_and_solar_range        .setText("Needs Implementing");
		battery_charge_remaining       .setText("Needs Implementing");
		solar_energy_remaining         .setText(Double.toString(EnergyModelFunctions.getEnergyLeftInDay(localTime, GMTOffset, latitude, longitude, dayTypeCode, startChargeInMorning, startRacingInMorning, chargeInEveTime, stopChargingInEve)));
		time_left_in_day               .setText(EnergyModelFunctions.getTimeLeftInDay(System.currentTimeMillis()));
		
		validate();
		repaint();
	}

	private void insertFields() {
		GridBagConstraints gbc = new GridBagConstraints();
		
		// GBC Constants
		gbc.insets = new Insets(3, 3, 3, 3);
		
		//--------------------------Labels------------------------//
		ArrayList<JLabel> labels = new ArrayList<JLabel>();
		
		labels.add(new JLabel("Array Power"));                        
		labels.add(new JLabel("Average Speed"));                      
		labels.add(new JLabel("Battery Only Run Time (60 secs)"));    
		labels.add(new JLabel("Battery Only Range (60 secs)"));       
		labels.add(new JLabel("Battery and Solar Run Time "));        
		labels.add(new JLabel("Battery and Solar Range"));               
		labels.add(new JLabel("Battery Charge Remaining"));           
		labels.add(new JLabel("Solar Energy Remaining"));                         
		labels.add(new JLabel("Battery Watt Hours"));                
		labels.add(new JLabel("Motor Power (60 secs)"));                            
		labels.add(new JLabel("Target Speed"));                      
		labels.add(new JLabel("Speed (60 sec)"));                     
		labels.add(new JLabel("Target Watt Hour Per Mile"));          
		labels.add(new JLabel("Target Watt Hour Per Mile (60 sec)")); 
		labels.add(new JLabel("Target Watt Hour Per Mile (Day)"));    
		labels.add(new JLabel("Distance Left in Day"));               
		labels.add(new JLabel("Time Left in Day"));                   
		labels.add(new JLabel("Target Average Motor Power"));         
		labels.add(new JLabel("Target Motor Energy"));                
		labels.add(new JLabel("Target Battery State of Charge"));     
		labels.add(new JLabel("Predicted Array Power"));              
		labels.add(new JLabel("Motor Watt Hours"));                   
		
		gbc.gridx = 0;
		gbc.gridy = 0;
		int i = 0;
		
		while(i < labels.size()) {
			labels.get(i).setFont(FIELD_FONT);
			add(labels.get(i), gbc);
			
			i++; 
			gbc.gridx = 2;
			
			if(i < labels.size()) {
				labels.get(i).setFont(FIELD_FONT);
				add(labels.get(i), gbc);
				gbc.gridx = 0;
				gbc.gridy++;
			}
		}
		
		//-------------------------------Fields--------------------------------//
		ArrayList<JLabel> fields = new ArrayList<JLabel>();

		fields.add(array_power);                       
		fields.add(average_speed);                      
		fields.add(battery_only_runtime_60_sec);      
		fields.add(battery_only_range_60_sec);         
		fields.add(battery_and_solar_runtime_60_sec); 
		fields.add(battery_and_solar_range);          
		fields.add(battery_charge_remaining);          
		fields.add(solar_energy_remaining);            
		fields.add(battery_watt_hours);                
		fields.add(motor_power_60_sec);                
		fields.add(target_speed);                      
		fields.add(speed_60_sec);                      
		fields.add(target_watt_hour_per_mile);         
		fields.add(target_watt_hour_per_mile_60_sec);  
		fields.add(target_watt_hour_per_mile_day);     
		fields.add(distance_left_in_day);              
		fields.add(time_left_in_day);                  
		fields.add(target_average_motor_power);        
		fields.add(target_motor_energy);               
		fields.add(target_battery_state_of_charge);    
		fields.add(predicted_array_power);             
		fields.add(motor_watt_hours);                  
		
		gbc.gridx = 1;
		gbc.gridy = 0;
		int j = 0;
		
		while(j < fields.size()) {
			fields.get(j).setFont(FIELD_FONT);
			fields.get(j).setOpaque(true);
			fields.get(j).setBackground(Color.ORANGE);
			fields.get(j).setMinimumSize(getPreferredSize());
			add(fields.get(j), gbc);
			
			j++; 
			gbc.gridx = 3;
			
			if(j < fields.size()) {
				fields.get(j).setFont(FIELD_FONT);
				fields.get(j).setOpaque(true);
				fields.get(j).setBackground(Color.ORANGE);
				fields.get(j).setMinimumSize(getPreferredSize());
				add(fields.get(j), gbc);
				gbc.gridx = 1;
				gbc.gridy++;
			}
		}
	}
	
	private double getAverage(SizedQueue<Double> queue) {
		double total = 0;
		for(Double d : queue) {
			total += d;
		}
		return total / queue.size();
	}
}
