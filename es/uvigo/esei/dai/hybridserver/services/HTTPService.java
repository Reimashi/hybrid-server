package es.uvigo.esei.dai.hybridserver.services;

import java.net.InetSocketAddress;
import java.util.logging.Level;
import java.util.logging.Logger;

import es.uvigo.esei.dai.hybridserver.Configuration;
import es.uvigo.esei.dai.hybridserver.http.HTTPServer;
import es.uvigo.esei.dai.hybridserver.httpcontrollers.*;
import es.uvigo.esei.dai.hybridserver.jws.HybridServerJwsClient;

public class HTTPService {
    private static final Logger log = Logger.getLogger(HTTPService.class.getName());
    
	private final Configuration config;
	private final DBService database;
	
	private final HybridServerJwsClient jwsclient;

	private HTTPServer httpserver;
    private Thread httpserver_th;
    
    private boolean started = false;
	
	public HTTPService (Configuration conf, DBService dbs) {
		this.config = conf;
		this.database = dbs;
		
		this.jwsclient = new HybridServerJwsClient(conf, dbs);
	}

    /**
     * Inicia el servicio HTTP
     */
	public void start() {
        if (this.httpserver_th == null || !this.httpserver_th.isAlive()) {
    		this.httpserver = new HTTPServer(this.config.getHttpPort(), this.config.getNumClients());
            this.httpserver.setServerName("HybridServer/0.2");

            // Establecemos las rutas (regexp) con su controlador asociado.
            this.httpserver.getRouter().addRoute("^/$", new IndexController(this.database, this.jwsclient));
            this.httpserver.getRouter().addRoute("^/welcome$", new IndexController(this.database, this.jwsclient));
            this.httpserver.getRouter().addRoute("^/html.*", new HtmlController(this.database, this.jwsclient));
            this.httpserver.getRouter().addRoute("^/xml.*", new XmlController(this.database, this.jwsclient));
            this.httpserver.getRouter().addRoute("^/xsd.*", new XsdController(this.database, this.jwsclient));
            this.httpserver.getRouter().addRoute("^/xslt.*", new XsltController(this.database, this.jwsclient));
            
            try {
            	if (this.httpserver.open()) {
                    this.httpserver_th = new Thread(this.httpserver);
                    this.httpserver_th.start();
                    this.started = true;
            	}
            }
            catch (Exception ex) {
                log.log(Level.SEVERE, "Error inexperado.\n{0}", ex.getMessage());
            }
        }
        else {
            log.info("El servicio HTTP ya había sido iniciado.");
        }
	}

    /**
     * Para el servicio HTTP
     */
	public void stop() {
		if (this.started && this.httpserver_th != null && this.httpserver_th.isAlive()) {
			this.httpserver_th.interrupt();
			
			if (!HTTPServer.testConnection(new InetSocketAddress("localhost", this.config.getHttpPort()))) {
	            log.log(Level.SEVERE, "No se ha podido generar la conexión de cierre del servidor.");
			}
			
            try {
                this.httpserver_th.join();
                this.httpserver.close();
            } catch (InterruptedException ex) {
                log.log(Level.SEVERE, null, ex);
            }
            
            this.httpserver_th = null;
            this.httpserver = null;
            this.started = false;
            
            // Para evitar que los test multiples fallen ya que el Sistema Operativo
            // no es capaz de eliminar el socket antes de que la siguiente instancia
            // del programa lo intente abrir de nuevo.
            try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
			}
            
            log.info("Servicio HTTP parado.");
		}
        else {
            log.info("El servicio HTTP ya estaba parado.");
        }
	}
    
    /**
     * Comprueba si el servidor esta iniciado
     * @return Estado del servidor
     */
    public boolean isStarted() {
    	return this.started;
    }
}
