/**
 *  HybridServer
 *  Copyright (C) 2014 Miguel Reboiro-Jato
 *  
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *  
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *  
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package es.uvigo.esei.dai.hybridserver.http;

public enum HTTPHeaders {
    ACCEPT_LANGUAGE("Accept-Language"),
    ACCEPT_ENCODING("Accept-Encoding"),
    CONTENT_LENGTH("Content-Length"),
    CACHE_CONTROL("Cache-Control"),
    CONTENT_ENCODING("Content-Encoding"),
    CONTENT_TYPE("Content-Type"),
    USER_AGENT("User-Agent"),
    X_REQUESTED_WITH("X-Requested-With"),
    CONNECTION("Connection"),
    ACCEPT("Accept"),
    HOST("Host"),
    PRAGMA("Pragma"),
    DNT("DNT"),                                 // Do not track
    HTTP_1_1("HTTP/1.1"),
    SERVER("Server");
	
	private String header;
	
	private HTTPHeaders(String header) {
		this.header = header;
	}
	
	public String getHeader() {
		return header;
	}
	
	public boolean equals (HTTPHeaders obj) {
		return obj.toString().toLowerCase() == this.header.toLowerCase();
	}
	
	public static HTTPHeaders fromString(String text) throws IllegalArgumentException {
        if (text != null) {
            for (HTTPHeaders b : HTTPHeaders.values()) {
                if (text.equalsIgnoreCase(b.getHeader())) {
                    return b;
                }
            }
        }
        
        throw new IllegalArgumentException();
    }
	
	public static boolean isHTTPVersion(String str) {
		return str.matches("HTTP/[0-2]?\\.[0-9]?");
	}
}
