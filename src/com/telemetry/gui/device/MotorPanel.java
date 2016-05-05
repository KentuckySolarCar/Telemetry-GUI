package com.telemetry.gui.device;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import org.json.simple.JSONObject;

import com.telemetry.custom.SizedQueue;

import java.util.Queue;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class MotorPanel extends JPanel {

	private static final long serialVersionUID = -4958513623339300406L;
	private JPanel motor_label_panel    = new JPanel();
	private JPanel motor_data_panel     = new JPanel();
	private JPanel motor_button_panel   = new JPanel();
	private JLabel speed_label          = new JLabel("VALUE");
	private JLabel current_label        = new JLabel("VALUE");
	private JLabel energy_label         = new JLabel("VALUE");
	private JLabel average_speed_label  = new JLabel("VALUE");
	private JButton energy_reset        = new JButton("Energy Reset");
	private JButton average_speed_reset = new JButton("Speed Reset");

	private Queue<Double> speed;
	private Queue<Double> current;
	private double amp_sec;
	private double watt_sec;
	private double odometer;
	private double energy;
	
	private static final double speed_conversion = 0.223693629;
	
	public MotorPanel() {
		speed = new SizedQueue<Double>(60);
		current = new SizedQueue<Double>(60);
		
		insertComponents();
	}
	
	@SuppressWarnings("unchecked")
	public JSONObject getData() {
		JSONObject data = new JSONObject();
		data.put("ave_speed", new Double(calculateAveSpeed()));
		data.put("ave_current", new Double(calculateAveCurrent()));
		data.put("amp_sec", new Double(amp_sec));
		data.put("watt_sec", new Double(watt_sec));
		data.put("odometer", new Double(odometer));
		return data;
	}
	
	private void insertComponents() {
		setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();

		// Constant attributes
		gbc.insets = new Insets(5, 5, 5, 5);
		
		//----------------------Title------------------------//
		gbc.gridx = 1;
		gbc.gridy = 0;
		gbc.anchor = GridBagConstraints.CENTER;
		JLabel motor_controller = new JLabel("Motor Controller");
		motor_controller.setFont(DevicePanel.TITLE_FONT);
		add(motor_controller, gbc);
		
		//----------------------Labels------------------------//
		gbc.gridx = 0;
		gbc.gridy = 1;
		JLabel speed = new JLabel("Speed:");
		speed.setFont(DevicePanel.FIELD_FONT);
		add(speed, gbc);

		gbc.gridx = 0;
		gbc.gridy = 2;
		JLabel current = new JLabel("Current:");
		current.setFont(DevicePanel.FIELD_FONT);
		add(current, gbc);

		gbc.gridx = 0;
		gbc.gridy = 3;
		JLabel energy = new JLabel("Energy:");
		energy.setFont(DevicePanel.FIELD_FONT);
		add(energy, gbc);

		gbc.gridx = 0;
		gbc.gridy = 4;
		JLabel av_speed = new JLabel("Av. Speed:");
		av_speed.setFont(DevicePanel.FIELD_FONT);
		add(av_speed, gbc);
			
		//----------------------Fields------------------------//
		gbc.gridx = 1;
		gbc.gridy = 1;
		speed_label.setFont(DevicePanel.FIELD_FONT);
		speed_label.setOpaque(true);
		speed_label.setBackground(Color.ORANGE);
		add(speed_label, gbc);

		gbc.gridx = 1;
		gbc.gridy = 2;
		current_label.setFont(DevicePanel.FIELD_FONT);
		current_label.setOpaque(true);
		current_label.setBackground(Color.ORANGE);
		add(current_label, gbc);

		gbc.gridx = 1;
		gbc.gridy = 3;
		energy_label.setFont(DevicePanel.FIELD_FONT);
		energy_label.setOpaque(true);
		energy_label.setBackground(Color.ORANGE);
		add(energy_label, gbc);

		gbc.gridx = 1;
		gbc.gridy = 4;
		average_speed_label.setFont(DevicePanel.FIELD_FONT);
		average_speed_label.setOpaque(true);
		average_speed_label.setBackground(Color.ORANGE);
		add(average_speed_label, gbc);

		//----------------------Buttons------------------------//
		gbc.insets = new Insets(0, 10, 0, 0);

		gbc.anchor = GridBagConstraints.CENTER;

		gbc.gridx = 2;
		gbc.gridy = 3;
		energy_reset.setBackground(Color.GRAY);
		energy_reset.addActionListener(new EnergyReset());
		add(energy_reset, gbc);

		gbc.gridx = 2;
		gbc.gridy = 4;
		average_speed_reset.setBackground(Color.GRAY);
		average_speed_reset.addActionListener(new AverageSpeedReset());
		add(average_speed_reset, gbc);
	}
	
	private void insertLabelPanel() {
		// Adding labels to motor_label_panel
		motor_label_panel.add(new JLabel("Motor Controller"));
		motor_label_panel.add(new JLabel("    Speed: "));
		motor_label_panel.add(new JLabel("    Current: "));
		motor_label_panel.add(new JLabel("    Energy: "));
		motor_label_panel.add(new JLabel("    Av. Speed: "));
		add(motor_label_panel);
	}
	
	private void insertDataPanel() {
		motor_data_panel.add(new JLabel(" "));
		motor_data_panel.add(speed_label);
		motor_data_panel.add(current_label);
		motor_data_panel.add(energy_label);
		motor_data_panel.add(average_speed_label);
		add(motor_data_panel);
	}
	
	private void insertButtonPanel() {
		motor_button_panel.add(new JLabel(" "));
		motor_button_panel.add(new JLabel(" "));
		motor_button_panel.add(new JLabel(" "));
		
		motor_button_panel.add(energy_reset);
		
		motor_button_panel.add(average_speed_reset);

		add(motor_button_panel);
	}
	
	public void updatePanel(JSONObject obj) {
		speed_label.setText((String) obj.get("S"));
		current_label.setText((String) obj.get("I"));
		
		speed.add(Double.parseDouble((String) obj.get("S")) * speed_conversion);
		current.add(Double.parseDouble((String) obj.get("I")));
		
		average_speed_label.setText(Double.toString(calculateAveSpeed()));
		validate();
		repaint();
	}
	
	private double calculateAveSpeed() {
		double sum = 0;
		for(Double d : speed)
			sum += d;
		return sum/speed.size();
	}
	
	private double calculateAveCurrent() {
		double sum = 0;
		for(Double d : current)
			sum += d;
		return sum/current.size();
	}
	
	class EnergyReset implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent arg0) {
			energy = 0;
			energy_label.setText(Double.toString(energy));
		}
	}
	
	class AverageSpeedReset implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			average_speed_label.setText("0.0");
		}
		
	}
}
