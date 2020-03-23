package it.fastweb.cancellazioniSP;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bmc.arsys.api.ARException;

import it.fastweb.cancellazioniSP.config.DataConfig;
import it.fastweb.cancellazioniSP.config.SessionRemedyConfiguration;
import it.fastweb.cancellazioniSP.model.Elemento;
import it.fastweb.cancellazioniSP.model.WorkOrderSP_cancellare;
import it.fastweb.cancellazioniSP.service.RemedyService;

public class CancellazioniSPApplication {

	private static DataConfig dataConfig;
	private static RemedyService remedy_service;
	private static SessionRemedyConfiguration remedy_conf;

	private static boolean config_test = false;

	private static  Logger logger = LoggerFactory.getLogger(CancellazioniSPApplication.class);

	private static void close_all_session(SessionRemedyConfiguration remedy_conf, DataConfig db ) {
		try {
			remedy_conf.logoutSessionRemedy();
			logger.info("Chiusa sessione Remedy");
		} catch (ARException e1) {
			e1.printStackTrace();
			logger.error("Errore chiusura sessione Remedy " + e1.getMessage());
		}finally {
			try {
				db.close_conn();
				logger.info("Chiusa connessione al DB");
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				logger.error("Errore chiusura sessione DB " + e.getMessage());
			}
		}
	}
	public static void main( String[] args ) 
	{
		// throws FileNotFoundException, IOException, SQLException
		logger.info("CancellazioniSP start");
		try {
			dataConfig = new DataConfig(config_test);
			logger.info("Aperta la sessione verso il database");
			remedy_conf =  new SessionRemedyConfiguration(dataConfig);

			logger.info("Configurata la sessione verso Remedy");
			remedy_service = new RemedyService(remedy_conf, dataConfig);
			logger.info("Aperta la sessione verso Remedy");

			ArrayList<Elemento> lista_da_cancellare_sp = dataConfig.read_data_dacancellare_sp();
			logger.info("Totale cancellazioni da eseguire : " + lista_da_cancellare_sp.size());
			
			for (Elemento sp_da_cancellare : lista_da_cancellare_sp){
				try {
					sp_da_cancellare = remedy_service.process_Cancellazione_SP(sp_da_cancellare);
					dataConfig.update_stato_cancellazioneSP(sp_da_cancellare);
				} catch (Exception e) {
					e.printStackTrace();
					logger.error("Errore elaborazione Work Order SP su remedy" + e.getMessage());
				}
			}
		} catch (ARException e1) {
			e1.printStackTrace();
			logger.error("Exception remedy ", e1);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Exception ", e);
		} finally {
			logger.info("Fine workorder da editare");
			close_all_session(remedy_conf,dataConfig);
		}
	}
}
