package es.uvigo.esei.dai.hybridserver.http;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Enrutador de peticiones HTTP.
 */
public class HTTPRouter {
    private static final Logger log = Logger.getLogger(HTTPRouter.class.getName());
    
    private final Map<String, HTTPRequestHandler> routes = new HashMap<>();
    
    /**
     * Añade una ruta para ser manejada por el enrutador.
     * 
     * @param path	Ruta a ser manejada.
     * @param contr Clase que manejará las peticiones hechas a dicha dirección.
     */
    public void addRoute (String path, HTTPRequestHandler contr) {
        this.routes.put(path, contr);
    }
    
    /**
     * Maneja una petición HTTP y genera una respuesta.
     * 
     * @param req	Petición HTTP a manejar.
     * @return		Respuesta HTTP generada.
     */
    public HTTPResponse handle (HTTPRequest req) {
        String link = '/' + req.getResourceName();

        try {
	        for (String route: this.routes.keySet()) {
	            if (link.matches(route)) {
	            	HTTPRequestHandler rh = this.routes.get(route);
	            	log.log(Level.INFO, "Manejando (" + req.getMethod().name() + " -> " + link + ") con: " + rh.getClass().getName());
	            	
	            	switch (req.getMethod()) {
		        		case HEAD:
		        			return rh.head(req);
		        		case GET:
		        			return rh.get(req);
		        		case POST:
		        			return rh.post(req);
		        		case PUT:
		        			return rh.put(req);
		        		case DELETE:
		        			return rh.delete(req);
		        		case TRACE:
		        			return rh.trace(req);
		        		case OPTIONS:
		        			return rh.options(req);
		        		case CONNECT:
		        			return rh.connect(req);
	            	}
	            }
	        }
        }
        catch (Exception e) {
        	log.log(Level.SEVERE, "Error al manejar una petición HTTP.", e);
        	return new HTTPResponse(HTTPResponseStatus.S500);
        }
        
        return new HTTPResponse(HTTPResponseStatus.S404);
    }
}
