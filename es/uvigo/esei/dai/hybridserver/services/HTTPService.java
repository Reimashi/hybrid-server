package es.uvigo.esei.dai.hybridserver.services;

import es.uvigo.esei.dai.hybridserver.Configuration;

public class HTTPService {
	private final Configuration config;
	private final DBService database;
	
	public HTTPService (Configuration conf) {
		this.config = conf;
		this.database = new DBService(this.config);
	}

    /**
     * Inicia el servicio HTTP
     */
	public void start() {
		
	}

    /**
     * Para el servicio HTTP
     */
	public void stop() {
		
	}
}
