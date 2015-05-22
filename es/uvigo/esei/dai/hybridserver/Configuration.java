package es.uvigo.esei.dai.hybridserver;

import java.util.ArrayList;
import java.util.Map;
import java.util.Properties;

public class Configuration {
    private int LPort;
    private int LNumUsers;
    private String LwsUrl;
    private String LdbUser;
    private String LdbPassword;
    private String LdbUrl;
    private ArrayList<ServerConfiguration> LServers;

    public Configuration() {
        this.LPort = 8888;
        this.LNumUsers = 50;
        this.LwsUrl = "";
        this.LdbUser = "dai";
        this.LdbPassword = "daipassword";
        this.LdbUrl = "jdbc:mysql://localhost/hstestdb";
        this.LServers = new ArrayList<ServerConfiguration>();
    }

    public Configuration (int port, int numUsers, String wsUrl, String dbUser, String dbPassword, String dbUrl, ArrayList<ServerConfiguration> servers) {
        this.LPort = port;
        this.LNumUsers = numUsers;
        this.LwsUrl = wsUrl;
        this.LdbUser = dbUser;
        this.LdbPassword = dbPassword;
        this.LdbUrl = dbUrl;
        this.LServers = servers;
    }
    
    public int getHttpPort() {
        return this.LPort;
    }
    
    public void setHttpPort(int port) {
    	this.LPort = port;
    }
    
    public int getNumClients() {
        return this.LNumUsers;
    }

    public void setNumClients(int numUsers) {
    	this.LNumUsers = numUsers;
    }
    
    public String getWebServiceURL() {
        return this.LwsUrl;
    }

    public void setWebServiceURL(String wsUrl) {
    	this.LwsUrl = wsUrl;
    }
    
    public String getDbUser() {
        return this.LdbUser;
    }

    public void setDbUser(String dbUser) {
    	this.LdbUser = dbUser;
    }
    
    public String getDbPassword() {
        return this.LdbPassword;
    }

    public void setDbPassword(String dbPassword) {
    	this.LdbPassword = dbPassword;
    }
    
    public String getDbURL() {
        return this.LdbUrl;
    }

    public void setDbURL(String dbURL) {
    	this.LdbUrl = dbURL;
    }
    
    public ArrayList<ServerConfiguration> getServers() {
        return this.LServers;
    }

    public void setServers(ArrayList<ServerConfiguration> servers) {
    	this.LServers = servers;
    }

    public static Configuration fromMap (Map<String, String> mp) {
        Properties prop = new Properties();
        prop.putAll(mp);
        return Configuration.fromProperties(prop);
    }

    public static Configuration fromProperties(Properties p) {
        Configuration defaultc = new Configuration();

        return new Configuration(
            Integer.parseInt(p.getProperty("port", ((Integer) defaultc.getHttpPort()).toString())),
            Integer.parseInt(p.getProperty("numClients", ((Integer) defaultc.getNumClients()).toString())),
            p.getProperty("wps", defaultc.getWebServiceURL()),
            p.getProperty("db.user", defaultc.getDbUser()),
            p.getProperty("db.password", defaultc.getDbPassword()),
            p.getProperty("db.url", defaultc.getDbURL()),
            new ArrayList<ServerConfiguration>()
        );
    }
    
    @Override
    public String toString() {
		StringBuilder conf = new StringBuilder();
		String EOL = System.getProperty("line.separator");

		conf.append("HTTP - Puerto: " + this.LPort + EOL);
		conf.append("HTTP - Max. peticiones: " + this.LNumUsers + EOL);
		conf.append("DB   - Usuario: " + this.LdbUser + EOL);
		conf.append("DB   - Contraseña: " + this.LdbPassword + EOL);
		conf.append("DB   - Dirección: " + this.LdbUrl + EOL);
		conf.append("JWS  - Dirección: " + this.LwsUrl + EOL);
		
		for (ServerConfiguration sc : this.getServers()) {
			conf.append("P2P  - " + sc.toString() + EOL);
		}
		
		return conf.toString();
    }
}
