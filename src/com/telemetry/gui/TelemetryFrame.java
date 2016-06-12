package com.telemetry.gui;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import com.telemetry.gui.device.DevicePanel;
import com.telemetry.serial.SerialPortHandler;
import com.telemetry.serial.TextFileInput;
import com.telemetry.actions.*;
import com.telemetry.graphs.GraphPanel;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class TelemetryFrame extends JFrame {
	private static final long serialVersionUID = 3028986629905272450L;
	private static final int WIDTH = 1280; //1280
	private static final int HEIGHT = 720; //720
	
	private JTabbedPane tab_panel;
	private static TextFileInput text_input;
	private static CalculationPanel calculation_panel;
	private static DevicePanel device_panel;
	private static GraphPanel graph_panel;
	private static LogPanel log_panel;
	private SerialPortHandler serial_port;
	private StartCalculation calculation_handler;
	private int tab_panel_x = 1920;	//1260
	private int tab_panel_y = 1080;	//640
	
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
	    calculation_panel = new CalculationPanel(graph_panel);
	    device_panel = new DevicePanel(tab_panel_x / 2, tab_panel_y);
	    graph_panel = new GraphPanel(calculation_panel);
	    log_panel = new LogPanel(tab_panel_x, tab_panel_y);
	    
	    JPanel combined_panel = new JPanel();
	    combined_panel.setLayout(new BorderLayout());
	    combined_panel.add(device_panel, BorderLayout.WEST);
	    combined_panel.add(new JSeparator(SwingConstants.VERTICAL),	BorderLayout.CENTER);
	    combined_panel.add(calculation_panel, BorderLayout.EAST);
	    
		tab_panel.add("Car Status", device_panel);
		tab_panel.add("Calculation", calculation_panel);
		tab_panel.add("Graphs", graph_panel);
	    tab_panel.add("Log", log_panel);
	    tab_panel.add("Map", new JLabel("This is just a sad stub..."));
		
		tab_panel.setSize(new Dimension(tab_panel_x, tab_panel_y));
		
	    // Uncomment next line to make tab_panel scalable
		// new JScrollPane(tab_panel);
		
		// Position tab_panel and log_panel in main_frame with tab_panel WEST
		// and log_panel EAST 
		add(tab_panel, BorderLayout.CENTER);
		
		createMenuBar();
		
		serial_port = new SerialPortHandler();
		
		// Reveals main_frame
		setVisible(true);
		setLocationRelativeTo(null);

		validate();
		repaint();
	}
	
	private void createMenuBar(){
		final JMenuBar menu_bar = new JMenuBar();
		
		// create menus
		JMenu file_menu = new JMenu("File");
		JMenu control_menu = new JMenu("Control");
		JMenu tool_menu = new JMenu("Tool");
		final JMenu about_menu = new JMenu("About");
		
		// create menu items
		JMenuItem exit_menu_item = new JMenuItem("Exit");
		exit_menu_item.addActionListener(new ExitMenuListener());
		
		JMenuItem change_port = new JMenuItem("Change Port");		
		JMenuItem start_monitor = new JMenuItem("Start Monitor");	
		JMenuItem start_calculations = new JMenuItem("Start Calculations");
		JMenuItem start_logging = new JMenuItem("Start Logging");
		JMenuItem stop_logging = new JMenuItem("Stop Logging");
		
		JMenuItem change_resolution = new JMenuItem("Change Resolution");
		JMenuItem test_monitor = new JMenuItem("Test Monitor");
		
		start_monitor.addActionListener(new StartMonitorListener());
		start_calculations.addActionListener(new StartCalculationListener());
		change_port.addActionListener(new ChangePortListener());
		change_resolution.addActionListener(new ChangeResolutionListener());
		test_monitor.addActionListener(new TestMonitorListener());
		
		JPopupMenu about_page = new JPopupMenu ("About");
		about_page.addAncestorListener (null);
		
		// Tool Menu Items
		tool_menu.add(change_resolution);
		tool_menu.add(test_monitor);
		
		//add menu items file menu);
		file_menu.add(exit_menu_item);
		//add control items to control menu
		control_menu.add(change_port);
		control_menu.add(start_monitor);
		control_menu.add(start_calculations);
		
		//add about menu
		about_menu.add(about_page);

		//pop up menu
		
		//add menu to menu bar
		menu_bar.add(file_menu);
		menu_bar.add(control_menu);
		menu_bar.add(tool_menu);
		menu_bar.add(about_menu);
		
		//add menu bar to the frame
		setJMenuBar(menu_bar);
		setVisible(true);
	}
	
	public void updateRunTime() {
		device_panel.updateRunTime();
	}
	
	class StartLoggingListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent arg0) {
			String s = JOptionPane.showInputDialog("Please enter the port number", null); 
			
		}
		
	}
	
	class ChangePortListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent arg0) {
			String s = JOptionPane.showInputDialog("Please enter the port number", null); 
			try {
				serial_port.connect(s);
				DisplayCurrentPortDialog(s);
			} catch (Exception e) {
				DisplayPortErrorDialog("Cannot Connect to " + s);
			}
		}
	}
	
	class TestMonitorListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent arg0) {
			try {
				testMonitor();
			} catch (IOException | ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	class ExitMenuListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			System.exit(0);
		}
		
	}
	
	class StartCalculationListener implements ActionListener {
		public void actionPerformed(ActionEvent e){
			if(serial_port.getPortReadStatus() != true) {
				DisplayPortErrorDialog("Not listening to any ports yet");
			}
			else {
				calculation_handler = new StartCalculation();
				calculation_handler.start();
			}
		}
	}
	
	class StartMonitorListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			try {
				if(serial_port.getPortNum() == "")
					DisplayPortErrorDialog("Port is Empty!");
				else   
					serial_port.startReadThread();
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}
	
	class ChangeResolutionListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent arg0) {
			changeResolution();
		}
		
	}
	
	public void testMonitor() throws IOException, ParseException {
		JFileChooser chooser = new JFileChooser();
		FileNameExtensionFilter filter = new FileNameExtensionFilter("Text Files", "txt");
		chooser.setFileFilter(filter);
		int returnVal = chooser.showOpenDialog(this);
		if(returnVal == JFileChooser.APPROVE_OPTION) {
			text_input = new TextFileInput(chooser.getSelectedFile().toString());
			text_input.start();
		}
	}
	
	public void changeResolution() {
		Object[] resolution_options = {"1920 x 1080", "1280 x 720"};
		String s = (String) JOptionPane.showInputDialog(
				this,
				"Please select your preferred resolution:",
				"Resolution Changer",
				JOptionPane.PLAIN_MESSAGE,
				null, resolution_options, resolution_options[0]);
		if(s == "1920 x 1080")
			setSize(1920, 1080);
		else if(s == "1280 x 720")
			setSize(1280, 720);
		validate();
		repaint();
	}
	
	public void DisplayPortErrorDialog(String err_msg) {
		JOptionPane.showMessageDialog(this, err_msg, "Port Error", JOptionPane.ERROR_MESSAGE);
	}
	
	public void DisplayCurrentPortDialog(String port_num) {
		String msg = "Now listening to " + port_num;
		JOptionPane.showMessageDialog(this, msg, "Port Number", JOptionPane.PLAIN_MESSAGE);
	}
	
	public static void updateCalculationPanel() {
		calculation_panel.updatePanel(device_panel.getDeviceData());
	}
	
	@SuppressWarnings("unchecked")
	public static void updateGraphPanel(JSONObject obj, String type) {
		JSONObject combined_data = new JSONObject();
		JSONObject device_data = device_panel.getDeviceData();
		JSONObject calculation_data = calculation_panel.getData();
		combined_data.put("device_data", device_data);
		combined_data.put("calculation_data", calculation_data);
		graph_panel.updateGraphs(combined_data);
	}
	
	// Constantly update GUI
	public static void updateDevicePanel(JSONObject obj, String type) {
		// Log Panel will not be updated for the time being
		// log_panel.updatePanel(obj);
		device_panel.updatePanel(obj, type);

		
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