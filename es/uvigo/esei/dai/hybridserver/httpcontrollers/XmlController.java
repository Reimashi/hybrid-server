package es.uvigo.esei.dai.hybridserver.httpcontrollers;

import java.sql.SQLException;
import java.util.UUID;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;

import es.uvigo.esei.dai.hybridserver.dao.DocumentDAO;
import es.uvigo.esei.dai.hybridserver.document.DocumentBean;
import es.uvigo.esei.dai.hybridserver.document.DocumentBeanType;
import es.uvigo.esei.dai.hybridserver.http.HTTPRequest;
import es.uvigo.esei.dai.hybridserver.http.HTTPResponse;
import es.uvigo.esei.dai.hybridserver.http.HTTPResponseStatus;
import es.uvigo.esei.dai.hybridserver.jws.HybridServerJwsClient;
import es.uvigo.esei.dai.hybridserver.services.DBService;

public class XmlController extends DocumentController {
    private static final Logger log = Logger.getLogger(HtmlController.class.getName());

	public XmlController(DBService db, HybridServerJwsClient jwsclient) {
		super(db, jwsclient, DocumentBeanType.XML);
	}
	
	@Override
	protected HTTPResponse getDocument(HTTPRequest req, UUID id) {
		DocumentDAO dao = new DocumentDAO(this.db);
		
		DocumentBean doctoret = null;

		// Obtenemos el archivo XML
		try {
			doctoret = dao.get(this.type, id);

			if (doctoret == null) {
				Entry <String, DocumentBean> ret = this.jwsclient.get(this.type, id);
				
				if (ret != null && ret.getValue() != null) {
					doctoret = ret.getValue();
				}
				else {
					// Si no se encuentra el archivo XML
					return new HTTPResponse(HTTPResponseStatus.S404);
				}
			}
		}
		catch (SQLException e) {
			log.log(Level.WARNING, "Error manejando una peticion. {0}", e.getMessage());
			return new HTTPResponse(HTTPResponseStatus.S500); // 503?
		}

		// Obtenemos el archivo XSLT si es necesario
		if (req.getResourceParameters().containsKey("xslt")) {
			DocumentBean docxslt = null;
			UUID xsltid = UUID.fromString(req.getResourceParameters().get("xslt"));
			
			try {
				docxslt = dao.get(DocumentBeanType.XSLT, xsltid);

				if (docxslt == null) {
					Entry <String, DocumentBean> ret = this.jwsclient.get(DocumentBeanType.XSLT, xsltid);
					
					if (ret != null && ret.getValue() != null) {
						docxslt = ret.getValue();
					}
					else {
						// Si no se encuentra el archivo XSLT
						return new HTTPResponse(HTTPResponseStatus.S404);
					}
				}
			}
			catch (SQLException e) {
				log.log(Level.WARNING, "Error manejando una peticion. {0}", e.getMessage());
				return new HTTPResponse(HTTPResponseStatus.S500); // 503?
			}

			// Obtenemos el XSD asociado al XSLT.
			DocumentBean docxsd = null;
			UUID xsdid = docxslt.getInfo().getXsd();

			try {
				docxsd = dao.get(DocumentBeanType.XSD, xsdid);

				if (docxsd == null) {
					Entry <String, DocumentBean> ret = this.jwsclient.get(DocumentBeanType.XSD, xsdid);
					
					if (ret != null && ret.getValue() != null) {
						docxsd = ret.getValue();
					}
					else {
						log.log(Level.WARNING, "No se ha encontrado el XSD {0} asociado al XSLT {1}.", new Object[] { xsdid.toString(), xsltid.toString() });
						// Si no se encuentra el archivo XSD (Según aclaraciones, 400 en lugar de 404)
						return new HTTPResponse(HTTPResponseStatus.S400);
					}
				}
			}
			catch (SQLException e) {
				log.log(Level.WARNING, "Error manejando una peticion. {0}", e.getMessage());
				return new HTTPResponse(HTTPResponseStatus.S500); // 503?
			}
			
			// Validamos el XML con el XSD asociado al XSLT
			if (!DocumentBean.validateXml(doctoret, docxsd)) {
				// Si no se valida con el archivo XSD (Según aclaraciones, 400)
				return new HTTPResponse(HTTPResponseStatus.S400);
			}
			else {
				doctoret = DocumentBean.applyXslt(doctoret, docxslt);
				
				// En caso de que falle la transformación XSLT. (El codigo 500 lo obliga en las aclaraciones)
				if (doctoret == null) {
					return new HTTPResponse(HTTPResponseStatus.S500);
				}
			}
		}
		
		// Recuperamos el documento
		return this.getDocumentResponse(doctoret);
	}
}
