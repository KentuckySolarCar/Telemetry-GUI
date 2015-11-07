package com.telemetry.gui;

import java.awt.*;
import java.awt.event.*;
import java.util.*;

import com.telemetry.gui.device.DevicePanel;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class TelemetryFrame extends Frame{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3028986629905272450L;
	private static final int WIDTH = 1600;
	private static final int HEIGHT = 900;
	private static final ActionListener CustomActionListener = null;
	
	private TabbedPane tab_panel;
	private CalculationPanel calculation_panel;
	private DevicePanel device_panel;
	private Panel log_panel;
	private Button update_button;
	private int tab_panel_x = 800;
	private int tab_panel_y = 800;
	
	// Constructor to initialize the GUI
	public TelemetryFrame() {
		super("University of Kentucky Solar Car Telemetry");
		
		// Initializes and edits the main window frame of GUI
		setSize(WIDTH, HEIGHT);
		setLayout(new BorderLayout());
		addWindowListener(new WindowAdapter() {
			
			public void windowClosing(WindowEvent windowEvent){
				System.exit(0);
			}
			
		});
		
		// Initializes and edits layout of tab_panel container
		tab_panel = new TabbedPane();
	    calculation_panel = new CalculationPanel(tab_panel_x, tab_panel_y);
	    device_panel = new DevicePanel(tab_panel_x, tab_panel_y);
	    update_button = new Button("UPDATE");
	    update_button.addActionListener(CustomActionListener);
		
		tab_panel.addTab("Car Status", device_panel);
		tab_panel.addTab("Calculation", calculation_panel);
		tab_panel.addTab("Graphs", createGraphPanel());
		tab_panel.addTab("Map", createMapPanel());
		
		tab_panel.setPreferredSize(new Dimension(tab_panel_x, tab_panel_y));
		
		// Initializes and edits layout of log_panel container
		log_panel = new Panel();
		log_panel.setLayout(new FlowLayout());
		createLogPanel();
		
		// Position tab_panel and log_panel in main_frame with tab_panel WEST
		// and log_panel EAST 
		add(tab_panel, BorderLayout.WEST);
		add(log_panel, BorderLayout.EAST);
		add(update_button, BorderLayout.CENTER);
		
		// Reveals main_frame
		setVisible(true);
	}
	
	class CustomActionListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			device_panel.updatePanel();
			repaint();
		}
	}
	
/*  TODO:
 * 	1. Implement this function	
 */
	private Panel createGraphPanel() {
		Panel master_panel = new Panel();
		master_panel.setSize(tab_panel_x, tab_panel_y);
		master_panel.setLayout(new GridLayout());
		return master_panel;
	}
	
/*  TODO:
 * 	1. Log data input needs to be retrieved
 * 	2. usr_text needs to take the input and store it somewhere
 * 	3. Change the font of log_label
 * 	4. Align log_label to left
 */
	private void createLogPanel() {
		// Initializes all components to log_panel
		Panel panel = new Panel();
		panel.setSize(tab_panel_x, tab_panel_y);
		panel.setLayout(new GridBagLayout());
		
		Label log_label = new Label("Log Display");
		
		Canvas log_canvas = new Canvas();
		log_canvas.setSize(500, 705);
		log_canvas.setBackground(Color.GRAY);
		
		final TextField usr_text = new TextField(61);
		
		// Creates a Grid Bag constraint and use it to add all components
		// to panel in a grid fashion
		GridBagConstraints gbc = new GridBagConstraints();
		
		gbc.fill = GridBagConstraints.VERTICAL;
		gbc.gridx = 0;
		gbc.gridy = 0;
		panel.add(log_label, gbc);
		
		gbc.gridx = 0;
		gbc.gridy = 1;
		panel.add(log_canvas, gbc);
		
		gbc.gridx = 0;
		gbc.gridy = 2;
		panel.add(usr_text, gbc);
		
		// Add panel as a whole into log_panel
		log_panel.add(panel);
		setVisible(true);
	}
	
	private void createMenuBar(){
		final MenuBar menu_bar = new MenuBar();
		
		// create menus
		Menu file_menu = new Menu("File");
		Menu control_menu = new Menu("Control");
		final Menu about_menu = new Menu("About");
		
		// create menu items
		MenuItem exit_menu_item = new MenuItem("Exit");
		exit_menu_item.setActionCommand("Exit");
		
		MenuItem change_port = new MenuItem("Change Port");		
		MenuItem start_monitor = new MenuItem("Start Monitor");	
		MenuItem start_logging = new MenuItem("Start Logging");	
		MenuItem reset_calculations = new MenuItem("Start Calculations");
				
		final CheckboxMenuItem show_window_menu = new CheckboxMenuItem("Show About", true);
		show_window_menu.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if(show_window_menu.getState())
					menu_bar.add(about_menu);
				else
					menu_bar.remove(about_menu);
			}
		});
		
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
		setMenuBar(menu_bar);
		setVisible(true);
	}
	
	private Panel createControlPanel(){
		//panel within panel
		Panel panel = new Panel();
		panel.setBackground(Color.white);
		panel.setSize(300, 300);
		GridBagLayout layout = new GridBagLayout();
				
		panel.setLayout(layout);
		GridBagConstraints gbc = new GridBagConstraints();
				
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.gridx = 0;
		gbc.gridy = 0;
		panel.add(new Button("Change Port"), gbc);
		
		gbc.gridx = 1;
		gbc.gridy = 0;
		panel.add(new Button("Start Monitor"), gbc);
				
		gbc.gridx = 2;
		gbc.gridy = 0;
		panel.add(new Button("Start Logging"), gbc);
				
		gbc.gridx = 3;
		gbc.gridy = 0;
		panel.add(new Button("Reset Calculations"), gbc);
				
		gbc.gridx = 4;
		gbc.gridy = 0;
		gbc.gridwidth = 4;
		panel.add(new Button("Exit"), gbc);
		
		return panel;	
	}
	
	private Panel createMapPanel() {
		Panel panel = new Panel();
		panel.setBackground(Color.white);
		panel.setSize(tab_panel_x, tab_panel_y);
		
		return panel;
	}
}
