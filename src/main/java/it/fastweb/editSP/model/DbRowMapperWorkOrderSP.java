package it.fastweb.editSP.model;

import it.fastweb.editSP.model.WorkOrderSP;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

public class DbRowMapperWorkOrderSP  {

  
	private static final String RCO_ID ="RCO_ID";
	private static final String WO_ID ="WO_ID";
	private static final String TECH = "Tech";
	private static final String STATO_ATTUALE = "Stato_Attuale";
	private static final String CDA_ATTUALE = "CDA_Attuale";
	private static final String DETT_CDA_ATTUALE = "Dett_CDA_Attuale";
	private static final String CDC_ATTUALE = "CDC_Attuale";
	private static final String DETT_CDC_ATTUALE = "Dett_CDC_Attuale";
	private static final String STATO_NUOVO = "Stato_Nuovo";
	private static final String CDA_NUOVO = "CDA_Nuovo";
	private static final String DETT_CDA_NUOVO = "Dett_CDA_Nuovo";
	private static final String CDC_NUOVO = "CDC_Nuovo";
	private static final String DETT_CDC_NUOVO = "Dett_CDC_Nuovo";
	private static final String NOTE_PM = "Note_PM";
	private static final String CELLULARE = "Cellulare";
	private static final String ORDER_ID = "Order_ID";
	
    // filed to be updated using  RCO_ID
	private static final String ESITO = "Esito";
	private static final String DATA_ESITO = "Data_Esito";
	private static final String ESITO_DETTAGLI =  "Esito_Dettagli";
	

    public WorkOrderSP mapRow(ResultSet rs, int rowNum) throws SQLException {

    	WorkOrderSP dati_apertura_wo = new WorkOrderSP();

    	dati_apertura_wo.setRco_id(rs.getString("RCO_ID"));
    	dati_apertura_wo.setIdentificativo(rs.getString("WO_ID"));
    	dati_apertura_wo.setTech(rs.getString("TECH"));
    	dati_apertura_wo.setAttuale_stato(rs.getString("STATO_ATTUALE"));
    	dati_apertura_wo.setAttuale_codice_attesa(rs.getString("CDA_ATTUALE"));
    	dati_apertura_wo.setAttuale_dett_codice_attesa(rs.getString("DETT_CDA_ATTUALE"));
    	dati_apertura_wo.setAttuale_codice_chiusura(rs.getString("CDC_ATTUALE"));
    	dati_apertura_wo.setAttuale_dett_codice_chiusura(rs.getString("DETT_CDC_ATTUALE"));
    	dati_apertura_wo.setNuovo_stato(rs.getString("STATO_NUOVO"));
    	dati_apertura_wo.setNuovo_codice_attesa(rs.getString("CDA_NUOVO"));
    	dati_apertura_wo.setNuovo_dett_codice_attesa(rs.getString("DETT_CDA_NUOVO"));
    	dati_apertura_wo.setNuovo_codice_chiusura(rs.getString("CDC_NUOVO"));
    	dati_apertura_wo.setNuovo_dett_codice_chiusura(rs.getString("DETT_CDC_NUOVO"));
    	dati_apertura_wo.setNote_pm(rs.getString("NOTE_PM"));
    	dati_apertura_wo.setCellulare(rs.getString("CELLULARE"));
    	dati_apertura_wo.setOrder_id(rs.getString("ORDER_ID"));

        return dati_apertura_wo;
    }

}
