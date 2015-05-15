package es.uvigo.esei.dai.hybridserver.http;

import es.uvigo.esei.dai.hybridserver.services.DBService;

public abstract class HTTPRequestHandler {
	protected DBService db;
	
	@SuppressWarnings("unused")
	private HTTPRequestHandler() {}
	
	public HTTPRequestHandler(DBService db) {
		this.db = db;
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
