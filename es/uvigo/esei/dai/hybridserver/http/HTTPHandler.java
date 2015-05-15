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

        try (BufferedReader in = new BufferedReader(new InputStreamReader(this.clientSocket.getInputStream())))
    	{
            // Manejamos la petición
            try {
                req = new HTTPRequest(in);
                req.setClientAddress(this.clientSocket.getInetAddress());

            	res = this.server.getRouter().handle(req);
            }
            catch (HTTPParseException ex) {
            	log.log(Level.INFO, "Bad request from " + this.clientSocket.getInetAddress().toString());
            	res = new HTTPResponse(HTTPResponseStatus.S400);
            }
        	
            // Añadimos información extra a la respuesta
            res.putParameter(HTTPHeaders.SERVER.getHeader(), this.server.getServerName());
            
            try (PrintWriter out = new PrintWriter(this.clientSocket.getOutputStream(), true)) {
                // Escribimos la respuesta
            	res.print(out);
            }
            catch (IOException ex) {
                HTTPHandler.log.log(Level.SEVERE, "<{0}> Error con la conexi\u00f3n al responder a la petici\u00f3n. {1}", 
                        new Object[]{this.clientSocket.getInetAddress().getHostAddress(), ex.getMessage()});
            }
            
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
