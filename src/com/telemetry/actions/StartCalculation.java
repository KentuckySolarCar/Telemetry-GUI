package com.telemetry.actions;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Timer;
import java.util.TimerTask;

import com.telemetry.gui.TelemetryFrame;

public class StartCalculation extends Thread {
	private boolean status;
	
	public StartCalculation() {
		status = true;
	}
	
	public void stopThread() {
		status = false;
	}
	
	@Override
	public void run() {
		while(status) {
			try {
//				TelemetryFrame.updateCalculationPanel();
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}	
}
