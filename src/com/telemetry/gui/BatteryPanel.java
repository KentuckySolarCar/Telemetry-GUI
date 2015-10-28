package com.telemetry.gui;

import java.awt.*;

public class BatteryPanel {
	private Panel battery_panel = new Panel();
	private Panel batman_unit_panel = new Panel();
	private Panel batman_label_panel = new Panel();
	private Panel batman_data_panel = new Panel();
	private Panel robin_unit_panel = new Panel();
	private Panel robin_label_panel = new Panel();
	private Panel robin_data_panel = new Panel();
	
	private Label batman_v_average = new Label("VALUE");
	private Label batman_v_max = new Label("VALUE");
	private Label batman_v_min = new Label("VALUE");
	private Label batman_current = new Label("VALUE");
	private Label batman_t_average = new Label("VALUE");
	private Label batman_t_max = new Label("VALUE");
	private Label batman_t_min = new Label("VALUE");
	
	private Label robin_v_average = new Label("VALUE");
	private Label robin_v_max = new Label("VALUE");
	private Label robin_v_min = new Label("VALUE");
	private Label robin_current = new Label("VALUE");
	private Label robin_t_average = new Label("VALUE");
	private Label robin_t_max = new Label("VALUE");
	private Label robin_t_min = new Label("VALUE");
	
	public BatteryPanel() {
		battery_panel.setLayout(new GridLayout(1, 6));
		batman_unit_panel.setLayout(new GridLayout(8, 1));
		batman_label_panel.setLayout(new GridLayout(8, 1));
		batman_data_panel.setLayout(new GridLayout(8, 1));
		robin_label_panel.setLayout(new GridLayout(8, 1));
		robin_data_panel.setLayout(new GridLayout(8, 1));
		robin_unit_panel.setLayout(new GridLayout(8, 1));
		
		insertBatmanLabelPanel();
		insertBatmanDataPanel();
		insertBatmanUnitPanel();
		insertRobinLabelPanel();
		insertRobinDataPanel();
	}
	
	private void insertBatmanLabelPanel() {
		batman_label_panel.add(new Label("    Batman"));
		batman_label_panel.add(new Label("V_Average:"));
		batman_label_panel.add(new Label("V_Max:"));
		batman_label_panel.add(new Label("V_Min:"));
		batman_label_panel.add(new Label("Current:"));
		batman_label_panel.add(new Label("T_Average:"));
		batman_label_panel.add(new Label("T_Max:"));
		batman_label_panel.add(new Label("T_Min:"));
		battery_panel.add(batman_label_panel);
	}
	
	private void insertBatmanDataPanel() {
		batman_data_panel.add(batman_v_average);
		batman_data_panel.add(batman_v_max);
		batman_data_panel.add(batman_v_min);
		batman_data_panel.add(batman_current);
		batman_data_panel.add(batman_t_average);
		batman_data_panel.add(batman_t_max);
		batman_data_panel.add(batman_t_min);
		battery_panel.add(batman_data_panel);
	}
	
	private void insertBatmanUnitPanel() {
		batman_unit_panel.add(new Label(""));
		batman_unit_panel.add(new Label("V"));
		batman_unit_panel.add(new Label("V (#)"));
		batman_unit_panel.add(new Label("V (#)"));
		batman_unit_panel.add(new Label("A"));
		batman_unit_panel.add(new Label("C"));
		batman_unit_panel.add(new Label("C"));
		batman_unit_panel.add(new Label("C"));
		battery_panel.add(batman_unit_panel);
	}
	
	private void insertRobinLabelPanel() {
		robin_label_panel.add(new Label("    Robin"));
		robin_label_panel.add(new Label("V_Average:"));
		robin_label_panel.add(new Label("V_Max:"));
		robin_label_panel.add(new Label("V_Min:"));
		robin_label_panel.add(new Label("Current:"));
		robin_label_panel.add(new Label("T_Average:"));
		robin_label_panel.add(new Label("T_Max:"));
		robin_label_panel.add(new Label("T_Min:"));
		battery_panel.add(robin_label_panel);
	}
	
	private void insertRobinDataPanel() {
		robin_data_panel.add(robin_v_average);
		robin_data_panel.add(robin_v_max);
		robin_data_panel.add(robin_v_min);
		robin_data_panel.add(robin_current);
		robin_data_panel.add(robin_t_average);
		robin_data_panel.add(robin_t_max);
		robin_data_panel.add(robin_t_min);
		battery_panel.add(robin_data_panel);
	}
}