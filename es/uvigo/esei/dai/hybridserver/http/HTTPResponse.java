package es.uvigo.esei.dai.hybridserver.http;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;

public class HTTPResponse {
	private static final Logger log = Logger.getLogger(HTTPResponse.class.getName());
    
    private static final String EOL = "\n";
    
    private HTTPResponseStatus status;
    private String version;
    private String content;
    private final Map<String, String> parameters = new LinkedHashMap<>();

	public HTTPResponse() {
		this(HTTPResponseStatus.S500);
	}
	
	public HTTPResponse(HTTPResponseStatus stat) {
		this.content = "";
		this.version = "HTML/1.1";
		this.status = stat;
	}
	
	public HTTPResponseStatus getStatus() {
		return this.status;
	}

	public void setStatus(HTTPResponseStatus status) {
		this.status = status;
	}

	public String getVersion() {
		return this.version;
	}

	public void setVersion(String version) {
		if (HTTPHeaders.isHTTPVersion(version)) {
			this.version = version.toUpperCase();
		}
		else {
			log.log(Level.WARNING, "Version string invalid. Has been ignored.");
		}
	}

	public String getContent() {
		return this.content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Map<String, String> getParameters() {
		return this.parameters;
	}
	
	public String putParameter(String name, String value) {
        return this.parameters.put(name, value);
	}
	
	public boolean containsParameter(String name) {
        return this.parameters.containsKey(name);
	}
	
	public String removeParameter(String name) {
        if (this.parameters.containsKey(name)) {
        	String value = this.parameters.get(name);
        	this.parameters.remove(name);
            return value;
        }
        else {
            return "";
        }
	}
	
	public void clearParameters() {
		this.parameters.clear();
	}
	
	public List<String> listParameters() {
        List<String> keys = new ArrayList<>();
        keys.addAll(this.getParameters().keySet());
        return keys;
	}

	public void print(Writer writer) throws IOException {
        writer.write(this.getVersion() + " " + 
        		this.getStatus().getCode() + " " + 
        		this.getStatus().getStatus());
        
        for (Entry<String, String> element: this.getParameters().entrySet()) {
            writer.write(element.getKey() + ": " + element.getValue() + HTTPResponse.EOL);
        }
        
        writer.write(HTTPResponse.EOL);
        
        if (this.getContent().length() > 0) {
            writer.write(this.getContent());
        }
        
        writer.flush();
	}
	
	@Override
	public String toString() {
		final StringWriter writer = new StringWriter();
		
		try {
			this.print(writer);
		} catch (IOException e) {}
		
		return writer.toString();
	}
}
