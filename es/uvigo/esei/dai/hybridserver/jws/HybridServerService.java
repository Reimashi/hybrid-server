package es.uvigo.esei.dai.hybridserver.jws;

import java.util.List;
import java.util.UUID;

import es.uvigo.esei.dai.hybridserver.dao.DocumentDAO;
import es.uvigo.esei.dai.hybridserver.document.DocumentBean;
import es.uvigo.esei.dai.hybridserver.document.DocumentBeanInfo;
import es.uvigo.esei.dai.hybridserver.document.DocumentBeanType;
import es.uvigo.esei.dai.hybridserver.services.DBService;

public class HybridServerService implements IDocumentJwsService {
	private final DocumentDAO dao;
	
	public HybridServerService (DBService db) {
		this.dao = new DocumentDAO(db);
	}

	@Override
	public DocumentBean get(DocumentBeanType type, UUID id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean add(DocumentBeanType type, DocumentBean document) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean delete(DocumentBeanType type, UUID id) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public List<DocumentBeanInfo> list(DocumentBeanType type) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int count(DocumentBeanType type) {
		// TODO Auto-generated method stub
		return 0;
	}
}
