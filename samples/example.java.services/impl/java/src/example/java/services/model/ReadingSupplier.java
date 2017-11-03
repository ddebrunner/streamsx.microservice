package example.java.services.model;

import java.io.ObjectStreamException;
import java.util.Random;

import com.ibm.streamsx.topology.function.Supplier;



public class ReadingSupplier implements Supplier<Reading> {
	
	transient Random rand = new Random();

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public Reading get() {
		long ts = System.currentTimeMillis();
		double reading = rand.nextDouble();
		return new Reading(ts, reading);
		
	}
	
	public Object readResolve() throws ObjectStreamException {
		rand = new Random();
		return this;
	}

}
