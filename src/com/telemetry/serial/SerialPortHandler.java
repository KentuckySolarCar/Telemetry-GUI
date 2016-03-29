package com.telemetry.serial;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;

import javax.swing.JOptionPane;

import gnu.io.*;

public class SerialPortHandler {
	private SerialPort serial_port;
	private OutputStream output_stream;
	private InputStream input_stream;
	private static SerialPortWriter writer;
	private String port_num;
	private SerialPortReader read_thread;
	
	public SerialPortHandler() {
		port_num = "";
	}
	
	public void changePortNum(String port_num) {
		this.port_num = port_num;
	}
	
	public String getPortNum() {
		return port_num;
	}
	
	public boolean getPortReadStatus() {
		return read_thread.getThreadStatus();
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
                serial_port.setSerialPortParams(57600,SerialPort.DATABITS_8,SerialPort.STOPBITS_1,SerialPort.PARITY_NONE);
                
                input_stream = serial_port.getInputStream();
                output_stream = serial_port.getOutputStream();
                
                (new Thread(new SerialPortReader(input_stream))).start();
            }
            else
            	System.out.println("Error: This is not a serial port!");
        }
	}
	
	public void stopReadThread() {
		read_thread.stopThread();
	}
	
	public void startReadThread() {
		try {
			read_thread = new SerialPortReader(input_stream);
			read_thread.start();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
