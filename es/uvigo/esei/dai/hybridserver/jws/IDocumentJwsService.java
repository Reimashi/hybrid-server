package es.uvigo.esei.dai.hybridserver.jws;

import java.util.List;
import java.util.UUID;

import javax.jws.WebMethod;
import javax.jws.WebService;

import es.uvigo.esei.dai.hybridserver.document.DocumentBean;
import es.uvigo.esei.dai.hybridserver.document.DocumentBeanInfo;
import es.uvigo.esei.dai.hybridserver.document.DocumentBeanType;

@WebService
public interface IDocumentJwsService {
	@WebMethod
	public DocumentBean get(DocumentBeanType type, UUID id);

    @WebMethod
    public boolean add(DocumentBeanType type, DocumentBean document);

    @WebMethod
    public boolean delete(DocumentBeanType type, UUID id);

    @WebMethod
    public List<DocumentBeanInfo> list(DocumentBeanType type);

    @WebMethod
    public int count(DocumentBeanType type);
}
