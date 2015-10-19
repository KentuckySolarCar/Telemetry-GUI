package com.telemetry.gui;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class TelemetryFrame extends Frame{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3028986629905272450L;
	private static final int WIDTH = 800;
	private static final int HEIGHT = 600;
	
	private TabbedPane tab_panel;
	CalculationPanel calculation_panel;
	private Panel log_panel;
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
		
		tab_panel.addTab("Car Status", createDevicePanel());
		tab_panel.addTab("Calculation", calculation_panel.getPanel());
		tab_panel.addTab("Graphs", createGraphPanel());
		tab_panel.addTab("Map", createMapPanel());
		
		tab_panel.setPreferredSize(new Dimension(tab_panel_x, tab_panel_y));
		
		// Initializes and edits layout of log_panel container
		log_panel = new Panel();
		log_panel.setLayout(new FlowLayout());
		
		// Position tab_panel and log_panel in main_frame with tab_panel WEST
		// and log_panel EAST 
		add(tab_panel, BorderLayout.WEST);
		add(log_panel, BorderLayout.EAST);
		
		// Reveals main_frame
		setVisible(true);
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
	
/*  TODO:
 * 	1. Find a better way to create the list
 * 	2. Implement code so that this panel reads the calculations and
 * 	   outputs them correctly
 * 	3. Change the font of everything so they look neater
 */
	private Panel createCalculationPanel() {
		Panel master_panel = new Panel();
		Panel label_panel = new Panel();
		Panel output_panel = new Panel();
		Panel button_panel = new Panel();
	    master_panel.setSize(tab_panel_x, tab_panel_y);
	    master_panel.setLayout(new BorderLayout());
		label_panel.setLayout(new GridLayout(17, 1, 10, 15));
		output_panel.setLayout(new GridLayout(17, 1, 10, 15));
		button_panel.setLayout(new GridLayout(3, 3, 10, 10));
		
		label_panel.add(new Label("Calculations:"));
		label_panel.add(new Label("    Array Power"));
		label_panel.add(new Label("    Gross Watt*Hours"));
		label_panel.add(new Label("    Net Watt*Hours"));
		label_panel.add(new Label("    Average Speed"));
		label_panel.add(new Label("    Average Gross Power"));
		label_panel.add(new Label("    Average Net Power"));
		label_panel.add(new Label("    Gross Average Power"));
		label_panel.add(new Label("    Gross Average Watt Hour/Mile"));
		label_panel.add(new Label("    Battery Only Run-Time Remaining"));
		label_panel.add(new Label("    Battery Only Range"));
		label_panel.add(new Label("    Battery and Solar Run-Time"));
		label_panel.add(new Label("    Battery and Solar Range"));
		label_panel.add(new Label("    Battery Charge Remaining"));
		label_panel.add(new Label("    Solar Energy Ramaining in Day"));
		label_panel.add(new Label("    Motor Power"));
		
		output_panel.add(new Label(" "));
		output_panel.add(new Label("VALUE"));
		output_panel.add(new Label("VALUE"));
		output_panel.add(new Label("VALUE"));
		output_panel.add(new Label("VALUE"));
		output_panel.add(new Label("VALUE"));
		output_panel.add(new Label("VALUE"));
		output_panel.add(new Label("VALUE"));
		output_panel.add(new Label("VALUE"));
		output_panel.add(new Label("VALUE"));
		output_panel.add(new Label("VALUE"));
		output_panel.add(new Label("VALUE"));
		output_panel.add(new Label("VALUE"));
		output_panel.add(new Label("VALUE"));
		output_panel.add(new Label("VALUE"));
		output_panel.add(new Label("VALUE"));
		
		Button change_port_button = new Button("Change Port");
		Button start_monitor_button = new Button("Start Monitor");
		Button start_logging_button = new Button("Start Logging");
		Button reset_calculation_button = new Button("Reset Calculation");
		
		button_panel.add(new Label("Controls"));
		button_panel.add(new Label(" "));
		button_panel.add(new Label(" "));
		button_panel.add(new Label("    COM PORT:"));
		button_panel.add(new Label("/Port"));
		button_panel.add(change_port_button);
		button_panel.add(start_monitor_button);
		button_panel.add(start_logging_button);
		button_panel.add(reset_calculation_button);
		
		master_panel.add(label_panel, BorderLayout.WEST);
		master_panel.add(output_panel, BorderLayout.EAST);
		master_panel.add(button_panel, BorderLayout.SOUTH);
		
		return master_panel;
	}

/*  TODO:
 * 	
 */
	private Panel createDevicePanel() {
		// create and define layout for master_panel which contains all child panels
		Panel master_panel = new Panel();
		String separator = "----------------------------------------------------------------------------------------------------------------";
		
		master_panel.setSize(tab_panel_x, tab_panel_y);
		master_panel.setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		
		// create and define layout for all child panels
	//	Panel widget_panel = new Panel();
		Panel time_panel = new Panel();
		Panel motor_panel = new Panel();
		Panel mppt_panel = new Panel();
		Panel battery_panel = new Panel();
	
	//	widget_panel.setLayout(new GridLayout(1, 2)); // Need Implementing
		time_panel.setLayout(new GridLayout(1, 2));
		motor_panel.setLayout(new GridLayout(1, 3));
		mppt_panel.setLayout(new GridLayout(1, 2));
		battery_panel.setLayout(new GridLayout(1, 6));
		
		// Initializing components in time_panel
		Panel time_label_panel = new Panel();
		Panel time_data_panel = new Panel();
		
		time_label_panel.setLayout(new GridLayout(3, 1, 10, 10));
		time_data_panel.setLayout(new GridLayout(3, 1, 10, 10));
		
		// Adding labels to time_label_panel
		time_label_panel.add(new Label("Time"));
		time_label_panel.add(new Label("    Current time: "));
		time_label_panel.add(new Label("    Run time: "));
		time_panel.add(time_label_panel);
		
		// Set Up date variables
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Date date = new Date();

		// Adding data to time_data_panel
		time_data_panel.add(new Label(" "));
		time_data_panel.add(new Label(dateFormat.format(date)));
		time_data_panel.add(new Label("VALUE"));
		time_panel.add(time_data_panel);
		
		// Initializing components in motor_panel
		Panel motor_label_panel = new Panel();
		Panel motor_data_panel = new Panel();
		Panel motor_button_panel = new Panel();
		
		motor_label_panel.setLayout(new GridLayout(5, 1, 10, 10));
		motor_data_panel.setLayout(new GridLayout(5, 1, 10, 10));
		motor_button_panel.setLayout(new GridLayout(5, 1, 10, 10));
		
		// Adding labels to motor_label_panel
		motor_label_panel.add(new Label("Motor Controller"));
		motor_label_panel.add(new Label("    Speed: "));
		motor_label_panel.add(new Label("    Current: "));
		motor_label_panel.add(new Label("    Energy: "));
		motor_label_panel.add(new Label("    Av. Speed: "));
		motor_panel.add(motor_label_panel);
		
		// Adding data to motor_data_panel
		motor_data_panel.add(new Label(" "));
		motor_data_panel.add(new Label("VALUE"));
		motor_data_panel.add(new Label("VALUE"));
		motor_data_panel.add(new Label("VALUE"));
		motor_data_panel.add(new Label("VALUE"));
		motor_panel.add(motor_data_panel);
		
		// Adding buttons to motor_button_panel
		motor_button_panel.add(new Label(" "));
		motor_button_panel.add(new Label(" "));
		motor_button_panel.add(new Label(" "));
		motor_button_panel.add(new Button("Reset"));
		motor_button_panel.add(new Button("Reset"));
		motor_panel.add(motor_button_panel);
		
		// Initializing components in mppt_panel
		Panel mppt_label_panel = new Panel();
		Panel mppt_data_panel = new Panel();
		
		mppt_label_panel.setLayout(new GridLayout(2, 1, 10, 10));
		mppt_data_panel.setLayout(new GridLayout(2, 1, 10, 10));
		
		// Adding labels to mppt_label_panel
		mppt_label_panel.add(new Label("MPPTs"));
		mppt_label_panel.add(new Label("    #"));
		mppt_panel.add(mppt_label_panel);
		
		// Adding data to mppt_data_panel
		mppt_data_panel.add(new Label(" "));
		mppt_data_panel.add(new Label("Out Current"));
		mppt_panel.add(mppt_data_panel);
	
		// Unit Panel Initialization
		Panel batman_unit_panel = new Panel();
		batman_unit_panel.setLayout(new GridLayout(8, 1));
			
		// Initialize batman_unit_panel
		batman_unit_panel.add(new Label(""));
		batman_unit_panel.add(new Label("V"));
		batman_unit_panel.add(new Label("V (#)"));
		batman_unit_panel.add(new Label("V (#)"));
		batman_unit_panel.add(new Label("A"));
		batman_unit_panel.add(new Label("C"));
		batman_unit_panel.add(new Label("C"));
		batman_unit_panel.add(new Label("C"));
		
		// Batman Panel Initialization
		Panel batman_label_panel = new Panel();
		Panel batman_data_panel = new Panel();
		
		batman_label_panel.setLayout(new GridLayout(8, 1));
		batman_data_panel.setLayout(new GridLayout(8, 1));
		
		// Initialize batman_label_panel
		batman_label_panel.add(new Label("    Batman"));
		batman_label_panel.add(new Label("V_Average:"));
		batman_label_panel.add(new Label("V_Max:"));
		batman_label_panel.add(new Label("V_Min:"));
		batman_label_panel.add(new Label("Current:"));
		batman_label_panel.add(new Label("T_Average:"));
		batman_label_panel.add(new Label("T_Max:"));
		batman_label_panel.add(new Label("T_Min:"));
		battery_panel.add(batman_label_panel);
		
		// Initialize batman_data_panel
		batman_data_panel.add(new Label(""));
		batman_data_panel.add(new Label("0.00"));
		batman_data_panel.add(new Label("0.00"));
		batman_data_panel.add(new Label("0.00"));
		batman_data_panel.add(new Label("0.00"));
		batman_data_panel.add(new Label("0.00"));
		batman_data_panel.add(new Label("0.00"));
		batman_data_panel.add(new Label("0.00"));
		battery_panel.add(batman_data_panel);
		
		// Add Unit Panel to display units for Batman
		battery_panel.add(batman_unit_panel);
		
		// Robin Panel Initialization
		Panel robin_label_panel = new Panel();
		Panel robin_data_panel = new Panel();
		Panel robin_unit_panel = new Panel();
		
		robin_label_panel.setLayout(new GridLayout(8, 1));
		robin_data_panel.setLayout(new GridLayout(8, 1));
		robin_unit_panel.setLayout(new GridLayout(8, 1));
		
		// Initialize robin_label_panel
		robin_label_panel.add(new Label("    Robin"));
		robin_label_panel.add(new Label("V_Average:"));
		robin_label_panel.add(new Label("V_Max:"));
		robin_label_panel.add(new Label("V_Min:"));
		robin_label_panel.add(new Label("Current:"));
		robin_label_panel.add(new Label("T_Average:"));
		robin_label_panel.add(new Label("T_Max:"));
		robin_label_panel.add(new Label("T_Min:"));
		battery_panel.add(robin_label_panel);
		
		// Initialize robin_data_panel
		robin_data_panel.add(new Label(""));
		robin_data_panel.add(new Label("0.00"));
		robin_data_panel.add(new Label("0.00"));
		robin_data_panel.add(new Label("0.00"));
		robin_data_panel.add(new Label("0.00"));
		robin_data_panel.add(new Label("0.00"));
		robin_data_panel.add(new Label("0.00"));
		robin_data_panel.add(new Label("0.00"));
		battery_panel.add(robin_data_panel);
		
		// Initialize robin_unit_panel
		robin_unit_panel.add(new Label(""));
		robin_unit_panel.add(new Label("V"));
		robin_unit_panel.add(new Label("V (#)"));
		robin_unit_panel.add(new Label("V (#)"));
		robin_unit_panel.add(new Label("A"));
		robin_unit_panel.add(new Label("C"));
		robin_unit_panel.add(new Label("C"));
		robin_unit_panel.add(new Label("C"));
		
		// Add Unit Panel to display units for Robin
		battery_panel.add(robin_unit_panel);
		
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.gridx = 0;
		gbc.gridy = 0;
		master_panel.add(time_panel, gbc);
		
		gbc.gridx = 0;
		gbc.gridy = 1;
		master_panel.add(new Label(separator), gbc);
		
		gbc.gridx = 0;
		gbc.gridy = 2;
		master_panel.add(motor_panel, gbc);
		
		gbc.gridx = 0;
		gbc.gridy = 3;
		master_panel.add(new Label(separator), gbc);
	
		gbc.gridx = 0;
		gbc.gridy = 4;
		master_panel.add(mppt_panel, gbc);
		
		gbc.gridx = 0;
		gbc.gridy = 5;
		master_panel.add(new Label(separator), gbc);
		
		gbc.gridx = 0;
		gbc.gridy = 6;
		master_panel.add(battery_panel, gbc);
		
		return master_panel;
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
