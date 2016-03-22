package com.telemetry.gui;

import javax.swing.*;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import com.telemetry.gui.device.DevicePanel;
import com.telemetry.serial.SerialPortHandler;
import com.telemetry.serial.TextFileInput;
import com.telemetry.actions.*;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.io.IOException;

public class TelemetryFrame extends JFrame {
	private static final long serialVersionUID = 3028986629905272450L;
	private static final int WIDTH = 1900; //1280
	private static final int HEIGHT = 1000; //720
	
	private JTabbedPane tab_panel;
	private static TextFileInput input;
	private static CalculationPanel calculation_panel;
	private static DevicePanel device_panel;
	private static GraphPanel graph_panel;
	private static JScrollPane log_scroll;
	private SerialPortHandler serial_port;
	private StartCalculation calculation_handler;
	private int tab_panel_x = 1880;	//1260
	private int tab_panel_y = 920;	//640
	
	// Constructor to initialize the GUI
	public TelemetryFrame() {
		super("University of Kentucky Solar Car Telemetry");
		
		// Initializes and edits the main window frame of GUI
		setSize(WIDTH, HEIGHT);
		setMinimumSize(new Dimension(WIDTH, HEIGHT));
//		setResizable(false);
		setLayout(new BorderLayout());
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		// Initializes and edits layout of tab_panel container
		tab_panel = new JTabbedPane();
	    calculation_panel = new CalculationPanel(tab_panel_x, tab_panel_y);
	    device_panel = new DevicePanel(tab_panel_x, tab_panel_y);
	    graph_panel = new GraphPanel(tab_panel_x, tab_panel_y);
	    
		tab_panel.add("Car Status", device_panel);
		tab_panel.add("Calculation", calculation_panel);
		tab_panel.add("Graphs", graph_panel);
	    tab_panel.add("Map", new JLabel("This is just a sad stub..."));
		
	    // Uncomment next line to set tab_panel's size
		tab_panel.setPreferredSize(new Dimension(tab_panel_x, tab_panel_y));
		
	    // Uncomment next line to make tab_panel scalable
		// new JScrollPane(tab_panel);
		
		// Position tab_panel and log_panel in main_frame with tab_panel WEST
		// and log_panel EAST 
		add(tab_panel, BorderLayout.CENTER);
		
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
		exit_menu_item.addActionListener(new ExitMenuListener());
		
		JMenuItem change_port = new JMenuItem("Change Port");		
		JMenuItem start_monitor = new JMenuItem("Start Monitor");	
		JMenuItem start_logging = new JMenuItem("Start Logging");	
		JMenuItem start_calculations = new JMenuItem("Start Calculations");
		
		start_monitor.addActionListener(new StartMonitorListener());
		start_calculations.addActionListener(new StartCalculationListener());
		
		JPopupMenu about_page = new JPopupMenu ("About");
		about_page.addAncestorListener (null);
		
		//add menu items file menu);
		file_menu.add(exit_menu_item);
		//add control items to control menu
		control_menu.add(change_port);
		control_menu.add(start_monitor);
		control_menu.add(start_logging);
		control_menu.add(start_calculations);
		
		//add about menu
		about_menu.add(about_page);

		//pop up menu
		
		//add menu to menu bar
		menu_bar.add(file_menu);
		menu_bar.add(control_menu);
		menu_bar.add(about_menu);
		
		//add menu bar to the frame
		setJMenuBar(menu_bar);
		setVisible(true);
	}
	class ExitMenuListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			System.exit(0);
		}
		
	}
	
	class StartCalculationListener implements ActionListener {
		public void actionPerformed(ActionEvent e){
			calculation_handler = new StartCalculation();
			calculation_handler.start();
		}
	}
	
	class StartMonitorListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
//			TextFileInput input = null;
//			try {
//				input = new TextFileInput();
//			} catch (FileNotFoundException e2) {
//				// TODO Auto-generated catch block
//				e2.printStackTrace();
//			}
//	
//			try {
//				input.Initiate();
//				validate();
//				repaint();
//			} catch (IOException | ParseException e1) {
//				// TODO Auto-generated catch block
//				e1.printStackTrace();
//			}
			serial_port = new SerialPortHandler();
			try {
				serial_port.connect("COM3");
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}
	
	public static void updateCalculationPanel() {
		calculation_panel.updatePanel(device_panel.getDeviceData());
	}
	
	// Constantly update GUI
	public static void updateDevicePanel(JSONObject obj, String type) {
		// Log Panel will not be updated for the time being
		// log_panel.updatePanel(obj);
		device_panel.updatePanel(obj, type);
		int[] time = device_panel.getTime();

		graph_panel.updateGraphs(calculation_panel.getData());
		
//		if(type.equals("motor")) {
//			double mph = Double.parseDouble((String) obj.get("S"));
//			graph_panel.updateSpeedDataSet(mph, time[0], time[1], time[2]);
//			
//			double mCurrent = Double.parseDouble((String) obj.get("S"));
//			graph_panel.updateMotorCurrentDataSet(mCurrent, time[0], time[1], time[2]);
//			
//			double aCurrent = Double.parseDouble((String) obj.get("S"));
//			graph_panel.updateArrayCurrentDataSet(aCurrent, time[0], time[1], time[2]);
//		}
//		else if((type.equals("bat_volt")) && ((String) obj.get("name")).equals("0")) {
//			double v = Double.parseDouble((String) obj.get("Vavg"));
//			graph_panel.updateBatteryVoltageDataSet(v, time[0], time[1], time[2]);
//		}
		
		// TODO: Implement Array and Motor current loops
		/*double mCurrent = Double.parseDouble((String) obj.get("S"));
		graph_panel.updateMotorCurrentDataSet(mCurrent, time[0], time[1], time[2]);
		
		double aCurrent = Double.parseDouble((String) obj.get("S"));
		graph_panel.updateArrayCurrentDataSet(aCurrent, time[0], time[1], time[2]);*/
	}
}
