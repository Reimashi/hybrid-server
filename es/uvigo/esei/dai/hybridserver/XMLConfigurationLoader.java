package es.uvigo.esei.dai.hybridserver;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.NodeList;

import es.uvigo.esei.dai.hybridserver.document.DocumentBean;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.util.ArrayList;

public class XMLConfigurationLoader {
	private final static String XSD_CONFIG_PATH = "configuration.xsd";
    /**
     * @param f The file to load and parse
     * @return A Configuration object containing the values of the xml
     */
    public Configuration load (File f) throws Exception {
        Configuration configuracion = new Configuration();
    	DocumentBean xml = DocumentBean.FromFile(f);
    	DocumentBean xsd = DocumentBean.FromFile(XSD_CONFIG_PATH);
    	
    	if (DocumentBean.validateXml(xml, xsd)) {
    		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document xmldoc = dBuilder.parse(new ByteArrayInputStream(xml.getContent().getBytes()));

            XPath xPath =  XPathFactory.newInstance().newXPath();

            String expression = "/configuration/connections/http";
            configuracion.setHttpPort(Integer.parseInt(
            		xPath.compile(expression).evaluate(xmldoc)));
            
            expression = "/configuration/connections/webservice";
            configuracion.setWebServiceURL(
            		xPath.compile(expression).evaluate(xmldoc));

            expression = "/configuration/connections/numClients";
            configuracion.setNumClients(Integer.parseInt(
            		xPath.compile(expression).evaluate(xmldoc)));
            
            expression = "/configuration/database/user";
            configuracion.setDbUser(
            		xPath.compile(expression).evaluate(xmldoc));
            
            expression = "/configuration/database/password";
            configuracion.setDbPassword(
            		xPath.compile(expression).evaluate(xmldoc));
            
            expression = "/configuration/database/url";
            configuracion.setDbURL(
            		xPath.compile(expression).evaluate(xmldoc));
            
            ArrayList<ServerConfiguration> jwsservers = new ArrayList<>();
            expression = "/configuration/servers/server";
            NodeList nodeList = (NodeList) xPath.compile(expression).evaluate(xmldoc, XPathConstants.NODESET);
            for (int i = 0; i < nodeList.getLength(); i++) {
                NamedNodeMap sattr = nodeList.item(i).getAttributes();
                ServerConfiguration sconf = new ServerConfiguration(
	                		sattr.getNamedItem("name").getNodeValue(),
	                		sattr.getNamedItem("wsdl").getNodeValue(),
	                		sattr.getNamedItem("namespace").getNodeValue(),
	                		sattr.getNamedItem("service").getNodeValue(),
	                		sattr.getNamedItem("httpAddress").getNodeValue()
                		);
                jwsservers.add(sconf);
            }
            configuracion.setServers(jwsservers);

            return configuracion;
    	}
    	else {
    		throw new Exception("El archivo de configuración XML no tiene el formato correcto");
    	}
    }
}
