package com.telemetry;

import java.util.Timer;
import java.util.TimerTask;

import com.telemetry.gui.TelemetryFrame;

public class Main {
	
	private static final float FPS = 1;
	
	private static TelemetryFrame window;

	// Creates an instance of WindowCreator and reveal it
	public static void main(String[] args) throws InterruptedException {
		window = new TelemetryFrame();
	}
	
}
