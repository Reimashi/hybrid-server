package es.uvigo.esei.dai.hybridserver.jws;

import java.util.List;
import java.util.UUID;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;

import es.uvigo.esei.dai.hybridserver.document.DocumentBean;
import es.uvigo.esei.dai.hybridserver.document.DocumentBeanInfo;
import es.uvigo.esei.dai.hybridserver.document.DocumentBeanType;

@WebService
public interface IDocumentJwsService {
	@WebMethod(operationName = "getDocument")
	@WebResult(name = "document")
	public DocumentBean get(@WebParam(name = "docType") DocumentBeanType type, @WebParam(name = "docId") UUID id);

	@WebMethod(operationName = "deleteDocument")
    @WebResult(name = "isDeleted")
    public boolean delete(@WebParam(name = "docType") DocumentBeanType type, @WebParam(name = "docId") UUID id);

    @WebMethod(operationName = "listDocuments")
    @WebResult(name = "listDocuments")
    public List<DocumentBeanInfo> list(@WebParam(name = "docType") DocumentBeanType type);

    @WebMethod(operationName = "countDocuments")
    @WebResult(name = "numDocuments")
    public int count(@WebParam(name = "docType") DocumentBeanType type);
}
