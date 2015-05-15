package es.uvigo.esei.dai.hybridserver.http;

public abstract class HTTPRequestHandler {
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
