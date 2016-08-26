package com.telemetry.custom;

import com.telemetry.strategy.StateOfCharge;

public class GetSOC {
	public static void main(String[] args) {
		StateOfCharge soc = new StateOfCharge();
		System.out.println(soc.calculateSOC(3.731D));
	}
}
