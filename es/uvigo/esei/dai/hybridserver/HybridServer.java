package es.uvigo.esei.dai.hybridserver;

import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import es.uvigo.esei.dai.hybridserver.services.DBService;
import es.uvigo.esei.dai.hybridserver.services.HTTPService;
import es.uvigo.esei.dai.hybridserver.services.JWSService;

public class HybridServer {
    private static final Logger log = Logger.getLogger(HybridServer.class.getName());
    
	private final Configuration config;

	private final DBService dbServer;
    private final HTTPService htmlServer;
    private final JWSService jwsServer;
	
	public HybridServer (Configuration conf) {
		this.config = conf;
		
		this.dbServer = new DBService(this.config);
		this.htmlServer = new HTTPService(this.config, this.dbServer);
		this.jwsServer = new JWSService(this.config, this.dbServer);
	}

    /**
     * Inicia el servidor HybridServer
     */
	public void start() {
		try {
			this.dbServer.start();
			this.htmlServer.start();
			this.jwsServer.start();
		}
		catch (SQLException e) {
			log.log(Level.SEVERE, "Error, no se ha podido conectar a la base de datos.", e);
		}
	}

    /**
     * Para el servidor HybridServer
     */
	public void stop() {
		try {
			this.dbServer.stop();
			this.htmlServer.stop();
			this.jwsServer.stop();
		}
		catch (SQLException e) {
			log.log(Level.SEVERE, "Error, no se ha podido cerrar la conexión con la base de datos.", e);
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
