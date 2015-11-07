package com.telemetry.gui;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.Panel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

public class CalculationPanel extends Panel{
	private static final long serialVersionUID = -2305051374349231050L;
	private Panel label_panel                     = new Panel();
	private Panel output_panel                    = new Panel();
	private Panel button_panel                    = new Panel();
	private Button change_port_button             = new Button("Change Port");
	private Button start_monitor_button           = new Button("Start Monitor");
	private Button start_logging_button           = new Button("Start Logging");
	private Button reset_calculation_button       = new Button("Reset Calculation");
	private Label array_power                     = new Label("VALUE");
	private Label gross_watt_hours                = new Label("VALUE");
	private Label net_watt_hours                  = new Label("VALUE");
	private Label average_speed                   = new Label("VALUE");
	private Label average_gross_power             = new Label("VALUE");
	private Label average_net_power               = new Label("VALUE");
	private Label gross_average_power             = new Label("VALUE");
	private Label gross_average_watt_hour         = new Label("VALUE");
	private Label battery_only_run_time_remaining = new Label("VALUE");
	private Label battery_only_range              = new Label("VALUE");
	private Label battery_and_solar_run_time      = new Label("VALUE");
	private Label battery_and_solar_range         = new Label("VALUE");
	private Label battery_charge_remaining        = new Label("VALUE");
	private Label solar_energy_remaining          = new Label("VALUE");
	private Label motor_power                     = new Label("VALUE");
	
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
	
	public void updatePanel() {
		array_power                    .setText("Needs Implementing");
		gross_watt_hours               .setText("Needs Implementing");
		net_watt_hours                 .setText("Needs Implementing");
		average_speed                  .setText("Needs Implementing");
		average_gross_power            .setText("Needs Implementing");
		average_net_power              .setText("Needs Implementing");
		gross_average_power            .setText("Needs Implementing");
		gross_average_watt_hour        .setText("Needs Implementing");
		battery_only_run_time_remaining.setText("Needs Implementing");
		battery_only_range             .setText("Needs Implementing");
		battery_and_solar_run_time     .setText("Needs Implementing");
		battery_and_solar_range        .setText("Needs Implementing");
		battery_charge_remaining       .setText("Needs Implementing");
		solar_energy_remaining         .setText("Needs Implementing");
		motor_power                    .setText("Needs Implementing");
	}

	private void insertOutputPanel() {
		output_panel.add(new Label(" "));
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
		button_panel.add(new Label("Controls"));
		button_panel.add(new Label(" "));
		button_panel.add(new Label(" "));
		button_panel.add(new Label("    COM PORT:"));
		button_panel.add(new Label("/Port"));
		button_panel.add(change_port_button);
		button_panel.add(start_monitor_button);
		button_panel.add(start_logging_button);
		button_panel.add(reset_calculation_button);
		
		add(button_panel, BorderLayout.SOUTH);
	}
	
	private void insertLabelPanel() {
		label_panel.add(new Label("Calculations:"));
		label_panel.add(new Label("    Array Power"));
		label_panel.add(new Label("    Gross Watt*Hours"));
		label_panel.add(new Label("    Net Watt*Hours"));
		label_panel.add(new Label("    Average Speed"));
		label_panel.add(new Label("    Average Gross Power"));
		label_panel.add(new Label("    Average Net Power"));
		label_panel.add(new Label("    Gross Average Power"));
		label_panel.add(new Label("    Gross Average Watt Hour/Mile"));
		label_panel.add(new Label("    Battery Only Run-Time Remaining"));
		label_panel.add(new Label("    Battery Only Range"));
		label_panel.add(new Label("    Battery and Solar Run-Time"));
		label_panel.add(new Label("    Battery and Solar Range"));
		label_panel.add(new Label("    Battery Charge Remaining"));
		label_panel.add(new Label("    Solar Energy Ramaining in Day"));
		label_panel.add(new Label("    Motor Power"));
		
		add(label_panel, BorderLayout.WEST);
	}
}
