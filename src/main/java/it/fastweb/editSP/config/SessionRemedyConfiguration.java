package it.fastweb.editSP.config;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bmc.arsys.api.ARException;
import com.bmc.arsys.api.ARServerUser;

//import it.fastweb.apreTT.client.SessionRemedyClient;

public class SessionRemedyConfiguration extends Common_configuration {

	 //private SessionRemedyClient sessionRemedy;
	 private static Logger logger = LoggerFactory.getLogger(SessionRemedyConfiguration.class);
	 private String servername ;
	 private String username;
	 private String password;
	 
	 //configurazione session
	 private ARServerUser ctx;  

	   
	    //public SessionRemedyConfiguration(//SessionRemedyClient sessionRemedyClient) {
	 public SessionRemedyConfiguration() throws IOException {
	        //this.sessionRemedy = sessionRemedyClient;
		    logger.info("Inizializzazione Session Remedy ");
	        Properties prop = new Properties();
			InputStream input = null;

	        input = new FileInputStream(getConfigurazione_file());
			// Carica il file di configurazione.
			prop.load(input);

			
			servername = prop.getProperty("serverName");
			username = prop.getProperty("userName");
			password = prop.getProperty("userPassword");
			
	        
	    	ctx = new ARServerUser();  
			ctx.setServer(servername); 
			ctx.setUser(username);  
			ctx.setPassword(password); 
			
			
			logger.info("ServerName Remedy = " + servername);
			logger.info("UserName Remedy = " + username);
			logger.info("Password Remedy = " + password);
	    	
	    }
	    
	    public ARServerUser getSessionRemedy() {
			return ctx;
		}
	    
	    public void  openSessionRemedy() throws ARException {
	    		    	
	    	logger.info("ServerName Remedy = " + this.servername);
	    	logger.info("UserName Remedy = " + this.username);
	    	logger.info("Password Remedy = " + this.password );
			ctx.login();
			logger.info("Session Remedy aperta ");
			return;
		}
			
		
		public void  logoutSessionRemedy() throws ARException {
			ctx.logout();
			logger.info("Session Remedy chiusa ");
			return;
		}
}
