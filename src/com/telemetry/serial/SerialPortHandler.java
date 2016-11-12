package com.telemetry.serial;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;

import com.telemetry.gui.TelemetryFrame;

import com.fazecast.jSerialComm.*;

/**
 * Class that handles the connection of the specified serial port and controls both
 * the read and write port
 * 
 * TODO Use a cross-platform serial reader
 * @author Weilian Song
 *
 */
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

	/**
	 * Get the current port name
	 * @return the current port
	 */
	public String getPortNum() {
		return port_num;
	}

	/**
	 * Get the status of the read thread
	 * @return whether the read thread is active or not
	 */
	public boolean getPortReadStatus() {
		if(port_connected) {
			return read_thread.getThreadStatus();
		}
		else
			return false;
	}

	//	/**
	//	 * Connect to the specified port. If successful, create both a read and write stream from it
	//	 * @param port_num
	//	 * @throws Exception Thrown if any part of connection has failed
	//	 */
	//	public void connect(String port_num) throws Exception {
	//		CommPortIdentifier portIdentifier = CommPortIdentifier.getPortIdentifier(port_num);
	//        if ( portIdentifier.isCurrentlyOwned() )
	//        {
	//            System.out.println("Error: Port is currently in use");
	//        }
	//        else
	//        {
	//            CommPort commPort = portIdentifier.open(this.getClass().getName(),2000);
	//            
	//            if ( commPort instanceof SerialPort )
	//            {
	//                serial_port = (SerialPort) commPort;
	//                serial_port.setSerialPortParams(19200,SerialPort.DATABITS_8,SerialPort.STOPBITS_1,SerialPort.PARITY_NONE);
	//            	this.port_num = port_num;
	//                
	//                input_stream = serial_port.getInputStream();
	//                output_stream = serial_port.getOutputStream();
	//                port_connected = true;
	//            }
	//            else
	//            	System.out.println("Error: This is not a serial port!");
	//        }
	//	}

	public void connect(String port_num) throws Exception {
		serial_port = SerialPort.getCommPort(port_num);
		if(serial_port.isOpen())
			throw new Exception("Port " + port_num + " is currently in use!");

		serial_port.setComPortParameters(19200, 8, SerialPort.ONE_STOP_BIT, SerialPort.NO_PARITY);
		if(serial_port.openPort()) {
			input_stream = serial_port.getInputStream();
			output_stream = serial_port.getOutputStream();
			this.port_num = port_num;
			port_connected = true;
		}
		else
			throw new Exception("Error in openeing port " + port_num + "!");
	}

	/**
	 * Stops all connections to the serial port if one is connected
	 */
	public void stopSerialPort() {
		if(port_connected) {
			try {
				read_thread.stopThread();
				read_thread.join();
				input_stream.close();
				output_stream.close();
				//				serial_port.close();
				serial_port.closePort();
				port_connected = false;
			} catch (IOException | InterruptedException e) {
				telem_frame.updateStatus("Failed to stop serial port. Try again?");
			}
		}
		else
			telem_frame.updateStatus("No Serial Port Connected");
	}

	/**
	 * Restarts all connections to the last serial port connected
	 * @throws Exception
	 */
	public void restartReadThread() throws Exception {
		stopSerialPort();
		if(port_num != "") {
			connect(port_num);
			startReadThread();
		}
	}

	/**
	 * Start reading thread, only if a serial port is connected
	 */
	public void startReadThread() {
		if(!port_connected)
			telem_frame.updateStatus("Serial Port Not Connected!");
		else {
			try {
				read_thread = new SerialPortReader(input_stream, telem_frame);
				read_thread.start();
			} catch (UnsupportedEncodingException e) {
				telem_frame.updateStatus("Failed to start read thread. Try again?");
			}
		}
	}
}