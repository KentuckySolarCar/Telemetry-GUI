package com.telemetry.gui;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import com.telemetry.gui.device.DevicePanel;
import com.telemetry.serial.SerialPortHandler;
import com.telemetry.serial.TextFileInput;
import com.sun.glass.events.KeyEvent;
import com.telemetry.custom.Tools;
import com.telemetry.data.CarData;
import com.telemetry.data.DataContainer;
import com.telemetry.graphs.GraphPanel;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TelemetryFrame extends JFrame {
	private static final long serialVersionUID = 3028986629905272450L;
	private static final int WIDTH = 1600; //1280
	private static final int HEIGHT = 900; //720
	
	private JTabbedPane tab_panel;
	private TextFileInput text_input;
	private CalculationPanel calculation_panel;
	private DevicePanel device_panel;
	private GraphPanel graph_panel;
	private LogPanel log_panel;
	private JTextArea status_bar;
	private SerialPortHandler serial_port;
	private AuxFrame aux_frame;
	private boolean aux_frame_on = false;
	private DateFormat date_format = new SimpleDateFormat("HH:mm:ss");
	private DataContainer all_data;
	
	// Temp
	JScrollPane log_pane;
	JTextArea log;
	
	// Constructor to initialize the GUI
	public TelemetryFrame() {
		super("University of Kentucky Solar Car Telemetry");
		
		// Initializes and edits the main window frame of GUI
		setSize(WIDTH, HEIGHT);
		setMinimumSize(new Dimension(WIDTH, HEIGHT));
		setLayout(new BorderLayout());
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		insertComponents();
		
		serial_port = new SerialPortHandler(this);
		all_data = new DataContainer();
		
		// Reveals main_frame
		setVisible(true);
		setLocationRelativeTo(null);

		validate();
		repaint();
	}
	
	private void insertComponents() {
		tab_panel = new JTabbedPane();
	    device_panel = new DevicePanel();
	    graph_panel = new GraphPanel();
	    log_panel = new LogPanel();
	    calculation_panel = new CalculationPanel();
	    
	    log = new JTextArea();
	    log_pane = new JScrollPane(log);
	    
	    JPanel combined_panel = new JPanel();
	    combined_panel.setLayout(new GridLayout(1,2));
	    combined_panel.add(device_panel);
	    combined_panel.add(calculation_panel);
	    
		tab_panel.add("Car Status", combined_panel);
		tab_panel.add("Graphs", graph_panel);
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
		
		createMenuBar();
	}
	
	private void createMenuBar(){
		final JMenuBar menu_bar = new JMenuBar();
		
		// create menus
		JMenu file_menu = new JMenu("File");
		JMenu control_menu = new JMenu("Control");
		JMenu tool_menu = new JMenu("Tool");
		JMenu aux_menu = new JMenu("Aux");
		
		// Set shortcut for control menu
		control_menu.setMnemonic(KeyEvent.VK_ALT);
		
		// create menu items
		JMenuItem exit_menu_item = new JMenuItem("Exit");
		JMenuItem change_port        = new JMenuItem("Change Port");		
		JMenuItem start_monitor      = new JMenuItem("Start Monitor");	
		JMenuItem stop_monitor       = new JMenuItem("Stop Monitor");
		JMenuItem restart_monitor    = new JMenuItem("Reconnect");
		JMenuItem main_resolution = new JMenuItem("Main Resolution");
		JMenuItem test_monitor = new JMenuItem("Test Monitor");
		JMenuItem start_aux_frame = new JMenuItem("Start Aux Frame");
		JMenuItem aux_resolution = new JMenuItem("Aux Resolution");
		JMenuItem main_font = new JMenuItem("Change Main Font");

		// Add menu button listeners
		exit_menu_item.addActionListener(new ExitMenuListener());
		start_monitor.addActionListener(new StartMonitorListener());
		stop_monitor.addActionListener(new StopMonitorListener());
		change_port.addActionListener(new ChangePortListener());
		restart_monitor.addActionListener(new RestartMonitorListener());
		main_resolution.addActionListener(new ChangeResolutionListener());
		test_monitor.addActionListener(new TestMonitorListener());
		start_aux_frame.addActionListener(new StartAuxFrameListener());
		aux_resolution.addActionListener(new AuxResolutionListener());
		main_font.addActionListener(new ChangeMainFontListener());
		
		// Tool Menu Items
		tool_menu.add(main_resolution);
		tool_menu.add(main_font);
		tool_menu.add(test_monitor);
		
		// Aux Menu Items
		aux_menu.add(start_aux_frame);
		aux_menu.add(aux_resolution);
		
		// add menu items file menu);
		file_menu.add(exit_menu_item);

		// add control items to control menu
		control_menu.add(change_port);
		control_menu.add(start_monitor);
		control_menu.add(restart_monitor);
		control_menu.add(stop_monitor);

		// add menu to menu bar
		menu_bar.add(file_menu);
		menu_bar.add(control_menu);
		menu_bar.add(tool_menu);
		menu_bar.add(aux_menu);
		
		// add menu bar to the frame
		setJMenuBar(menu_bar);
		setVisible(true);
	}
	
	public void updateRunTime() {
		device_panel.updateRunTime();
		if(aux_frame_on)
			aux_frame.updateRunTime();
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
				DisplayCurrentPortDialog(s);
			} catch (Exception e) {
				displayErrorDialog("Cannot Connect to " + s);
			}
		}
	}
	
	class AuxResolutionListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent arg0) {
			try {
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
				JFileChooser chooser = new JFileChooser();
				FileNameExtensionFilter filter = new FileNameExtensionFilter("Text Files", "txt");
				chooser.setFileFilter(filter);
				int returnVal = chooser.showOpenDialog(TelemetryFrame.this);
				if(returnVal == JFileChooser.APPROVE_OPTION) {
					text_input = new TextFileInput(chooser.getSelectedFile().toString(), TelemetryFrame.this);
					text_input.start();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	class ExitMenuListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			try {
				if(serial_port.getPortReadStatus())
					serial_port.stopSerialPort();
				System.exit(0);
			} catch(Exception e1) {
				e1.printStackTrace();
			}
		}
	}
	
	class StartMonitorListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			try {
				if(serial_port.getPortNum() == "")
					displayErrorDialog("Port is Empty!");
				else {
					serial_port.startReadThread();
					JFileChooser chooser = new JFileChooser();
					chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
					int returnVal = chooser.showOpenDialog(TelemetryFrame.this);
					if(returnVal == JFileChooser.APPROVE_OPTION) {
						String root_dir = chooser.getSelectedFile().toString();
						int[] current_time = device_panel.getSystemTime();
						String log_filename = root_dir + File.separator + current_time[0] + "_"
													   + current_time[1] + "_" + current_time[2] 
													   + "_log.txt";
						serial_port.startLogging(log_filename);
					}
				}
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
	}
	
	class ChangeResolutionListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent arg0) {
			changeMainResolution();
		}
	}
	
	class ChangeMainFontListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent arg0) {
			String s = JOptionPane.showInputDialog("Please enter the desired font", null);
			changeFontHelper(Tools.FIELD_FONT);
		}
		
	}
	
	public void logFileSaver() throws IOException {
		JFileChooser chooser = new JFileChooser();
		chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		int returnVal = chooser.showOpenDialog(this);
		if(returnVal == JFileChooser.APPROVE_OPTION) {
			String root_dir = chooser.getSelectedFile().toString();
			int[] current_time = device_panel.getSystemTime();
			String log_filename = root_dir + File.separator + current_time[0] + "_"
										   + current_time[1] + "_" + current_time[2] 
										   + "_log.txt";
			serial_port.startLogging(log_filename);
		}
	}
	
	public void testMonitor() throws IOException, ParseException {
		JFileChooser chooser = new JFileChooser();
		FileNameExtensionFilter filter = new FileNameExtensionFilter("Text Files", "txt");
		chooser.setFileFilter(filter);
		int returnVal = chooser.showOpenDialog(this);
		if(returnVal == JFileChooser.APPROVE_OPTION) {
			text_input = new TextFileInput(chooser.getSelectedFile().toString(), this);
			text_input.start();
		}
	}
	
	public void displayErrorDialog(String err_msg) {
		JOptionPane.showMessageDialog(this, err_msg, "Error", JOptionPane.ERROR_MESSAGE);
	}
	
	public void DisplayCurrentPortDialog(String port_num) {
		String msg = "Now listening to " + port_num;
		JOptionPane.showMessageDialog(this, msg, "Port Number", JOptionPane.PLAIN_MESSAGE);
	}
	
	public void updateAllPanels(JSONObject obj) {
		Date date = new Date();
		all_data.updateCarData(obj);
		device_panel.updatePanel(all_data.getCarData());
		calculation_panel.updatePanel(all_data.getCarData(), 0);
		graph_panel.updatePanel(all_data.getCarData());
		obj.put("actual_time", date_format.format(date));
		log.append("\n" + obj.toString() + "\n");
		status_bar.setText(obj.toString());
		if(aux_frame_on)
			aux_frame.updatePanel(all_data.getCarData());
	}
	
	public void updateStatus(String text) {
		status_bar.removeAll();
		status_bar.setText(text);
	}
	
	public void processInvalidData(String json_str) {
		Date date = new Date();
		log.append("\n-----------------------------------------------------\n" 
					+ "*INVALID* " + json_str + " " + date_format.format(date)
					+ "\n-----------------------------------------------------\n");
	}
}