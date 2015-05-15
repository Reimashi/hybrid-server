package es.uvigo.esei.dai.hybridserver.httpcontrollers;

import es.uvigo.esei.dai.hybridserver.http.HTTPRequestHandler;
import es.uvigo.esei.dai.hybridserver.services.DBService;

public class XsltController extends HTTPRequestHandler {
	public XsltController(DBService db) {
		super(db);
	}
}
