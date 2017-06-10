package com.telemetry.gui;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.json.simple.JSONObject;

import com.telemetry.gui.auxiliary.AuxFrame;
import com.telemetry.gui.calculation.CalculationPanel;
import com.telemetry.gui.car_graph.GraphPanel;
import com.telemetry.gui.device.DevicePanel;
import com.telemetry.gui.misc.LogPanel;
import com.telemetry.gui.weather.WeatherPanel;
import com.telemetry.serial.SerialPortHandler;
import com.telemetry.serial.TextFileInput;
import com.telemetry.util.LogWriter;
import com.telemetry.util.Tools;
import com.sun.glass.events.KeyEvent;
import com.telemetry.data.DataContainer;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TelemetryFrame extends JFrame {
	private static final long serialVersionUID = 3028986629905272450L;
	private static final int WIDTH = 1600; //1280
	private static final int HEIGHT = 900; //720
	
	// GUI Stuff
	private JTabbedPane tab_panel;
	private TextFileInput text_input;
	private CalculationPanel calculation_panel;
	private DevicePanel device_panel;
	private GraphPanel graph_panel;
	private LogPanel log_panel;
	private JTextArea status_bar;
	private AuxFrame aux_frame;
	private WeatherPanel weather_panel;
	private boolean weather_frame_on = false;
	private boolean aux_frame_on = false;
	
	// Non-GUI Stuff
	private SerialPortHandler serial_port;
	private DateFormat date_format = new SimpleDateFormat("HH:mm:ss");
	private DataContainer all_data;
	private LogWriter logger;
	private JFileChooser chooser;
	
	// Constructor to initialize the GUI
	public TelemetryFrame() {
		super("University of Kentucky Solar Car Telemetry");
		// Initializes and edits the main window frame of GUI
		setSize(WIDTH, HEIGHT);
		setLocationRelativeTo(null);
		setMinimumSize(new Dimension(WIDTH, HEIGHT));
		setLayout(new BorderLayout());
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		insertComponents();
		
		serial_port = new SerialPortHandler(this);
		all_data = new DataContainer();
		logger = new LogWriter(this);
		chooser = new JFileChooser();

		chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
	    chooser.setAcceptAllFileFilterUsed(false);

		int returnVal = chooser.showOpenDialog(this);
		if(returnVal == JFileChooser.APPROVE_OPTION) {
			String dest_dir = chooser.getSelectedFile().toString();
			logger.initializeWriter(dest_dir);
		}
		
		chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
		chooser.setAcceptAllFileFilterUsed(true);
		
		// Reveals main_frame
		setVisible(true);
	}
	
	private void insertComponents() {
		// Add the menu bar first
		createMenuBar();

		tab_panel = new JTabbedPane();
	    device_panel = new DevicePanel();
	    graph_panel = new GraphPanel();
	    log_panel = new LogPanel();
	    calculation_panel = new CalculationPanel();
	    weather_panel = new WeatherPanel();
	    
	    // Combining device and calculation panel into one
	    JPanel combined_panel = new JPanel();
	    combined_panel.setLayout(new GridLayout(1,2));
	    combined_panel.add(device_panel);
	    combined_panel.add(calculation_panel);
	    
		tab_panel.add("Car Status", combined_panel);
		tab_panel.add("Graphs", graph_panel);
		tab_panel.add("Weather", weather_panel);
	    tab_panel.add("Log", log_panel);
	    tab_panel.add("Map", new JLabel("This is just a sad stub..."));
		
		add(tab_panel, BorderLayout.CENTER);

		// Add status bar at the bottom
	    status_bar = new JTextArea();
	    status_bar.setFont(Tools.FIELD_FONT);
	    status_bar.setEditable(false);
	    status_bar.setBounds(status_bar.getX(),
	    					 status_bar.getY(),
	    					 WIDTH-10, 
	    					 status_bar.getHeight());
	    add(status_bar, BorderLayout.SOUTH);
	}
	
	private void createMenuBar(){
		final JMenuBar menu_bar = new JMenuBar();
		
		// create menus
		JMenu file_menu = new JMenu("File");
		JMenu control_menu = new JMenu("Control");
		JMenu tool_menu = new JMenu("Tool");
		JMenu aux_menu = new JMenu("Aux");
		JMenu weather_menu = new JMenu("Weather");
		
		// Set shortcut for control menu
		// control_menu.setMnemonic(KeyEvent.VK_ALT);
		
		// create menu items
		JMenuItem exit               = new JMenuItem("Exit");
		JMenuItem change_port        = new JMenuItem("Change Port");		
		JMenuItem start_monitor      = new JMenuItem("Start Monitor");	
		JMenuItem stop_monitor       = new JMenuItem("Stop Monitor");
		JMenuItem restart_monitor    = new JMenuItem("Reconnect");
		JMenuItem main_resolution    = new JMenuItem("Main Resolution");
		JMenuItem test_monitor       = new JMenuItem("Test Monitor");
		JMenuItem start_aux_frame    = new JMenuItem("Start Aux Frame");
		JMenuItem start_weather_frame    = new JMenuItem("Start Weather Frame");
		JMenuItem aux_resolution     = new JMenuItem("Aux Resolution");
		JMenuItem main_font          = new JMenuItem("Change Main Font");
		JMenuItem update_weather     = new JMenuItem("Update Weather");

		// Add menu button listeners
		exit.addActionListener(new ExitMenuListener());
		start_monitor.addActionListener(new StartMonitorListener());
		stop_monitor.addActionListener(new StopMonitorListener());
		change_port.addActionListener(new ChangePortListener());
		restart_monitor.addActionListener(new RestartMonitorListener());
		main_resolution.addActionListener(new ChangeResolutionListener());
		test_monitor.addActionListener(new TestMonitorListener());
		start_aux_frame.addActionListener(new StartAuxFrameListener());
//		start_weather_frame.addActionListener(new StartWeatherFrameListener());
		aux_resolution.addActionListener(new AuxResolutionListener());
		main_font.addActionListener(new ChangeMainFontListener());
		update_weather.addActionListener(new UpdateWeatherListener());
		
		// Tool Menu Items
		tool_menu.add(main_resolution);
		tool_menu.add(main_font);
		tool_menu.add(test_monitor);
		
		// Aux Menu Items
		aux_menu.add(start_aux_frame);
		aux_menu.add(aux_resolution);
		aux_menu.add(start_weather_frame);
		
		// add menu items file menu
		file_menu.add(exit);

		// add control items to control menu
		control_menu.add(change_port);
		control_menu.add(start_monitor);
		control_menu.add(restart_monitor);
		control_menu.add(stop_monitor);
		
		// add weather items to weather menu
		weather_menu.add(update_weather);

		// add menu to menu bar
		menu_bar.add(file_menu);
		menu_bar.add(control_menu);
		menu_bar.add(tool_menu);
		menu_bar.add(aux_menu);
		menu_bar.add(weather_menu);
		
		// add menu bar to the frame
		setJMenuBar(menu_bar);
		setVisible(true);
	}
	
	/**
	 * Called by a ticking thread to update device panel's system and elapsed time, and update
	 * aux frame's system time if the aux frame is currently displayed
	 */
	public void updateRunTime() {
		device_panel.updateRunTime();
		if(aux_frame_on)
			aux_frame.updateRunTime();
	}
	
	// Let the user change which folder to store logs in, after the GUI is started
	class reinitializeLogger implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			// chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			int returnVal = chooser.showOpenDialog(TelemetryFrame.this);
			if(returnVal == JFileChooser.APPROVE_OPTION) {
				try {
					logger.closeWriter();
					logger.initializeWriter(chooser.getSelectedFile().toString());
				} catch (IOException e1) {
					updateStatus("Failed to re-initialize logger... Try again?");
				}
			}
		}
	}
	
	class StartAuxFrameListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent arg0) {
			aux_frame = new AuxFrame();
			aux_frame.setSize(1080, 1080);
			aux_frame.setLocationRelativeTo(null);
			aux_frame.setVisible(true);
			aux_frame_on = true;
		}
	}
	
//	class StartWeatherFrameListener implements ActionListener {
//		@Override
//		public void actionPerformed(ActionEvent arg0) {
//			weather_frame = new WeatherFrame();
//			weather_frame.setSize(1080, 1080);
//			weather_frame.setVisible(true);
//			weather_frame_on = true;
//		}
//	}
	
	class RestartMonitorListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent arg0) {
			if(serial_port.getPortReadStatus())
				try {
					serial_port.restartReadThread();
				} catch (Exception e) {
					e.printStackTrace();
				}
			else
				updateStatus("No serial port running");
		}
	}
	
	class StopMonitorListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			serial_port.stopSerialPort();
			updateStatus("Serial Port Stopped");
		}
	}
	
	class ChangePortListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent arg0) {
			String s = JOptionPane.showInputDialog("Please enter the port number", null); 
			try {
				serial_port.connect(s);
				// Let user know which port they are connected to
				DisplayCurrentPortDialog(s);
			} catch (Exception e) {
				displayErrorDialog(e.getMessage());
			}
		}
	}
	
	class AuxResolutionListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent arg0) {
			try {
				// Checks to see if aux_frame is initialized. If not, exception will be caught
				aux_frame.getClass();
				String s = JOptionPane.showInputDialog("Please enter the resolution\nSeparated by a comma (W/H)", null);
				String resolution[] = s.split(",");
				aux_frame.setSize(Tools.stringToInt(resolution[0]), Tools.stringToInt(resolution[1]));
				updateStatus("Aux Frame Resolution Changed");
			} catch(NullPointerException e) {
				updateStatus("Aux Frame not initialized");
			}
		}
	}
	
	class TestMonitorListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent arg0) {
			try {
				FileNameExtensionFilter filter = new FileNameExtensionFilter("Text Files", "txt");
				chooser.addChoosableFileFilter(filter);
				int returnVal = chooser.showOpenDialog(TelemetryFrame.this);
				if(returnVal == JFileChooser.APPROVE_OPTION) {
					text_input = new TextFileInput(chooser.getSelectedFile().toString(), TelemetryFrame.this);
					text_input.start();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Safely exits the GUI program
	 * @author Weilian Song
	 *
	 */
	class ExitMenuListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			try {
				// Stops serial port if it is currently listening
				serial_port.stopSerialPort();
				logger.closeWriter();
				System.exit(0);
			} catch(Exception e1) {
				displayErrorDialog("Cannot exit, try again");
				e1.printStackTrace();
			}
		}
	}
	
	class StartMonitorListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			try {
				serial_port.startReadThread();
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
	}
	
	class ChangeResolutionListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent arg0) {
			// Placeholder for when needing to change resolution
		}
	}
	
	class ChangeMainFontListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent arg0) {
			// Placeholder for when needing to change font
		}
		
	}
	
	class UpdateWeatherListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			all_data.updateWeatherData();
			weather_panel.updateCurrentlyPanel(all_data.getWeatherData().getLatestCurrentData());
			weather_panel.updateMinutelyPanel(all_data.getWeatherData().getMinutelyData());
			weather_panel.updateHourlyPanel(all_data.getWeatherData().getHourlyData());
			
//			//update weather panel if it is currently open
//			if(weather_frame_on){
//				all_data.updateWeatherData();
//				weather_frame.updateCurrentlyPanel(all_data.getWeatherData().getLatestCurrentData());
//				weather_frame.updateMinutelyPanel(all_data.getWeatherData().getMinutelyData());
//				weather_frame.updateHourlyPanel(all_data.getWeatherData().getHourlyData());
//			}
		}
	}
	
	public void displayErrorDialog(String err_msg) {
		JOptionPane.showMessageDialog(this, err_msg, "Error", JOptionPane.ERROR_MESSAGE);
	}
	
	public void DisplayCurrentPortDialog(String port_num) {
		String msg = "Now listening to " + port_num;
		JOptionPane.showMessageDialog(this, msg, "Port Number", JOptionPane.PLAIN_MESSAGE);
	}
	
	public void updateAllPanels(JSONObject obj) throws IOException {
		// Update data container first
		all_data.updateCarData(obj);
		
		// Write msg to log
		if(logger.getState() == true) {
			logger.writeJSON(obj.toString());
		}
		
		// Then update all panels
		device_panel.updatePanel(all_data.getCarData());
		calculation_panel.updatePanel(all_data.getCarData());
		graph_panel.updatePanel(all_data.getCarData());
		log_panel.updatePanel(obj.toString());
		status_bar.setText(obj.toString());
		if(aux_frame_on)
			aux_frame.updatePanel(all_data.getCarData());
	}
	
	/**
	 * Sets status bar's message
	 * @param text
	 */
	public synchronized void updateStatus(String text) {
		status_bar.removeAll();
		status_bar.setText(text);
	}
	
	public void processInvalidData(String json_str) {
		Date date = new Date();
//		log_panel.append("\n-----------------------------------------------------\n" 
//					+ "*INVALID* " + json_str + " " + date_format.format(date)
//					+ "\n-----------------------------------------------------\n");
	}
	
	public LogWriter getLogger() {
		return logger;
	}
}