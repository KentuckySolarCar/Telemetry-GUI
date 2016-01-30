package com.telemetry.serial;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import gnu.io.*;

public class SerialPortHandler {
	private SerialPort serial_port;
	private OutputStream output_stream;
	private InputStream input_stream;
	private static SerialPortWriter writer;
	
	public SerialPortHandler() {
		
	}
	
	public void connect(String port_name) throws Exception {
	/*	try {
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
		} */
		
		CommPortIdentifier portIdentifier = CommPortIdentifier.getPortIdentifier(port_name);
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
                serial_port.setSerialPortParams(57600,SerialPort.DATABITS_8,SerialPort.STOPBITS_1,SerialPort.PARITY_NONE);
                
                input_stream = serial_port.getInputStream();
                output_stream = serial_port.getOutputStream();
                
                (new Thread(new SerialPortReader(input_stream))).start();
                writer = new SerialPortWriter(output_stream);
            }
            else
            {
                System.out.println("Error: Only serial ports are handled by this example.");
            }
        }
	}
	
	public static void write_command(int command) throws IOException {
		writer.write(command);
	}
		
/*	private void setSerialPortParameters() throws IOException {
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
    } */
}
