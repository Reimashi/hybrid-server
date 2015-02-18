package es.uvigo.esei.dai.hybridserver.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class HTTPHandler implements Runnable {
    private static final Logger log = Logger.getLogger(HTTPHandler.class.getName());
    
    private final Socket clientSocket;
    private final HTTPServer server;
    
    public HTTPHandler(Socket client, HTTPServer from) {
        this.clientSocket = client;
        this.server = from;
    }

    @Override
    public void run() {
    	HTTPRequest req;
    	HTTPResponse res;

        try (BufferedReader in = new BufferedReader(new InputStreamReader(this.clientSocket.getInputStream()));
            	PrintWriter out = new PrintWriter(this.clientSocket.getOutputStream(), true))
    	{
            // Manejamos la petición
            try {
                req = new HTTPRequest(in);
                
                res = this.server.getRouter().handle(req);
            }
            catch (HTTPParseException ex) {
            	res = new HTTPResponse(HTTPResponseStatus.S400);
            }
        	
            // Añadimos información extra a la respuesta
            res.putParameter(HTTPHeaders.SERVER.getHeader(), this.server.getServerName());
            
            // Escribimos la respuesta
        	res.print(out);
        	
        	// Cerramos la conexión
        	in.close();
        	out.close();
        }
        catch (IOException ex) {
            HTTPHandler.log.log(Level.SEVERE, "<{0}> Error con la conexi\u00f3n. {1}", 
                    new Object[]{this.clientSocket.getInetAddress().getHostAddress(), ex.getMessage()});
        }
        finally {
            try {
                this.clientSocket.close();
            } catch (IOException ex) {
                HTTPHandler.log.log(Level.SEVERE, ex.getMessage());
            }
        }

    }
}
