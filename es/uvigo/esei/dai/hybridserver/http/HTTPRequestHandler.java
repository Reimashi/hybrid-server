package es.uvigo.esei.dai.hybridserver.http;

import es.uvigo.esei.dai.hybridserver.jws.HybridServerJwsClient;
import es.uvigo.esei.dai.hybridserver.services.DBService;

public abstract class HTTPRequestHandler {
	protected DBService db;
	protected HybridServerJwsClient jwsclient;
	
	@SuppressWarnings("unused")
	private HTTPRequestHandler() {}
	
	public HTTPRequestHandler(DBService db, HybridServerJwsClient jws) {
		this.db = db;
		this.jwsclient = jws;
	}
	
	public HTTPResponse head(HTTPRequest req) {
		return new HTTPResponse(HTTPResponseStatus.S501);
	}

	public HTTPResponse get(HTTPRequest req) {
		return new HTTPResponse(HTTPResponseStatus.S501);
	}

	public HTTPResponse post(HTTPRequest req) {
		return new HTTPResponse(HTTPResponseStatus.S501);
	}

	public HTTPResponse put(HTTPRequest req) {
		return new HTTPResponse(HTTPResponseStatus.S501);
	}

	public HTTPResponse delete(HTTPRequest req) {
		return new HTTPResponse(HTTPResponseStatus.S501);
	}

	public HTTPResponse trace(HTTPRequest req) {
		return new HTTPResponse(HTTPResponseStatus.S501);
	}

	public HTTPResponse options(HTTPRequest req) {
		return new HTTPResponse(HTTPResponseStatus.S501);
	}

	public HTTPResponse connect(HTTPRequest req) {
		return new HTTPResponse(HTTPResponseStatus.S501);
	}
}
