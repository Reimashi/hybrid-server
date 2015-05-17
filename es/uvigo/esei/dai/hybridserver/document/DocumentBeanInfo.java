package es.uvigo.esei.dai.hybridserver.document;

import java.io.Serializable;
import java.util.UUID;

import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlAccessType;

@XmlRootElement(name = "documentBeanInfo")
@XmlAccessorType(XmlAccessType.NONE)
@XmlType(name = "documentInfoType", 
	namespace="es.uvigo.esei.dai.hybridserver",
	propOrder = {
        "id",
        "type",
        "xsd"
})
public class DocumentBeanInfo implements Serializable {
	private static final long serialVersionUID = -5225478590252872738L;

    @XmlElement(name = "type", required = true)
	private DocumentBeanType type;
	
	public DocumentBeanType getType() {
		return this.type;
	}
	
	public void setType(DocumentBeanType t) {
		this.type = t;
	}

    @XmlElement(name = "id", required = true)
	private UUID id;
	
	public UUID getID() {
		return this.id;
	}
	
	public void setID(UUID id) {
		this.id = id;
	}

    @XmlElement(name = "info", required = false)
	private UUID xsd;
	
	public UUID getXsd() {
		return this.xsd;
	}
	
	public void setXsd(UUID xsd) {
		this.xsd = xsd;
	}
}
