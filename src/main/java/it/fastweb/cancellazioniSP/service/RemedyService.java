package it.fastweb.cancellazioniSP.service;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import it.fastweb.cancellazioniSP.config.DataConfig;
import it.fastweb.cancellazioniSP.config.SessionRemedyConfiguration;
import it.fastweb.cancellazioniSP.model.Elemento;
import it.fastweb.cancellazioniSP.model.WorkOrderSP_cancellare;

public class RemedyService {
	private SessionRemedyConfiguration sessionRemedy;
	private RemedyDao remedy_dao;
	private DataConfig db_session;
	private static Boolean config_test = false;


	private static Logger logger = LoggerFactory.getLogger(RemedyService.class);

	public RemedyService(SessionRemedyConfiguration sessionClient, DataConfig db_config) {
		super();
		sessionRemedy = sessionClient;
		db_session = db_config;
		remedy_dao = new RemedyDao(sessionRemedy.getSessionRemedy());

	}

	
	private void cancellazione_SP(Elemento sp_to_delete) {
		if (config_test) {
			logger.info("TEST cancellazione SP ");
			sp_to_delete.setEseguito(true);
			sp_to_delete.setEsito(true);
		} else {
			logger.info("cancellazione SP ");
			if(remedy_dao.cancellazione(sp_to_delete))
			{
				sp_to_delete.setEseguito(true);
				sp_to_delete.setEsito(true);
				sp_to_delete.setMessaggio_esito("Regolare");
			}
			else
			{
				sp_to_delete.setEseguito(false);
				sp_to_delete.setEsito(false);
				sp_to_delete.setErrore("Errore_generico");

			}
		}	
	}
	public Elemento process_Cancellazione_SP(Elemento sp_to_delete) throws Exception 
	{
		logger.info("*****Sto processando la cancellazione di : " + sp_to_delete.getIdentificativo());
		sp_to_delete.setEseguito(false);

		if(sp_to_delete.getProcesso().equalsIgnoreCase("CANC_COMM") 
				|| sp_to_delete.getProcesso().equalsIgnoreCase("CANC_TEC")) 
		{

			if(remedy_dao.verificaStatoCancellazione(sp_to_delete))
			{
				cancellazione_SP(sp_to_delete);
				
			}  else {
				sp_to_delete.setEseguito(false);
				sp_to_delete.setEsito(false);
				sp_to_delete.setErrore( "Errore_wo_non_trovato");
				logger.info("Errore_wo_non_trovato - Elemento " +sp_to_delete.getIdentificativo()+ " gia in stato aggiornato o categoria sbagliata");

			}

		} else if(sp_to_delete.getProcesso().equalsIgnoreCase("CH_WO_SBLOCCO_ADSL") 
				|| sp_to_delete.getProcesso().equalsIgnoreCase("CH_WO_SBLOCCO_FIBRA")
				|| sp_to_delete.getProcesso().equalsIgnoreCase("CH_WO_SBLOCCO_WS")
				|| sp_to_delete.getProcesso().equalsIgnoreCase("CH_WO_FTTS_POST")
				|| sp_to_delete.getProcesso().equalsIgnoreCase("CH_WO_FTTS_PRE")
				|| sp_to_delete.getProcesso().equalsIgnoreCase("CH_WO_FTTS_PRE_OLO"))
		{		
			remedy_dao.getDettagliWO(sp_to_delete);
			
			if(remedy_dao.verificaStatoWO(sp_to_delete))
			{
				if(sp_to_delete.is_set_Dettaglio_codice_chiusura())
				{
					cancellazione_SP(sp_to_delete);
				}
			    else
			    {
					sp_to_delete.setEseguito(false);
					sp_to_delete.setEsito(false);
					sp_to_delete.setErrore( "Errore_dcda");
					logger.info("Errore_dcda - Elemento " +sp_to_delete.getIdentificativo()+ " non contiene il dettaglio codice chiusura.");
				}
			}
			else
			{
				sp_to_delete.setEseguito(false);
				sp_to_delete.setEsito(false);
				sp_to_delete.setErrore( "Errore_wo_non_trovato");
				logger.info("Errore_wo_non_trovato - Elemento " +sp_to_delete.getIdentificativo()+ " gia in stato aggiornato o categoria errata.");

			}
		}
		else
		{
			remedy_dao.getTicketConCampi(sp_to_delete, true);
		}
		return sp_to_delete;
	}
}

