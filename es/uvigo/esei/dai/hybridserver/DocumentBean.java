package es.uvigo.esei.dai.hybridserver;

import java.io.IOException;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.XMLConstants;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.xml.sax.SAXException;

/**
 * Clase contenedora (bean) de documentos genéricos.
 */
public class DocumentBean {
	private final static Logger log = Logger.getLogger(DocumentBean.class.getName());
	
	public enum DocumentType {
		HTML, XML, XSD, XSLT
	}
	
	private DocumentType type = null;
	private UUID id = null;
	private String content = "";
	
	public DocumentBean() {}
	
	public DocumentBean(DocumentType type) {
		this(type, "", UUID.randomUUID());
	}
		
	public DocumentBean(DocumentType type, String content) {
		this(type, content, UUID.randomUUID());
	}
			
	public DocumentBean(DocumentType type, String content, UUID id) {
		this.type = type;
		this.content = content;
		this.id = id;
	}
	
	public DocumentType getType() {
		return this.type;
	}
	
	public void setType(DocumentType type) {
		this.type = type;
	}
	
	public UUID getUuid() {
		return this.id;
	}
	
	public void setUuid(UUID id) {
		this.id = id;
	}
	
	public String getContent() {
		return this.content;
	}
	
	public void setContent(String content) {
		this.content = content;
	}
	
	/**
	 * Valida un documento XML a partir de un documento XSD
	 * @param xml Documento XML
	 * @param xsd Documento XSD
	 * @return Estado de la validación
	 */
	public static boolean validateXml (DocumentBean xml, DocumentBean xsd) {
		if (xml.getType() == DocumentType.XML &&
				xsd.getType() == DocumentType.XSD) {
			try {
                Source xsdsrc = new StreamSource(new java.io.StringReader(xsd.getContent()));
                Source xmlsrc = new StreamSource(new java.io.StringReader(xml.getContent()));

                SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
                Schema schema = schemaFactory.newSchema(xsdsrc);

                Validator validator = schema.newValidator();
                validator.validate(xmlsrc);
                return true;
            } catch (IOException | SAXException e) {
                log.log(Level.WARNING, "Error while parse an XML document with a XSD document.", e);
            }
		}
		
		return false;
	}
}
