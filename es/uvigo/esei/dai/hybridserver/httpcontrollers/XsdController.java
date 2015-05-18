package es.uvigo.esei.dai.hybridserver.httpcontrollers;

import java.util.logging.Logger;

import es.uvigo.esei.dai.hybridserver.document.DocumentBeanType;
import es.uvigo.esei.dai.hybridserver.jws.HybridServerJwsClient;
import es.uvigo.esei.dai.hybridserver.services.DBService;

public class XsdController extends DocumentController {
    private static final Logger log = Logger.getLogger(HtmlController.class.getName());

	public XsdController(DBService db, HybridServerJwsClient jwsclient) {
		super(db, jwsclient, DocumentBeanType.XSD);
	}
}
