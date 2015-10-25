package com.telemetry.gui;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.Panel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

public class DevicePanel extends Panel{
	
	Panel master_panel = new Panel();
	
	public DevicePanel(int tab_panel_x, int tab_panel_y) {
		super();
		
		master_panel.setSize(tab_panel_x, tab_panel_y);
		master_panel.setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		
	}
}
