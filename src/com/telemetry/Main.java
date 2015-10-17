package com.telemetry;

import com.telemetry.gui.TelemetryFrame;

public class Main {
	
	// Creates an instance of WindowCreator and reveal it
	public static void main(String[] args) throws InterruptedException {
		TelemetryFrame window = new TelemetryFrame();
		window.showCreatedWindow();
	}
	
}
