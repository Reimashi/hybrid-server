package es.uvigo.esei.dai.hybridserver.document;


import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Source;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
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
     * @param xsdPath Ruta al documento XSD
     * @return Datos obtenidos del documento validado
     */
    public static DocumentBean FromFile(String xmlPath, String xsdPath){
        DocumentBean configuracion = new DocumentBean();
        DocumentBean xsdConfiguracion = new DocumentBean();
        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(xmlPath);

            doc.getDocumentElement().normalize();

            //XSD
            if (!validateXml(configuracion,xsdConfiguracion)) {
                NodeList nList = doc.getElementsByTagName("configuration");
                Node nNode = nList.item(0);
                Element eElement = (Element) nNode;

                //ConnectionsConfig
                nList = doc.getElementsByTagName("connections");
                nNode = nList.item(0);
                eElement = (Element) nNode;
                if (eElement.getElementsByTagName("http").item(0) != null) {
                    configuracion.setHttpPort(Integer.parseInt(eElement.getElementsByTagName("http").item(0).getTextContent()));
                } else {
                    throw new java.lang.Exception();
                }
                if (eElement.getElementsByTagName("webservice").item(0) != null) {
                    configuracion.setWebServiceURL(eElement.getElementsByTagName("webservice").item(0).getTextContent());
                }
                if (eElement.getElementsByTagName("numClients").item(0) != null) {
                    configuracion.setNumClients(Integer.parseInt(eElement.getElementsByTagName("numClients").item(0).getTextContent()));
                }

                //DBconfig
                nList = doc.getElementsByTagName("database");
                nNode = nList.item(0);
                eElement = (Element) nNode;
                if (eElement.getElementsByTagName("url").item(0) != null) {
                    configuracion.setDbUser(eElement.getElementsByTagName("user").item(0).getTextContent());
                }
                if (eElement.getElementsByTagName("url").item(0) != null) {
                    configuracion.setDbPassword(eElement.getElementsByTagName("password").item(0).getTextContent());
                }
                if (eElement.getElementsByTagName("url").item(0) != null) {
                    configuracion.setDbURL(eElement.getElementsByTagName("url").item(0).getTextContent());
                }

                //P2PServerList
                ArrayList<ServerConfiguration> servers = new ArrayList<>();
                nList = doc.getElementsByTagName("server");
                for (int temp = 0; temp < nList.getLength(); temp++) {
                    nNode = nList.item(temp);
                    eElement = (Element) nNode;
                    String name;
                    if (eElement.getAttribute("name") != null) {
                        name = eElement.getAttribute("name");
                    } else {
                        throw new java.lang.Exception();
                    }
                    String wsdl;
                    if (eElement.getAttribute("wsdl") != "") {
                        wsdl = eElement.getAttribute("wsdl");
                    } else {
                        throw new java.lang.Exception();
                    }
                    String namespace;
                    if (eElement.getAttribute("namespace") != "") {
                        namespace = eElement.getAttribute("namespace");
                    } else {
                        throw new java.lang.Exception();
                    }
                    String service;
                    if (eElement.getAttribute("service") != "") {
                        service = eElement.getAttribute("service");
                    } else {
                        throw new java.lang.Exception();
                    }
                    String httpAddress;
                    if (eElement.getAttribute("httpAddress") != "") {
                        httpAddress = eElement.getAttribute("httpAddress");
                    } else {
                        throw new java.lang.Exception();
                    }
                    ServerConfiguration server = new ServerConfiguration(name, wsdl, namespace, service, httpAddress);
                    servers.add(server);

                }
                configuracion.setServers(servers);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return configuracion;
    }
}
