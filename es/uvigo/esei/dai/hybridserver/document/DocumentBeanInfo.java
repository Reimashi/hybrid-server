package es.uvigo.esei.dai.hybridserver.document;

import java.util.UUID;

public class DocumentBeanInfo {
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
