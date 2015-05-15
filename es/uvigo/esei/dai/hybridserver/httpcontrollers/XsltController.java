package es.uvigo.esei.dai.hybridserver.httpcontrollers;

import es.uvigo.esei.dai.hybridserver.http.HTTPRequestHandler;
import es.uvigo.esei.dai.hybridserver.jws.HybridServerJwsClient;
import es.uvigo.esei.dai.hybridserver.services.DBService;

public class XsltController extends HTTPRequestHandler {
	public XsltController(DBService db, HybridServerJwsClient jwsclient) {
		super(db, jwsclient);
	}
}
