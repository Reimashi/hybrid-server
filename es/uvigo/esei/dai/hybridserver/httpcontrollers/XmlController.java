package es.uvigo.esei.dai.hybridserver.httpcontrollers;

import es.uvigo.esei.dai.hybridserver.http.HTTPRequestHandler;
import es.uvigo.esei.dai.hybridserver.services.DBService;

public class XmlController extends HTTPRequestHandler {
	public XmlController(DBService db) {
		super(db);
	}
}
