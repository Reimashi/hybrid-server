package es.uvigo.esei.dai.hybridserver.jws;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.jws.WebMethod;
import javax.jws.WebService;

import es.uvigo.esei.dai.hybridserver.dao.DocumentDAO;
import es.uvigo.esei.dai.hybridserver.document.DocumentBean;
import es.uvigo.esei.dai.hybridserver.document.DocumentBeanInfo;
import es.uvigo.esei.dai.hybridserver.document.DocumentBeanType;
import es.uvigo.esei.dai.hybridserver.services.DBService;

@WebService
public class HybridServerJwsService implements IDocumentJwsService {
	private final DocumentDAO dao;
	
	public HybridServerJwsService (DBService db) {
		this.dao = new DocumentDAO(db);
	}

    @WebMethod
	@Override
	public DocumentBean get(DocumentBeanType type, UUID id) {
    	try {
    		return this.dao.get(type, id);
    	}
    	catch (SQLException e) {
    		return null;
    	}
	}

    @WebMethod
	@Override
	public boolean delete(DocumentBeanType type, UUID id) {
    	try {
    		return this.dao.delete(type, id);
    	}
    	catch (SQLException e) {
    		return false;
    	}
	}

    @WebMethod
	@Override
	public List<DocumentBeanInfo> list(DocumentBeanType type) {
    	try {
    		return this.dao.getList(type);
    	}
    	catch (SQLException e) {
    		return new ArrayList<DocumentBeanInfo>();
    	}
	}

    @WebMethod
	@Override
	public int count(DocumentBeanType type) {
    	try {
    		return this.dao.length(type);
    	}
    	catch (SQLException e) {
    		return 0;
    	}
	}
}
