package com.telemetry.serial;

import java.io.IOException;
import java.io.OutputStream;

public class SerialPortWriter implements Runnable {
	
	OutputStream output_stream;
	
	public SerialPortWriter (OutputStream output_stream) {
		this.output_stream = output_stream;
	}

	@Override
	public void run() {
		try {
			int c = 0;
            while ( ( c = System.in.read()) > -1 )
            {
                this.output_stream.write(c);
            }
		} catch(IOException e) {
			e.printStackTrace();
		}
	}

}