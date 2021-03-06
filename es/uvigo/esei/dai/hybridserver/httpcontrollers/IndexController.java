package es.uvigo.esei.dai.hybridserver.httpcontrollers;

import es.uvigo.esei.dai.hybridserver.http.HTTPRequest;
import es.uvigo.esei.dai.hybridserver.http.HTTPRequestHandler;
import es.uvigo.esei.dai.hybridserver.http.HTTPResponse;
import es.uvigo.esei.dai.hybridserver.http.HTTPResponseStatus;
import es.uvigo.esei.dai.hybridserver.jws.HybridServerJwsClient;
import es.uvigo.esei.dai.hybridserver.services.DBService;

public class IndexController extends HTTPRequestHandler {
	protected final static String Html_Encoding_Meta = "<meta charset=\"UTF-8\">";
	
	public IndexController(DBService db, HybridServerJwsClient jwsclient) {
		super(db, jwsclient);
	}

	@Override
	public HTTPResponse get(HTTPRequest req) {
		HTTPResponse res = new HTTPResponse(HTTPResponseStatus.S200);
		
		StringBuilder html = new StringBuilder();

		html.append("<html>");
		
		html.append("<head>"
				+ Html_Encoding_Meta
				+ "<title>Hybrid Server - Inicio</title>"
				+ "</head>");
		
		html.append("<body>"
					+ "<p><h1>Hybrid Server</h1></p>"
					+ "<p>"
						+ "<p><h2>Autores</h2></p>"
						+ "<ul>"
							+ "<li><a href=\"mailto:adrian.gonzalez.barbosa@gmail.com\">Adrián González Barbosa</a></li>"
							+ "<li><a href=\"mailto:reimashi@gmail.com\">Aitor González Fernández</a></li>"
						+ "</ul>"
					+ "</p>"
					+ "<p>"
						+ "<p><h2>Repositorios</h2></p>"
						+ "<ol>"
							+ "<li><a href=\"/html\">HTML</a></li>"
							+ "<li><a href=\"/xml\">XML</a></li>"
							+ "<li><a href=\"/xsd\">XSD</a></li>"
							+ "<li><a href=\"/xslt\">XSLT</a></li>"
						+ "</ol>"
					+ "</p>"
				+ "</body>");
		
		html.append("</html>");
		
		res.setContent(html.toString());
		
		return res;
	}
}
