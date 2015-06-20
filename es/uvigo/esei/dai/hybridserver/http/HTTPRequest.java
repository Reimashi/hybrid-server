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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;

public class HTTPRequest {
    private static final Logger log = Logger.getLogger(HTTPRequest.class.getName());
    
    private final HTTPRequestMethod rp_method;
    private final String rp_path;
    private final String rp_resource_path;
    private final String rp_httpversion;
    
    private final Map<String, String> rp_headerparameters = new LinkedHashMap<>();
    private final Map<String, String> rp_resourceparameters = new LinkedHashMap<>();
    
    private final String rp_content;
    
    private InetAddress client_address;
    
	public HTTPRequest(Reader reader) throws IOException, HTTPParseException {
        BufferedReader br = new BufferedReader(reader);
        int line = 0;

        try {
        	// Parseamos la primera linea
            String httpline = br.readLine();
            
            String[] splits = httpline.split(" ");
            if (splits.length == 3) {
                try {
                    this.rp_method = HTTPRequestMethod.fromString(splits[0]);
                }
                catch(IllegalArgumentException ex) {
                    throw new HTTPParseException("Linea " + line + 
                    		": Método HTTP incorrecto.");
                }

                this.rp_path = splits[1];

                this.rp_httpversion = splits[2];
                if (!HTTPHeaders.isHTTPVersion(this.rp_httpversion)) {
                    throw new HTTPParseException("Linea " + line + 
                    		": No es una petición HTTP.");
                }
            }
            else {
                throw new HTTPParseException("Linea " + line + 
                		": Error en la cabecera. Formato incorrecto.");
            }
            
            // Parseamos los parametros pasados por url
            if (this.rp_path.contains("?")) {
            	this.rp_resource_path = this.rp_path.substring(0, this.rp_path.indexOf("?"));
                String url = this.rp_path.substring(this.rp_path.indexOf("?"));
                
                if (url.length() > 0) {
                	String[] params = url.substring(1).split("&");
                	
                	for (String param : params) {
                		if (param.contains("=")) {
                			String[] part = param.split("=");
                			
                			if (part.length == 2) {
                				this.rp_resourceparameters.put(part[0], part[1]);
                			}
                		}
                	}
                }
            }
            else {
            	this.rp_resource_path = this.rp_path;
            }
            
            // Parseamos las tags de la cabecera
            while (!(httpline = br.readLine()).equals("")) {
            	line++;
                if ((splits = httpline.split(":")).length == 2) {
                    try {
                        HTTPHeaders header = HTTPHeaders.fromString(splits[0].trim());
                        this.rp_headerparameters.put(header.getHeader(), splits[1].trim());
                    }
                    catch(IllegalArgumentException ex) {
                        HTTPRequest.log.log(Level.WARNING, "Linea " + line + 
                        		": Ignorando cabecera desconocida <{0}>.", httpline);
                    }
                }
            }
            
            // FIXME: Quizas necesito leer salto de linea.
            
            // Parseamos el cuerpo del mensaje
            if (this.rp_headerparameters.containsKey(CONTENT_LENGTH.getHeader())) {
            	String cl = this.rp_headerparameters.get(CONTENT_LENGTH.getHeader());
            	Integer cln = 0;
            	
            	try {
                    cln = Integer.parseInt(cl);
                }
                catch (NumberFormatException ex) {
                    throw new HTTPParseException("Linea " + line + 
                    		": No se puede determinar el tamaño del cuerpo HTTP.");
                }
                
                char[] content = new char[cln];

                if (br.read(content) != cln) {
                    throw new HTTPParseException("Linea " + line + 
                    		": El tamaño del cuerpo no coincide con el " + 
                    		CONTENT_LENGTH.name() + ".");
                }

                this.rp_content = new String(content);
            }
            else {
                this.rp_content = "";
            }
            
            // Parseamos los parámetros de POST
            MIME mtype = MIME.FORM;
            boolean defaultmtype = true;
            if (this.rp_headerparameters.containsKey(CONTENT_TYPE.getHeader())) {
                String ctype = this.rp_headerparameters.get(CONTENT_TYPE.getHeader());
                
            	try {
	                MIME msype = MIME.fromString(ctype);
	                if (msype != null) {
	                	mtype = msype;
	                	defaultmtype = false;
	                }
            	}
                catch (IllegalArgumentException e) {
                    throw new HTTPParseException("Linea " + line + 
                    		": Tipo MIME desconocido " + ctype + ".");
                }
            }

            if (MIME.FORM == mtype && this.rp_content.length() > 0) {
            	if (this.rp_headerparameters.containsKey(HTTPHeaders.CONTENT_ENCODING.getHeader())) {
            		String cencoding = this.rp_headerparameters.get(HTTPHeaders.CONTENT_ENCODING.getHeader());
            		this.rp_resourceparameters.putAll(HTTPRequest.parseForm(this.rp_content, cencoding));
                } 
            	else {
                    this.rp_resourceparameters.putAll(HTTPRequest.parseForm(this.rp_content));
                }
            }
            else if (!defaultmtype) {
                throw new HTTPParseException("Linea " + line + 
                		": Error al decodificar un formulario POST.");
            }
        }
        catch (NullPointerException ex) {
            throw new HTTPParseException("Linea " + line + 
            		": Error de lectura en la petición HTTP.");
        }
	}
	
	public void setClientAddress(InetAddress addr) {
		this.client_address = addr;
	}
	
	public InetAddress getClientAddress() {
		return this.client_address;
	}

	public HTTPRequestMethod getMethod() {
        return this.rp_method;
	}

	public String getResourceChain() {
		return this.rp_path;
	}
	
	public String[] getResourcePath() {
		return this.rp_resource_path.split("/");
		// No lo usa en los test... es esto lo que debería hacer?
	}
	
	public String getResourceName() {
		if (this.rp_resource_path.contains("/")) {
			if (this.rp_resource_path.indexOf("/") == this.rp_resource_path.length()) {
				return "";
			}
			else {
				return this.rp_resource_path.substring(this.rp_resource_path.indexOf("/") + 1);
			}
		}
		else {
			return this.rp_resource_path;
		}
	}
	
	public Map<String, String> getResourceParameters() {
        return this.rp_resourceparameters;
	}

	public String getHttpVersion() {
        return this.rp_httpversion;
	}

	public Map<String, String> getHeaderParameters() {
		return this.rp_headerparameters;
	}

	public String getContent() {
        if (this.getContentLength() == 0) {
            return null;
        }
        else {
            if (this.getHeaderParameters().containsKey("Content-Type") &&
                this.getHeaderParameters().get("Content-Type").equals(MIME.FORM.getMime())) {
                
                try {
                    return URLDecoder.decode(this.rp_content, "UTF8");
                } 
                catch (UnsupportedEncodingException ex) {
                    HTTPRequest.log.log(Level.SEVERE, "Error al decodificar el formato url-encoded del formulario. Enviando en texto plano.", ex);
                    return this.rp_content;
                }
            }
            else {
                return this.rp_content;
            }
        }
	}
	
	public int getContentLength() {
        return this.rp_content.length();
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
		
		if (this.getContentLength() > 0) {
			sb.append('\n').append(this.getContent());
		}
		
		return sb.toString();
	}

	/**
	 * Analiza un body codificado en form-encoded y extrae los pares clave - valor.
	 * @param str Texto a ser analizado.
	 * @param encoding Codificación del texto.
	 * @return 
	 * @throws UnsupportedEncodingException
	 */
    public static Map<String, String> parseForm(String str, String encoding) throws UnsupportedEncodingException {
        Map<String, String> parsed = new HashMap<>();
        StringTokenizer st = new StringTokenizer(URLDecoder.decode(str, "UTF-8"), "&");

        while (st.hasMoreTokens()) {
            StringTokenizer elemst = new StringTokenizer(st.nextToken(), "=");

            String key = elemst.nextToken();

            if (elemst.hasMoreTokens()) {
                parsed.put(key, elemst.nextToken());
            }
        }

        return parsed;
    }

	/**
	 * Analiza un body codificado en form-encoded y extrae los pares clave - valor.
	 * @param str Texto a ser analizado.
	 * @return 
	 * @throws UnsupportedEncodingException
	 */
    public static Map<String, String> parseForm(String str) throws UnsupportedEncodingException {
        return HTTPRequest.parseForm(str, "UTF-8");
    }
}
