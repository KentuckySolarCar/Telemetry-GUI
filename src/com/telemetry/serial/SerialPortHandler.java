package com.telemetry.serial;

import java.io.IOException;
import java.io.OutputStream;

import gnu.io.*;

public class SerialPortHandler {
	private SerialPort serial_port;
	private OutputStream output_stream;
	
	public void connect(String port_name) throws IOException {
		try {
			// Obtain a CommPortIdentifier object for the port you want to open
			CommPortIdentifier port_id = CommPortIdentifier.getPortIdentifier(port_name);
			
			// Get the port's ownership
			serial_port = (SerialPort) port_id.open("Telemetry Connection", 5000);
			
			 // Set the parameters of the connection.
            setSerialPortParameters();
 
            // Open the input and output streams for the connection. If they won't
            // open, close the port before throwing an exception.
            output_stream = serial_port.getOutputStream();
            
		} catch(NoSuchPortException e) {
			throw new IOException(e.getMessage());
		} catch(PortInUseException e) {
			throw new IOException(e.getMessage());
		} catch(IOException e) {
			serial_port.close();
			throw e;
		}
	}
	
	public OutputStream getSerialInputStream() {
        return output_stream;
    }
	
	private void setSerialPortParameters() throws IOException {
        int baudRate = 57600; // 57600bps
 
        try {
            serial_port.setSerialPortParams(
                    baudRate,
                    SerialPort.DATABITS_8,
                    SerialPort.STOPBITS_1,
                    SerialPort.PARITY_NONE);
 
            serial_port.setFlowControlMode(
                    SerialPort.FLOWCONTROL_NONE);
        } catch (UnsupportedCommOperationException ex) {
            throw new IOException("Unsupported serial port parameter");
        }
    }
}
