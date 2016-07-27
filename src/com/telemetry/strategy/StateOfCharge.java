package com.telemetry.strategy;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Set;

import com.jmatio.io.MatFileReader;
import com.jmatio.types.MLArray;
import com.jmatio.types.MLDouble;
import com.jmatio.types.MLStructure;

public class StateOfCharge {
	private MatFileReader mat_reader;
	private Map<String, MLArray> mat_content;
	private float[] voltage;
	private float[] amp_hour;
	private float total_amp_hour;
	
	public StateOfCharge() {
		
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
		String mat_file = "C:\\Users\\William\\Documents\\GitHub\\Telemetry-GUI\\dataForHayden.mat";
		try {
			mat_reader = new MatFileReader(mat_file);
		} catch (IOException e) {
			e.printStackTrace();
		}
		mat_content = mat_reader.getContent();
		MLStructure m_array = (MLStructure) mat_content.get("dataForHayden");
		MLArray discharge_data = m_array.getField("dischargeData");
		int N = discharge_data.getNDimensions();
		for(int i = 0; i < N; i++) {
			
		}
	}
	
//	public static void calculateSOC(double q_voltage, double[] voltages, double[] ) {
//		
//	}
}
