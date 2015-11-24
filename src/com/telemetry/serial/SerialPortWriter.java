package com.telemetry.serial;

import java.io.IOException;
import java.io.OutputStream;

public class SerialPortWriter {
	
	OutputStream output_stream;
	
	public SerialPortWriter (OutputStream output_stream) {
		this.output_stream = output_stream;
	}
	
	public void write(int command) throws IOException {
		this.output_stream.write(command);
	}

/*	@Override
	public void run() {
		try {
			Thread.sleep(1000);
			int c = 0;
            while ( ( c = System.in.read()) > -1 )
            {
                this.output_stream.write(c);
            }
		} catch(IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	} */
}