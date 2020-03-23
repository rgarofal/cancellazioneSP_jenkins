package it.fastweb.cancellazioniSP.config;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bmc.arsys.api.ARException;
import com.bmc.arsys.api.ARServerUser;

//import it.fastweb.apreTT.client.SessionRemedyClient;

public class SessionRemedyConfiguration  {

	 //private SessionRemedyClient sessionRemedy;
	 static Logger logger = LoggerFactory.getLogger(SessionRemedyConfiguration.class);
	 private String servername ;
	 private String username;
	 private String password;
	 
	 //configurazione session
	 private ARServerUser ctx;  

	   
	    //public SessionRemedyConfiguration(//SessionRemedyClient sessionRemedyClient) {
	 public SessionRemedyConfiguration(DataConfig db) throws IOException, SQLException, ARException {
	        
		    logger.info("Inizializzazione Session Remedy ");
		    
		    
		    Properties prop = db.getConfigurationForRemedy();
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
			openSessionRemedy();
	    	
	    }
	    
	    public ARServerUser getSessionRemedy() {
			return ctx;
		}
	    
	    public void  openSessionRemedy() throws ARException {
	    		    	
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
