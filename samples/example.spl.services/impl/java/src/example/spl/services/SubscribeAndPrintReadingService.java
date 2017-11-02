//*******************************************************************************
//* Copyright (C) 2017 International Business Machines Corporation
//* All Rights Reserved
//*******************************************************************************

package example.spl.services;

import java.util.HashMap;
import java.util.Map;

import com.ibm.streamsx.microservices.common.AbstractSPLService;

public class SubscribeAndPrintReadingService extends AbstractSPLService {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static void main(String[] args) {
		SubscribeAndPrintReadingService service = new SubscribeAndPrintReadingService();
		service.run();
	}

	
	@Override
	protected String getMainCompositeFQN() {
		return "example.spl.services::SubscribeAndPrintReadingService";
	}
 
	@Override
	protected Map<String, Object> getParameters() {
		HashMap<String, Object> params = new HashMap<>();
		
		params.put("topic", "example/spl/services/PublishReadingsService/reading/v1");
		
		return params;
	}
	
}
