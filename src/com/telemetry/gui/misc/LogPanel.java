package com.telemetry.gui.misc;

import javax.swing.*;

import org.json.simple.JSONObject;

/**
 * Currently not used, random bug that prevents this panel from updating
 * @author Weilian Song
 *
 */
public class LogPanel extends JScrollPane {

	private static final long serialVersionUID = 4467077703107588725L;
	private JTextArea text_area;
	
	public LogPanel() {
		text_area = new JTextArea();
		text_area.setEditable(false);
		add(text_area);
	}
	
	public void updatePanel(JSONObject obj) {
		String line = obj.toString();
		text_area.append(line + "\n\n");
		
		validate();
		repaint();
	}
}