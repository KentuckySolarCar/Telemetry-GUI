package com.telemetry.gui.misc;

import java.awt.BorderLayout;

import javax.swing.*;

import org.json.simple.JSONObject;

import com.telemetry.util.Tools;

/**
 * Currently not used, random bug that prevents this panel from updating
 * @author Weilian Song
 *
 */
public class LogPanel extends JPanel {

	private static final long serialVersionUID = 4467077703107588725L;
	private JTextArea text_area;
	
	public LogPanel() {
		setLayout(new BorderLayout());
		text_area = new JTextArea();
		text_area.setEditable(false);
		add(text_area, BorderLayout.SOUTH);
		validate();
		repaint();
	}
	
	public void updatePanel(JSONObject obj) {
		String line = obj.toString();
		text_area.append(line + "\n\n");

	}
}