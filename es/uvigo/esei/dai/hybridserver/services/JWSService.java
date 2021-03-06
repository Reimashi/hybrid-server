package es.uvigo.esei.dai.hybridserver.services;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.ws.Endpoint;

import es.uvigo.esei.dai.hybridserver.Configuration;
import es.uvigo.esei.dai.hybridserver.HybridServerService;

/**
 * Servicio Java Web Service para HybridServer
 * @author aitor
 *
 */
public class JWSService {
    private static Logger log = Logger.getLogger(JWSService.class.toString());

    private final Configuration config;
    private final DBService database;

    private Endpoint htmlep;
    
    public final String url;

    public JWSService(Configuration conf, DBService db) {
        this.config = conf;
        this.database = db;
        this.url = this.config.getWebServiceURL() != null ? this.config.getWebServiceURL() : "";
    }

    /**
     * Inicia el servicio JWS
     */
    public void start() {
    	if (this.htmlep == null && this.url.length() > 0) {
    		this.htmlep = Endpoint.publish(
    				this.url, 
    				new HybridServerService(this.database)
    			);
    		log.log(Level.INFO, "Servidor JWS iniciado. <" + this.url + ">");
    	}
    }

    /**
     * Para el servicio JWS
     */
    public void stop() {
    	if (this.htmlep != null) {
	        try {
	            this.htmlep.stop();
	            this.htmlep = null;
	            
	            log.info("Servicio JWS parado.");
	        }
	        catch (NullPointerException e) {
	            log.log(Level.WARNING, "No se ha podido parar el JWS.");
	        }
    	}
    }
	
	/**
	 * Obtiene la configuración del servidor.
	 * 
	 * @return Objeto de configuración.
	 */
	public Configuration getConfiguration() {
		return this.config;
	}
}
