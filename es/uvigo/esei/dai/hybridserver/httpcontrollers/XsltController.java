package es.uvigo.esei.dai.hybridserver.httpcontrollers;

import java.util.logging.Logger;

import es.uvigo.esei.dai.hybridserver.document.DocumentBeanType;
import es.uvigo.esei.dai.hybridserver.jws.HybridServerJwsClient;
import es.uvigo.esei.dai.hybridserver.services.DBService;

public class XsltController extends DocumentController {
    private static final Logger log = Logger.getLogger(HtmlController.class.getName());

	public XsltController(DBService db, HybridServerJwsClient jwsclient) {
		super(db, jwsclient, DocumentBeanType.XSLT);
	}
}
