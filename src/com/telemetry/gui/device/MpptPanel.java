package com.telemetry.gui.device;

import java.awt.*;

public class MpptPanel extends Panel {
	private static final long serialVersionUID = 4997694178272359227L;
	private Panel mppt_label_panel = new Panel();
	private Panel mppt_data_panel = new Panel();
	private Label out_current = new Label("Out Current");
	
	public MpptPanel() {
		setLayout(new GridLayout(1, 2));
		mppt_label_panel.setLayout(new GridLayout(2, 1, 10, 10));
		mppt_data_panel.setLayout(new GridLayout(2, 1, 10, 10));
		
		insertLabelPanel();
		insertDataPanel();
	}
	
	private void insertLabelPanel() {
		mppt_label_panel.add(new Label("MPPTs"));
		mppt_label_panel.add(new Label("    #"));
		add(mppt_label_panel);
	}
	
	private void insertDataPanel() {
		mppt_data_panel.add(new Label(" "));
		mppt_data_panel.add(out_current);
		add(mppt_data_panel);
	}
	
	public void updatePanel() {
		out_current.setText("Need Implementing!");
		repaint();
	}
}