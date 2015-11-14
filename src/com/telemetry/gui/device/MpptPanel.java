package com.telemetry.gui.device;

import java.awt.GridLayout;
import javax.swing.*;

public class MpptPanel extends JPanel {
	private static final long serialVersionUID = 4997694178272359227L;
	private JPanel mppt_label_panel = new JPanel();
	private JPanel mppt_data_panel = new JPanel();
	private JLabel out_current = new JLabel("Out Current");
	
	public MpptPanel() {
		setLayout(new GridLayout(1, 2));
		mppt_label_panel.setLayout(new GridLayout(2, 1, 10, 10));
		mppt_data_panel.setLayout(new GridLayout(2, 1, 10, 10));
		
		insertLabelPanel();
		insertDataPanel();
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
		repaint();
	}
}
