package es.uvigo.esei.dai.hybridserver.jws;

import es.uvigo.esei.dai.hybridserver.Configuration;
import es.uvigo.esei.dai.hybridserver.HybridServerService;
import es.uvigo.esei.dai.hybridserver.ServerConfiguration;
import es.uvigo.esei.dai.hybridserver.document.DocumentBean;
import es.uvigo.esei.dai.hybridserver.document.DocumentBeanInfo;
import es.uvigo.esei.dai.hybridserver.document.DocumentBeanType;

import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import javax.xml.ws.WebServiceException;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;

public class HybridServerJwsClient {
    private static final Logger log = Logger.getLogger(HybridServerJwsClient.class.getName());
    
    private Configuration configuration;

    public HybridServerJwsClient(Configuration conf) {
        this.configuration = conf;
    }

    public Map<String, HybridServerService> getServices() {
        
    	Map<String, HybridServerService> services = new HashMap<>();

        for (ServerConfiguration co : this.configuration.getServers()) {
            try {
                QName name = new QName(co.getNamespace(), co.getService());
                Service service = Service.create(new URL(co.getHttpAddress()), name);
                
                HybridServerService serv = service.getPort(HybridServerService.class);
                
                services.put(co.getName(), serv);
            } catch (MalformedURLException e) {
    			log.log(Level.WARNING, "La dirección de un servicio web no es valida. {0}", e.getMessage());
            }
            catch (WebServiceException e) {
    			log.log(Level.WARNING, "Error al conectar a un servicio web. {0}", e.getMessage());
            }
        }
        
        return services;
    }

	public Entry<String, DocumentBean> get(DocumentBeanType type, UUID id) {
    	Map<String, HybridServerService> services = this.getServices();
		for (String servername : services.keySet()) {
			HybridServerService service = services.get(servername);
			DocumentBean document = service.get(type, id);

			if (document != null) { 
				return new AbstractMap.SimpleEntry<>(servername, document);
			}
		}

		return null;
	}

	public Map<String, Boolean> delete(DocumentBeanType type, UUID id) {
    	Map<String, HybridServerService> services = this.getServices();
		Map<String, Boolean> toret = new HashMap<String,Boolean>();

		for (String servername : services.keySet()) {
			HybridServerService service = services.get(servername);
			toret.put(servername, service.delete(type, id));
		}
		
		return toret;
	}

	public Map<String, List<DocumentBeanInfo>> list(DocumentBeanType type) {
    	Map<String, HybridServerService> services = this.getServices();
		Map<String, List<DocumentBeanInfo>> toret = new HashMap<>();

		for (String servername : services.keySet()) {
			HybridServerService service = services.get(servername);
			toret.put(servername, service.list(type));
		}
		
		return toret;
	}

	public Map<String, Integer> count(DocumentBeanType type) {
    	Map<String, HybridServerService> services = this.getServices();
		Map<String, Integer> toret = new HashMap<>();

		for (String servername : services.keySet()) {
			HybridServerService service = services.get(servername);
			toret.put(servername, service.count(type));
		}
		
		return toret;
	}

    public int countTotal(DocumentBeanType type) {
		return uniqueList(this.list(type)).size();
	}
	
	public static List<DocumentBeanInfo> uniqueList(Map<String, List<DocumentBeanInfo>> list) {
		List<UUID> ids = new ArrayList<>();
		List<DocumentBeanInfo> documents = new ArrayList<>();
		
		for (List<DocumentBeanInfo> ds : list.values()) {
			for (DocumentBeanInfo bi : ds) {
				if (!ids.contains(bi.getID())) {
					documents.add(bi);
				}
			}
		}
		
		return documents;
	}
}
