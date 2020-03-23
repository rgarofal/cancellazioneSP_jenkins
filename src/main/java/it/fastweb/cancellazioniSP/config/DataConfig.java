package it.fastweb.cancellazioniSP.config;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import it.fastweb.cancellazioniSP.model.DbRowMapperCancellazioniSP;
import it.fastweb.cancellazioniSP.model.Elemento;
import it.fastweb.cancellazioniSP.model.WorkOrderSP_cancellare;

public class DataConfig extends Common_configuration {

	static Logger logger = LoggerFactory.getLogger(DataConfig.class);
	private String DB_URL;
	private String USER;
	private String PASS;
	//	private String serverName;
	//	private String userName;
	//	private String userPassword;
	private String table;
	private Connection conn = null;

	
	private static final String QUERY_FIND_CONFIG_FOR_REMEDY = "select * from rco_m0.api_configuration WHERE chiave LIKE 'cancellasp_%'";

	private String sql_read_wo_sp = 
			"SELECT * FROM cmpa.tbl_cancellazioni_basedati WHERE Esito IS NULL"
					+ " AND processo IN ('CANC_COMM','CANC_TEC','CH_WO_SBLOCCO_FIBRA','CH_WO_SBLOCCO_ADSL',"
					+ "'CH_WO_SBLOCCO_WS','CH_WO_FTTS_POST','CH_WO_FTTS_PRE','CH_WO_FTTS_PRE_OLO')"
					+ " AND Data_esecuzione <=  Date(now())";
			
			
	
	private String updateResponse = "UPDATE cmpa.tbl_cancellazioni_basedati SET Esito= ?, bpa_agent = 'API'"
			+ " WHERE id = ? " ;  // +singoloElemento.id+ "' Esito='Regolare'  "
	
	
	
	
	private static final Logger log = LoggerFactory.getLogger(DataConfig.class);

	public DataConfig(boolean  config_test) throws IOException, FileNotFoundException, SQLException {
		super(config_test);
		Properties prop = new Properties();
		InputStream input = null;
		Utility ut = new Utility();

		log.info("Lettura Configurazione database");

		try {
			// Carica il file di configurazione.

			input = ClassLoader.class.getResourceAsStream("/"+ getConfigurazione_file());

			prop.load(input);


			DB_URL = prop.getProperty("database");
			USER = prop.getProperty("username");
			PASS = prop.getProperty("password");

			conn = (DriverManager.getConnection(DB_URL, USER, PASS));
			logger.info("Connessione al Database configurata");
			logger.info("Database = " + DB_URL);
			logger.info("username" + USER);
		} catch (FileNotFoundException e) {
			logger.error("File di configurazione non trovato.");
			throw e;
		} catch (IOException e) {
			logger.error(e.getMessage());
			throw e;
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
					logger.error(e.getMessage());
					throw e;
				}
			}
		}
	}

	public Properties getConfigurationForRemedy() throws SQLException {

		Properties properties = new Properties();
		ResultSet resultSet = null;
		Statement stmt = null;


		stmt = conn.createStatement();
		resultSet = stmt.executeQuery(QUERY_FIND_CONFIG_FOR_REMEDY);
		log.debug("Ho eseguito query: " + QUERY_FIND_CONFIG_FOR_REMEDY);

		Map<String, String> values = new HashMap<String, String>();

		while (resultSet.next()) {

			String key = resultSet.getString("chiave");
			String value = resultSet.getString("valore");
			values.put(key, value);

		}

		stmt.close();
		//conn.close();
		properties.setProperty("userName", values.get("cancellasp_remedy_username"));
		properties.setProperty("userPassword", values.get("cancellasp_remedy_password"));
		properties.setProperty("serverName", values.get("cancellasp_server"));

		return properties;
	}
	public ArrayList<Elemento> read_data_dacancellare_sp() throws SQLException {

		ResultSet rs = null;
		Statement stmt = null;
		DbRowMapperCancellazioniSP row = new DbRowMapperCancellazioniSP();
		ArrayList<Elemento> lista_elementi = new ArrayList<Elemento>();

		stmt = conn.createStatement();
		rs = stmt.executeQuery(sql_read_wo_sp);

		while (rs.next()) {
			lista_elementi.add(row.mapRow(rs, rs.getRow()));
		}

		return lista_elementi;
	}

	public Connection getConn() {
		return conn;
	}

	public void close_conn() throws SQLException {
		logger.info("Chiusura connessione Database");
		conn.close();
	}

	public void update_stato_cancellazioneSP(Elemento elemento_cancellato) throws Exception {

		logger.info("Cancellazione SP - aggiornamento dati su db " + elemento_cancellato.getIdentificativo());
		
		try {	
            if(elemento_cancellato.isEseguito()) {
            	updateEsito(elemento_cancellato.getMessaggio_esito(),elemento_cancellato.getId());
            }
            else
            {
            	updateEsito(elemento_cancellato.getErrore(),elemento_cancellato.getId());
            }
			
		} catch (SQLException e) {
			e.printStackTrace();
			logger.error("*****Errore nella cancellazione SP  " + elemento_cancellato.getIdentificativo());
		}


	}

	private void updateEsito(String esito_messaggio, String rco_id) throws SQLException {

		String sql = "";

		logger.info("Aggiorno l'esito ");
		sql = updateResponse;
		

		PreparedStatement pstmt = conn.prepareStatement(sql);


		pstmt.setString(1, esito_messaggio);
		pstmt.setString(2, rco_id);
        pstmt.toString();
		pstmt.executeUpdate();
      
	}
}
