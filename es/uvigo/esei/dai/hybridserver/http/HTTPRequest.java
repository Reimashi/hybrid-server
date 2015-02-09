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

import static es.uvigo.esei.dai.hybridserver.http.HTTPHeaders.CONTENT_LENGTH;
import static es.uvigo.esei.dai.hybridserver.http.HTTPHeaders.CONTENT_TYPE;

import java.io.IOException;
import java.io.Reader;
import java.util.Map;

public class HTTPRequest {
	public HTTPRequest(Reader reader) throws IOException, HTTPParseException {
	}

	public HTTPRequestMethod getMethod() {
	}

	public String getResourceChain() {
	}
	
	public String[] getResourcePath() {
	}
	
	public String getResourceName() {
	}
	
	public Map<String, String> getResourceParameters() {
	}

	public String getHttpVersion() {
	}

	public Map<String, String> getHeaderParameters() {
	}

	public String getContent() {
	}
	
	public int getContentLength() {
	}
	
	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder(this.getMethod().name())
			.append(' ').append(this.getResourceChain())
			.append(' ').append(this.getHttpVersion())
		.append('\n');
		
		for (Map.Entry<String, String> param : this.getHeaderParameters().entrySet()) {
			sb.append(param.getKey()).append(": ").append(param.getValue()).append('\n');
		}
		
		if (this.contentLength > 0) {
			sb.append('\n').append(this.getContent());
		}
		
		return sb.toString();
	}
}
