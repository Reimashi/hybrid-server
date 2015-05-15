package es.uvigo.esei.dai.hybridserver.http;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.ServerSocket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

import es.uvigo.esei.dai.hybridserver.HybridServer;

public class HTTPServer implements Runnable {
    private static final Logger log = Logger.getLogger(HTTPServer.class.getName());
    
    protected String server_name = "Java/HTTPServer";

    protected final int serverPort;
    protected final int maxclients;
    protected final HTTPRouter router;
    
    public HTTPServer (int port, int maxclients) {
        this.serverPort = port;
        this.maxclients = maxclients;
        this.router = new HTTPRouter();
    }
    
    @Override
    public void run() {
        ExecutorService ex = Executors.newFixedThreadPool(this.maxclients);
        
        try (ServerSocket serverSocket = new ServerSocket(this.serverPort)) {
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
        catch (IOException e) {
            log.severe("No se puede abrir el puerto " + this.serverPort + ". Quizás ya está en uso. ");
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