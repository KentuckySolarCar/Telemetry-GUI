package com.telemetry.strategy;

public class SolarPosition {
	public double azimuth;
	public double elevation;
	
	public SolarPosition(double azimuth, double elevation)
	{
		this.azimuth = azimuth;
		this.elevation = elevation;
	}
	
	public SolarPosition()
	{
		this.azimuth = -1;
		this.elevation = -1;
	}
}
