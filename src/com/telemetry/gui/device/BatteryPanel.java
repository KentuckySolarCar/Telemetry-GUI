package com.telemetry.gui.device;

import java.awt.GridLayout;
import javax.swing.*;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class BatteryPanel extends JPanel {
	private static final long serialVersionUID = 3458289157169322167L;
	private JPanel batman_unit_panel = new JPanel();
	private JPanel batman_label_panel = new JPanel();
	private JPanel batman_data_panel = new JPanel();
	private JPanel robin_unit_panel = new JPanel();
	private JPanel robin_label_panel = new JPanel();
	private JPanel robin_data_panel = new JPanel();
	
	private JLabel batman_v_average_l = new JLabel("VALUE");
	private JLabel batman_v_max_l = new JLabel("VALUE");
	private JLabel batman_v_min_l = new JLabel("VALUE");
	private JLabel batman_current_l = new JLabel("VALUE");
	private JLabel batman_t_average_l = new JLabel("VALUE");
	private JLabel batman_t_max_l = new JLabel("VALUE");
	private JLabel batman_t_min_l = new JLabel("VALUE");
	private double batman_ave_temp = 0;
	private double batman_v_average = 0;
	private double batman_v_min = 0;
	private double batman_current_average = 0;
	
	private JLabel robin_v_average_l = new JLabel("VALUE");
	private JLabel robin_v_max_l = new JLabel("VALUE");
	private JLabel robin_v_min_l = new JLabel("VALUE");
	private JLabel robin_current_l = new JLabel("VALUE");
	private JLabel robin_t_average_l = new JLabel("VALUE");
	private JLabel robin_t_max_l = new JLabel("VALUE");
	private JLabel robin_t_min_l = new JLabel("VALUE");
	private double robin_ave_temp = 0;
	private double robin_v_average = 0;
	private double robin_v_min = 0;
	private double robin_current_average = 0;
	
	public BatteryPanel() {
		setLayout(new GridLayout(1, 6));
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
		insertRobinUnitPanel();
	}
	
	public void updatePanel(JSONObject obj, String type) {
		if(type.equals("bat_temp")) {
			if(((String) obj.get("name")).equals("0")) {
				batman_ave_temp = Double.parseDouble((String) obj.get("Tavg"));
				batman_t_average_l.setText((String) obj.get("Tavg"));
				batman_t_max_l.setText((String) obj.get("Tmin"));
				batman_t_min_l.setText((String) obj.get("Tmax"));
			}
			else {
				robin_ave_temp = Double.parseDouble((String) obj.get("Tavg"));
				robin_t_average_l.setText((String) obj.get("Tavg"));
				robin_t_max_l.setText((String) obj.get("Tmin"));
				robin_t_min_l.setText((String) obj.get("Tmax"));
			}
		}
		else if(type.equals("bat_volt")) {
			if(((String) obj.get("name")).equals("0")) {
				batman_v_average       = Double.parseDouble((String) obj.get("Vavg"));
				batman_v_min           = Double.parseDouble((String) obj.get("Vmin"));
				batman_current_average = Double.parseDouble((String) obj.get("BC"));
				batman_v_average_l.setText((String) obj.get("Vavg"));
				batman_v_max_l.setText((String) obj.get("Vmax"));
				batman_v_min_l.setText((String) obj.get("Vmin"));
				batman_current_l.setText((String) obj.get("BC"));
			}
			else {
				robin_v_average       = Double.parseDouble((String) obj.get("Vavg"));
				robin_v_min           = Double.parseDouble((String) obj.get("Vmin"));
				robin_current_average = Double.parseDouble((String) obj.get("BC"));
				robin_v_average_l.setText((String) obj.get("Vavg"));
				robin_v_max_l.setText((String) obj.get("Vmax"));
				robin_v_min_l.setText((String) obj.get("Vmin"));
				robin_current_l.setText((String) obj.get("BC"));
			}
		}
		
		validate();
		repaint();
	}
	
	private void insertBatmanLabelPanel() {
		batman_label_panel.add(new JLabel("    Batman"));
		batman_label_panel.add(new JLabel("V_Average:"));
		batman_label_panel.add(new JLabel("V_Max:"));
		batman_label_panel.add(new JLabel("V_Min:"));
		batman_label_panel.add(new JLabel("Current:"));
		batman_label_panel.add(new JLabel("T_Average:"));
		batman_label_panel.add(new JLabel("T_Max:"));
		batman_label_panel.add(new JLabel("T_Min:"));
		add(batman_label_panel);
	}
	
	private void insertBatmanDataPanel() {
		batman_data_panel.add(new JLabel(" "));
		batman_data_panel.add(batman_v_average_l);
		batman_data_panel.add(batman_v_max_l);
		batman_data_panel.add(batman_v_min_l);
		batman_data_panel.add(batman_current_l);
		batman_data_panel.add(batman_t_average_l);
		batman_data_panel.add(batman_t_max_l);
		batman_data_panel.add(batman_t_min_l);
		add(batman_data_panel);
	}
	
	private void insertBatmanUnitPanel() {
		batman_unit_panel.add(new JLabel(" "));
		batman_unit_panel.add(new JLabel(" V"));
		batman_unit_panel.add(new JLabel(" V (#)"));
		batman_unit_panel.add(new JLabel(" V (#)"));
		batman_unit_panel.add(new JLabel(" A"));
		batman_unit_panel.add(new JLabel(" C"));
		batman_unit_panel.add(new JLabel(" C"));
		batman_unit_panel.add(new JLabel(" C"));
		add(batman_unit_panel);
	}
	
	private void insertRobinLabelPanel() {
		robin_label_panel.add(new JLabel("    Robin"));
		robin_label_panel.add(new JLabel("V_Average:"));
		robin_label_panel.add(new JLabel("V_Max:"));
		robin_label_panel.add(new JLabel("V_Min:"));
		robin_label_panel.add(new JLabel("Current:"));
		robin_label_panel.add(new JLabel("T_Average:"));
		robin_label_panel.add(new JLabel("T_Max:"));
		robin_label_panel.add(new JLabel("T_Min:"));
		add(robin_label_panel);
	}
	
	private void insertRobinDataPanel() {
		robin_data_panel.add(new JLabel(" "));
		robin_data_panel.add(robin_v_average_l);
		robin_data_panel.add(robin_v_max_l);
		robin_data_panel.add(robin_v_min_l);
		robin_data_panel.add(robin_current_l);
		robin_data_panel.add(robin_t_average_l);
		robin_data_panel.add(robin_t_max_l);
		robin_data_panel.add(robin_t_min_l);
		add(robin_data_panel);
	}
	
	private void insertRobinUnitPanel() {
		robin_unit_panel.add(new JLabel(" "));
		robin_unit_panel.add(new JLabel(" V"));
		robin_unit_panel.add(new JLabel(" V (#)"));
		robin_unit_panel.add(new JLabel(" V (#)"));
		robin_unit_panel.add(new JLabel(" A"));
		robin_unit_panel.add(new JLabel(" C"));
		robin_unit_panel.add(new JLabel(" C"));
		robin_unit_panel.add(new JLabel(" C"));
		add(robin_unit_panel);
	}
}