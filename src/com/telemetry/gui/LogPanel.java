package com.telemetry.gui;

import java.awt.*;
import java.awt.event.*;
import java.io.IOException;

import javax.swing.*;

import org.json.simple.JSONObject;

import com.telemetry.serial.SerialPortHandler;

public class LogPanel extends JPanel implements ActionListener {

	private static final long serialVersionUID = 4467077703107588725L;
	private JTextArea text_area;
	private JTextField text_field;
	private JScrollPane scroll_pane;
	
	public LogPanel(int tab_panel_x, int tab_panel_y) {
		super();
		
		setSize(tab_panel_x, tab_panel_y);
		setLayout(new BorderLayout());
		
		text_area = new JTextArea();
		text_area.setEditable(false);
		text_area.setLineWrap(true);
		text_area.setAutoscrolls(true);
		
		text_field = new JTextField();
		
		text_field.addActionListener(this);
		
		scroll_pane = new JScrollPane();
		scroll_pane.add(text_area);
	
		add(scroll_pane, BorderLayout.NORTH);
		add(text_field, BorderLayout.SOUTH);
	}
	
	public void updatePanel(JSONObject obj) {
		String line = obj.toString();
		text_area.append(line + "\n\n");
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String command = text_field.getText();
		
		try {
			SerialPortHandler.write_command(command.getBytes());
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		text_field.selectAll();
	}
}
