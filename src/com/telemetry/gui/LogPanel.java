package com.telemetry.gui;

import java.awt.*;
import java.awt.event.*;
import java.io.IOException;

import javax.swing.*;

import org.json.simple.JSONObject;

import com.telemetry.serial.SerialPortHandler;

public class LogPanel extends JPanel {

	private static final long serialVersionUID = 4467077703107588725L;
	private JTextArea text_area;
	private JScrollPane scroll_pane;
	
	public LogPanel() {
//		setSize(tab_panel_x, tab_panel_y);
		setLayout(new BorderLayout());
		
		text_area = new JTextArea();
		text_area.setEditable(false);
		text_area.setLineWrap(true);
//		text_area.setAutoscrolls(true);
		
		scroll_pane = new JScrollPane(text_area);
	
		add(scroll_pane, BorderLayout.CENTER);
	}
	
	public void updatePanel(JSONObject obj) {
		String line = obj.toString();
		text_area.append(line + "\n\n");
		
		validate();
		repaint();
	}
}
