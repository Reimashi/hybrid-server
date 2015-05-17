package es.uvigo.esei.dai.hybridserver.services;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import es.uvigo.esei.dai.hybridserver.Configuration;

public class DBService {
    private static final Logger log = Logger.getLogger(DBService.class.getName());

	private final Configuration config;
	
	private Connection connection;

    public DBService(Configuration conf) {
    	this.config = conf;
    }

    /**
     * Inicia el servicio Database
     */    
    public void start() throws SQLException {
    	if (this.connection == null) {
	    	this.connection = DriverManager.getConnection(
	    			this.config.getDbURL(), 
	    			this.config.getDbUser(), 
	    			this.config.getDbPassword()
	    		);
    		log.log(Level.INFO, "Cliente DB iniciado. <" + this.config.getDbURL() + ">");
    	}
    }

    /**
     * Inicia el servicio Database
     */
    public void stop() throws SQLException {
    	if (this.connection != null) {
	    	this.connection.close();
	    	this.connection = null;
    		log.log(Level.INFO, "Cliente DB parado.");
    	}
    }

	/**
	 * Obtiene la configuración del servidor.
	 * 
	 * @return Objeto de configuración.
	 */
    public Connection getConnection() {
    	return this.connection;
    }
}
