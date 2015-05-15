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

public enum MIME {
	// Generic
    BINARY("application/octet-stream"),
    
    // HTTP
    FORM("application/x-www-form-urlencoded"),

    // Documents
    HTML("text/html"),
    CSS("text/css"),
    JAVASCRIPT("text/javascript"),
    XML("application/xml"),
    XSL("text/xsl"),
    XSLT("application/xslt+xml"),
    PDF("application/pdf"),
    TEXT("text/plain"),

    // Images
    BMP("image/x-windows-bmp"),
    ICO("image/x-icon"),
    JPEG("image/jpeg"),
    PNG("image/png"),
    GIF("image/gif");

	private String mime;
	
	private MIME(String mime) {
		this.mime = mime;
	}
	
	public String getMime() {
		return mime;
	}
	
	public boolean equals(MIME m) {
		return m.getMime().toLowerCase() == this.mime.toLowerCase();
	}
	
	public boolean equals(String str) {
		return str.toLowerCase() == this.mime.toLowerCase();
	}
	
	public static MIME fromString(String text) throws IllegalArgumentException {
        if (text != null) {
            for (MIME b : MIME.values()) {
                if (text.equalsIgnoreCase(b.getMime())) {
                    return b;
                }
            }
        }
        
        throw new IllegalArgumentException();
    }
}