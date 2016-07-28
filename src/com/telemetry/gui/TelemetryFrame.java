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
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TelemetryFrame extends JFrame {
	private static final long serialVersionUID = 3028986629905272450L;
	private static final int WIDTH = 1600; //1280
	private static final int HEIGHT = 900; //720
	
	private JTabbedPane tab_panel;
	private static TextFileInput text_input;
	private static CalculationPanel calculation_panel;
	private static DevicePanel device_panel;
	private static GraphPanel graph_panel;
	private static LogPanel log_panel;
	private JTextArea serial_bar;
	private SerialPortHandler serial_port;
	private AuxFrame aux_frame;
	private boolean aux_frame_on = false;
	private DateFormat date_format = new SimpleDateFormat("HH:mm:ss");
	
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
		
		// Reveals main_frame
		setVisible(true);
		setLocationRelativeTo(null);

		validate();
		repaint();
	}
	
	private void insertComponents() {
		tab_panel = new JTabbedPane();
	    device_panel = new DevicePanel();
	    graph_panel = new GraphPanel(calculation_panel);
	    log_panel = new LogPanel();
	    calculation_panel = new CalculationPanel(graph_panel);
	    
	    log = new JTextArea();
	    log_pane = new JScrollPane(log);
	    
	    JPanel combined_panel = new JPanel();
	    combined_panel.setLayout(new GridLayout(1,2));
	    combined_panel.add(device_panel);
	    combined_panel.add(calculation_panel);
	    
		tab_panel.add("Car Status", combined_panel);
//		tab_panel.add("Calculation", calculation_panel);
		tab_panel.add("Graphs", graph_panel);
	    tab_panel.add("Log", log_pane);
	    tab_panel.add("Map", new JLabel("This is just a sad stub..."));
		
		// Position tab_panel and log_panel in main_frame with tab_panel WEST
		// and log_panel EAST 
		add(tab_panel, BorderLayout.CENTER);
		
		//---------------------------------Temp-----------------------------//
//		JPanel serial_panel = new JPanel();
//		serial_panel.setLayout(new BorderLayout());

	    serial_bar = new JTextArea();
	    serial_bar.setFont(Tools.FIELD_FONT);
	    serial_bar.setEditable(false);
	    serial_bar.setBounds(serial_bar.getX(),
	    					 serial_bar.getY(),
	    					 WIDTH-10, 
	    					 serial_bar.getHeight());
	    add(serial_bar, BorderLayout.SOUTH);
		//---------------------------------Temp-----------------------------//
		
		createMenuBar();
	}
	
	private void createMenuBar(){
		final JMenuBar menu_bar = new JMenuBar();
		
		// create menus
		JMenu file_menu = new JMenu("File");
		JMenu control_menu = new JMenu("Control");
		JMenu tool_menu = new JMenu("Tool");
		JMenu aux_menu = new JMenu("Aux");
		JMenu about_menu = new JMenu("About");
		control_menu.setMnemonic(KeyEvent.VK_ALT);
		
		// create menu items
		JMenuItem exit_menu_item = new JMenuItem("Exit");
		JMenuItem change_port        = new JMenuItem("Change Port");		
		JMenuItem start_monitor      = new JMenuItem("Start Monitor");	
		JMenuItem stop_monitor       = new JMenuItem("Stop Monitor");
		JMenuItem restart_monitor    = new JMenuItem("Reconnect");
		JMenuItem start_calculations = new JMenuItem("Start Calculations");
		JMenuItem start_logging      = new JMenuItem("Start Logging");
		JMenuItem main_resolution = new JMenuItem("Main Resolution");
		JMenuItem test_monitor = new JMenuItem("Test Monitor");
		JMenuItem start_aux_frame = new JMenuItem("Start Aux Frame");
		JMenuItem aux_resolution = new JMenuItem("Aux Resolution");
		JMenuItem main_font = new JMenuItem("Change Main Font");
		
		JPopupMenu about_page = new JPopupMenu ("About");

		exit_menu_item.addActionListener(new ExitMenuListener());
		start_monitor.addActionListener(new StartMonitorListener());
		stop_monitor.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				serial_port.stopSerialPort();
				updateStatus("");
				
			}
		});
		change_port.addActionListener(new ChangePortListener());
		restart_monitor.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				try {
					serial_port.restartReadThread();
				} catch (NullPointerException e) {
					updateStatus("No serial port running");
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		main_resolution.addActionListener(new ChangeResolutionListener());
		test_monitor.addActionListener(new TestMonitorListener());
		start_logging.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				try {
					logFileSaver();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		start_aux_frame.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				startAuxFrame();
			}
		});
		aux_resolution.addActionListener(new AuxResolutionListener());
		main_font.addActionListener(new ChangeMainFontListener());
		about_page.addAncestorListener(null);
		
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
		control_menu.add(start_calculations);
		control_menu.add(start_logging);
		
		
		// add about menu
		about_menu.add(about_page);

		// add menu to menu bar
		menu_bar.add(file_menu);
		menu_bar.add(control_menu);
		menu_bar.add(tool_menu);
		menu_bar.add(aux_menu);
		menu_bar.add(about_menu);
		
		// add menu bar to the frame
		setJMenuBar(menu_bar);
		setVisible(true);
	}
	
	public void updateRunTime() {
		device_panel.updateRunTime();
		if(aux_frame_on)
			aux_frame.updateRunTime();
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
				else   
					serial_port.startReadThread();
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
//		String root_dir = "C:\\Users\\William\\Documents\\GitHub\\Telemetry-GUI";
			int[] current_time = device_panel.getSystemTime();
			String log_filename = root_dir + "\\" + current_time[0] + "_"
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
//		text_input = new TextFileInput("C:\\Users\\William\\Documents\\GitHub\\Telemetry-GUI\\0_0_14_log.txt", this);
		text_input.start();
	}
	
	public void startAuxFrame() {
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		GraphicsDevice[] screens = ge.getScreenDevices();
		GraphicsConfiguration target_screen_id = null;
		for(int i = 0; i < screens.length; i++) {
			if(screens[i].getDefaultConfiguration().getDevice() != this.getGraphicsConfiguration().getDevice());
				target_screen_id = screens[i].getDefaultConfiguration();
		}
		
		aux_frame = new AuxFrame(target_screen_id);
		aux_frame.setSize(1080, 1080);
		aux_frame.setLocationRelativeTo(null);
		aux_frame.setVisible(true);
		aux_frame_on = true;
	}
	
	public void changeMainResolution() {
		Object[] resolution_options = {"1920 x 1080", "1280 x 720"};
		String s = (String) JOptionPane.showInputDialog(
				this,
				"Please select your preferred resolution:",
				"Resolution Changer",
				JOptionPane.PLAIN_MESSAGE,
				null, resolution_options, resolution_options[0]);
		if(s == "1920 x 1080") {
			setSize(1920, 1080);
			validate();
		}
		else if(s == "1280 x 720") {
			setSize(1280, 720);
		}
		validate();
		repaint();
	}
	
	public void displayErrorDialog(String err_msg) {
		JOptionPane.showMessageDialog(this, err_msg, "Error", JOptionPane.ERROR_MESSAGE);
	}
	
	public void DisplayCurrentPortDialog(String port_num) {
		String msg = "Now listening to " + port_num;
		JOptionPane.showMessageDialog(this, msg, "Port Number", JOptionPane.PLAIN_MESSAGE);
	}
	
	public void updateAllPanels(JSONObject obj) {
		if(aux_frame_on) {
			aux_frame.updatePanel(obj);
			aux_frame.validate();
			aux_frame.repaint();
		}
		if((String) obj.get("message_id") == "messages")
			processMessages((String[]) obj.get("Messages"));
		else {
			Date date = new Date();
			device_panel.updatePanel(obj);
			calculation_panel.updatePanel(device_panel.getDeviceData());
			log.append("\n" + obj.toString() + " " + date_format.format(date) + "\n");
			serial_bar.setText(obj.toString());
			// GraphPanel is updated with calculation panel, for concurrency issues
		}
	}
	
	public void processMessages(String[] messages) {
		if(aux_frame_on)
			aux_frame.processMessages(messages);
	}
	
	public void updateStatus(String text) {
		serial_bar.removeAll();
		serial_bar.setText(text);
	}
	
	public void processInvalidData(String json_str) {
		Date date = new Date();
		log.append("\n-----------------------------------------------------\n" 
					+ "*INVALID* " + json_str + " " + date_format.format(date)
					+ "\n-----------------------------------------------------\n");
	}
	
	public void changeFontHelper(Font font) {
		Tools.changeFrameFonts(this, font);
	}
}