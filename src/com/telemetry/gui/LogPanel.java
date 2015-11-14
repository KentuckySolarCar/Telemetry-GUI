package com.telemetry.gui;

import java.awt.*;
import javax.swing.*;

public class LogPanel extends Panel {

	private static final long serialVersionUID = 4467077703107588725L;
	private static JLabel log_label = new JLabel("Log Display");
	private static Canvas log_canvas = new Canvas();
	private final TextField usr_text = new TextField(61);
	
	public LogPanel(int tab_panel_x, int tab_panel_y) {
		super();
		
		setSize(tab_panel_x, tab_panel_y);
		setLayout(new GridLayout(1, 2));
		
		log_canvas.setSize(500, 705);
		log_canvas.setBackground(Color.GRAY);
		
		add(log_label);
		add(log_canvas);
		add(usr_text);
	}
}
