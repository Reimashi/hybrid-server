package es.uvigo.esei.dai.hybridserver.http;

public abstract class HTTPRequestHandler {
	HTTPResponse head(HTTPRequest req) {
		return new HTTPResponse(HTTPResponseStatus.S501);
	}

	HTTPResponse get(HTTPRequest req) {
		return new HTTPResponse(HTTPResponseStatus.S501);
	}

	HTTPResponse post(HTTPRequest req) {
		return new HTTPResponse(HTTPResponseStatus.S501);
	}

	HTTPResponse put(HTTPRequest req) {
		return new HTTPResponse(HTTPResponseStatus.S501);
	}

	HTTPResponse delete(HTTPRequest req) {
		return new HTTPResponse(HTTPResponseStatus.S501);
	}

	HTTPResponse trace(HTTPRequest req) {
		return new HTTPResponse(HTTPResponseStatus.S501);
	}

	HTTPResponse options(HTTPRequest req) {
		return new HTTPResponse(HTTPResponseStatus.S501);
	}

	HTTPResponse connect(HTTPRequest req) {
		return new HTTPResponse(HTTPResponseStatus.S501);
	}
}
