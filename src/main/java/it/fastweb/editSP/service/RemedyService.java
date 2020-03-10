package it.fastweb.editSP.service;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import it.fastweb.editSP.config.SessionRemedyConfiguration;
import it.fastweb.editSP.model.WorkOrderSP;

public class RemedyService {
	private SessionRemedyConfiguration sessionRemedy;
	private RemedyDao remedy_dao;
	private static Boolean config_test = false;

	
	private static Logger logger = LoggerFactory.getLogger(RemedyService.class);

	public RemedyService(SessionRemedyConfiguration sessionClient) {
		super();
		sessionRemedy = sessionClient;
		remedy_dao = new RemedyDao(sessionRemedy.getSessionRemedy());

	}

	public WorkOrderSP process_WO_SP(WorkOrderSP wo_sp_to_edit) throws Exception {
		logger.info("*****Sto processando: " + wo_sp_to_edit.getIdentificativo());
        
		if((wo_sp_to_edit.getIdentificativo() != null && !wo_sp_to_edit.getIdentificativo().isEmpty()) 
				&&(wo_sp_to_edit.getTech() != null && !wo_sp_to_edit.getTech().isEmpty())) {

			// Carica lo stato attuale del wo per successive verifiche e per verificare che esista
			if(remedy_dao.caricaDettagliWO(wo_sp_to_edit))
			{
				// Controlla che lo stato ed il codice dello stato presenti nel TT siano conformi alle aspettative
				if(wo_sp_to_edit.gestibilita())
				{
					if (config_test) {
						logger.info("TEST aggiornamento ticket ");
					} else {
						// E' un ticket gestibile ed � possibile aggiornarlo. Quindi lancio l'aggiornamento.
						logger.info("aggiornamento ticket ");
						remedy_dao.aggiorna(wo_sp_to_edit);
					}
					
				}
			}

		} else {
			wo_sp_to_edit.setEsito(false);
			wo_sp_to_edit.setEsito_message("MOD_SP_NG");
			logger.error("Informazioni mancanti nell'entry ID: " +wo_sp_to_edit.getIdentificativo());
		}

		return wo_sp_to_edit;
	}

}

