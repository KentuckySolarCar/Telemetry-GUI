package com.telemetry.serial;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class SerialPortReader implements Runnable {
	
//	DataInputStream input_stream;
	BufferedReader input_stream;
	
	public SerialPortReader (InputStream input_stream) {
//		this.input_stream = new DataInputStream(input_stream);
		this.input_stream = new BufferedReader( new InputStreamReader( input_stream ) );
	}

	@Override
	public void run() {
		while(true){
			try {
				
				Thread.sleep(1000);
			/*	String line = "";
	
				while(( line = new String(null, input_stream.readByte(), 0, "UTF-8")) != null) {
					System.out.println(line);
				} */
				String line = "";
				
				while( (line = input_stream.readLine()) != null ){
					System.out.println(line);
				}
				
			} catch(IOException e) {
//				e.printStackTrace();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
