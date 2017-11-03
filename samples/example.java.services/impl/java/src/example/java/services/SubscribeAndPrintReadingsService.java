package example.java.services;

import com.ibm.streamsx.microservices.common.AbstractService;
import com.ibm.streamsx.microservices.common.json.JsonSubscriber;
import com.ibm.streamsx.microservices.common.json.JsonToModelConverter;
import com.ibm.streamsx.topology.TStream;
import com.ibm.streamsx.topology.Topology;

import example.java.services.model.Reading;

public class SubscribeAndPrintReadingsService extends AbstractService {
	

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public static void main(String[] args) {
		SubscribeAndPrintReadingsService service = new SubscribeAndPrintReadingsService();
		service.run();
	}

	@Override
	protected Topology createTopology() 
	{
		Topology topo = new Topology("SubscribeAndPrintReadingsService");
		
		JsonToModelConverter<Reading> toModel = new JsonToModelConverter<>(Reading.class);
		
		TStream<String> jsonString = JsonSubscriber.subscribe(topo, IServiceConstants.READING_TOPIC);
		TStream<Reading> reading = jsonString.transform(t-> {
			return toModel.apply(t);
		});
		
		reading.print();
		
		return topo;
	}

}
