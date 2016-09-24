package com.telemetry.strategy;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.jmatio.io.MatFileReader;
import com.jmatio.types.MLArray;
import com.jmatio.types.MLDouble;
import com.jmatio.types.MLStructure;

/**
 * This class is messy with comments, but strategy is still at experimental stage
 * @author wsong
 *
 */
public class StateOfCharge {
	private MatFileReader mat_reader;
	private Map<String, MLArray> mat_content;
	private double[] voltage;
	private double[] amp_hour;
	private static final int M = 6989;
	private static final double t_amp = 0.0004286;
	
	public StateOfCharge() {
		initializeData();
	}
	
//	1x10 struct array with fields: 
//	*	cycleData
//		esrCycleData
//		esrCycleVoltage
//		cycleESR
//		cycleCapacity
//	*5	dischargeData
//		esrDischargeData
//		esrDischargeVoltage
//		dischargeESR
//		dischargeCapacity
//	*	chargeData
//		esrChargeData
//		esrChargeVoltage
//		chargeESR
//		chargeCapacity
//		weight
//		scaleNormalizedCapacity

	public void initializeData() {
		String mat_file = "dataForHayden.mat";
		try {
			mat_reader = new MatFileReader(mat_file);
		} catch (IOException e) {
			e.printStackTrace();
		}
		mat_content = mat_reader.getContent();
		MLStructure m_array = (MLStructure) mat_content.get("dataForHayden");
		MLArray discharge_data = m_array.getField("dischargeData");
//		int M = discharge_data.getM();
		voltage = new double[M];
		amp_hour = new double[M];
		double[][] array = ((MLDouble) discharge_data).getArray();
		for(int i = 0; i < M; i++) {
			voltage[i] = array[i][0];
			amp_hour[i] = array[i][6];
		}
	}
	
	public double calculateTotalWattHour() {
//		int q_amp_hour = 0;
//		double t_amp = 0D;
//		for(int i = 0; i < M; i++) {
//			double delta_y = amp_hour[q_amp_hour] - amp_hour[q_amp_hour+i]; 
//			double delta_x = i+1;
//			t_amp += (delta_y / delta_x);
//		}
//		t_amp /= M;
		
		double watt_hour = 0D;
		for(int i = 0; i < M-1; i++) {
			watt_hour += getTrapizoidArea(voltage[i]*t_amp, 
										  voltage[i+1]*t_amp, 1);
		}
		return watt_hour;
	}
	
	public double calculateSOC(double t_voltage) {
//		int q_amp_hour = 0;
//		for(int i = 0; i < M-1; i++) {
//			if(voltage[i] < t_voltage) {
//				q_amp_hour = i;
//				break;
//			}
//		}
//		double t_amp = 0D;
//		int	bounds = (q_amp_hour < M/2) ? q_amp_hour : (M - q_amp_hour);
//		for(int i = 1; i < bounds; i++) {
//			double delta_x = i * 2;
//			double delta_y = amp_hour[q_amp_hour+i] - amp_hour[q_amp_hour-i];
//			t_amp += delta_y / delta_x;
//		}
//		// Average combined 5 values for derivative (slope)
//		t_amp /= bounds;
		List<Double> race_voltages = new ArrayList<Double>();
		for(int i = 0; i < M; i++) {
			if(voltage[i] >= t_voltage)
				race_voltages.add(voltage[i]);
			else
				break;
		}
		
		double current_watt_hour = 0D;
		for(int i = 0; i < race_voltages.size()-1; i++) {
			current_watt_hour += getTrapizoidArea(race_voltages.get(i)*t_amp,
												  race_voltages.get(i)*t_amp, 1);
		}
		return 100D - 100*current_watt_hour / calculateTotalWattHour();
	}
	
	public static double getTrapizoidArea(double y_left, double y_right, double delta_x) {
		return (y_left + y_right) * delta_x / 2;
	}
}
