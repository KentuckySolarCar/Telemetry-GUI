 package com.telemetry.gui.device;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.*;

import org.json.simple.JSONObject;

public class BatteryPanel extends JPanel {
	private static final long serialVersionUID = 3458289157169322167L;
	
	private JLabel batman_v_average_l = new JLabel("0000.000");
	private JLabel batman_v_max_l = new JLabel("0000.000");
	private JLabel batman_v_global_max_l = new JLabel("0000.000");
	private JLabel batman_v_min_l = new JLabel("0000.000");
	private JLabel batman_v_global_min_l = new JLabel("0000.000");
	private JLabel batman_current_l = new JLabel("0000.000");
	private JLabel batman_t_average_l = new JLabel("0000.000");
	private JLabel batman_t_max_l = new JLabel("0000.000");
	private JLabel batman_t_min_l = new JLabel("0000.000");
	private double batman_ave_temp = 0;
	private double batman_v_average = 0;
	private double batman_v_max = 0;
	private double batman_v_global_max = 0;
	private double batman_v_min = 0;
	private double batman_v_global_min = 999999;
	private double batman_current_average = 0;
	private JButton batman_v_global_max_reset = new JButton("Reset");
	private JButton batman_v_global_min_reset = new JButton("Reset");
	
	private JLabel robin_v_average_l = new JLabel("0000.000");
	private JLabel robin_v_max_l = new JLabel("0000.000");
	private JLabel robin_v_global_max_l = new JLabel("0000.000");
	private JLabel robin_v_min_l = new JLabel("0000.000");
	private JLabel robin_v_global_min_l = new JLabel("0000.000");
	private JLabel robin_current_l = new JLabel("0000.000");
	private JLabel robin_t_average_l = new JLabel("0000.000");
	private JLabel robin_t_max_l = new JLabel("0000.000");
	private JLabel robin_t_min_l = new JLabel("0000.000");
	private double robin_ave_temp = 0;
	private double robin_v_average = 0;
	private double robin_v_max = 0;
	private double robin_v_global_max = 0;
	private double robin_v_min = 0;
	private double robin_v_global_min = 9999999;
	private double robin_current_average = 0;
	private JButton robin_v_global_max_reset = new JButton("Reset");
	private JButton robin_v_global_min_reset = new JButton("Reset");
	
	// Threshold for fields
	private double v_max_threshold = 9999999;
	private double v_min_threshold = 0;
	private double v_avg_threshold = 9999999;
	private double t_max_threshold = 9999999;
	private double t_min_threshold = 0;
	private double t_avg_threshold = 9999999;
	private double current_threshold = 9999999;
	
	public BatteryPanel() {
		setLayout(new GridBagLayout());

		insertComponents();
	}
	
	public void updatePanel(JSONObject obj, String type) {
		if(type.equals("bat_temp")) {
			if(((String) obj.get("name")).equals("0")) {
				batman_ave_temp = Double.parseDouble((String) obj.get("Tavg"));
				if(batman_ave_temp > t_avg_threshold)
					batman_t_average_l.setBackground(Color.RED);
				else
					batman_t_average_l.setBackground(Color.GREEN);
				batman_t_average_l.setText(DevicePanel.roundDouble((String) obj.get("Tavg")));
				
				if(Double.parseDouble((String) obj.get("Tmax")) > t_max_threshold)
					batman_t_max_l.setBackground(Color.RED);
				else
					batman_t_max_l.setBackground(Color.GREEN);
				batman_t_max_l.setText(DevicePanel.roundDouble((String) obj.get("Tmax")));
				
				if(Double.parseDouble((String) obj.get("Tmin")) < t_min_threshold)
					batman_t_min_l.setBackground(Color.RED);
				else
					batman_t_min_l.setBackground(Color.GREEN);
				batman_t_min_l.setText(DevicePanel.roundDouble((String) obj.get("Tmin")));
			}
			else {
				robin_ave_temp = Double.parseDouble((String) obj.get("Tavg"));
				if(robin_ave_temp > t_avg_threshold)
					robin_t_average_l.setBackground(Color.RED);
				else
					robin_t_average_l.setBackground(Color.GREEN);
				robin_t_average_l.setText(DevicePanel.roundDouble((String) obj.get("Tavg")));
				
				if(Double.parseDouble((String) obj.get("Tmax")) > t_max_threshold)
					robin_t_max_l.setBackground(Color.RED);
				else
					robin_t_max_l.setBackground(Color.GREEN);
				robin_t_max_l.setText(DevicePanel.roundDouble((String) obj.get("Tmax")));
				
				if(Double.parseDouble((String) obj.get("Tmin")) < t_min_threshold)
					robin_t_min_l.setBackground(Color.RED);
				else
					robin_t_min_l.setBackground(Color.GREEN);
				robin_t_min_l.setText(DevicePanel.roundDouble((String) obj.get("Tmin")));
			}
		}
		else if(type.equals("bat_volt")) {
			if(((String) obj.get("name")).equals("0")) {
				batman_v_average       = Double.parseDouble((String) obj.get("Vavg"));
				batman_v_max           = Double.parseDouble((String) obj.get("Vmax"));
				batman_v_min           = Double.parseDouble((String) obj.get("Vmin"));
				batman_current_average = Double.parseDouble((String) obj.get("BC"));
			
				if(batman_v_global_max < batman_v_max ) {
					batman_v_global_max = batman_v_max;
					if(batman_v_global_max > v_max_threshold)
						batman_v_global_max_l.setBackground(Color.RED);
					else
						batman_v_global_max_l.setBackground(Color.GREEN);
					batman_v_global_max_l.setText(DevicePanel.roundDouble((String) obj.get("Vmax")));
				}

				if(batman_v_global_min > batman_v_min ) {
					batman_v_global_min = batman_v_min;
					if(batman_v_global_min < v_min_threshold)
						batman_v_global_min_l.setBackground(Color.RED);
					else
						batman_v_global_min_l.setBackground(Color.GREEN);
					batman_v_global_min_l.setText(DevicePanel.roundDouble((String) obj.get("Vmin")));
				}

				if(batman_v_average > v_avg_threshold)
					batman_v_average_l.setBackground(Color.RED);
				else
					batman_v_average_l.setBackground(Color.GREEN);
				batman_v_average_l.setText(DevicePanel.roundDouble((String) obj.get("Vavg")));

				if(batman_v_max > v_max_threshold)
					batman_v_max_l.setBackground(Color.RED);
				else
					batman_v_max_l.setBackground(Color.GREEN);
				batman_v_max_l.setText(DevicePanel.roundDouble((String) obj.get("Vmax")));

				if(batman_v_min < v_min_threshold)
					batman_v_min_l.setBackground(Color.RED);
				else
					batman_v_min_l.setBackground(Color.GREEN);
				batman_v_min_l.setText(DevicePanel.roundDouble((String) obj.get("Vmin")));

				if(batman_current_average > current_threshold)
					batman_current_l.setBackground(Color.RED);
				else
					batman_current_l.setBackground(Color.GREEN);
				batman_current_l.setText(DevicePanel.roundDouble((String) obj.get("BC")));
			}
			else {
				robin_v_average       = Double.parseDouble((String) obj.get("Vavg"));
				robin_v_max           = Double.parseDouble((String) obj.get("Vmax"));
				robin_v_min           = Double.parseDouble((String) obj.get("Vmin"));
				robin_current_average = Double.parseDouble((String) obj.get("BC"));
			
				if(robin_v_global_max < robin_v_max ) {
					robin_v_global_max = robin_v_max;
					if(robin_v_global_max > v_max_threshold)
						robin_v_global_max_l.setBackground(Color.RED);
					else
						robin_v_global_max_l.setBackground(Color.GREEN);
					robin_v_global_max_l.setText(DevicePanel.roundDouble((String) obj.get("Vmax")));
				}

				if(robin_v_global_min > robin_v_min ) {
					robin_v_global_min = robin_v_min;
					if(robin_v_global_min < v_min_threshold)
						robin_v_global_min_l.setBackground(Color.RED);
					else
						robin_v_global_min_l.setBackground(Color.GREEN);
					robin_v_global_min_l.setText(DevicePanel.roundDouble((String) obj.get("Vmin")));
				}

				if(robin_v_average > v_avg_threshold)
					robin_v_average_l.setBackground(Color.RED);
				else
					robin_v_average_l.setBackground(Color.GREEN);
				robin_v_average_l.setText(DevicePanel.roundDouble((String) obj.get("Vavg")));

				if(robin_v_max > v_max_threshold)
					robin_v_max_l.setBackground(Color.RED);
				else
					robin_v_max_l.setBackground(Color.GREEN);
				robin_v_max_l.setText(DevicePanel.roundDouble((String) obj.get("Vmax")));

				if(robin_v_min < v_min_threshold)
					robin_v_min_l.setBackground(Color.RED);
				else
					robin_v_min_l.setBackground(Color.GREEN);
				robin_v_min_l.setText(DevicePanel.roundDouble((String) obj.get("Vmin")));

				if(robin_current_average > current_threshold)
					robin_current_l.setBackground(Color.RED);
				else
					robin_current_l.setBackground(Color.GREEN);
				robin_current_l.setText(DevicePanel.roundDouble((String) obj.get("BC")));
			}
		}
		
		validate();
		repaint();
	}
	
	@SuppressWarnings("unchecked")
	public JSONObject getData() {
		JSONObject batman_data = new JSONObject();
		JSONObject robin_data = new JSONObject();
		JSONObject combined_data = new JSONObject();
		
		batman_data.put("ave_temp", new Double(batman_ave_temp));
		batman_data.put("v_average", new Double(batman_v_average));
		batman_data.put("v_min", new Double(batman_v_min));
		batman_data.put("current_average", new Double(batman_current_average));
		
		robin_data.put("ave_temp", new Double(robin_ave_temp));
		robin_data.put("v_average", new Double(robin_v_average));
		robin_data.put("v_min", new Double(robin_v_min));
		robin_data.put("current_average", new Double(robin_current_average));
		
		combined_data.put("batman", batman_data);
		combined_data.put("robin", robin_data);
		
		return combined_data;
	}
	
	private void insertComponents() {
		GridBagConstraints gbc = new GridBagConstraints();
		
		// GBC constants
		gbc.insets = new Insets(3, 3, 3, 3);
		gbc.anchor = GridBagConstraints.CENTER;

		//--------------------------Title--------------------------//
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.gridwidth = 8;
		JLabel title = new JLabel("Batteries");
		title.setFont(DevicePanel.TITLE_FONT);
		add(title, gbc);

		//-----------------------Sub-Titles------------------------//
		// Sets the grid width to 4
		gbc.gridwidth = 4;
		
		gbc.gridx = 0;
		gbc.gridy = 1;
		JLabel batman_title = new JLabel("Batman");
		batman_title.setFont(new Font("Consolas", Font.BOLD, 16));
		add(batman_title, gbc);
		
		gbc.gridx = 3;
		gbc.gridy = 1;
		JLabel robin_title = new JLabel("Robin");
		robin_title.setFont(new Font("Consolas", Font.BOLD, 16));
		add(robin_title, gbc);
		
		// Set the grid width back to 1
		gbc.gridwidth = 1;

		//----------------------Batman Labels----------------------//
		ArrayList<JLabel> batman_labels = new ArrayList<JLabel>();
		
		batman_labels.add(new JLabel("Voltage Average:"));
		batman_labels.add(new JLabel("Voltage Max:"));
		batman_labels.add(new JLabel("Voltage Min:"));
		batman_labels.add(new JLabel("Global Voltage Max:"));
		batman_labels.add(new JLabel("Global Voltage Min:"));
		batman_labels.add(new JLabel("Current:"));
		batman_labels.add(new JLabel("Temp. Average:"));
		batman_labels.add(new JLabel("Temp. Max:"));
		batman_labels.add(new JLabel("Temp. Min:"));
		
		// Initial position
		gbc.gridx = 0;
		gbc.gridy = 2;
		
		for(JLabel batman_label : batman_labels) {
			batman_label.setFont(DevicePanel.FIELD_FONT);
			add(batman_label, gbc);
			gbc.gridy++;
		}

		//----------------------Robin Labels----------------------//
		ArrayList<JLabel> robin_labels = new ArrayList<JLabel>();
		
		robin_labels.add(new JLabel("Voltage Average:"));
		robin_labels.add(new JLabel("Voltage Max:"));
		robin_labels.add(new JLabel("Voltage Min:"));
		robin_labels.add(new JLabel("Global Voltage Max:"));
		robin_labels.add(new JLabel("Global Voltage Min:"));
		robin_labels.add(new JLabel("Current:"));
		robin_labels.add(new JLabel("Temp. Average:"));
		robin_labels.add(new JLabel("Temp. Max:"));
		robin_labels.add(new JLabel("Temp. Min:"));
		
		// Initial position
		gbc.gridx = 4;
		gbc.gridy = 2;
		
		for(JLabel robin_label : robin_labels) {
			robin_label.setFont(DevicePanel.FIELD_FONT);
			add(robin_label, gbc);
			gbc.gridy++;
		}

		//----------------------Unit Labels----------------------//
		ArrayList<JLabel> batman_units = new ArrayList<JLabel>();

		batman_units.add(new JLabel(" V"));
		batman_units.add(new JLabel(" V (#)"));
		batman_units.add(new JLabel(" V (#)"));
		batman_units.add(new JLabel(" V (#)"));
		batman_units.add(new JLabel(" V (#)"));
		batman_units.add(new JLabel(" A"));
		batman_units.add(new JLabel(" C"));
		batman_units.add(new JLabel(" C"));
		batman_units.add(new JLabel(" C"));
		
		// Initial position for Batman
		gbc.gridx = 2;
		gbc.gridy = 2;
		
		for(JLabel unit_label : batman_units) {
			unit_label.setFont(DevicePanel.FIELD_FONT);
			add(unit_label, gbc);
			gbc.gridy++;
		}
		
		// Initial position for Robin
		gbc.gridx = 6;
		gbc.gridy = 2;
		
		ArrayList<JLabel> robin_units = new ArrayList<JLabel>();

		robin_units.add(new JLabel(" V"));
		robin_units.add(new JLabel(" V (#)"));
		robin_units.add(new JLabel(" V (#)"));
		robin_units.add(new JLabel(" V (#)"));
		robin_units.add(new JLabel(" V (#)"));
		robin_units.add(new JLabel(" A"));
		robin_units.add(new JLabel(" C"));
		robin_units.add(new JLabel(" C"));
		robin_units.add(new JLabel(" C"));
		
		for(JLabel unit_label : robin_units) {
			unit_label.setFont(DevicePanel.FIELD_FONT);
			add(unit_label, gbc);
			gbc.gridy++;
		}

		//-------------------Batman Fields--------------------//
		gbc.insets = new Insets(3, 30, 3, 30);
		
		gbc.gridx = 1;
		gbc.gridy = 2;
		batman_v_average_l.setFont(DevicePanel.FIELD_FONT);
		batman_v_average_l.setOpaque(true);
		batman_v_average_l.setBackground(Color.ORANGE);
		add(batman_v_average_l, gbc);
		
		gbc.gridx = 1;
		gbc.gridy = 3;
		batman_v_max_l.setFont(DevicePanel.FIELD_FONT);
		batman_v_max_l.setOpaque(true);
		batman_v_max_l.setBackground(Color.ORANGE);
		add(batman_v_max_l, gbc);
		
		gbc.gridx = 1;
		gbc.gridy = 4;
		batman_v_min_l.setFont(DevicePanel.FIELD_FONT);
		batman_v_min_l.setOpaque(true);
		batman_v_min_l.setBackground(Color.ORANGE);
		add(batman_v_min_l, gbc);
		
		gbc.gridx = 1;
		gbc.gridy = 5;
		batman_v_global_max_l.setFont(DevicePanel.FIELD_FONT);
		batman_v_global_max_l.setOpaque(true);
		batman_v_global_max_l.setBackground(Color.ORANGE);
		batman_v_global_max_l.setPreferredSize(batman_v_global_max_l.getMinimumSize());
		batman_v_global_max_l.setMaximumSize(batman_v_global_max_l.getMinimumSize());
		add(batman_v_global_max_l, gbc);
		
		gbc.gridx = 1;
		gbc.gridy = 6;
		batman_v_global_min_l.setFont(DevicePanel.FIELD_FONT);
		batman_v_global_min_l.setOpaque(true);
		batman_v_global_min_l.setBackground(Color.ORANGE);
		batman_v_global_min_l.setPreferredSize(batman_v_global_min_l.getMinimumSize());
		batman_v_global_min_l.setMaximumSize(batman_v_global_min_l.getMinimumSize());
		add(batman_v_global_min_l, gbc);
		
		gbc.gridx = 1;
		gbc.gridy = 7;
		batman_current_l.setFont(DevicePanel.FIELD_FONT);
		batman_current_l.setOpaque(true);
		batman_current_l.setBackground(Color.ORANGE);
		add(batman_current_l, gbc);
		
		gbc.gridx = 1;
		gbc.gridy = 8;
		batman_t_average_l.setFont(DevicePanel.FIELD_FONT);
		batman_t_average_l.setOpaque(true);
		batman_t_average_l.setBackground(Color.ORANGE);
		add(batman_t_average_l, gbc);
		
		gbc.gridx = 1;
		gbc.gridy = 9;
		batman_t_max_l.setFont(DevicePanel.FIELD_FONT);
		batman_t_max_l.setOpaque(true);
		batman_t_max_l.setBackground(Color.ORANGE);
		add(batman_t_max_l, gbc);
		
		gbc.gridx = 1;
		gbc.gridy = 10;
		batman_t_min_l.setFont(DevicePanel.FIELD_FONT);
		batman_t_min_l.setOpaque(true);
		batman_t_min_l.setBackground(Color.ORANGE);
		add(batman_t_min_l, gbc);
		
		//-------------------Robin Fields--------------------//
		gbc.gridx = 5;
		gbc.gridy = 2;
		robin_v_average_l.setFont(DevicePanel.FIELD_FONT);
		robin_v_average_l.setOpaque(true);
		robin_v_average_l.setBackground(Color.ORANGE);
		add(robin_v_average_l, gbc);
		
		gbc.gridx = 5;
		gbc.gridy = 3;
		robin_v_max_l.setFont(DevicePanel.FIELD_FONT);
		robin_v_max_l.setOpaque(true);
		robin_v_max_l.setBackground(Color.ORANGE);
		add(robin_v_max_l, gbc);
		
		gbc.gridx = 5;
		gbc.gridy = 4;
		robin_v_min_l.setFont(DevicePanel.FIELD_FONT);
		robin_v_min_l.setOpaque(true);
		robin_v_min_l.setBackground(Color.ORANGE);
		add(robin_v_min_l, gbc);
		
		gbc.gridx = 5;
		gbc.gridy = 5;
		robin_v_global_max_l.setFont(DevicePanel.FIELD_FONT);
		robin_v_global_max_l.setOpaque(true);
		robin_v_global_max_l.setBackground(Color.ORANGE);
		robin_v_global_min_l.setPreferredSize(robin_v_global_min_l.getMinimumSize());
		robin_v_global_min_l.setMaximumSize(robin_v_global_min_l.getMinimumSize());
		add(robin_v_global_max_l, gbc);
		
		gbc.gridx = 5;
		gbc.gridy = 6;
		robin_v_global_min_l.setFont(DevicePanel.FIELD_FONT);
		robin_v_global_min_l.setOpaque(true);
		robin_v_global_min_l.setBackground(Color.ORANGE);
		robin_v_global_min_l.setPreferredSize(robin_v_global_min_l.getMinimumSize());
		robin_v_global_min_l.setMaximumSize(robin_v_global_min_l.getMinimumSize());
		add(robin_v_global_min_l, gbc);
		
		gbc.gridx = 5;
		gbc.gridy = 7;
		robin_current_l.setFont(DevicePanel.FIELD_FONT);
		robin_current_l.setOpaque(true);
		robin_current_l.setBackground(Color.ORANGE);
		add(robin_current_l, gbc);
		
		gbc.gridx = 5;
		gbc.gridy = 8;
		robin_t_average_l.setFont(DevicePanel.FIELD_FONT);
		robin_t_average_l.setOpaque(true);
		robin_t_average_l.setBackground(Color.ORANGE);
		add(robin_t_average_l, gbc);
		
		gbc.gridx = 5;
		gbc.gridy = 9;
		robin_t_max_l.setFont(DevicePanel.FIELD_FONT);
		robin_t_max_l.setOpaque(true);
		robin_t_max_l.setBackground(Color.ORANGE);
		add(robin_t_max_l, gbc);
		
		gbc.gridx = 5;
		gbc.gridy = 10;
		robin_t_min_l.setFont(DevicePanel.FIELD_FONT);
		robin_t_min_l.setOpaque(true);
		robin_t_min_l.setBackground(Color.ORANGE);
		add(robin_t_min_l, gbc);

		//-------------------Reset Buttons--------------------//
		gbc.insets = new Insets(3, 10, 3, 10);
		
		gbc.gridx = 3;
		gbc.gridy = 5;
		batman_v_global_max_reset.setBackground(Color.GRAY);
		batman_v_global_max_reset.addActionListener(new BatmanGlobalMaxVoltageReset());
		add(batman_v_global_max_reset, gbc);
		
		gbc.gridx = 3;
		gbc.gridy = 6;
		batman_v_global_min_reset.setBackground(Color.GRAY);
		batman_v_global_min_reset.addActionListener(new BatmanGlobalMinVoltageReset());
		add(batman_v_global_min_reset, gbc);
		
		gbc.gridx = 7;
		gbc.gridy = 5;
		robin_v_global_max_reset.setBackground(Color.GRAY);
		robin_v_global_max_reset.addActionListener(new RobinGlobalMaxVoltageReset());
		add(robin_v_global_max_reset, gbc);
		
		gbc.gridx = 7;
		gbc.gridy = 6;
		robin_v_global_min_reset.setBackground(Color.GRAY);
		robin_v_global_min_reset.addActionListener(new RobinGlobalMinVoltageReset());
		add(robin_v_global_min_reset, gbc);
		
		// Reset Insets
		gbc.insets = new Insets(3, 3, 3, 3);
	}

	class BatmanGlobalMaxVoltageReset implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			batman_v_global_max = 0;
			batman_v_global_max_l.setText(Double.toString(batman_v_global_max));
		}
	}

	class BatmanGlobalMinVoltageReset implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			batman_v_global_min = 9999;
			batman_v_global_min_l.setText(Double.toString(batman_v_global_min));
		}
	}
	
	class RobinGlobalMaxVoltageReset implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			robin_v_global_max = 0;
			robin_v_global_max_l.setText(Double.toString(robin_v_global_max));
		}
	}
	
	class RobinGlobalMinVoltageReset implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			robin_v_global_min = 9999;
			robin_v_global_min_l.setText(Double.toString(robin_v_global_min));
		}
	}
}