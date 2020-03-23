package it.fastweb.cancellazioniSP.model;

import java.time.LocalDate;
import java.util.Date;

import it.fastweb.cancellazioniSP.config.Utility;

public class WorkOrderSP {

	private String rco_id;
	private String identificativo;            // WO_ID
	private String azione_preimpostata;
	private String tech;
	private String form;
	private String account_no;
	private String nome_wo;
	private String order_id;
	private String segmento_cliente;
	private String cellulare;
	private String attuale_stato;              // Stato_Attuale
	private String attuale_codice_attesa;      // CDA_Attuale
	private String attuale_dett_codice_attesa;
	private String attuale_codice_chiusura;    // CDC_Attuale
	private String attuale_dett_codice_chiusura;
	private String nuovo_stato;                // Stato_Nuovo
	private String nuovo_codice_attesa;        // CDA_Nuovo
	private String nuovo_dett_codice_attesa;   
	private String nuovo_codice_chiusura;
	private String nuovo_dett_codice_chiusura;
	private String note_pm;
	// to trace the status of request of Remedy
	private boolean esito;
	private Date data_esito;
	private String esito_dettagli;
	private Date data_inserimento;

	private String guid;

	private String process_CFG_ID;
	private String fo_ID;
	private String process_ID;
	private String nome_FO;
	private String processo_wo;

	private String site_ID;

	private String automa;

	// Data to add - Now are static
	private String nuovo_WO_nome;
	private String nuovo_WO_stato;
	private String nuovo_WO_codice_attesa;
	private String nuovo_WO_codice_attesa_precedente;
	private String nuovo_WO_dett_codice_attesa;

	private String esito_message;

	// Details to be dinamically load 
	private String stato;
	private String codice_attesa;
	private String codice_chiusura;
	private String dettaglio_codice_attesa;
	private String dettaglio_codice_chiusura;
	private String codice_transizione;

	public WorkOrderSP() {
		super();
	}

	public WorkOrderSP(String rco_id, 
			String identificativo, 
			String azione_preimpostata,
			String tech, 
			String form,
			String account_no,
			String nome_wo,
			String segmento_cliente,
			String attuale_stato, 
			String attuale_codice_attesa,
			String attuale_dett_codice_attesa,
			String attuale_codice_chiusura,
			String attuale_dett_codice_chiusura,
			String nuovo_stato,
			String nuovo_codice_attesa,
			String nuovo_dett_codice_attesa,
			String nuovo_codice_chiusura,
			String nuovo_dett_codice_chiusura,
			String note_pm,
			String automa,
			String nuovo_WO_nome,
			String nuovo_WO_stato,
			String nuovo_WO_codice_attesa,
			String nuovo_WO_codice_attesa_precedente,
			String nuovo_WO_dettaglio_codice_attesa,
			String cellulare, 
			String order_id) {
		/*
		 * private String rco_id;
	private String identificativo;            // WO_ID
	private String azione_preimpostata;
	private String tech;
	private String form;
	private String account_no;
	private String nome_wo;
	private String order_id;
	private String segmento_cliente;
	private String cellulare;
	private String attuale_stato;              // Stato_Attuale
	private String attuale_codice_attesa;      // CDA_Attuale
	private String attuale_dett_codice_attesa;
	private String attuale_codice_chiusura;    // CDC_Attuale
	private String attuale_dett_codice_chiusura;
	private String nuovo_stato;                // Stato_Nuovo
	private String nuovo_codice_attesa;        // CDA_Nuovo
	private String nuovo_dett_codice_attesa;   
	private String nuovo_codice_chiusura;
	private String nuovo_dett_codice_chiusura;
	private String note_pm;
	// to trace the status of request of Remedy
	private boolean esito;
	private Date data_esito;
	private String esito_dettagli;
	private Date data_inserimento;
		 */
		super();
		this.setRco_id(rco_id);
		this.setIdentificativo(identificativo);
		this.setTech(tech);
		this.setAzione_preimpostata(azione_preimpostata);
		this.setTech(tech); 
		this.setForm(form);
		this.setAccount_no(account_no);
		this.setNome_wo(nome_wo);
		this.setSegmento_cliente(segmento_cliente);
		
		this.setAttuale_stato(attuale_stato);
		this.setAttuale_codice_attesa(attuale_codice_attesa);
		this.setAttuale_dett_codice_attesa(attuale_dett_codice_attesa);
		this.setAttuale_codice_chiusura(attuale_codice_chiusura);
		this.setAttuale_dett_codice_chiusura(attuale_dett_codice_chiusura);
		this.setNuovo_stato(nuovo_stato);

		this.setNuovo_codice_attesa(nuovo_codice_attesa);
		this.setNuovo_dett_codice_attesa(nuovo_dett_codice_attesa);
		this.setNuovo_codice_chiusura(nuovo_codice_chiusura);
		this.setNuovo_dett_codice_chiusura(nuovo_dett_codice_chiusura);
		this.setNote_pm(note_pm);
		this.setAutoma(automa);
		this.setNuovo_WO_nome(nuovo_WO_nome);
		this.setNuovo_WO_stato(nuovo_WO_stato);

		this.setNuovo_WO_codice_attesa(nuovo_WO_codice_attesa_precedente);
		this.setNuovo_WO_codice_attesa_precedente(nuovo_WO_codice_attesa_precedente);
		this.setNuovo_WO_dett_codice_attesa(nuovo_WO_dettaglio_codice_attesa);
		this.setCellulare(cellulare);
		this.setOrder_id(order_id);

		this.setEsito(false);
		this.setEsito_message("");
		this.setEsito_dettagli("");

	}

	public String getRco_id() {
		return rco_id;
	}

	public void setRco_id(String rco_id) {
		this.rco_id = rco_id;
	}

	public String getIdentificativo() {
		return identificativo;
	}

	public void setIdentificativo(String identificativo) {
		this.identificativo = identificativo;
	}

	public String getTech() {
		return tech;
	}

	public void setTech(String tech) {
		this.tech = tech;
	}

	public String getCellulare() {
		return cellulare;
	}

	public void setCellulare(String cellulare) {
		this.cellulare = cellulare;
	}

	public String getOrder_id() {
		return order_id;
	}

	public void setOrder_id(String order_id) {
		this.order_id = order_id;
	}

	public String getAttuale_stato() {
		return attuale_stato;
	}

	public void setAttuale_stato(String attuale_stato) {
		this.attuale_stato = attuale_stato;
	}

	public String getAttuale_codice_attesa() {
		return attuale_codice_attesa;
	}

	public void setAttuale_codice_attesa(String attuale_codice_attesa) {
		this.attuale_codice_attesa = attuale_codice_attesa;
	}

	public String getAttuale_dett_codice_attesa() {
		return attuale_dett_codice_attesa;
	}

	public void setAttuale_dett_codice_attesa(String attuale_dett_codice_attesa) {
		this.attuale_dett_codice_attesa = attuale_dett_codice_attesa;
	}

	public String getAttuale_codice_chiusura() {
		return attuale_codice_chiusura;
	}

	public void setAttuale_codice_chiusura(String attuale_codice_chiusura) {
		this.attuale_codice_chiusura = attuale_codice_chiusura;
	}

	public String getAttuale_dett_codice_chiusura() {
		return attuale_dett_codice_chiusura;
	}

	public void setAttuale_dett_codice_chiusura(String attuale_dett_codice_chiusura) {
		this.attuale_dett_codice_chiusura = attuale_dett_codice_chiusura;
	}

	public String getNuovo_stato() {
		return nuovo_stato;
	}

	public void setNuovo_stato(String nuovo_stato) {
		this.nuovo_stato = nuovo_stato;
	}

	public String getNuovo_codice_attesa() {
		return nuovo_codice_attesa;
	}

	public void setNuovo_codice_attesa(String nuovo_codice_attesa) {
		this.nuovo_codice_attesa = nuovo_codice_attesa;
	}

	public String getNuovo_dett_codice_attesa() {
		return nuovo_dett_codice_attesa;
	}

	public void setNuovo_dett_codice_attesa(String nuovo_dett_codice_attesa) {
		this.nuovo_dett_codice_attesa = nuovo_dett_codice_attesa;
	}

	public String getNuovo_codice_chiusura() {
		return nuovo_codice_chiusura;
	}

	public void setNuovo_codice_chiusura(String nuovo_codice_chiusura) {
		this.nuovo_codice_chiusura = nuovo_codice_chiusura;
	}

	public String getNuovo_dett_codice_chiusura() {
		return nuovo_dett_codice_chiusura;
	}

	public void setNuovo_dett_codice_chiusura(String nuovo_dett_codice_chiusura) {
		this.nuovo_dett_codice_chiusura = nuovo_dett_codice_chiusura;
	}

	public String getNote_pm() {
		return note_pm;
	}

	public void setNote_pm(String note_pm) {
		this.note_pm = note_pm;
	}

	public String getNuovo_WO_nome() {
		return nuovo_WO_nome;
	}

	public void setNuovo_WO_nome(String nuovo_WO_nome) {

		if (nuovo_WO_nome != null) {
			this.nuovo_WO_nome = nuovo_WO_nome;
		} else {
			this.nuovo_WO_nome = "ASSEGNAZIONE RISORSE";
		}

	}

	public String getNuovo_WO_stato() {
		return nuovo_WO_stato;
	}

	public void setNuovo_WO_stato(String nuovo_WO_stato) {

		if (nuovo_WO_stato != null) {
			this.nuovo_WO_stato = nuovo_WO_stato;
		} else {
			this.nuovo_WO_stato = "In attesa";
		}

	}

	public String getNuovo_WO_codice_attesa() {
		return nuovo_WO_codice_attesa;
	}

	public void setNuovo_WO_codice_attesa(String nuovo_WO_codice_attesa) {

		if (nuovo_WO_codice_attesa != null) {
			this.nuovo_WO_codice_attesa = nuovo_WO_codice_attesa;
		} else {
			this.nuovo_WO_codice_attesa = "Assegnazione Risorse su NETDB";
		}

	}

	public String getNuovo_WO_codice_attesa_precedente() {
		return nuovo_WO_codice_attesa_precedente;
	}

	public void setNuovo_WO_codice_attesa_precedente(String nuovo_WO_codice_attesa_precedente) {

		if (nuovo_WO_codice_attesa_precedente != null) {
			this.nuovo_WO_codice_attesa_precedente = nuovo_WO_codice_attesa_precedente;
		} else {
			this.nuovo_WO_codice_attesa_precedente = "";
		}
	}

	public String getNuovo_WO_dett_codice_attesa() {
		return nuovo_WO_dett_codice_attesa;
	}

	public void setNuovo_WO_dett_codice_attesa(String nuovo_WO_dettaglio_codice_attesa) {

		if (nuovo_WO_dettaglio_codice_attesa != null) {
			this.nuovo_WO_dett_codice_attesa = nuovo_WO_dettaglio_codice_attesa;
		} else {
			this.nuovo_WO_dett_codice_attesa = "";
		}

	}

	public String getEsito_message() {
		return esito_message;
	}

	public void setEsito_message(String esito_message) {
		this.esito_message = esito_message;
	}

	public String getEsito_dettagli() {
		return esito_dettagli;
	}

	public void setEsito_dettagli(String esito_dettagli) {
		this.esito_dettagli = esito_dettagli;
	}

	public boolean isEsito() {
		return esito;
	}

	public void setEsito(boolean esito) {
		this.esito = esito;
	}

	public String getGuid() {
		return guid;
	}

	public void setGuid(String guid) {
		this.guid = guid;
	}

	public String getAccount_no() {
		return account_no;
	}

	public void setAccount_no(String account_no) {
		this.account_no = account_no;
	}

	public String getProcess_CFG_ID() {
		return process_CFG_ID;
	}

	public void setProcess_CFG_ID(String process_CFG_ID) {
		this.process_CFG_ID = process_CFG_ID;
	}

	public String getFo_ID() {
		return fo_ID;
	}

	public void setFo_ID(String fo_ID) {
		this.fo_ID = fo_ID;
	}

	public String getProcess_ID() {
		return process_ID;
	}

	public void setProcess_ID(String process_ID) {
		this.process_ID = process_ID;
	}

	public String getNome_FO() {
		return nome_FO;
	}

	public void setNome_FO(String nome_FO) {
		this.nome_FO = nome_FO;
	}

	public String getProcesso_wo() {
		return processo_wo;
	}

	public void setProcesso_wo(String processo_wo) {
		this.processo_wo = processo_wo;
	}

	public String getSite_ID() {
		return site_ID;
	}

	public void setSite_ID(String site_ID) {
		this.site_ID = site_ID;
	}

	public String getNome_wo() {
		return nome_wo;
	}

	public void setNome_wo(String nome_wo) {
		this.nome_wo = nome_wo;
	}

	public String getAutoma() {
		return automa;
	}

	public void setAutoma(String automa) {
		this.automa = automa;
	}

	public String getStato() {
		return stato;
	}

	public void setStato(String stato) {
		this.stato = stato;
	}

	public String getCodice_attesa() {
		return codice_attesa;
	}

	public void setCodice_attesa(String codice_attesa) {
		this.codice_attesa = codice_attesa;
	}

	public String getCodice_chiusura() {
		return codice_chiusura;
	}

	public void setCodice_chiusura(String codice_chiusura) {
		this.codice_chiusura = codice_chiusura;
	}

	public String getDettaglio_codice_attesa() {
		return dettaglio_codice_attesa;
	}

	public void setDettaglio_codice_attesa(String dettaglio_codice_attesa) {
		this.dettaglio_codice_attesa = dettaglio_codice_attesa;
	}

	public String getDettaglio_codice_chiusura() {
		return dettaglio_codice_chiusura;
	}

	public void setDettaglio_codice_chiusura(String dettaglio_codice_chiusura) {
		this.dettaglio_codice_chiusura = dettaglio_codice_chiusura;
	}

	public String getCodice_transizione() {
		return codice_transizione;
	}

	public void setCodice_transizione(String codice_transizione) {
		this.codice_transizione = codice_transizione;
	}

	/*
	 * Se lo stato atteso � diverso da quello che si trova,
	 * o il codice (o dettaglio) di attesa/chiusura sono diversi, si restituisce errore.
	 * 
	 */
	public boolean gestibilita()
	{
		boolean gestibile = true;

		if(this.attuale_stato != null 
				&& !this.attuale_stato.isEmpty()
				&& !this.stato.equals(this.attuale_stato))
			gestibile = false;

		if(this.attuale_codice_attesa != null 
				&& !this.attuale_codice_attesa.isEmpty()
				&& this.codice_attesa != null 
				&& !this.codice_attesa.equals(this.attuale_codice_attesa))
			gestibile = false;

		if(this.attuale_dett_codice_attesa != null 
				&& !this.attuale_dett_codice_attesa.isEmpty()
				&& this.dettaglio_codice_attesa != null
				&& !this.dettaglio_codice_attesa.equals(this.attuale_dett_codice_attesa))
			gestibile = false;

		if(this.attuale_codice_chiusura != null 
				&& !this.attuale_codice_chiusura.isEmpty()
				&& this.codice_chiusura != null
				&& !this.codice_chiusura.equals(this.attuale_codice_chiusura))
			gestibile = false;

		if(this.attuale_dett_codice_chiusura != null 
				&& !this.attuale_dett_codice_chiusura.isEmpty()
				&& this.dettaglio_codice_chiusura != null
				&& !this.dettaglio_codice_chiusura.equals(this.attuale_dett_codice_chiusura))
			gestibile = false;


		if(!gestibile)
		{
			//logger.info("Ticket non gestibile. ID: "+this.identificativo);
			this.esito_message = "MOD_SP_NG";
			this.esito_dettagli = "Non gestibile";
			this.esito = false;
		}

		return gestibile;
	}

	public String getAzione_preimpostata() {
		return azione_preimpostata;
	}

	public void setAzione_preimpostata(String azione_preimpostata) {
		this.azione_preimpostata = azione_preimpostata;
	}

	public String getForm() {
		return form;
	}

	public void setForm(String form) {
		this.form = form;
	}

	public String getSegmento_cliente() {
		return segmento_cliente;
	}

	public void setSegmento_cliente(String segmento_cliente) {
		this.segmento_cliente = segmento_cliente;
	}

	public LocalDate getData_esito() {
		LocalDate localDate;
		if (data_esito != null) {
			localDate = Utility.convertDateToLocalDate(data_esito);
		}else {
			localDate = null;
		}
		return localDate;
	}

	public void setData_esito(Date data_esito) {
		this.data_esito = data_esito;
	}

	public LocalDate getData_inserimento() {
		LocalDate localDate;
		if (data_inserimento != null) {
			localDate = Utility.convertDateToLocalDate(data_inserimento);
		}else {
			localDate = null;
		}
		return localDate;
		
	}

	public void setData_inserimento(Date data_inserimento) {
		this.data_inserimento = data_inserimento;
	}



}
