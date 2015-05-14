package es.uvigo.esei.dai.hybridserver.services;

import java.net.InetSocketAddress;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import es.uvigo.esei.dai.hybridserver.Configuration;
import es.uvigo.esei.dai.hybridserver.HybridServer;
import es.uvigo.esei.dai.hybridserver.http.HTTPServer;
import es.uvigo.esei.dai.hybridserver.httpcontrollers.*;

public class HTTPService {
    private static final Logger log = Logger.getLogger(HTTPService.class.getName());
    
	private final Configuration config;
	private final DBService database;

	private HTTPServer httpserver;
    private Thread httpserver_th;
	
	public HTTPService (Configuration conf, DBService dbs) {
		this.config = conf;
		this.database = dbs;
		
		this.httpserver = new HTTPServer(conf.getHttpPort(), conf.getNumClients());
        this.httpserver.setServerName("HybridServer/0.2");

        // Establecemos las rutas (regexp) con su controlador asociado.
        this.httpserver.getRouter().addRoute("^/$", new IndexController());
        this.httpserver.getRouter().addRoute("^/welcome$", new IndexController());
        this.httpserver.getRouter().addRoute("^/html.*", new HtmlController());
        this.httpserver.getRouter().addRoute("^/xml.*", new XmlController());
        this.httpserver.getRouter().addRoute("^/xsd.*", new XsdController());
        this.httpserver.getRouter().addRoute("^/xslt.*", new XsltController());
	}

    /**
     * Inicia el servicio HTTP
     */
	public void start() {
        if (this.httpserver_th == null || !this.httpserver_th.isAlive()) {
            try {
                this.httpserver_th = new Thread(this.httpserver);
                this.httpserver_th.start();
            }
            catch (Exception ex) {
                log.log(Level.SEVERE, "Error inexperado.\n{0}", ex.getMessage());
            }
        }
        else {
            log.info("El servidor ya había sido iniciado.");
        }
	}

    /**
     * Para el servicio HTTP
     */
	public void stop() {
		if (this.httpserver_th != null && this.httpserver_th.isAlive()) {
			this.httpserver_th.interrupt();
			
			if (!HTTPServer.testConnection(new InetSocketAddress("localhost", this.config.getHttpPort()))) {
	            log.log(Level.SEVERE, "No se ha podido generar la conexión de cierre del servidor.");
			}
			
            try {
                this.httpserver_th.join();
            } catch (InterruptedException ex) {
                log.log(Level.SEVERE, null, ex);
            }
            
            this.httpserver_th = null;
		}
        else {
            log.info("El servidor ya estaba parado.");
        }
	}
}
