package com.telemetry.gui;

import java.awt.*;
import java.awt.event.*;
import java.io.IOException;

import javax.swing.*;

import org.json.simple.JSONObject;

import com.telemetry.serial.SerialPortHandler;

public class LogPanel extends JPanel implements ActionListener {

	private static final long serialVersionUID = 4467077703107588725L;
	private static JLabel log_label = new JLabel("Log Display");
	private static JTextArea text_area;
	private final JTextField text_field = new JTextField(40);
	
	public LogPanel(int tab_panel_x, int tab_panel_y) {
		super();
		
		setSize(tab_panel_x, tab_panel_y);
		setLayout(new BorderLayout());
		
		text_area = new JTextArea();
		text_area.setEditable(false);
		text_area.setLineWrap(true);
		text_area.setAutoscrolls(true);
//		JScrollPane text_pane = new JScrollPane(text_area);
		
		text_field.addActionListener(this);
	
		add(log_label, BorderLayout.NORTH);
		add(text_area, BorderLayout.CENTER);
		add(text_field, BorderLayout.SOUTH);
	}
	
	public void updatePanel(JSONObject obj) {
		String line = obj.toString();
		text_area.append(line + "\n\n");
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		int command = Integer.valueOf(text_field.getText());
		
		try {
			SerialPortHandler.write_command(command);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		text_field.selectAll();
	}
}
