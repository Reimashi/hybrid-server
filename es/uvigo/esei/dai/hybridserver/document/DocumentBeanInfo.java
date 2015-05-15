package es.uvigo.esei.dai.hybridserver.document;

import java.io.Serializable;
import java.util.UUID;

public class DocumentBeanInfo implements Serializable {
	private static final long serialVersionUID = -5225478590252872738L;
	
	private DocumentBeanType type;
	
	public DocumentBeanType getType() {
		return this.type;
	}
	
	public void setType(DocumentBeanType t) {
		this.type = t;
	}
	
	private UUID id;
	
	public UUID getID() {
		return this.id;
	}
	
	public void setID(UUID id) {
		this.id = id;
	}
	
	private UUID xsd;
	
	public UUID getXsd() {
		return this.xsd;
	}
	
	public void setXsd(UUID xsd) {
		this.xsd = xsd;
	}
}
