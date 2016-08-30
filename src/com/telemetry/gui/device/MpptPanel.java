package com.telemetry.gui.device;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.*;

/**
 * This panel is not currently used... 
 * @author Weilian Song
 */
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
