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
            while (true)
            {
                this.output_stream.write(5);
            }
		} catch(IOException e) {
			e.printStackTrace();
		}
	}

	public void write(byte[] bs) {
		try {
			output_stream.write(bs);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}