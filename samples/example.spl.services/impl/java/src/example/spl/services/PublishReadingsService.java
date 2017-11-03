package example.spl.services;

import com.ibm.streamsx.microservices.common.AbstractSPLService;

public class PublishReadingsService extends AbstractSPLService {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static void main(String[] args) {
		PublishReadingsService service = new PublishReadingsService();
		service.run();
	}

	
	@Override
	protected String getMainCompositeFQN() {
		return "example.spl.services::PublishReadingsService";
	}

}
