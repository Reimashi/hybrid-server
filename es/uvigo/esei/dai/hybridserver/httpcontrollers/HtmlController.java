package es.uvigo.esei.dai.hybridserver.httpcontrollers;

import java.sql.SQLException;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import es.uvigo.esei.dai.hybridserver.dao.DocumentDAO;
import es.uvigo.esei.dai.hybridserver.document.DocumentBean;
import es.uvigo.esei.dai.hybridserver.document.DocumentBeanInfo;
import es.uvigo.esei.dai.hybridserver.document.DocumentBeanType;
import es.uvigo.esei.dai.hybridserver.http.HTTPRequest;
import es.uvigo.esei.dai.hybridserver.http.HTTPRequestHandler;
import es.uvigo.esei.dai.hybridserver.http.HTTPResponse;
import es.uvigo.esei.dai.hybridserver.http.HTTPResponseStatus;
import es.uvigo.esei.dai.hybridserver.services.DBService;

public class HtmlController extends HTTPRequestHandler {
    private static final Logger log = Logger.getLogger(HtmlController.class.getName());
    
	private final static String jQuery_url = "<script src=\"https://code.jquery.com/jquery-2.1.4.min.js\"></script>";
    
	public HtmlController(DBService db) {
		super(db);
	}
	
	@Override
	public HTTPResponse get(HTTPRequest req) {
		if (req.getResourceParameters().containsKey("uuid")) {
			UUID id = UUID.fromString(req.getResourceParameters().get("uuid"));
			return this.getDocument(id);
		}
		else {
			return this.getList();
		}
	}
	
	private HTTPResponse getList() {
		try {
			// Obtenemos la lista de documentos
			DocumentDAO dao = new DocumentDAO(this.db);
			List<DocumentBeanInfo> documents = dao.getList(DocumentBeanType.HTML);
			
			StringBuilder html = new StringBuilder();

			html.append("<html>");
			
			html.append("<head>"
					+ jQuery_url
					+ "<title>Hybrid Server - HTML</title>"
					+ "</head>");
			
			html.append("<body><p><h1>Hybrid Server</h1></p>");

			html.append("<p><h2>Nuevo documento HTML</h2></p>");
			html.append("<p>"
						+ "<form action=\"/html\" method=\"POST\">"
							+ "<textarea name=\"html\"></textarea>"
							+ "<br>"
							+ "<button type=\"submit\">Crear</button>"
						+ "<form>"
					+ "</p>");

			html.append("<p><h2>Documentos HTML (Servidor local)</h2></p>");
			
			if (documents.size() > 0) {
				html.append("<p><ul>");
				
				for (DocumentBeanInfo doc : documents) {
					html.append("<li><a href=\"/html?uuid=" + doc.getID().toString() + "\">" + doc.getID().toString() + "</a> - " + deleteButton(doc.getID()) + "</li>");
				}
				
				html.append("</ul></p>");
			}
			else {
				html.append("<p>No hay ningún documento en el repositorio</p>");
			}

			html.append("</body>");
			
			html.append("</html>");

			HTTPResponse res = new HTTPResponse(HTTPResponseStatus.S200);
			res.setContent(html.toString());
			return res;
		} catch (SQLException e) {
			log.log(Level.WARNING, "Error manejando una peticion. {0}", e.getMessage());
			return new HTTPResponse(HTTPResponseStatus.S500); // 503?
		}
	}
	
	private HTTPResponse getDocument(UUID id) {
		DocumentDAO dao = new DocumentDAO(this.db);
		
		try {
			DocumentBean doc = dao.get(DocumentBeanType.HTML, id);
			
			if (doc != null) {
				HTTPResponse res = new HTTPResponse(HTTPResponseStatus.S200);
				res.setContent(doc.getContent());
				return res;
			}
			else {
				return new HTTPResponse(HTTPResponseStatus.S404);
			}
		} catch (SQLException e) {
			log.log(Level.WARNING, "Error manejando una peticion. {0}", e.getMessage());
			return new HTTPResponse(HTTPResponseStatus.S500); // 503?
		}
	}

	@Override
	public HTTPResponse post(HTTPRequest req) {
		if (req.getResourceParameters().containsKey("html")) {
			DocumentDAO dao = new DocumentDAO(this.db);
			
			DocumentBeanInfo documentInfo = new DocumentBeanInfo();
			documentInfo.setType(DocumentBeanType.HTML);
			documentInfo.setID(UUID.randomUUID());
			
			DocumentBean document = new DocumentBean();
			document.setContent(req.getResourceParameters().get("html"));
			document.setInfo(documentInfo);
			
			try {
				dao.add(document.getInfo().getType(), document);
				
				StringBuilder html = new StringBuilder();

				html.append("<html>");
				
				html.append("<head>"
						+ "<title>Hybrid Server - HTML</title>"
						+ "</head>");
				
				html.append("<body>"
						+ "<p>Se ha registrado un nuevo documento: <a href=\"/html?uuid=" + document.getInfo().getID().toString() + "\">" + document.getInfo().getID().toString() + "</a></p>"
						+ "<p><a href=\"/html\">Volver</a></p>"
						+ "</body>");
				
				HTTPResponse res = new HTTPResponse(HTTPResponseStatus.S200);
				res.setContent(html.toString());
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

	@Override
	public HTTPResponse delete(HTTPRequest req) {
		if (req.getResourceParameters().containsKey("uuid")) {
			DocumentDAO dao = new DocumentDAO(this.db);
			UUID id = UUID.fromString(req.getResourceParameters().get("uuid"));
			
			try {
				dao.delete(DocumentBeanType.HTML, id);
				return new HTTPResponse(HTTPResponseStatus.S200);
			} catch (SQLException e) {
				return new HTTPResponse(HTTPResponseStatus.S500);
			}
		}
		else {
			return new HTTPResponse(HTTPResponseStatus.S404);
		}
	}

	private static String deleteButton(UUID id) {
		return "<a href=\"#\" onclick=\""
		    + "$.ajax({ url: '/html?uuid=" + id.toString() + "', type: 'DELETE', success: function(result) { location.reload(); } });"
			+ "\">Borrar</a>";
	}
}
