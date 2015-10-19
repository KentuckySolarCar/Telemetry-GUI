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
	
	Panel master_panel = new Panel();
	Panel label_panel = new Panel();
	Panel output_panel = new Panel();
	Panel button_panel = new Panel();
	
	public CalculationPanel(int tab_panel_x, int tab_panel_y) {
		super();
		
		master_panel.setSize(tab_panel_x, tab_panel_y);
	    master_panel.setLayout(new BorderLayout());
		label_panel.setLayout(new GridLayout(17, 1, 10, 15));
		output_panel.setLayout(new GridLayout(17, 1, 10, 15));
		button_panel.setLayout(new GridLayout(3, 3, 10, 10));
		
		createLabelPanel();
		createButtonPanel();
		updateOutputPanel();
	}
	
	public Panel getPanel() {
		updateOutput();
		return master_panel;
	}
	
	private void updateOutput() {
		master_panel.remove(output_panel);
		updateOutputPanel();
	}

	private void updateOutputPanel() {
		output_panel.removeAll();
		output_panel.add(new Label(" "));
		output_panel.add(new Label("VALUE"));
		output_panel.add(new Label("VALUE"));
		output_panel.add(new Label("VALUE"));
		output_panel.add(new Label("VALUE"));
		output_panel.add(new Label("VALUE"));
		output_panel.add(new Label("VALUE"));
		output_panel.add(new Label("VALUE"));
		output_panel.add(new Label("VALUE"));
		output_panel.add(new Label("VALUE"));
		output_panel.add(new Label("VALUE"));
		output_panel.add(new Label("VALUE"));
		output_panel.add(new Label("VALUE"));
		output_panel.add(new Label("VALUE"));
		output_panel.add(new Label("VALUE"));
		output_panel.add(new Label("VALUE"));
		
		master_panel.add(output_panel, BorderLayout.EAST);
	}
	
	private void createButtonPanel() {
		Button change_port_button = new Button("Change Port");
		Button start_monitor_button = new Button("Start Monitor");
		Button start_logging_button = new Button("Start Logging");
		Button reset_calculation_button = new Button("Reset Calculation");
		
		button_panel.add(new Label("Controls"));
		button_panel.add(new Label(" "));
		button_panel.add(new Label(" "));
		button_panel.add(new Label("    COM PORT:"));
		button_panel.add(new Label("/Port"));
		button_panel.add(change_port_button);
		button_panel.add(start_monitor_button);
		button_panel.add(start_logging_button);
		button_panel.add(reset_calculation_button);
		
		master_panel.add(button_panel, BorderLayout.SOUTH);
	}
	
	private void createLabelPanel() {
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
		
		master_panel.add(label_panel, BorderLayout.WEST);
	}
}
