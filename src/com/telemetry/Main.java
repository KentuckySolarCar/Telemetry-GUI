package com.telemetry;

import java.util.Timer;
import java.util.TimerTask;

import com.telemetry.gui.TelemetryFrame;

public class Main implements Runnable {
	
	private static final float FPS = 1;
	
	private static TelemetryFrame window;
	
	public Main() {
		Thread thread = new Thread(this, "Graphics_Engine");
		window = new TelemetryFrame();
		thread.start();
	}

	// Creates an instance of WindowCreator and reveal it
	public static void main(String[] args) throws InterruptedException {
		new Main();
	}

	@Override
	public void run() {
		Timer T = new Timer();
		T.scheduleAtFixedRate(new TimerTask() {

			@Override
			public void run() {
				try {
				//	window.updateTelemetryFrame();
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
		}, 0, 1000/30);
	}
}
