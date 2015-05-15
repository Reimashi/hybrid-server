package es.uvigo.esei.dai.hybridserver.http;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.ServerSocket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

public class HTTPServer implements Runnable {
    private static final Logger log = Logger.getLogger(HTTPServer.class.getName());
    
    protected String server_name = "Java/HTTPServer";

    protected final int serverPort;
    protected final int maxclients;
    protected final HTTPRouter router;
    
    private ServerSocket serverSocket;
    
    public HTTPServer (int port, int maxclients) {
        this.serverPort = port;
        this.maxclients = maxclients;
        this.router = new HTTPRouter();
    }
    
    public boolean open() {
    	try {
    		this.serverSocket = new ServerSocket(this.serverPort);
            log.log(Level.INFO, "Servidor HTTP iniciado. <http://localhost:" + this.serverPort + ">");
            return true;
    	}
        catch (IOException e){
            log.log(Level.SEVERE, "Error al iniciar el servidor HTTP. El puerto " + this.serverPort + " ya se encuentra en uso.");
        	return false;
        }
    }
    
    @Override
    public void run() {
        ExecutorService ex = Executors.newFixedThreadPool(this.maxclients);
        
        while (!Thread.currentThread().isInterrupted()) {
            
            try {
                ex.execute(new HTTPHandler(serverSocket.accept(), this));
            } 
            catch (IOException e) {
                log.log(Level.SEVERE, "Error al aceptar una conexión de un cliente.", e);
            }
        }
        
        ex.shutdown();
    }
    
    public void close() {
    	if (this.serverSocket != null) {
    		try {
    	    	this.serverSocket.close();
    	    	this.serverSocket = null;
    		}
    		catch(IOException e) {}
    	}
    }
    
    public String getServerName() {
        return this.server_name;
    }
    
    public void setServerName(String name) {
        if (name != null) {
            this.server_name = name.trim();
        }
    }
    
    public HTTPRouter getRouter() {
        return this.router;
    }
    
    public static boolean testConnection(InetSocketAddress addr) {
    	try {
            Socket exitsock = new Socket();
            exitsock.connect(addr);
            exitsock.close();
            return true;
        } catch (IOException e) {
        	return false;
        }
    }
}