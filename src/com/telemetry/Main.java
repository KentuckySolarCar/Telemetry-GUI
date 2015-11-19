package com.telemetry;

import java.util.Timer;
import java.util.TimerTask;

import com.telemetry.gui.TelemetryFrame;

public class Main {
	
	private static final float FPS = 1;
	
	private static TelemetryFrame window;

	// Creates an instance of WindowCreator and reveal it
	public static void main(String[] args) throws InterruptedException {
		GraphicsEngine engine = new GraphicsEngine();
		new Thread(engine).start();
		window = new TelemetryFrame();
	}
	
	static class GraphicsEngine implements Runnable {

		@Override
		public void run() {
			window.updateTelemetryFrame();
			try {
				Thread.sleep(1000);
			} catch(InterruptedException ex) {
				System.out.println(ex);
			}
		}
	}
}
