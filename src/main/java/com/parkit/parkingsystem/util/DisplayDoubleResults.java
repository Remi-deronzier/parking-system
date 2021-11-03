package com.parkit.parkingsystem.util;

public class DisplayDoubleResults {
	private final double fees;
	private final boolean isDataBaseWellUpdated;

	public DisplayDoubleResults(double fees, boolean isDataBaseWellUpdated) {
		this.fees = fees;
		this.isDataBaseWellUpdated = isDataBaseWellUpdated;
	}

	public double getFees() {
		return fees;
	}

	public boolean isDataBaseWellUpdated() {
		return isDataBaseWellUpdated;
	}

}
