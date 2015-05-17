package es.uvigo.esei.dai.hybridserver.document;


import java.io.*;
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

import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlAccessType;

@XmlRootElement(name = "documentBean")
@XmlAccessorType(XmlAccessType.NONE)
@XmlType(name = "documentType",
	namespace="es.uvigo.esei.dai.hybridserver", 
	propOrder = {
        "info",
        "content"
})
public class DocumentBean implements Serializable {
	private static final long serialVersionUID = 4729980263308146090L;
	private final static Logger log = Logger.getLogger(DocumentBean.class.getName());

    @XmlElement(name = "info", required = true)
	private DocumentBeanInfo info;
	
	public DocumentBeanInfo getInfo() {
		return this.info;
	}
	
	public void setInfo(DocumentBeanInfo info) {
		this.info = info;
	}

    @XmlElement(name = "content", required = true)
	private String content;
	
	public String getContent() {
		return this.content;
	}
	
	public void setContent(String c) {
		this.content = c;
	}

	/**
	 * Valida un documento XML a partir de un documento XSD (SAX)
	 * @param xml Documento XML
	 * @param xsd Documento XSD
	 * @return Estado de la validación
	 */
	public static boolean validateXml (DocumentBean xml, DocumentBean xsd) {
		if (xml.getInfo().getType() == DocumentBeanType.XML && xsd.getInfo().getType() == DocumentBeanType.XSD) {
            try {
                Source xsdsrc = new StreamSource(new java.io.StringReader(xsd.getContent()));
                Source xmlsrc = new StreamSource(new java.io.StringReader(xml.getContent()));
                
                try {
	                SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
	                Schema schema = schemaFactory.newSchema(xsdsrc);
	                
	                try {
	                    Validator validator = schema.newValidator();
	                    validator.validate(xmlsrc);
	                    
	                    return true;
	                }
	                catch(SAXException e) {
	                    log.log(Level.WARNING, "Error while parse an XML document.", e);
	                }
                }
                catch(SAXException e) {
                    log.log(Level.WARNING, "Error while parse an XSD document.", e);
                }
            } catch (IOException e) {
                log.log(Level.WARNING, "Error while parse an XML document with a XSD document.", e);
            }
        }
		
        return false;
	}
	
	/**
	 * Carga un archivo en un contenedor DocumentBean
	 * @param xmlPath Ruta del archivo
	 * @return Contenedor del documento
	 */
    public static DocumentBean FromFile(String fPath) throws FileNotFoundException, IOException {
    	return DocumentBean.FromFile(new File(fPath));
    }
	
	/**
	 * Carga un archivo en un contenedor DocumentBean
	 * @param xmlFile Archivo
	 * @return Contenedor del documento
	 * @throws IOException 
	 * @throws FileNotFoundException 
	 */
    public static DocumentBean FromFile(File fFile) throws FileNotFoundException, IOException {
        DocumentBean doc = new DocumentBean();
        DocumentBeanInfo docMeta = new DocumentBeanInfo();
        docMeta.setID(UUID.randomUUID());
        
        String extension = "";
        int i = fFile.getName().lastIndexOf('.');
        if (i > 0 && i < fFile.getName().length()) {
            extension = fFile.getName().substring(i+1).toLowerCase();
        }
        
        switch (extension) {
	        case "html":
	        	docMeta.setType(DocumentBeanType.HTML);
	        	break;
	        case "xml":
	        	docMeta.setType(DocumentBeanType.XML);
	        	break;
	        case "xsd":
	        	docMeta.setType(DocumentBeanType.XSD);
	        	break;
	        case "xslt":
	        	docMeta.setType(DocumentBeanType.XSLT);
	        	break;
	    	default:
	        	docMeta.setType(DocumentBeanType.UNDEFINED);
	        	break;	
        }
        
        doc.setInfo(docMeta);

        StringBuilder sb = new StringBuilder();
		String EOL = System.getProperty("line.separator");
        
        try (BufferedReader br = new BufferedReader(new FileReader(fFile)))
        {
        	String line;
            while ((line = br.readLine()) != null) {
                sb.append(line + EOL);
            }
        }
        
        doc.setContent(sb.toString());
        
        return doc;
    }
}
