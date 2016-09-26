package com.telemetry.gui;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.*;


import com.telemetry.data.CarData;
import com.telemetry.util.Tools; 

public class CalculationPanel extends JPanel{
	private static final long serialVersionUID = -2305051374349231050L;
	
	// Finished
	private JLabel array_power_l                     = new JLabel("000.000"); 
	private JLabel battery_power_l                   = new JLabel("000.000");
	private JLabel soc_l                             = new JLabel("000.000");
	private JLabel motor_power_l                     = new JLabel("000.000"); 
	
	// Unfinished
	private JLabel average_speed                    = new JLabel("000.000"); 
	// Comment
	private JLabel solar_energy_remaining           = new JLabel("000.000"); 
	private JLabel time_left_in_day                 = new JLabel("000.000"); 
	private JLabel battery_only_runtime_60_sec      = new JLabel("000.000"); 
	private JLabel battery_only_range_60_sec        = new JLabel("000.000"); 
	private JLabel motor_power_60_sec               = new JLabel("000.000"); 
	private JLabel speed_60_sec                     = new JLabel("000.000"); 
	private JLabel battery_and_solar_runtime_60_sec = new JLabel("000.000");
	private JLabel battery_charge_remaining         = new JLabel("000.000");
	private JLabel motor_watt_hours                 = new JLabel("000.000");
	private JLabel battery_watt_hours               = new JLabel("000.000");
	private JLabel battery_and_solar_range          = new JLabel("000.000");
	private JLabel distance_left_in_day             = new JLabel("000.000"); 
	private JLabel target_speed                     = new JLabel("000.000"); 
	private JLabel target_watt_hour_per_mile        = new JLabel("000.000"); 
	private JLabel target_watt_hour_per_mile_60_sec = new JLabel("000.000");
	private JLabel target_watt_hour_per_mile_day    = new JLabel("000.000"); 
	private JLabel target_average_motor_power       = new JLabel("000.000"); 
	private JLabel target_motor_energy              = new JLabel("000.000"); 
	private JLabel target_battery_state_of_charge   = new JLabel("000.000"); 
	private JLabel predicted_array_power            = new JLabel("000.000"); 

	public CalculationPanel() {
	    setLayout(new GridBagLayout());
		insertComponents();
	}
	
	/**
	 * Updates the panel, given all the car's data
	 * @param data
	 */
	public void updatePanel(CarData data) {
		HashMap<String, Double> calculation_data = data.getCalculationData();
		
		// Currently only these values are verified to be somewhat correct
		array_power_l.setText(Tools.roundDouble(calculation_data.get("array_power")));
		motor_power_l.setText(Tools.roundDouble(calculation_data.get("motor_power")));
		battery_power_l.setText(Tools.roundDouble(calculation_data.get("battery_power")));
		soc_l.setText(Tools.roundDouble(calculation_data.get("state_of_charge")));
		
		validate();
		repaint();
	}
	
	private void insertComponents() {
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
			labels.get(i).setFont(Tools.FIELD_FONT);
			add(labels.get(i), gbc);
			
			i++; 
			gbc.gridx = 2;
			
			if(i < labels.size()) {
				labels.get(i).setFont(Tools.FIELD_FONT);
				add(labels.get(i), gbc);
				gbc.gridx = 0;
				gbc.gridy++;
			}
		}
		
		//-------------------------------Fields--------------------------------//
		ArrayList<JLabel> fields = new ArrayList<JLabel>();

		fields.add(array_power_l);                       
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
			fields.get(j).setFont(Tools.FIELD_FONT);
			fields.get(j).setOpaque(true);
			fields.get(j).setBackground(Color.ORANGE);
			fields.get(j).setMinimumSize(getPreferredSize());
			add(fields.get(j), gbc);
			
			j++; 
			gbc.gridx = 3;
			
			if(j < fields.size()) {
				fields.get(j).setFont(Tools.FIELD_FONT);
				fields.get(j).setOpaque(true);
				fields.get(j).setBackground(Color.ORANGE);
				fields.get(j).setMinimumSize(getPreferredSize());
				add(fields.get(j), gbc);
				gbc.gridx = 1;
				gbc.gridy++;
			}
		}
	}
}
