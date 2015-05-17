package es.uvigo.esei.dai.hybridserver.jws;

import es.uvigo.esei.dai.hybridserver.Configuration;
import es.uvigo.esei.dai.hybridserver.HybridServerService;
import es.uvigo.esei.dai.hybridserver.ServerConfiguration;
import es.uvigo.esei.dai.hybridserver.document.DocumentBean;
import es.uvigo.esei.dai.hybridserver.document.DocumentBeanInfo;
import es.uvigo.esei.dai.hybridserver.document.DocumentBeanType;

import javax.xml.namespace.QName;
import javax.xml.ws.Service;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class HybridServerJwsClient implements IDocumentJwsService {
    private Configuration configuration;

    public HybridServerJwsClient(Configuration conf) {
        this.configuration = conf;
    }

    private List<HybridServerService> getServices() {
        ArrayList<HybridServerService> services = new ArrayList<>();

        for (ServerConfiguration co : this.configuration.getServers()) {
            try {
                QName name = new QName(co.getNamespace(), co.getService());
                Service service = Service.create(new URL(co.getHttpAddress()), name);
                HybridServerService serv = service.getPort(HybridServerService.class);
                services.add(serv);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }

        return services;
    }

	@Override
	public DocumentBean get(DocumentBeanType type, UUID id) {
		for (HybridServerService service : this.getServices()) {
			DocumentBean document = service.get(type, id);
			if (document != null) { return document; }
		}

		return null;
	}

	@Override
	public boolean delete(DocumentBeanType type, UUID id) {
		for (HybridServerService service : this.getServices()) {
			service.delete(type, id);
		}
		
		return true;
	}

	@Override
	public List<DocumentBeanInfo> list(DocumentBeanType type) {
		List<UUID> listids = new ArrayList<>();
		List<DocumentBeanInfo> documents = new ArrayList<>();
		
		for (HybridServerService service : this.getServices()) {
			for (DocumentBeanInfo doc : service.list(type)) {
				if (!listids.contains(doc.getID())) {
					listids.add(doc.getID());
					documents.add(doc);
				}
			}
		}
		
		return documents;
	}

	@Override
	public int count(DocumentBeanType type) {
		return this.list(type).size();
	}
}
