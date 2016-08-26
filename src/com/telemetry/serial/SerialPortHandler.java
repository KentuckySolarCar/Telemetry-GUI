package com.telemetry.serial;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.Enumeration;

import com.telemetry.gui.TelemetryFrame;

import gnu.io.*;

public class SerialPortHandler {
	private SerialPort serial_port;
	private OutputStream output_stream;
	private InputStream input_stream;
	private String port_num = "";
	private SerialPortReader read_thread;
	private TelemetryFrame telem_frame;
	private boolean port_connected = false;
	
	public SerialPortHandler(TelemetryFrame telem_frame) {
		this.telem_frame = telem_frame;
	}
	
	public void changePortNum(String port_num) {
		this.port_num = port_num;
	}
	
	public String getPortNum() {
		return port_num;
	}
	
	public boolean getPortReadStatus() {
		if(port_connected) {
			return read_thread.getThreadStatus();
		}
		else
			return false;
	}
	
	public void connect(String port_num) throws Exception {
		CommPortIdentifier portIdentifier = CommPortIdentifier.getPortIdentifier(port_num);
        if ( portIdentifier.isCurrentlyOwned() )
        {
            System.out.println("Error: Port is currently in use");
        }
        else
        {
            CommPort commPort = portIdentifier.open(this.getClass().getName(),2000);
            
            if ( commPort instanceof SerialPort )
            {
                serial_port = (SerialPort) commPort;
                serial_port.setSerialPortParams(19200,SerialPort.DATABITS_8,SerialPort.STOPBITS_1,SerialPort.PARITY_NONE);
            	this.port_num = port_num;
                
                input_stream = serial_port.getInputStream();
                output_stream = serial_port.getOutputStream();
                port_connected = true;
            }
            else
            	System.out.println("Error: This is not a serial port!");
        }
	}
	
	public void stopSerialPort() {
		if(port_connected) {
			try {
				read_thread.stopThread();
				input_stream.close();
				output_stream.close();
				serial_port.close();
				port_connected = false;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		else
			telem_frame.updateStatus("No Serial Port Connected");
	}
	
	public void restartReadThread() throws Exception {
		stopSerialPort();
		if(port_num != "")
			connect(port_num);
		startReadThread();
	}
	
	public void startReadThread() {
		try {
			read_thread = new SerialPortReader(input_stream, telem_frame);
			read_thread.start();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void startLogging(String log_filename) throws IOException {
		read_thread.enableLogging(log_filename);
	}
}
