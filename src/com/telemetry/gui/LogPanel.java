package com.telemetry.gui;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

public class LogPanel extends JPanel implements ActionListener {

	private static final long serialVersionUID = 4467077703107588725L;
	private static JLabel log_label = new JLabel("Log Display");
//	private static Canvas log_canvas = new Canvas();
	private static JTextArea text_area;
	private final JTextField text_field = new JTextField(40);
	private final static String new_line = "\n";
	
	public LogPanel(int tab_panel_x, int tab_panel_y) {
		super();
		
		setSize(tab_panel_x, tab_panel_y);
		setLayout(new BorderLayout());
		
		text_area = new JTextArea();
		text_area.setEditable(false);
		JScrollPane text_pane = new JScrollPane(text_area);
		
		text_field.addActionListener(this);
	
		add(log_label, BorderLayout.NORTH);
		add(text_pane, BorderLayout.CENTER);
		add(text_field, BorderLayout.SOUTH);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String text = text_field.getText();
		text_area.append(text + new_line);
		text_field.selectAll();
	}
}
