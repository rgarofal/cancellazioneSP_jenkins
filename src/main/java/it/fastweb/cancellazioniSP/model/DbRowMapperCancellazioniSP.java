package it.fastweb.cancellazioniSP.model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import it.fastweb.cancellazioniSP.model.WorkOrderSP;

public class DbRowMapperCancellazioniSP  {

	private static final String ID = "id";
	private static final String PROCESSO = "processo";
	private static final String IDENTIFICATIVO = "identificativo";
	private static final String DATA_INSERIMENTO = "Data_inserimento";
	private static final String DATA_ESECUZIONE = "Data_esecuzione";
	private static final String ESITO = "Esito";
	private static final String DATA_ESITO  = "Data_esito";
	private static final String BPA_AGENT  = "bpa_agent";

	


	
	private static final String ESITO_DETTAGLI =  "Esito_Dettagli";
	
	  

	public Elemento mapRow(ResultSet rs, int rowNum) throws SQLException {

		Elemento dati_cancellazione_SP  = new Elemento();

		dati_cancellazione_SP.setProcesso(rs.getString(PROCESSO));
		dati_cancellazione_SP.setIdentificativo(rs.getString(IDENTIFICATIVO));
		dati_cancellazione_SP.setId(rs.getString(ID));
		
		return dati_cancellazione_SP ;
	}

}
