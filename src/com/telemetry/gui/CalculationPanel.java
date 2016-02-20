package com.telemetry.gui;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.time.LocalDateTime;

import javax.swing.*;

import org.json.simple.JSONObject;

import com.telemetry.equations.EnergyModelFunctions; 

public class CalculationPanel extends JPanel{
	private static final long serialVersionUID = -2305051374349231050L;
	private JPanel label_panel                     = new JPanel();
	private JPanel output_panel                    = new JPanel();
	private JPanel button_panel                    = new JPanel();
	private JButton change_port_button             = new JButton("Change Port");
	private JButton start_monitor_button           = new JButton("Start Monitor");
	private JButton start_logging_button           = new JButton("Start Logging");
	private JButton reset_calculation_button       = new JButton("Reset Calculation");
	private JLabel array_power                     = new JLabel("VALUE");
	private JLabel gross_watt_hours                = new JLabel("VALUE");
	private JLabel net_watt_hours                  = new JLabel("VALUE");
	private JLabel average_speed                   = new JLabel("VALUE");
	private JLabel average_gross_power             = new JLabel("VALUE");
	private JLabel average_net_power               = new JLabel("VALUE");
	private JLabel gross_average_power             = new JLabel("VALUE");
	private JLabel gross_average_watt_hour         = new JLabel("VALUE");
	private JLabel battery_only_run_time_remaining = new JLabel("VALUE");
	private JLabel battery_only_range              = new JLabel("VALUE");
	private JLabel battery_and_solar_run_time      = new JLabel("VALUE");
	private JLabel battery_and_solar_range         = new JLabel("VALUE");
	private JLabel battery_charge_remaining        = new JLabel("VALUE");
	private JLabel solar_energy_remaining          = new JLabel("VALUE");
	private JLabel motor_power                     = new JLabel("VALUE");
	
	public CalculationPanel(int tab_panel_x, int tab_panel_y) {
		super();
		
		setSize(tab_panel_x, tab_panel_y);
	    setLayout(new BorderLayout());
		label_panel.setLayout(new GridLayout(17, 1, 10, 15));
		output_panel.setLayout(new GridLayout(17, 1, 10, 15));
		button_panel.setLayout(new GridLayout(3, 3, 10, 10));
		
		insertLabelPanel();
		insertButtonPanel();
		insertOutputPanel();
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
		gross_watt_hours               .setText("Needs Implementing");
		net_watt_hours                 .setText("Needs Implementing");
		average_speed                  .setText(Double.toString(ave_speed));
		average_gross_power            .setText("Needs Implementing");
		average_net_power              .setText("Needs Implementing");
		gross_average_power            .setText("Needs Implementing");
		gross_average_watt_hour        .setText("Needs Implementing");
		battery_only_run_time_remaining.setText("Needs Implementing");
		battery_only_range             .setText("Needs Implementing");
		battery_and_solar_run_time     .setText("Needs Implementing");
		battery_and_solar_range        .setText("Needs Implementing");
		battery_charge_remaining       .setText("Needs Implementing");
		solar_energy_remaining         .setText(Double.toString(EnergyModelFunctions.getEnergyLeftInDay(localTime, GMTOffset, latitude, longitude, dayTypeCode, startChargeInMorning, startRacingInMorning, chargeInEveTime, stopChargingInEve)));
		motor_power                    .setText(Double.toString(EnergyModelFunctions.getMotorPower(ave_current, total_batt_v)));
		
		validate();
		repaint();
	}

	private void insertOutputPanel() {
		output_panel.add(new JLabel(" "));
		output_panel.add(array_power);
		output_panel.add(gross_watt_hours);
		output_panel.add(net_watt_hours);
		output_panel.add(average_speed);
		output_panel.add(average_gross_power);
		output_panel.add(average_net_power);
		output_panel.add(gross_average_power);
		output_panel.add(gross_average_watt_hour);
		output_panel.add(battery_only_run_time_remaining);
		output_panel.add(battery_only_range);
		output_panel.add(battery_and_solar_run_time);
		output_panel.add(battery_and_solar_range);
		output_panel.add(battery_charge_remaining);
		output_panel.add(solar_energy_remaining);
		output_panel.add(motor_power);
		
		add(output_panel, BorderLayout.EAST);
	}
	
	private void insertButtonPanel() {
		button_panel.add(new JLabel("Controls"));
		button_panel.add(new JLabel(" "));
		button_panel.add(new JLabel(" "));
		button_panel.add(new JLabel("    COM PORT:"));
		button_panel.add(new JLabel("/Port"));
		button_panel.add(change_port_button);
		button_panel.add(start_monitor_button);
		button_panel.add(start_logging_button);
		button_panel.add(reset_calculation_button);
		
		add(button_panel, BorderLayout.SOUTH);
	}
	
	private void insertLabelPanel() {
		label_panel.add(new JLabel("Calculations:"));
		label_panel.add(new JLabel("    Array Power"));
		label_panel.add(new JLabel("    Gross Watt*Hours"));
		label_panel.add(new JLabel("    Net Watt*Hours"));
		label_panel.add(new JLabel("    Average Speed"));
		label_panel.add(new JLabel("    Average Gross Power"));
		label_panel.add(new JLabel("    Average Net Power"));
		label_panel.add(new JLabel("    Gross Average Power"));
		label_panel.add(new JLabel("    Gross Average Watt Hour/Mile"));
		label_panel.add(new JLabel("    Battery Only Run-Time Remaining"));
		label_panel.add(new JLabel("    Battery Only Range"));
		label_panel.add(new JLabel("    Battery and Solar Run-Time"));
		label_panel.add(new JLabel("    Battery and Solar Range"));
		label_panel.add(new JLabel("    Battery Charge Remaining"));
		label_panel.add(new JLabel("    Solar Energy Ramaining in Day"));
		label_panel.add(new JLabel("    Motor Power"));
		
		add(label_panel, BorderLayout.WEST);
	}
}
