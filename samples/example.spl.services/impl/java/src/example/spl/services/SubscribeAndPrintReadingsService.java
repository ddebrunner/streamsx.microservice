//*******************************************************************************
//* Copyright (C) 2017 International Business Machines Corporation
//* All Rights Reserved
//*******************************************************************************

package example.spl.services;

import com.ibm.streamsx.microservices.common.AbstractSPLService;

public class SubscribeAndPrintReadingsService extends AbstractSPLService {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static void main(String[] args) {
		SubscribeAndPrintReadingsService service = new SubscribeAndPrintReadingsService();
		service.run();
	}

	
	@Override
	protected String getMainCompositeFQN() {
		return "example.spl.services::SubscribeAndPrintReadingsService";
	}
	
}
