package it.fastweb.editSP.model;

import it.fastweb.editSP.model.WorkOrderSP;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

public class DbRowMapperWorkOrderSP  {


	private static final String RCO_ID ="RCO_ID";
	private static final String WO_ID ="WO_ID";
	private static final String AZIONE_PREIMPOSTATA ="Azione_preimpostata";
	private static final String TECH = "Tech";
	private static final String FORM = "Form";
	private static final String ACCOUNT = "Account";
	private static final String NOME_WO = "Nome_WO";
	private static final String ORDER_ID = "Order_ID";
	private static final String SEGMENTO_CLIENTE = "Segmento_Cliente";
	private static final String CELLULARE = "Cellulare";
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



	// filed to be updated using  RCO_ID
	private static final String ESITO = "Esito";
	private static final String DATA_ESITO = "Data_Esito";
	private static final String ESITO_DETTAGLI =  "Esito_Dettagli";
	private static final String DATA_INSERIMENTO =  "Data_Inserimento";

	  /*`RCO_ID` int(11) NOT NULL AUTO_INCREMENT,
	  `WO_ID` varchar(20) NOT NULL,
	  `Azione_preimpostata` varchar(40) DEFAULT NULL,
	  `Tech` varchar(15) NOT NULL,
	  `Form` varchar(30) DEFAULT NULL,
	  `Account` varchar(11) DEFAULT NULL,
	  `Nome_WO` varchar(45) DEFAULT NULL,
	  `Order_ID` varchar(8) DEFAULT NULL,
	  `Segmento_Cliente` varchar(15) DEFAULT NULL,
	  `Cellulare` varchar(15) DEFAULT NULL,
	  `Stato_Attuale` varchar(20) DEFAULT NULL,
	  `CDA_Attuale` varchar(45) DEFAULT NULL,
	  `Dett_CDA_Attuale` varchar(45) DEFAULT NULL,
	  `CDC_Attuale` varchar(45) DEFAULT NULL,
	  `Dett_CDC_Attuale` varchar(45) DEFAULT NULL,
	  `Stato_Nuovo` varchar(20) DEFAULT NULL,
	  `CDA_Nuovo` varchar(45) DEFAULT NULL,
	  `Dett_CDA_Nuovo` varchar(45) DEFAULT NULL,
	  `CDC_Nuovo` varchar(45) DEFAULT NULL,
	  `Dett_CDC_Nuovo` varchar(45) DEFAULT NULL,
	  `Note_PM` varchar(300) DEFAULT NULL,
	  `Esito` varchar(20) DEFAULT NULL,
	  `Data_Esito` datetime DEFAULT NULL,
	  `Esito_Dettagli` varchar(200) DEFAULT NULL,
	  `Data_Inserimento` datetime DEFAULT CURRENT_TIMESTAMP*/

	public WorkOrderSP mapRow(ResultSet rs, int rowNum) throws SQLException {

		WorkOrderSP dati_apertura_wo = new WorkOrderSP();

		dati_apertura_wo.setRco_id(rs.getString(RCO_ID));
		dati_apertura_wo.setIdentificativo(rs.getString(WO_ID));
		dati_apertura_wo.setAzione_preimpostata(rs.getString(AZIONE_PREIMPOSTATA));
		dati_apertura_wo.setTech(rs.getString(TECH));
		dati_apertura_wo.setForm(rs.getString(FORM));

		dati_apertura_wo.setAccount_no(rs.getString(ACCOUNT));
		dati_apertura_wo.setNome_wo(rs.getString(NOME_WO));
		dati_apertura_wo.setOrder_id(rs.getString(ORDER_ID));
		dati_apertura_wo.setSegmento_cliente(rs.getString(SEGMENTO_CLIENTE));
		dati_apertura_wo.setCellulare(rs.getString(CELLULARE));
		dati_apertura_wo.setAttuale_stato(rs.getString(STATO_ATTUALE));
		dati_apertura_wo.setAttuale_codice_attesa(rs.getString(CDA_ATTUALE));
		dati_apertura_wo.setAttuale_dett_codice_attesa(rs.getString(DETT_CDA_ATTUALE));
		dati_apertura_wo.setAttuale_codice_chiusura(rs.getString(CDC_ATTUALE));
		dati_apertura_wo.setAttuale_dett_codice_chiusura(rs.getString(DETT_CDC_ATTUALE));
		dati_apertura_wo.setNuovo_stato(rs.getString(STATO_NUOVO));
		dati_apertura_wo.setNuovo_codice_attesa(rs.getString(CDA_NUOVO));
		dati_apertura_wo.setNuovo_dett_codice_attesa(rs.getString(DETT_CDA_NUOVO));
		dati_apertura_wo.setNuovo_codice_chiusura(rs.getString(CDC_NUOVO));
		dati_apertura_wo.setNuovo_dett_codice_chiusura(rs.getString(DETT_CDC_NUOVO));
		dati_apertura_wo.setNote_pm(rs.getString(NOTE_PM));
		
		dati_apertura_wo.setEsito(rs.getBoolean(ESITO));
		dati_apertura_wo.setData_esito(rs.getDate(DATA_ESITO));
		dati_apertura_wo.setEsito_dettagli(rs.getString(ESITO_DETTAGLI));
		dati_apertura_wo.setData_inserimento(rs.getDate(DATA_INSERIMENTO));

		return dati_apertura_wo;
	}

}
