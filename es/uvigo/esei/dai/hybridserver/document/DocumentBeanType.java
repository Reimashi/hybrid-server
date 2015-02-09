package es.uvigo.esei.dai.hybridserver.document;

public enum DocumentBeanType {
	HTML ("HTML"),
	XML ("XML"),
	XSD ("XSD"),
	XSLT ("XSLT");
	
	private final String value;
	
	private DocumentBeanType (String str) {
		this.value = str;
	}
	
	public String getName() {
		return this.value;
	}
}
