package es.uvigo.esei.dai.hybridserver.httpcontrollers;

import java.sql.SQLException;
import java.util.UUID;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;

import es.uvigo.esei.dai.hybridserver.dao.DocumentDAO;
import es.uvigo.esei.dai.hybridserver.document.DocumentBean;
import es.uvigo.esei.dai.hybridserver.document.DocumentBeanInfo;
import es.uvigo.esei.dai.hybridserver.document.DocumentBeanType;
import es.uvigo.esei.dai.hybridserver.http.HTTPHeaders;
import es.uvigo.esei.dai.hybridserver.http.HTTPRequest;
import es.uvigo.esei.dai.hybridserver.http.HTTPResponse;
import es.uvigo.esei.dai.hybridserver.http.HTTPResponseStatus;
import es.uvigo.esei.dai.hybridserver.http.MIME;
import es.uvigo.esei.dai.hybridserver.jws.HybridServerJwsClient;
import es.uvigo.esei.dai.hybridserver.services.DBService;

public class XsltController extends DocumentController {
    private static final Logger log = Logger.getLogger(HtmlController.class.getName());

	public XsltController(DBService db, HybridServerJwsClient jwsclient) {
		super(db, jwsclient, DocumentBeanType.XSLT);
	}

	@Override
	public HTTPResponse post(HTTPRequest req) {
		if (req.getResourceParameters().containsKey(this.type.getName().toLowerCase()) &&
				req.getResourceParameters().containsKey(DocumentBeanType.XSD.getName().toLowerCase())) {
			DocumentDAO dao = new DocumentDAO(this.db);
			UUID xsdid = null;
			
			try {
				xsdid = UUID.fromString(req.getResourceParameters().get("xsd"));
			}
			catch (IllegalArgumentException e) {
				return new HTTPResponse(HTTPResponseStatus.S400);
			}
			
			DocumentBeanInfo documentInfo = new DocumentBeanInfo();
			documentInfo.setType(this.type);
			documentInfo.setID(UUID.randomUUID());
			documentInfo.setXsd(xsdid);
			
			DocumentBean document = new DocumentBean();
			document.setContent(req.getResourceParameters().get(this.type.getName().toLowerCase()));
			document.setInfo(documentInfo);
			
			DocumentBean xsddocument = null;
			
			try {
				xsddocument = dao.get(DocumentBeanType.XSD, xsdid);
				
				if (xsddocument == null) {
					Entry <String, DocumentBean> ret = this.jwsclient.get(DocumentBeanType.XSD, xsdid);
					
					if (ret == null) {
						log.log(Level.WARNING, "Error insertando un XSLT con un XSD inexistente.");
						return new HTTPResponse(HTTPResponseStatus.S404);
					}
				}
			}
			catch (SQLException e) {
				log.log(Level.WARNING, "Error insertando un XSLT con un XSD inexistente. {0}", e.getMessage());
				return new HTTPResponse(HTTPResponseStatus.S404); // 503?
			}
			
			try {
				dao.add(DocumentBeanType.XSLT, document);
				
				StringBuilder html = new StringBuilder();

				html.append("<html>");
				
				html.append("<head>"
						+ Html_Encoding_Meta
						+ "<title>Hybrid Server - " + this.type.getName() + "</title>"
						+ "</head>");
				
				html.append("<body>"
						+ "<p>Se ha registrado un nuevo documento: <a href=\"" + this.type.getName().toLowerCase() + "?uuid=" + document.getInfo().getID().toString() + "\">" + document.getInfo().getID().toString() + "</a></p>"
						+ "<p><a href=\"/" + this.type.getName().toLowerCase() + "\">Volver</a></p>"
						+ "</body>");
				
				HTTPResponse res = new HTTPResponse(HTTPResponseStatus.S200);
				res.setContent(html.toString());
				res.putParameter(HTTPHeaders.CONTENT_TYPE.getHeader(), MIME.HTML.getMime());
				return res;
			}
			catch(SQLException e) {
				log.log(Level.WARNING, "Error manejando una peticion. {0}", e.getMessage());
				return new HTTPResponse(HTTPResponseStatus.S500);
			}
		}
		else {
			return new HTTPResponse(HTTPResponseStatus.S400);
		}
	}
}
