package it.fastweb.editSP.config;

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
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import it.fastweb.editSP.model.DbRowMapperWorkOrderSP;
import it.fastweb.editSP.model.WorkOrderSP;

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



	private String sql_read_wo_sp = "SELECT * FROM tts_sp_service.master_SP_EDIT  WHERE Esito IS NULL AND Azione_preimpostata = 'VirtualAgent'" + " ORDER BY data_inserimento";
	private String updateResponseWO = "UPDATE tts_sp_service.master_SP_EDIT SET Esito= ? , Data_Esito = NOW(), Esito_Dettagli = ?  WHERE RCO_ID = ?";


	public DataConfig() throws IOException, FileNotFoundException, SQLException {

		Properties prop = new Properties();
		InputStream input = null;
		Utility ut = new Utility();

		try {

			logger.info("Lettura Configurazione");
			String path = null;
			try {
				path = ut.writeResourceToFile(getConfigurazione_file());
			} catch (URISyntaxException e) {
				logger.error("File di configurazione non trovato." + e.getMessage());
			}
			input = new FileInputStream(path);
			// Carica il file di configurazione.

			prop.load(input);

			DB_URL = prop.getProperty("database");
			USER = prop.getProperty("username");
			PASS = prop.getProperty("password");

			table = prop.getProperty("table");

			conn = (DriverManager.getConnection(DB_URL, USER, PASS));
			logger.info("Connessione al Database configurata");
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

	public ArrayList<WorkOrderSP> read_data_wo_sp() throws SQLException {

		ResultSet rs = null;
		Statement stmt = null;
		DbRowMapperWorkOrderSP row = new DbRowMapperWorkOrderSP();
		ArrayList<WorkOrderSP> lista_elementi = new ArrayList<WorkOrderSP>();

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
		conn.close();
	}
	
	public void update_stato_workorder_elaborato(WorkOrderSP workOrdersp_aggiornato) throws Exception {

		logger.info("Work Order aggiornato - aggiornamento dati su db " + workOrdersp_aggiornato.getIdentificativo());

		try {	

			updateEsito(workOrdersp_aggiornato.getEsito_message(),workOrdersp_aggiornato.getEsito_dettagli(),workOrdersp_aggiornato.getRco_id());
		} catch (SQLException e) {
			e.printStackTrace();
			logger.error("*****Errore nell'inserimento del ticket nr " + workOrdersp_aggiornato.getIdentificativo());
		}


	}

	private void updateEsito(String esito_messaggio, String esito_dettagli, String rco_id) throws SQLException {

		String sql = "";

		logger.info("Update updateResponseCodeOk");
		sql = updateResponseWO;

		PreparedStatement pstmt = conn.prepareStatement(sql);


		pstmt.setString(1, esito_messaggio);
		pstmt.setString(2, esito_dettagli);
		pstmt.setString(3, rco_id);

		pstmt.executeUpdate();

	}
}
