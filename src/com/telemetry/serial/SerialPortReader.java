package com.telemetry.serial;

import java.io.IOException;
import java.io.InputStream;

public class SerialPortReader implements Runnable {
	
	InputStream input_stream;
	
	public SerialPortReader (InputStream input_stream) {
		this.input_stream = input_stream;
	}

	@Override
	public void run() {
		byte[] buffer = new byte[1024];
		int length = -1;
		try {
			while((length = this.input_stream.read(buffer)) > -1) {
				System.out.println(new String(buffer, 0, length));
			}
		} catch(IOException e) {
			e.printStackTrace();
		}
	}

}
