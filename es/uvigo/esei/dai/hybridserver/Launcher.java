package es.uvigo.esei.dai.hybridserver;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Properties;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public class Launcher {
	private static final Logger log = Logger.getLogger(Launcher.class.getName());
    
    private final HybridServer server;
    
    public Launcher() {
    	this(new Configuration());
    }
    
    public Launcher(Properties p) {
    	this(Configuration.fromProperties(p));
    }
    
    // Requerida por ClientRequestTest
    public Launcher(Map<String, String> pages) {
    	throw new NotImplementedException();
    }
    
    public Launcher(Configuration c) {
    	this.server = new HybridServer(c);
    }
    
    /**
     * Obtiene el puerto HTTP del servidor.
     * 
     * @return Número de puerto.
     */
    public int getPort() {
    	return this.server.getConfiguration().getHttpPort();
    }
    
    /**
     * Inicia el servidor HybridServer
     */
    public void start() {
    	this.server.start();
    }
    
    /**
     * Para el servidor HybridServer
     */
    public void stop() {
    	this.server.stop();
    }
    
    /**
     * Comprueba si el servidor esta iniciado
     * @return Estado del servidor
     */
    public boolean isStarted() {
    	return this.server.isStarted();
    }
    
    /**
     * Punto de inicio del programa
     * @param args Argumentos pasados al ejecutable
     */
    public static void main(String[] args) {
    	
    	Launcher daemon = null;

        if (args.length > 0) {
        	if (args[0].endsWith(".conf")) {
        		try (InputStream is = new FileInputStream(args[0])) {
        			Properties props = new Properties();
                    props.load(is);
                    daemon = new Launcher(props);
                } catch (FileNotFoundException ex) {
                    Launcher.log.log(Level.SEVERE, "The config file <{0}> not found.", args[0]);
                } catch (IOException ex) {
                    Launcher.log.log(Level.SEVERE, "The config file <{0}> couldn't be loaded correctly.", args[0]);
                }
        	}
        	else if (args[0].endsWith(".xml")) {
        		// Not implemented yet
        	}
        	else {
                Launcher.log.log(Level.SEVERE, "Invalid config file format.");
        	}
        }
    	else {
    		// With default configuration
    		daemon = new Launcher();
    	}
        
        if (daemon != null) {
            daemon.start();
            
            if (daemon.isStarted()) {
	            String command = "";
	            Scanner sc = new Scanner(System.in);
	            
	            while (true) {
	            	System.out.print("-> ");
	            	command = sc.nextLine();
	            	
	            	if (command.matches("help")) {
	            		Launcher.commandHelp();
	            	}
	            	else if (command.matches("stop")) {
	            		daemon.stop();
	            		break;
	            	}
	            	else if (command.matches("restart")) {
	            		daemon.stop();
	            		daemon = null;
	            		Launcher.main(args);
	            		break;
	            	}
	            	else {
	            		System.out.println("Command not found. Type \"help\"");
	            	}
	            }
	            
	            sc.close();
            }
            else {
            	System.out.println("Server can't be initiated. Check error log.");
            }
        }
        else {
            Launcher.log.log(Level.SEVERE, "Unexpected error while server has initiating.", args[0]);
        }
    }
    
    /**
     * Ejecuta el comando HELP
     */
    private static void commandHelp() {
    	System.out.println();
    	System.out.println("Commands:");
    	System.out.println();
    	System.out.println("\tstop\tStop and close the server.");
    	System.out.println("\trestart\tRestart the server.");
    	System.out.println("\thelp\tShow this help.");
    	System.out.println();
    }
}
