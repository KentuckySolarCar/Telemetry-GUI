package com.telemetry.serial;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.util.Timer;
import java.util.TimerTask;

public class SerialPortReader extends Thread {
	
	private BufferedReader input_stream;
	private InputStream is;
	private boolean status;
	
	public SerialPortReader (InputStream input_stream) throws UnsupportedEncodingException {
		status = false;
		this.input_stream = new BufferedReader(new InputStreamReader(input_stream));
	}
	
	public void stopThread() {
		status = false;
	}
	
	public boolean getThreadStatus() {
		return status;
	}

	@Override
	public void run() {
		status = true;
		try {
			String line = "";
			
			while(((line = input_stream.readLine()) != null) && (status)) {
				System.out.println(line);
				Thread.sleep(1000);
			}
			
		} catch (IOException | InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
