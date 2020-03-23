package it.fastweb.cancellazioniSP.model;

public class Elemento {
	private String id;
	private String processo;
	public String identificativo;
	private String dettaglio_codice_chiusura;
	private String automa;
	private String codice_transizione;
	private String order_ID;
	private String guid;
	private String process_CFG_ID;
	private String fo_ID;
	private String nome_FO;
	private String processo_wo;
	private String dettaglio_codice_attesa; // Che � lo stesso di dettaglio codice chiusura
	private String site_ID;
	private String account_no;
	private String process_ID;
	
	private boolean esito;
	private String errore;
	private String messaggio_esito = "Regolare";
	private boolean eseguito;
	
	public Elemento() {
		
	}
	
	public Elemento(String identificativo, String processo, String id, String automa)
	{
		this.identificativo = identificativo;
		this.processo = processo;
		this.id = id;
		this.dettaglio_codice_chiusura = "";
		this.automa = automa;
		this.codice_transizione = "";
		this.order_ID = "";
		this.guid = "";
		this.esito = false;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getProcesso() {
		return processo;
	}

	public void setProcesso(String processo) {
		this.processo = processo;
	}

	public String getIdentificativo() {
		return identificativo;
	}

	public void setIdentificativo(String identificativo) {
		this.identificativo = identificativo;
	}

	public String getDettaglio_codice_chiusura() {
		return dettaglio_codice_chiusura;
	}
	
	public boolean is_set_Dettaglio_codice_chiusura() {
		boolean esito = false;
		if (this.dettaglio_codice_chiusura != null ||
	        !this.dettaglio_codice_chiusura.equals("")){
	        	esito = true;
	        } else {
	        	
	        }
	   return esito;
	}

	public void setDettaglio_codice_chiusura(String dettaglio_codice_chiusura) {
		this.dettaglio_codice_chiusura = dettaglio_codice_chiusura;
	}

	public String getAutoma() {
		return automa;
	}

	public void setAutoma(String automa) {
		this.automa = automa;
	}

	public String getCodice_transizione() {
		return codice_transizione;
	}

	public void setCodice_transizione(String codice_transizione) {
		this.codice_transizione = codice_transizione;
	}

	public String getOrder_ID() {
		return order_ID;
	}

	public void setOrder_ID(String order_ID) {
		this.order_ID = order_ID;
	}

	public String getGuid() {
		return guid;
	}

	public void setGuid(String guid) {
		this.guid = guid;
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

	public String getDettaglio_codice_attesa() {
		return dettaglio_codice_attesa;
	}

	public void setDettaglio_codice_attesa(String dettaglio_codice_attesa) {
		this.dettaglio_codice_attesa = dettaglio_codice_attesa;
	}

	public String getSite_ID() {
		return site_ID;
	}

	public void setSite_ID(String site_ID) {
		this.site_ID = site_ID;
	}

	public String getAccount_no() {
		return account_no;
	}

	public void setAccount_no(String account_no) {
		this.account_no = account_no;
	}

	public String getProcess_ID() {
		return process_ID;
	}

	public void setProcess_ID(String process_ID) {
		this.process_ID = process_ID;
	}

	public boolean isEsito() {
		return esito;
	}

	public void setEsito(boolean esito) {
		this.esito = esito;
	}

	public String getErrore() {
		return errore;
	}

	public void setErrore(String errore) {
		this.errore = errore;
	}

	public boolean isEseguito() {
		return eseguito;
	}

	public void setEseguito(boolean eseguito) {
		this.eseguito = eseguito;
	}

	public String getMessaggio_esito() {
		return messaggio_esito;
	}

	public void setMessaggio_esito(String messaggio_esito) {
		this.messaggio_esito = messaggio_esito;
	}

}

