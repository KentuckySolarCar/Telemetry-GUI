package com.telemetry.serial;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.util.Timer;
import java.util.TimerTask;

public class SerialPortReader implements Runnable {
	
	BufferedReader input_stream;
	
	public SerialPortReader (InputStream input_stream) throws UnsupportedEncodingException {
		this.input_stream = new BufferedReader(new InputStreamReader(input_stream, "UTF-8"));
	}

	@Override
	public void run() {
		Timer T = new Timer();
		T.scheduleAtFixedRate(new TimerTask() {
			
			@Override
			public void run() {
				try {
					String line = "";
					
					while((line = input_stream.readLine()) != null) {
						System.out.println(line);
						Thread.sleep(50);
					}
					
				} catch (IOException | InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}, 0, 1000/30);
	}
}
