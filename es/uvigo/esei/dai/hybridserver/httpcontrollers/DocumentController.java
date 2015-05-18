package es.uvigo.esei.dai.hybridserver.httpcontrollers;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import es.uvigo.esei.dai.hybridserver.jws.HybridServerJwsClient;
import es.uvigo.esei.dai.hybridserver.services.DBService;
import es.uvigo.esei.dai.hybridserver.dao.DocumentDAO;
import es.uvigo.esei.dai.hybridserver.document.DocumentBean;
import es.uvigo.esei.dai.hybridserver.document.DocumentBeanInfo;
import es.uvigo.esei.dai.hybridserver.document.DocumentBeanType;
import es.uvigo.esei.dai.hybridserver.http.HTTPHeaders;
import es.uvigo.esei.dai.hybridserver.http.HTTPRequest;
import es.uvigo.esei.dai.hybridserver.http.HTTPRequestHandler;
import es.uvigo.esei.dai.hybridserver.http.HTTPResponse;
import es.uvigo.esei.dai.hybridserver.http.HTTPResponseStatus;
import es.uvigo.esei.dai.hybridserver.http.MIME;

public abstract class DocumentController extends HTTPRequestHandler {
    private static final Logger log = Logger.getLogger(DocumentController.class.getName());

	protected final static String jQuery_url = "<script src=\"https://code.jquery.com/jquery-2.1.4.min.js\"></script>";
	protected final static String Html_Encoding_Meta = "<meta charset=\"UTF-8\">";

	protected final DocumentBeanType type;
	
	public DocumentController(DBService db, HybridServerJwsClient jwsclient, DocumentBeanType type) {
		super(db, jwsclient);
		this.type = type;
	}
	
	@Override
	public HTTPResponse get(HTTPRequest req) {
		if (req.getResourceParameters().containsKey("uuid")) {
			UUID id = null;
			
			try {
				id = UUID.fromString(req.getResourceParameters().get("uuid"));
			}
			catch (IllegalArgumentException e) {
				return new HTTPResponse(HTTPResponseStatus.S400);
			}
			
			return this.getDocument(req, id);
		}
		else {
			return this.getList();
		}
	}
	
	protected HTTPResponse getList() {
		try {
			// Obtenemos la lista de documentos
			DocumentDAO dao = new DocumentDAO(this.db);
			List<DocumentBeanInfo> documents = dao.getList(this.type);
			Map<String, List<DocumentBeanInfo>> remoteDocuments = this.jwsclient.list(this.type);
			
			StringBuilder html = new StringBuilder();

			html.append("<html>");
			
			html.append("<head>"
					+ Html_Encoding_Meta
					+ jQuery_url
					+ "<title>Hybrid Server - " + this.type.getName() + "</title>"
					+ "</head>");

			html.append("<body><p><h1>Hybrid Server</h1></p>");
			html.append("<body><p><a href=\"/\">Inicio</a></p>");

			html.append("<p><h2>Nuevo documento " + this.type.getName() + "</h2></p>");
			html.append("<p>"
						+ "<form action=\"/" + this.type.getName().toLowerCase() + "\" method=\"POST\">"
							+ "<textarea name=\"" + this.type.getName().toLowerCase() + "\"></textarea>"
							+ "<br>"
							+ "<button type=\"submit\">Crear</button>"
						+ "<form>"
					+ "</p>");

			html.append("<p><h2>Documentos " + this.type.getName() + "</h2></p>");
			
			html.append(this.htmlDocumentList("Localhost", documents));
			
			for (String sname : remoteDocuments.keySet()) {
				html.append(this.htmlDocumentList(sname, remoteDocuments.get(sname)));
			}

			html.append("</body>");
			
			html.append("</html>");

			HTTPResponse res = new HTTPResponse(HTTPResponseStatus.S200);
			res.setContent(html.toString());
			res.putParameter(HTTPHeaders.CONTENT_TYPE.getHeader(), MIME.HTML.getMime());
			return res;
		} catch (SQLException e) {
			log.log(Level.WARNING, "Error manejando una peticion. {0}", e.getMessage());
			return new HTTPResponse(HTTPResponseStatus.S500); // 503?
		}
	}
	
	protected HTTPResponse getDocument(HTTPRequest req, UUID id) {
		DocumentDAO dao = new DocumentDAO(this.db);
		
		try {
			DocumentBean doc = dao.get(this.type, id);
			
			if (doc != null) {
				return this.getDocumentResponse(doc);
			}
			else {
				Entry <String, DocumentBean> ret = this.jwsclient.get(this.type, id);
				
				if (ret != null) {
					return this.getDocumentResponse(ret.getValue());
				}
				else {
					return new HTTPResponse(HTTPResponseStatus.S404);
				}
			}
		} catch (SQLException e) {
			log.log(Level.WARNING, "Error manejando una peticion. {0}", e.getMessage());
			return new HTTPResponse(HTTPResponseStatus.S500); // 503?
		}
	}
	
	protected HTTPResponse getDocumentResponse(DocumentBean doc) {
		HTTPResponse res = new HTTPResponse(HTTPResponseStatus.S200);
		res.setContent(doc.getContent());
		
		switch(doc.getInfo().getType()) {
		case XSD:
		case XSLT:
		case XML:
			res.putParameter(HTTPHeaders.CONTENT_TYPE.getHeader(), MIME.XML.getMime());
			break;
		case HTML:
		default:
			res.putParameter(HTTPHeaders.CONTENT_TYPE.getHeader(), MIME.HTML.getMime());
			break;
		}
		
		return res;
	}

	@Override
	public HTTPResponse post(HTTPRequest req) {
		if (req.getResourceParameters().containsKey(this.type.getName().toLowerCase())) {
			DocumentDAO dao = new DocumentDAO(this.db);
			
			DocumentBeanInfo documentInfo = new DocumentBeanInfo();
			documentInfo.setType(this.type);
			documentInfo.setID(UUID.randomUUID());
			
			DocumentBean document = new DocumentBean();
			document.setContent(req.getResourceParameters().get(this.type.getName().toLowerCase()));
			document.setInfo(documentInfo);
			
			try {
				dao.add(document.getInfo().getType(), document);
				
				StringBuilder html = new StringBuilder();

				html.append("<html>");
				
				html.append("<head>"
						+ Html_Encoding_Meta
						+ "<title>Hybrid Server - " + this.type.getName() + "</title>"
						+ "</head>");
				
				html.append("<body>"
						+ "<p>Se ha registrado un nuevo documento: <a href=\"/" + this.type.getName().toLowerCase() + "?uuid=" + document.getInfo().getID().toString() + "\">" + document.getInfo().getID().toString() + "</a></p>"
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

	@Override
	public HTTPResponse delete(HTTPRequest req) {
		if (req.getResourceParameters().containsKey("uuid")) {
			DocumentDAO dao = new DocumentDAO(this.db);
			UUID id = null;
			
			try {
				id = UUID.fromString(req.getResourceParameters().get("uuid"));
			}
			catch (IllegalArgumentException e) {
				return new HTTPResponse(HTTPResponseStatus.S400);
			}
			
			try {
				dao.delete(this.type, id);
				this.jwsclient.delete(this.type, id);
				
				return new HTTPResponse(HTTPResponseStatus.S200);
			} catch (SQLException e) {
				return new HTTPResponse(HTTPResponseStatus.S500);
			}
		}
		else {
			return new HTTPResponse(HTTPResponseStatus.S404);
		}
	}
	
	protected String htmlDocumentList(String server, List<DocumentBeanInfo> documents) {
		StringBuilder html = new StringBuilder();
		
		if (documents.size() > 0) {
			html.append("<p><h3>- " + server + "</h3></p>");
			html.append("<p><ul>");
			
			for (DocumentBeanInfo doc : documents) {
				html.append("<li><a href=\"/" + this.type.getName().toLowerCase() + "?uuid=" + doc.getID().toString() + "\">" + doc.getID().toString() + "</a> - " + deleteButton(doc.getID()) + "</li>");
			}
			
			html.append("</ul></p>");
		}
		else {
			html.append("<p>No hay ningún documento en el repositorio</p>");
		}
		
		return html.toString();
	}

	protected String deleteButton(UUID id) {
		return "<a href=\"#/delete/" + id.toString() + "\" onclick=\""
		    + "$.ajax({ url: '/" + this.type.getName().toLowerCase() + "?uuid=" + id.toString() + "', type: 'DELETE', success: function(result) { location.reload(); } }); return false;"
			+ "\">Borrar</a>";
	}
}
