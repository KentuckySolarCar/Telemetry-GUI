package com.telemetry.gui;

import javax.swing.*;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.telemetry.gui.device.DevicePanel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class TelemetryFrame extends JFrame implements ActionListener {
	private static final long serialVersionUID = 3028986629905272450L;
	private static final int WIDTH = 1280;
	private static final int HEIGHT = 720;
	
	private JTabbedPane tab_panel;
	private static CalculationPanel calculation_panel;
	private static DevicePanel device_panel;
	private static LogPanel log_panel;
	private static GraphPanel graph_panel;
	private static JScrollPane log_scroll;
	private int tab_panel_x = 800;
	private int tab_panel_y = 640;
	
	// Constructor to initialize the GUI
	public TelemetryFrame() {
		super("University of Kentucky Solar Car Telemetry");
		
		// Initializes and edits the main window frame of GUI
		setSize(WIDTH, HEIGHT);
		setMinimumSize(new Dimension(WIDTH, HEIGHT));
		setResizable(false);
		setLayout(new BorderLayout());
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		// Initializes and edits layout of tab_panel container
		tab_panel = new JTabbedPane();
	    calculation_panel = new CalculationPanel(tab_panel_x, tab_panel_y);
	    device_panel = new DevicePanel(tab_panel_x, tab_panel_y);
	    log_panel = new LogPanel(tab_panel_x, tab_panel_y);
	    graph_panel = new GraphPanel(tab_panel_x, tab_panel_y);
	    
		tab_panel.add("Car Status", device_panel);
		tab_panel.add("Calculation", calculation_panel);
		tab_panel.add("Graphs", graph_panel);
	    tab_panel.add("Map", new JLabel("This is just a sad stub..."));
		
		tab_panel.setPreferredSize(new Dimension(tab_panel_x, tab_panel_y));
		
		new JScrollPane(tab_panel);
		log_scroll = new JScrollPane(log_panel);
		
		// Position tab_panel and log_panel in main_frame with tab_panel WEST
		// and log_panel EAST 
		add(tab_panel, BorderLayout.WEST);
		add(log_scroll, BorderLayout.EAST);
		
		createMenuBar();
		
		// Reveals main_frame
		setVisible(true);
		setLocationRelativeTo(null);
	}
	
	private void createMenuBar(){
		final JMenuBar menu_bar = new JMenuBar();
		
		// create menus
		JMenu file_menu = new JMenu("File");
		JMenu control_menu = new JMenu("Control");
		final JMenu about_menu = new JMenu("About");
		
		// create menu items
		JMenuItem exit_menu_item = new JMenuItem("Exit");
		exit_menu_item.setActionCommand("Exit");
		
		JMenuItem change_port = new JMenuItem("Change Port");		
		JMenuItem start_monitor = new JMenuItem("Start Monitor");	
		JMenuItem start_logging = new JMenuItem("Start Logging");	
		JMenuItem reset_calculations = new JMenuItem("Start Calculations");
		
		start_monitor.addActionListener(this);
		
		//add menu items to menus
		file_menu.add(exit_menu_item);
		
		//add control items to control menu
		control_menu.add(change_port);
		control_menu.add(start_monitor);
		control_menu.add(start_logging);
		control_menu.add(reset_calculations);
		
		//add menu to menu bar
		menu_bar.add(file_menu);
		menu_bar.add(control_menu);
		menu_bar.add(about_menu);
		
		//add menu bar to the frame
		setJMenuBar(menu_bar);
		setVisible(true);
	}
	
	public void actionPerformed(ActionEvent e) {
		calculation_panel.updatePanel();
		validate();
		repaint();
	}
	
	public static void updateTelemetryFrame(JSONArray array, String type) {
		device_panel.updatePanel(array, type);
		calculation_panel.updatePanel();
	}
	
	@SuppressWarnings("unused")
	private JPanel createControlPanel(){
		//panel within panel
		JPanel panel = new JPanel();
		panel.setBackground(Color.white);
		panel.setSize(300, 300);
		GridBagLayout layout = new GridBagLayout();
				
		panel.setLayout(layout);
		GridBagConstraints gbc = new GridBagConstraints();
				
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.gridx = 0;
		gbc.gridy = 0;
		panel.add(new JButton("Change Port"), gbc);
		
		gbc.gridx = 1;
		gbc.gridy = 0;
		panel.add(new JButton("Start Monitor"), gbc);
				
		gbc.gridx = 2;
		gbc.gridy = 0;
		panel.add(new JButton("Start Logging"), gbc);
				
		gbc.gridx = 3;
		gbc.gridy = 0;
		panel.add(new JButton("Reset Calculations"), gbc);
				
		gbc.gridx = 4;
		gbc.gridy = 0;
		gbc.gridwidth = 4;
		panel.add(new JButton("Exit"), gbc);
		
		return panel;	
	}
}
