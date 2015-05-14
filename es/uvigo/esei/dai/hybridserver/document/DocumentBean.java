package es.uvigo.esei.dai.hybridserver.document;


import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.XMLConstants;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.xml.sax.SAXException;

public class DocumentBean {
	private final static Logger log = Logger.getLogger(DocumentBean.class.getName());
	
	private DocumentBeanInfo info;
	
	public DocumentBeanInfo getInfo() {
		return this.info;
	}
	
	public void setInfo(DocumentBeanInfo info) {
		this.info = info;
	}
	
	private String content;
	
	public String getContent() {
		return this.content;
	}
	
	public void setContent(String c) {
		this.content = c;
	}

	/**
	 * Valida un documento XML a partir de un documento XSD
	 * @param xml Documento XML
	 * @param xsd Documento XSD
	 * @return Estado de la validación
	 */
	public static boolean validateXml (DocumentBean xml, DocumentBean xsd) {
		if (xml.getInfo().getType() == DocumentBeanType.XML && xsd.getInfo().getType() == DocumentBeanType.XSD) {
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

    /**
     * Valida un documento XML a partir de un documento XSD
     * @param xmlPath Ruta al documento XML
     * @return DocumentBean con un string conteniendo el documento
     */
    public static DocumentBean FromFile(String xmlPath){
        DocumentBean configuracion = new DocumentBean();
        if (configuracion.getInfo().getType() == DocumentBeanType.XML) {
            BufferedReader br = null;
            try {
                br = new BufferedReader(new FileReader(new File(xmlPath)));
                String line;
                StringBuilder sb = new StringBuilder();

                if (br != null) {
                    while ((line = br.readLine()) != null) {
                        sb.append(line.trim());
                    }
                configuracion.setContent(sb.toString());
                }
            } catch (IOException e){
                log.log(Level.WARNING, "Error while parse an XML document.", e);
            }
        }
        return configuracion;
    }
}
