package es.uvigo.esei.dai.hybridserver.httpcontrollers;

import es.uvigo.esei.dai.hybridserver.http.HTTPRequestHandler;
import es.uvigo.esei.dai.hybridserver.services.DBService;

public class XsdController extends HTTPRequestHandler {
	public XsdController(DBService db) {
		super(db);
	}
}
