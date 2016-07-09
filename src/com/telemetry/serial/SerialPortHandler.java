package com.telemetry.serial;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;

import javax.swing.JOptionPane;

import com.telemetry.gui.TelemetryFrame;

import gnu.io.*;

public class SerialPortHandler {
	private SerialPort serial_port;
	private OutputStream output_stream;
	private InputStream input_stream;
	private String port_num;
	private SerialPortReader read_thread;
	private TelemetryFrame telem_frame;
	
	public SerialPortHandler(TelemetryFrame telem_frame) {
		port_num = "";
		this.telem_frame = telem_frame;
	}
	
	public void changePortNum(String port_num) {
		this.port_num = port_num;
	}
	
	public String getPortNum() {
		return port_num;
	}
	
	public boolean getPortReadStatus() {
		if(read_thread != null) {
			if(read_thread.getThreadStatus() != true)
				return false;
			else
				return true;
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
            }
            else
            	System.out.println("Error: This is not a serial port!");
        }
	}
	
	public void stopSerialPort() {
		try {
			read_thread.stopThread();
			input_stream.close();
			output_stream.close();
			serial_port.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void restartReadThread() throws Exception {
		stopSerialPort();
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
	
//	public static void write_command(byte[] bs) throws IOException {
//		writer.write(bs);
//	}
		
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
