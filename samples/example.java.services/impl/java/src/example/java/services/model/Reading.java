package example.java.services.model;

import java.io.Serializable;

public class Reading implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private long ts;
	
	private double reading;
	
	public Reading(long ts, double reading) {
		this.ts = ts;
		this.reading = reading;
	}
	
	public long getTs() {
		return ts;
	}
	
	public double getReading() {
		return reading;
	};
	
	@Override
	public String toString() {
		return "TS: " + ts + " reading: " + reading;
	}

}
