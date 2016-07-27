package com.telemetry.strategy;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class EnergyModelFunctions {

	/*
	 * CONSTANTS
	 */
	private static final int[] daysInMonth = new int[] { 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 };
	private static double directMaxPower = 900;
	private static double indirectMaxPower = 100;
	private static long seconds_in_day = 86400;
	
	/*
	 * EQUATIONS:
	 * 
	 * Power = Voltage x Current (P = V x I)
	 */
	
	public static void main(String[] args)
	{
		LocalDateTime local = LocalDateTime.now();
		int GMTOffset = 5;
		SolarPosition soPos = SolarAngles.getSolarPosition(38.0297, -84.4947, local.plusHours(GMTOffset), true);
		double energyLeft = getEnergyLeftInDay(local, GMTOffset, 38.0297, -84.4947, 1, 7, 9, 17, 20);
		System.out.println("Elevation: " + soPos.elevation);
		System.out.println("Azimuth: " + soPos.azimuth);
		System.out.println("Energy Left: " + energyLeft);
		System.out.println(getTimeLeftInDay());
	}
	
	/**
	 * Returns the time left in day from the current time
	 * @TODO Offset hour by timezone and race end time
	 * @param current_time (seconds)
	 */
	public static String getTimeLeftInDay() {
		LocalDateTime local = LocalDateTime.now();
		int current_time = local.getHour()*3600 + local.getMinute()*60 + local.getSecond();
		int seconds_left = (int) (seconds_in_day - current_time);
		int hour = seconds_left / 3600;
		int minute = (seconds_left - hour * 3600) / 60;
		int second = seconds_left - hour * 3600 - minute * 60;
		return hour + ":" + minute + ":" + second;
	}
	
	/**
	 * Returns the battery range in miles from the current
	 * motor speed and battery runtime in seconds
	 * @param batteryRuntime (seconds)
	 * @param motorSpeed (miles per hour)
	 * @return
	 */
	public static double getBatteryRange(double batteryRuntime, double motorSpeed)
	{
		return ((batteryRuntime) * (motorSpeed / 3600));
	}
	
	/**
	 * Returns the average battery runtime left in seconds
	 * @param stateOfCharge (Joules)
	 * @param avgPower (Joules / Second)
	 * @return
	 */
	public static double getAverageBatteryRuntime(double stateOfCharge, //Energy (Joules) left in battery pack
												   double longTermAvgBatteryPower) //Joules/Second
	{
		return stateOfCharge / longTermAvgBatteryPower;
	}
	
	/**
	 * Returns the instantaneous battery runtime left in seconds
	 * @param stateOfCharge (Joules)
	 * @param totalBatteryVoltage (Volts)
	 * @param totalBatteryCurrent (Amperes)
	 * @return
	 */
	public static double getInstantaneousBatteryRuntime(double stateOfCharge, //Energy (Joules) left in battery pack
														 double totalBatteryVoltage,
														 double totalBatteryCurrent)
	{
		double currentPower = totalBatteryVoltage * totalBatteryCurrent;
		
		return stateOfCharge / currentPower;
	}
		
	/**
	 * Returns the average
	 * @param batman
	 * @param robin
	 * @return
	 */
	public static double getAverage(double first, double second)
	{
		return (first + second) / 2;
	}
	
	/**
	 * Returns the total battery voltage given
	 * the average voltage across each battery's
	 * 20 cells
	 * @param avgBatmanVoltage
	 * @param avgRobinVoltage
	 * @return
	 */
	public static double getTotalBatteryVoltage(double avgBatmanVoltage, double avgRobinVoltage)
	{
		return 35 * (avgBatmanVoltage + avgRobinVoltage);
	}
	
	/**
	 * Returns the array power
	 * @param motorCurrent
	 * @param avgBatteryCurrent
	 * @param individualBatteryVoltage
	 * @return
	 */
	public static double getArrayPower(double motorCurrent, double avgBatteryCurrent, double individualBatteryVoltage)
	{
		double array_power = 35 * (motorCurrent - avgBatteryCurrent) * individualBatteryVoltage;
		if(array_power < 0)
			array_power = 0D;
		return array_power;
	}
	
	/**
	 * Returns the motor power
	 * @param motorCurrent
	 * @param motorVoltage
	 * @return
	 */
	public static double getMotorPower(double motorCurrent, double motorVoltage)
	{
		return motorCurrent * motorVoltage;
	}
	
	/**
	 * Equation to get array current until MPPTs work.
	 * @param motorCurrent
	 * @param batteryCurrent
	 * @param c - the idle power draw which will be less than 1 amp.
	 * @return
	 */
	public static double getArrayCurrent(double motorCurrent, double batteryCurrent, double c)
	{
		return motorCurrent - batteryCurrent + c;
	}
	
	/**
	 * Returns the battery power
	 * @param individualBatteryVoltage
	 * @param avgCurrent
	 * @return
	 */
	public static double getBatteryPower(double individualBatteryVoltage, double avgCurrent)
	{
		return individualBatteryVoltage * avgCurrent * 35;
	}
	
	/**
	 * Equation to calculate the average of all values in an ArrayList
	 * @return long term average of values in list
	 */
	public static double getLongTermAverage(ArrayList<Double> values)
	{
		double ltavg = 0;
		
		for(int i = 0; i < values.size(); i++)
			ltavg += values.get(i);
		
		return ltavg / values.size();
	}
	
	/**
	 * 
	 * @param GMTTime - the current time but converted to GMT time
	 * @param latitude - current latitude
	 * @param longitude - current longitude
	 * @param dayTypeCode - 0, 1, or 2 (0 for first day, 1 for second day, 2 for third day)
	 *****Times in Military time*******
	 * @param startChargeInMorning - default = 7
	 * @param startRacingInMorning - default = 9
	 * @param chargeInEveTime - default = 17
	 * @param stopChargingInEve - default = 20
	 * @return
	 */
	public static double getEnergyLeftInDay(LocalDateTime localTime, int GMTOffset, double latitude, double longitude, int dayTypeCode, double startChargeInMorning, double startRacingInMorning, double chargeInEveTime, double stopChargingInEve)
	{
		double energy = 0,
			   dtHours = .05;
		long dt = 3; // minutes
		LocalDateTime GMTTime = localTime.plusHours(GMTOffset); 
		double angle;
		
		if(dayTypeCode == 0)
		{
			while(localTime.getHour() <= chargeInEveTime && localTime.getHour() >= startRacingInMorning)
			{
				angle = SolarAngles.getSolarPosition(latitude, longitude, GMTTime, true).elevation;
				energy += powerWhileDriving((angle < 0) ? 0 : angle) * dtHours;
				localTime = localTime.plusMinutes(dt);
				GMTTime = GMTTime.plusMinutes(dt);
			}
			
			while(localTime.getHour() <= stopChargingInEve)
			{
				angle = SolarAngles.getSolarPosition(latitude, longitude, GMTTime, true).elevation;
				energy += powerWhileCharging((angle < 0) ? 0 : angle) * dtHours;
				localTime = localTime.plusMinutes(dt);
				GMTTime = GMTTime.plusMinutes(dt);
			}
		}
		else if(dayTypeCode == 1)
		{
			while(localTime.getHour() <= startRacingInMorning && localTime.getHour() >= startChargeInMorning )
			{
				angle = SolarAngles.getSolarPosition(latitude, longitude, GMTTime, true).elevation;
				energy += powerWhileDriving((angle < 0) ? 0 : angle) * dtHours;
				localTime = localTime.plusMinutes(dt);
				GMTTime = GMTTime.plusMinutes(dt);
			}
			
			while(localTime.getHour() <= chargeInEveTime)
			{
				angle = SolarAngles.getSolarPosition(latitude, longitude, GMTTime, true).elevation;
				energy += powerWhileDriving((angle < 0) ? 0 : angle) * dtHours;
				localTime = localTime.plusMinutes(dt);
				GMTTime = GMTTime.plusMinutes(dt);
			}
			
			while(localTime.getHour() <= stopChargingInEve)
			{
				angle = SolarAngles.getSolarPosition(latitude, longitude, GMTTime, true).elevation;
				energy += powerWhileCharging((angle < 0) ? 0 : angle) * dtHours;
				localTime = localTime.plusMinutes(dt);
				GMTTime = GMTTime.plusMinutes(dt);
			}
		}
		else
		{
			while(localTime.getHour() <= startRacingInMorning && localTime.getHour() >= startChargeInMorning)
			{
				angle = SolarAngles.getSolarPosition(latitude, longitude, GMTTime, true).elevation;
				energy += powerWhileDriving((angle < 0) ? 0 : angle) * dtHours;
				localTime = localTime.plusMinutes(dt);
				GMTTime = GMTTime.plusMinutes(dt);
			}
			
			while(localTime.getHour() <= chargeInEveTime)
			{
				angle = SolarAngles.getSolarPosition(latitude, longitude, GMTTime, true).elevation;
				energy += powerWhileDriving((angle < 0) ? 0 : angle) * dtHours;
				localTime = localTime.plusMinutes(dt);
				GMTTime = GMTTime.plusMinutes(dt);
			}
		}
		
		return energy;
	}
	
	/**
	 * Equation to calculate the power while charging
	 * @param solarAngle
	 * @return the power while charging
	 */
	public static double powerWhileCharging(double solarAngle)
	{
		return directMaxPower * (Math.pow(Math.abs(Math.cos(90 - solarAngle)), 1.0 / 3.0)) + indirectMaxPower;
	}
	
	/**
	 * Equation to calculate the power while driving
	 * @param solarAngle
	 * @return the power while driving
	 */
	public static double powerWhileDriving(double solarAngle)
	{
		return directMaxPower * (Math.pow(Math.abs(Math.cos(90 - solarAngle)), 1.0 / 3.0)) + indirectMaxPower * ((180 - solarAngle) / 180);
	}

	/**
	 * Converts degrees to radians
	 * @param radians
	 * @return
	 */
	public static double degreesToRadians(double radians)
	{
		return radians * (Math.PI / 180);
	}
}

