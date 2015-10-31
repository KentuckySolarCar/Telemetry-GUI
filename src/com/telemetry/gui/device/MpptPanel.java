package com.telemetry.gui.device;

import java.awt.*;

public class MpptPanel {
	private Panel mppt_panel = new Panel();
	private Panel mppt_label_panel = new Panel();
	private Panel mppt_data_panel = new Panel();
	private Label out_current = new Label("Out Current");
	
	public MpptPanel() {
		mppt_panel.setLayout(new GridLayout(1, 2));
		mppt_label_panel.setLayout(new GridLayout(2, 1, 10, 10));
		mppt_data_panel.setLayout(new GridLayout(2, 1, 10, 10));
		
		insertLabelPanel();
		insertDataPanel();
	}
	
	private void insertLabelPanel() {
		mppt_label_panel.add(new Label("MPPTs"));
		mppt_label_panel.add(new Label("    #"));
		mppt_panel.add(mppt_label_panel);
	}
	
	private void insertDataPanel() {
		mppt_data_panel.add(new Label(" "));
		mppt_data_panel.add(out_current);
		mppt_panel.add(mppt_data_panel);
	}
	
	@SuppressWarnings("unused")
	private void updatePanel() {
		out_current.setText("Need Implementing!");
	}
	
	public Panel getPanel() {
		return mppt_panel;
	}
}
