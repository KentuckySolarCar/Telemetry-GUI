package com.telemetry.gui.device;

import java.awt.GridLayout;
import javax.swing.*;

public class BatteryPanel extends JPanel {
	private static final long serialVersionUID = 3458289157169322167L;
	private JPanel batman_unit_panel = new JPanel();
	private JPanel batman_label_panel = new JPanel();
	private JPanel batman_data_panel = new JPanel();
	private JPanel robin_unit_panel = new JPanel();
	private JPanel robin_label_panel = new JPanel();
	private JPanel robin_data_panel = new JPanel();
	
	private JLabel batman_v_average = new JLabel("VALUE");
	private JLabel batman_v_max = new JLabel("VALUE");
	private JLabel batman_v_min = new JLabel("VALUE");
	private JLabel batman_current = new JLabel("VALUE");
	private JLabel batman_t_average = new JLabel("VALUE");
	private JLabel batman_t_max = new JLabel("VALUE");
	private JLabel batman_t_min = new JLabel("VALUE");
	
	private JLabel robin_v_average = new JLabel("VALUE");
	private JLabel robin_v_max = new JLabel("VALUE");
	private JLabel robin_v_min = new JLabel("VALUE");
	private JLabel robin_current = new JLabel("VALUE");
	private JLabel robin_t_average = new JLabel("VALUE");
	private JLabel robin_t_max = new JLabel("VALUE");
	private JLabel robin_t_min = new JLabel("VALUE");
	
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
	
	public void updatePanel() {
		batman_v_average.setText("Needs Implementing!");
		batman_v_max.setText("Need Implementing!");
		batman_v_min.setText("Needs Implementing!");
		batman_current.setText("Needs Implementing!");
		batman_t_average.setText("Needs Implementing!");
		batman_t_max.setText("Needs Implementing!");
		batman_t_min.setText("Needs Implementing!");
		
		robin_v_average.setText("Needs Implementing!");
		robin_v_max.setText("Need Implementing!");
		robin_v_min.setText("Needs Implementing!");
		robin_current.setText("Needs Implementing!");
		robin_t_average.setText("Needs Implementing!");
		robin_t_max.setText("Needs Implementing!");
		robin_t_min.setText("Needs Implementing!");
		
		validate();
		repaint();
	}
	
/*	public Panel getPanel() {
		return battery_panel;
	} */
	
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
		batman_data_panel.add(batman_v_average);
		batman_data_panel.add(batman_v_max);
		batman_data_panel.add(batman_v_min);
		batman_data_panel.add(batman_current);
		batman_data_panel.add(batman_t_average);
		batman_data_panel.add(batman_t_max);
		batman_data_panel.add(batman_t_min);
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
		robin_data_panel.add(robin_v_average);
		robin_data_panel.add(robin_v_max);
		robin_data_panel.add(robin_v_min);
		robin_data_panel.add(robin_current);
		robin_data_panel.add(robin_t_average);
		robin_data_panel.add(robin_t_max);
		robin_data_panel.add(robin_t_min);
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