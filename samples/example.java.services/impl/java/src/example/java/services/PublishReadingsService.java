//*******************************************************************************
//* Copyright (C) 2017 International Business Machines Corporation
//* All Rights Reserved
//*******************************************************************************

package example.java.services;

import java.util.concurrent.TimeUnit;

import com.ibm.streamsx.microservices.common.AbstractService;
import com.ibm.streamsx.microservices.common.json.JsonPublisher;
import com.ibm.streamsx.microservices.common.json.ModelToJsonConverter;
import com.ibm.streamsx.topology.TStream;
import com.ibm.streamsx.topology.Topology;

import example.java.services.model.Reading;
import example.java.services.model.ReadingSupplier;

public class PublishReadingsService extends AbstractService{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public static void main(String[] args) {
		PublishReadingsService service = new PublishReadingsService();
		service.run();
	}

	@Override
	protected Topology createTopology() {

		Topology topo = new Topology("PublishReadingsService");
		ModelToJsonConverter<Reading> toJson = new ModelToJsonConverter<>();
				
		TStream<Reading> readings = topo.periodicSource(new ReadingSupplier(), 500, TimeUnit.MILLISECONDS);
		
		// The "Proper Way" of publishing as JSON is as follows
		// However, it seems like we have to take an extra step to implement JSONable
		// for each of the model object that we would like to convert, which may require a bit of work.
		// Furthermore, there does not seem to be a way to go the opposite direction, where
		// we want to convert JSONObject to model object.
		// Our contribution uses GSON to solve this problem
		
		// TStream<JSONObject> jsonStream = JSONStreams.toJSON(readings);
		// jsonStream.publish(IServiceConstants.READING_TOPIC);
		
		TStream<String> jsonString = readings.transform(t->{
			return toJson.apply(t);
		});
		
		// Publisher converts stream jsonString to tuple<rstring jsonStrin> in SPL before publishing
		JsonPublisher.publish(jsonString, IServiceConstants.READING_TOPIC);		
		return topo;
	}

}
