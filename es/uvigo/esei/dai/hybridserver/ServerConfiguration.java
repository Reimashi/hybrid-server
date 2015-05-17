package es.uvigo.esei.dai.hybridserver;

public class ServerConfiguration {
    private String LName;
    private String LWsdl;
    private String LNamespace;
    private String LService;
    private String LHttpAddress;

    public ServerConfiguration (String name, String wsdl, String namespace, String service, String httpAddress) {
        this.LName = name;
        this.LWsdl = wsdl;
        this.LNamespace = namespace;
        this.LService = service;
        this.LHttpAddress = httpAddress;
    }
    
    public String getName() {
        return this.LName;
    }
    
    public String getWsdl() {
        return this.LWsdl;
    }
    
    public String getNamespace() {
        return this.LNamespace;
    }
    
    public String getService() {
        return this.LService;
    }
    
    public String getHttpAddress() {
        return this.LHttpAddress;
    }
    
    @Override
    public String toString() {
    	StringBuilder sb = new StringBuilder();

		sb.append("Name: " + this.getName());
		sb.append(" | Wsdl: " + this.getWsdl());
		sb.append(" | Namespace: " + this.getNamespace());
		sb.append(" | Service: " + this.getService());
		sb.append(" | HttpAddress: " + this.getHttpAddress());
    	
    	return sb.toString();
    }
}