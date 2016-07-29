package com.telemetry;

import java.io.File;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import org.json.simple.parser.ParseException;

import com.telemetry.gui.TelemetryFrame;
import com.telemetry.strategy.StateOfCharge;

public class Main implements Runnable {
	
	private static TelemetryFrame window;

	// Creates an instance of WindowCreator and reveal it
	public static void main(String[] args) throws InterruptedException, IOException, ParseException {
		Thread thread = new Thread(new Main());
		window = new TelemetryFrame();
		thread.start();
	}

	@Override
	public void run() {
		Timer T = new Timer();
		T.scheduleAtFixedRate(new TimerTask() {

			@Override
			public void run() {
				try {
					window.updateRunTime();
					Thread.sleep(100);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
		}, 0, 1000/30);
	}
}
