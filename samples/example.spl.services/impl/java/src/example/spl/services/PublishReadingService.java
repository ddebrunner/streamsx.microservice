package example.spl.services;

import com.ibm.streamsx.microservices.common.AbstractSPLService;

public class PublishReadingService extends AbstractSPLService {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static void main(String[] args) {
		PublishReadingService service = new PublishReadingService();
		service.run();
	}

	
	@Override
	protected String getMainCompositeFQN() {
		return "example.spl.services::PublishReadingsService";
	}

}
