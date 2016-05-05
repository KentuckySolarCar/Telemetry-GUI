package com.telemetry.gui.device;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;

import javax.swing.*;

public class MpptPanel extends JPanel {
	private static final long serialVersionUID = 4997694178272359227L;
	private JPanel mppt_label_panel = new JPanel();
	private JPanel mppt_data_panel = new JPanel();
	private JLabel out_current = new JLabel("Out Current");
	
	public MpptPanel() {
		setLayout(new GridBagLayout());
		insertComponents();
	}
	
	private void insertComponents() {
		GridBagConstraints gbc = new GridBagConstraints();
		
		// GBC constants
		gbc.insets = new Insets(5, 5, 5, 5);
		gbc.anchor = GridBagConstraints.CENTER;
		
		//----------------------Title------------------------//
		gbc.gridx = 1;
		gbc.gridy = 0;
		JLabel title = new JLabel("MPPTs");
		title.setFont(DevicePanel.TITLE_FONT);
		add(title, gbc);
		
		//----------------------Labels------------------------//
		gbc.gridx = 0;
		gbc.gridy = 1;
		JLabel mppt_label = new JLabel("#");
		mppt_label.setFont(DevicePanel.FIELD_FONT);
		add(mppt_label, gbc);
		
		//----------------------Labels------------------------//
		gbc.gridx = 2;
		gbc.gridy = 1;
		out_current.setFont(DevicePanel.FIELD_FONT);
		out_current.setOpaque(true);
		out_current.setBackground(Color.ORANGE);
		add(out_current, gbc);
	}
	
	private void insertLabelPanel() {
		mppt_label_panel.add(new JLabel("MPPTs"));
		mppt_label_panel.add(new JLabel("    #"));
		add(mppt_label_panel);
	}
	
	private void insertDataPanel() {
		mppt_data_panel.add(new JLabel(" "));
		mppt_data_panel.add(out_current);
		add(mppt_data_panel);
	}
	
	public void updatePanel() {
		out_current.setText("Need Implementing!");
		validate();
		repaint();
	}
}
