package es.uvigo.esei.dai.hybridserver;

import java.sql.SQLException;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import es.uvigo.esei.dai.hybridserver.dao.DocumentDAO;
import es.uvigo.esei.dai.hybridserver.document.DocumentBean;
import es.uvigo.esei.dai.hybridserver.document.DocumentBeanInfo;
import es.uvigo.esei.dai.hybridserver.document.DocumentBeanType;
import es.uvigo.esei.dai.hybridserver.services.DBService;
import es.uvigo.esei.dai.hybridserver.services.HTTPService;
import es.uvigo.esei.dai.hybridserver.services.JWSService;

public class HybridServer {
    private static final Logger log = Logger.getLogger(HybridServer.class.getName());
    
	private final Configuration config;

	private final DBService dbServer;
    private final HTTPService htmlServer;
    private final JWSService jwsServer;
    
    private boolean started = false;
	
	public HybridServer () {
		this(new Configuration());
	}
	
	public HybridServer (Properties conf) {
		this(Configuration.fromProperties(conf));
	}
	
	/**
	 * Necesario para test de week2
	 * @param pages
	 */
	public HybridServer (Map<String, String> pages) {
		this(new Configuration());

		try {
			this.dbServer.start();
		}
		catch (SQLException e) {
			log.log(Level.SEVERE, "Error al conectar a la base de datos. {0}", e.getMessage());
		}
		
		DocumentDAO dao = new DocumentDAO(this.dbServer);
		
		for (String id : pages.keySet()) {
			DocumentBeanInfo docinfo = new DocumentBeanInfo();
			docinfo.setID(UUID.fromString(id));
			docinfo.setType(DocumentBeanType.HTML);
			
			DocumentBean doc = new DocumentBean();
			doc.setInfo(docinfo);
			doc.setContent(pages.get(id));
			
			try {
				dao.add(DocumentBeanType.HTML, doc);
			} catch (SQLException e) {
				log.log(Level.SEVERE, "Error al insertar pagina de ejemplo ({0}). {1}", new Object[] { id, e.getMessage() });
			}
		}
		
		try {
			this.dbServer.stop();
		}
		catch (SQLException e) {
			log.log(Level.SEVERE, "Error, no se ha podido cerrar la conexión con la base de datos.", e);
		}
	}
		
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
			this.jwsServer.start();
			this.htmlServer.start();
			this.started = true;
		}
		catch (SQLException e) {
			log.log(Level.SEVERE, "Error al conectar a la base de datos. {0}", e.getMessage());
		}
	}

    /**
     * Para el servidor HybridServer
     */
	public void stop() {
		try {
			this.dbServer.stop();
			this.jwsServer.stop();
			this.htmlServer.stop();
			this.started = false;
		}
		catch (SQLException e) {
			log.log(Level.SEVERE, "Error, no se ha podido cerrar la conexión con la base de datos.", e);
		}
	}
    
    /**
     * Comprueba si el servidor esta iniciado
     * @return Estado del servidor
     */
    public boolean isStarted() {
    	return this.started && this.htmlServer.isStarted();
    }
	
	/**
	 * Obtiene la configuración del servidor.
	 * 
	 * @return Objeto de configuración.
	 */
	public Configuration getConfiguration() {
		return this.config;
	}
    
    /**
     * Obtiene el puerto HTTP del servidor.
     * 
     * @return Número de puerto.
     */
    public int getPort() {
    	return this.config.getHttpPort();
    }
}
