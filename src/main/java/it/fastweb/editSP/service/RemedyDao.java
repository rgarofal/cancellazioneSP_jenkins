package it.fastweb.editSP.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bmc.arsys.api.ARException;
import com.bmc.arsys.api.ARServerUser;
import com.bmc.arsys.api.CharacterField;
import com.bmc.arsys.api.Entry;
import com.bmc.arsys.api.EnumItem;
import com.bmc.arsys.api.Field;
import com.bmc.arsys.api.QualifierInfo;
import com.bmc.arsys.api.SelectionField;
import com.bmc.arsys.api.SelectionFieldLimit;
import com.bmc.arsys.api.Value;

import it.fastweb.editSP.model.WorkOrderSP;

public class RemedyDao {

	private static ARServerUser ctx;
	private static Logger logger = LoggerFactory.getLogger(RemedyDao.class);
	private static Boolean config_test = false;

	public RemedyDao(ARServerUser session) {
		super();
		ctx = session;
	}


	/*
	 * In base alla tecnologia, si usa un form diverso, restituito da questo metodo.
	 * 
	 */
	private String getForm(String tech)
	{
		if(tech.equalsIgnoreCase("ADSL"))
			return "SP_DSL_WO_ALL";
		else if(tech.equalsIgnoreCase("ADSL_WS"))
			return "SP_HSL_WO_ALL";
		else if(tech.equalsIgnoreCase("SUBULL"))
			return "SP_SLU_WO";
		else if(tech.equalsIgnoreCase("FIBRA"))
			return "";

		return "";
	}


	/*
	 * In caso di tecnologia FTTS, � necessario creare esplicitamente la transizione in SP.
	 * 
	 */
	private boolean creaTransizioneFTTS(WorkOrderSP workorder_da_processare) throws ARException
	{
		String schemaName = "SP_SLU_WO_TRANSIZIONI";
		Entry entry = new Entry();

		int[] intArray = new int[10];
		intArray[0] = 7;	// Stato
		intArray[1] = 2;	// Utente
		intArray[2] = 8;	// Nota -> .
		intArray[3] = 700000086;	// WO ID
		intArray[4]	= 800000023;	// Codice di attesa
		intArray[5]	= 800000223;	// Dettaglio Codice Attesa
		intArray[6]	= 800000005;	// Codice di Chiusura
		intArray[7]	= 800000205;	// Dettaglio Codice Chiusura
		intArray[8]	= 802000001;	// Order ID
		intArray[9]	= 800000542;	// GUID


		entry.put(intArray[1],new Value(workorder_da_processare.getAutoma()));
		entry.put(intArray[2],new Value("."));
		entry.put(intArray[3],new Value(workorder_da_processare.getIdentificativo()));	
		entry.put(intArray[8],new Value(workorder_da_processare.getOrder_id()));
		entry.put(intArray[9],new Value(workorder_da_processare.getGuid()));

		if(workorder_da_processare.getNuovo_stato() != null && !workorder_da_processare.getNuovo_stato().isEmpty()) {
			entry.put(intArray[0],new Value(workorder_da_processare.getNuovo_stato()));
		}else {
			entry.put(intArray[0],new Value(workorder_da_processare.getStato()));
		}
		if(workorder_da_processare.getNuovo_codice_attesa() != null && !workorder_da_processare.getNuovo_codice_attesa().isEmpty()) {
			entry.put(intArray[4],new Value(workorder_da_processare.getNuovo_codice_attesa()));
		}
		if(workorder_da_processare.getNuovo_dett_codice_attesa() != null && !workorder_da_processare.getNuovo_dett_codice_attesa().isEmpty()) {
			entry.put(intArray[5],new Value(workorder_da_processare.getNuovo_dett_codice_attesa()));
		}
		if(workorder_da_processare.getNuovo_codice_chiusura() != null && !workorder_da_processare.getNuovo_codice_chiusura().isEmpty()) {
			entry.put(intArray[6],new Value(workorder_da_processare.getNuovo_codice_chiusura()));
		}
		if(workorder_da_processare.getNuovo_dett_codice_chiusura() != null && !workorder_da_processare.getNuovo_dett_codice_chiusura().isEmpty()) {
			entry.put(intArray[7],new Value(workorder_da_processare.getNuovo_dett_codice_chiusura()));
		}
		if(config_test) {
			logger.info("TEST Transazione Remedy Codice Chiusura e Codice Attesa ");
			workorder_da_processare.setCodice_transizione("REMEDY_TRANS_MOCK");
		}else {
			logger.info("Transazione Remedy Codice Chiusura e Codice Attesa ");
			workorder_da_processare.setCodice_transizione(ctx.createEntry(schemaName, entry));
		}

		return true;	
	}

	/*
	 * C'� bisogno sia di creare il wo successivo, sia la sua relativa transizione.
	 * 
	 */
	public boolean creaWoSuccessivo(WorkOrderSP workorder_da_processare)
	{
		// Creo l'oggetto relativo al nuovo wo
		WorkOrderSP nuovo_elemento = new WorkOrderSP(
				"", //rco_id
				"", //identificativo
				"", //azione_preimpostata
				workorder_da_processare.getTech(),
				"", //form
				"", //acoount_no
				"",	// nome_wo
				"", // segmento_cliente
				"", // attuale_stato
				"", // attuale_codice_attesa
				"", // attuale_dett_codice_attesa
				"", // attuale_codice_chiusura
				"", // attuale_dett_codice_chiusura
				"", // nuovo_stato
				"", // nuovo_codice_attesa
				"", // nuovo_dett_codice_attesa
				"", // nuovo_codice_chiusura
				"", // nuovo_dett_codice_chiusura
				"", // note_pm
				workorder_da_processare.getAutoma(),
				"", // nuovo_WO_nome
				"", // nuovo_WO_stato
				"", // nuovo_WO_codice_attesa
				"", // nuovo_WO_codice_attesa_precedente
				"", // nuovo_WO_dettaglio_codice_attesa
				"", // cellulare
				""  // order_id
				);

		if(!workorder_da_processare.getNuovo_WO_stato().isEmpty() && !workorder_da_processare.getNuovo_WO_codice_attesa().isEmpty())
		{
			try
			{
				int[] intArray = new int[16];
				intArray[0] = 536870922;	// Account no
				intArray[1] = 700000013;	// Process CFG ID
				intArray[2] = 7;			// Stato -> In attesa
				intArray[3] = 700000082;	// FO ID
				intArray[4] = 700000087;	// Creato Da WO
				intArray[5] = 700000221;	// Codice di attesa precedente -> NO
				intArray[6] = 700000222;	// Shadow.Codice di attesa -> Assegnazione Risorse su NETDB
				intArray[7] = 800000001;	// Nome_WO -> ASSEGNAZIONE RISORSE
				intArray[8] = 800000007;	// Nome FO
				intArray[9] = 800000023;	// Codice di attesa -> Assegnazione Risorse su NETDB
				intArray[10] = 800000029;	// Process ID
				intArray[11] = 800000041;	// Processo
				intArray[12] = 800000223;	// Dettaglio Codice Attesa
				intArray[13] = 800000778;	// Operatore Ultima Modifica
				intArray[14] = 802000001;	// Order ID			
				intArray[15] = 802000002;	// Site ID

				// Passaggio di tutti i parametri (anche se pu� essere superfluo)
				nuovo_elemento.setAccount_no(workorder_da_processare.getAccount_no());
				nuovo_elemento.setProcess_CFG_ID( workorder_da_processare.getProcess_CFG_ID());
				nuovo_elemento.setFo_ID(workorder_da_processare.getFo_ID());
				nuovo_elemento.setNome_FO(workorder_da_processare.getNome_FO());
				nuovo_elemento.setProcess_ID( workorder_da_processare.getProcess_ID());
				nuovo_elemento.setProcesso_wo(workorder_da_processare.getProcesso_wo());
				nuovo_elemento.setAutoma(workorder_da_processare.getAutoma());
				nuovo_elemento.setOrder_id(workorder_da_processare.getOrder_id());
				nuovo_elemento.setSite_ID(workorder_da_processare.getSite_ID());


				Entry entry = new Entry();
				entry.put(intArray[0],new Value(nuovo_elemento.getAccount_no()));
				entry.put(intArray[1],new Value(nuovo_elemento.getProcess_CFG_ID()));
				entry.put(intArray[2],new Value(workorder_da_processare.getNuovo_WO_stato()));
				entry.put(intArray[3],new Value(nuovo_elemento.getFo_ID()));
				entry.put(intArray[4],new Value(workorder_da_processare.getNome_wo()));	

				if(!workorder_da_processare.getNuovo_WO_codice_attesa_precedente().isEmpty()) {
					entry.put(intArray[5],new Value(workorder_da_processare.getNuovo_WO_codice_attesa_precedente()));
				}

				entry.put(intArray[6],new Value(workorder_da_processare.getNuovo_WO_codice_attesa()));
				entry.put(intArray[7],new Value(workorder_da_processare.getNuovo_WO_nome()));
				entry.put(intArray[8],new Value(nuovo_elemento.getNome_FO()));
				entry.put(intArray[9],new Value(workorder_da_processare.getNuovo_WO_codice_attesa()));
				entry.put(intArray[10],new Value(nuovo_elemento.getProcess_ID()));
				entry.put(intArray[11],new Value(nuovo_elemento.getProcesso_wo()));

				if(!workorder_da_processare.getNuovo_WO_dett_codice_attesa().isEmpty()) {
					entry.put(intArray[12],new Value(workorder_da_processare.getNuovo_WO_dett_codice_attesa()));
				}
				entry.put(intArray[13],new Value(nuovo_elemento.getAutoma()));
				entry.put(intArray[14],new Value(nuovo_elemento.getOrder_id()));
				entry.put(intArray[15],new Value(nuovo_elemento.getSite_ID()));
				if ( config_test) {
					logger.info("TEST WO successivo creato ");
					nuovo_elemento.setIdentificativo("IDENTIF_WO_MOCK");
				}else {
					nuovo_elemento.setIdentificativo(ctx.createEntry(getForm(nuovo_elemento.getTech()), entry));  
					logger.info("WO successivo creato: " +nuovo_elemento.getIdentificativo());
				}



                logger.info("Carico informazioni per il nuovo WO");;
				// Una volta creato l'elemento, � necessario reperire i campi impostati, come il GUID
				caricaDettagliWO(nuovo_elemento);

				int[] intArrayTransizione = new int[17];
				intArrayTransizione[0] = 2;	// Utente
				intArrayTransizione[1] = 7;	// Stato -> In attesa
				intArrayTransizione[2] = 8;	// Nota -> .
				intArrayTransizione[3] = 700000086;	// WO ID -> identificativo
				intArrayTransizione[4] = 700000090;	// From Codice di Chiusura -> Ordine Cancellato   (scritto con solo le iniziali maiuscole)
				intArrayTransizione[5] = 700000092;	// From Dettaglio Codice Chiusura -> dettaglio_codice_attesa
				intArrayTransizione[6] = 700000094;	// From FO ID
				intArrayTransizione[7] = 700000095;	// From Process ID
				intArrayTransizione[8] = 700000096;	// From Nome FO
				intArrayTransizione[9] = 700000097;	// From Processo
				intArrayTransizione[10] = 700000100;	// From Nome WO -> SBLOCCO CLT POST/PRE/PRE OLO INVIO
				intArrayTransizione[11] = 707000507;	// From Stato -> Chiuso
				intArrayTransizione[12] = 800000023;	// Codice di attesa -> Assegnazione Risorse su NETDB
				intArrayTransizione[13] = 800000223;	// Dettaglio Codice Attesa -> dettaglio_codice_attesa
				intArrayTransizione[14] = 800000542;	// GUID
				intArrayTransizione[15] = 802000001;	// Order ID


				String form = "SP_SLU_WO_TRANSIZIONI";

				String queryString = "'WO ID' = \"" +nuovo_elemento.getIdentificativo()+"\"";  
				QualifierInfo qual = ctx.parseQualification(form, queryString);   
				List<Entry> entries = ctx.getListEntryObjects(form, qual, 0, 0, null, null, false, null); 	
				Entry transizione = ctx.getEntry(form, entries.get(0).getEntryId(), intArray);


				transizione.put(intArrayTransizione[0],new Value(nuovo_elemento.getAutoma()));
				transizione.put(intArrayTransizione[1],new Value(workorder_da_processare.getNuovo_WO_stato()));
				transizione.put(intArrayTransizione[2],new Value("."));
				transizione.put(intArrayTransizione[3],new Value(nuovo_elemento.getIdentificativo()));
				transizione.put(intArrayTransizione[4],new Value(workorder_da_processare.getNuovo_codice_chiusura()));

				//				if(!workorder_da_processare.nuovo_WO_dettaglio_codice_attesa.isEmpty())
				//					transizione.put(intArrayTransizione[5],new Value(""));

				transizione.put(intArrayTransizione[6],new Value(nuovo_elemento.getFo_ID()));
				transizione.put(intArrayTransizione[7],new Value(nuovo_elemento.getProcess_ID()));
				transizione.put(intArrayTransizione[8],new Value(nuovo_elemento.getNome_FO()));
				transizione.put(intArrayTransizione[9],new Value(nuovo_elemento.getProcesso_wo()));
				transizione.put(intArrayTransizione[10],new Value(workorder_da_processare.getNome_wo()));	             
				transizione.put(intArrayTransizione[11],new Value(workorder_da_processare.getNuovo_stato()));
				transizione.put(intArrayTransizione[12],new Value(workorder_da_processare.getNuovo_WO_codice_attesa()));

				if(!workorder_da_processare.getNuovo_WO_dett_codice_attesa().isEmpty()) {
					transizione.put(intArrayTransizione[13],new Value(workorder_da_processare.getNuovo_WO_dett_codice_attesa()));
				}
				transizione.put(intArrayTransizione[14],new Value(nuovo_elemento.getGuid()));
				transizione.put(intArrayTransizione[15],new Value(nuovo_elemento.getOrder_id()));		
                if(config_test) {
                	logger.info("TEST Configurazione transizione per SP_SLU_WO_TRANSIZIONI");
                }else {
                	logger.info("Configurazione transizione per SP_SLU_WO_TRANSIZIONI");
                	ctx.setEntry(form, entries.get(0).getEntryId(), transizione, null, 0);
                }
				
				return true;
			}
			catch (ARException e) {   
				logger.error(e.getMessage() +" INFO: identificativo generatore: " +workorder_da_processare.getIdentificativo());
				return false;
			}
		}
		else
			return false;	
	}






	/*
	 * Le note PM vengono gestite in maniera leggermente differente tra le tecnologie
	 * 
	 */
	private boolean modificaNotePM(WorkOrderSP workorder_da_processare)
	{
		try{

			int[] intArray = new int[3];
			intArray[0] = 800000479;	// Note PM - ADSL e SUBULL


			String schemaName = "";
			Entry entry = new Entry();

			if(workorder_da_processare.getTech().equalsIgnoreCase("SUBULL"))
			{
				intArray[1] = 707000080;	// Cliente contattato
				//				intArray[2] = 800000478;	// Note - Solo in SUBULL
				intArray[2] = 800000479;	// Note PM - Solo in SUBULL
				schemaName = "SLU:CHK CALL CON RIS";
			}
			else if(workorder_da_processare.getTech().equalsIgnoreCase("ADSL"))
			{
				intArray[1] = 804000150;	// Cliente contattato
				intArray[2] = 1;			// Per passare qualcosa
				schemaName = "SP DSL J WOCheckCall RSSP";
			}
			else if(workorder_da_processare.getTech().equalsIgnoreCase("ADSL_WS"))
			{
				intArray[1] = 804000150;	// Cliente contattato
				intArray[2] = 1;			// Per passare qualcosa
				schemaName = "SP_HSL_J_WOCheckCall";
			}

			String queryString = "'WO ID' = \"" +workorder_da_processare.getIdentificativo()+"\"";  
			QualifierInfo qual = ctx.parseQualification(schemaName, queryString);   
			List<Entry> entries = ctx.getListEntryObjects(schemaName, qual, 0, 0, null, null, false, null); 

			/* E' necessario  portare le eventuali note precedenti per non perderle nelle nuove note. */
			if(entries.size() > 0)
			{
				if(workorder_da_processare.getNote_pm() != null && !workorder_da_processare.getNote_pm().isEmpty())
				{
					entry = ctx.getEntry(schemaName, entries.get(0).getEntryId(), intArray);

					Value note = entry.get(intArray[0]);
					String note_precedenti = note.toString();

					entry.put(intArray[0],new Value(workorder_da_processare.getNote_pm()+ "\n\n" +note_precedenti));
					entry.put(intArray[1],new Value("0"));
					if (config_test) {
						logger.info("TEST setEntry modifica Note PM");
					}else {
						logger.info("setEntry modifica Note PM");
						ctx.setEntry(schemaName, entries.get(0).getEntryId(), entry, null, 0);
					}
					return true;
				}

			}
			return false;
		}
		catch (ARException e)
		{  
			// In alcuni casi (come nel form ADSL e WS), l'eccezione non provoca errori: le note vengono scritte comunque.
			// Per questo � necessario gestire il blocco try-catch.
			if(
					(!workorder_da_processare.getTech().equalsIgnoreCase("ADSL") && !e.getMessage().startsWith("ERROR (10000):"))
					&& (!workorder_da_processare.getTech().equalsIgnoreCase("ADSL_WS") && !e.getMessage().startsWith("ERROR (552):"))
					)
			{

				workorder_da_processare.setEsito(false);
				workorder_da_processare.setEsito_message( "MOD_SP_KO");

				workorder_da_processare.setEsito_dettagli(e.getMessage());
				logger.error(workorder_da_processare.getIdentificativo()+": "+e.getMessage());
				return false;
			}
			else
			{
				return true;
			}
		}            
	}
	/*
	 * Per prima cosa, questo metodo viene lanciato per estrarre
	 * tutti i dettagli del wo, per effettuare le eventuali verifiche successive
	 * o per creare le transizioni, che riportano tali dettagli.
	 * 
	 */
	public boolean caricaDettagliWO(WorkOrderSP workorder_da_processare)
	{
		try
		{
			// Per le categorie FTTS
			int[] intArray = new int[15];
			intArray[0] = 7;			// Stato
			intArray[1] = 800000023;	// Codice di Attesa
			intArray[2] = 800000223;	// Dettaglio Codice Attesa
			intArray[3] = 800000005; 	// Codice di chiusura
			intArray[4] = 800000205;	// Dettaglio codice di chiusura
			intArray[5] = 800000542;	// GUID
			intArray[6] = 802000001;	// Order ID
			intArray[7] = 536870922;	// Account no
			intArray[8] = 700000013;	// Process CFG ID
			intArray[9] = 700000082;	// FO ID
			intArray[10] = 800000029;	// Process ID
			intArray[11] = 800000007;	// Nome FO
			intArray[12] = 800000041;	// Processo		
			intArray[13] = 802000002;	// Site ID
			intArray[14] = 800000001;   // Nome WO

			// Per le categorie ADSL/FIBRA/WS
			int[] intArray_ADSL_FIBRA_WS = new int[6];
			intArray_ADSL_FIBRA_WS[0] = 7;			// Stato
			intArray_ADSL_FIBRA_WS[1] = 800000023;	// Codice di Attesa
			intArray_ADSL_FIBRA_WS[2] = 800000223;	// Dettaglio Codice Attesa
			intArray_ADSL_FIBRA_WS[3] = 800000005;	// Codice di Chiusura
			intArray_ADSL_FIBRA_WS[4] = 800000205;	// Dettaglio Codice Chiusura
			intArray_ADSL_FIBRA_WS[5] = 536870922;	// Account no


			Entry entry = new Entry();

			if(workorder_da_processare.getTech().equalsIgnoreCase("SUBULL")) {
				entry = ctx.getEntry(getForm(workorder_da_processare.getTech()), workorder_da_processare.getIdentificativo(), intArray);
			}else if(workorder_da_processare.getTech().equalsIgnoreCase("ADSL")) {
				entry = ctx.getEntry(getForm(workorder_da_processare.getTech()), workorder_da_processare.getIdentificativo(), intArray_ADSL_FIBRA_WS);
			}else if(workorder_da_processare.getTech().equalsIgnoreCase("ADSL_WS")) {
				entry = ctx.getEntry(getForm(workorder_da_processare.getTech()), workorder_da_processare.getIdentificativo(), intArray_ADSL_FIBRA_WS);
			}
			for (Integer fieldID : entry.keySet())
			{  
				Value val = entry.get(fieldID);
				Field field = ctx.getField(getForm(workorder_da_processare.getTech()), fieldID);
				String data_entry = val.toString();

				if (data_entry != null)
				{
					if (field instanceof CharacterField)
					{ 

						if(field.getName().equals("GUID")) {
							workorder_da_processare.setGuid(data_entry);
						}else if(field.getName().equals("Account no")) {
							workorder_da_processare.setAccount_no(data_entry);
						}else if(field.getName().equals("Process CFG ID")) {
							workorder_da_processare.setProcess_CFG_ID(data_entry);
						}else if(field.getName().equals("FO ID")) {
							workorder_da_processare.setFo_ID(data_entry);
						}else if(field.getName().equals("Process ID")) {
							workorder_da_processare.setProcess_ID(data_entry);
						}else if(field.getName().equals("Nome FO")) {
							workorder_da_processare.setNome_FO(data_entry);
						}else if(field.getName().equals("Processo")) {
							workorder_da_processare.setProcesso_wo(data_entry);
						}else if(field.getName().equals("Codice di attesa")) {
							workorder_da_processare.setCodice_attesa(data_entry);
						}else if(field.getName().equals("Codice di Chiusura")) {
							workorder_da_processare.setCodice_chiusura(data_entry);
						}else if(field.getName().equals("Dettaglio Codice Attesa")) {
							workorder_da_processare.setDettaglio_codice_attesa(data_entry);
						}else if(field.getName().equals("Dettaglio Codice Chiusura")) {
							workorder_da_processare.setDettaglio_codice_chiusura(data_entry);
						}else if(field.getName().equals("Nome_WO")) {
							workorder_da_processare.setNome_wo(data_entry);
						}
					}
					else if (field instanceof SelectionField)
					{  
						SelectionFieldLimit sFieldLimit = (SelectionFieldLimit)field.getFieldLimit();  
						if (sFieldLimit != null) 
						{  
							List<EnumItem> eItemList = sFieldLimit.getValues();  
							for (EnumItem eItem : eItemList) 
							{  
								if (eItem.getEnumItemNumber() == Integer.parseInt(data_entry))
								{
									if(field.getName().equals("Stato"))
									{
										workorder_da_processare.setStato(eItem.getEnumItemName());
									}
								}  
							}  
						}  
					}
					else 
					{  
						if(field.getName().equals("Order ID")) {
							workorder_da_processare.setOrder_id(data_entry);
						}else if(field.getName().equals("Site ID")) {
							workorder_da_processare.setSite_ID(data_entry);
						}
					}  
				}
			}
			return true;
		}
		catch (ARException e)
		{  
			logger.error(workorder_da_processare.getIdentificativo() + e.getMessage());
			workorder_da_processare.setEsito_dettagli( e.getMessage());
			return false;
		} 
	}


	public boolean aggiorna(WorkOrderSP workorder_da_processare)
	{
		try
		{
			if(workorder_da_processare.getTech().equalsIgnoreCase("ADSL_WS") 
					&& (workorder_da_processare.getNuovo_stato() != null && !workorder_da_processare.getNuovo_stato().isEmpty() && workorder_da_processare.getNuovo_stato().equalsIgnoreCase("Chiusa")))
			{
				String queryString = "'Order ID' = " +workorder_da_processare.getOrder_id() +"";  
				QualifierInfo qual = ctx.parseQualification("SP_HSL_ORDER_ALL", queryString);
				// entries � solo l'elenco dei risultati che matchano la query effettuata. Ma � necessario prelevare tutte le altre info con getEntry()
				List<Entry> entries = ctx.getListEntryObjects("SP_HSL_ORDER_ALL", qual, 0, 0, null, null, false, null); 

				int[] intArray = new int[2];
				intArray[0] = 800011127; // Cellulare Corretto WS
				intArray[1] = 800011126; // Cellulare Corretto WS

				// Se la lista dei risultati � piena, allora posso prelevarmi l'elemento che mi interessa.
				if(entries.size() > 0)
				{
					Entry entry = ctx.getEntry("SP_HSL_ORDER_ALL", entries.get(0).getEntryId(), intArray);

					entry.put(800011127 ,new Value(workorder_da_processare.getCellulare()));
					// Gianni rutta e piange: se sta valorizzato il 126, forse non va rivalorizzato.
					// Fa il doppio salto dall'oggetto ritornato dal metodo get(), chiamando il metodo toString()
					//					String numero_precedente = entry.get(800011126).toString();
					//					if(numero_precedente.isEmpty())
					entry.put(800011126 ,new Value(workorder_da_processare.getCellulare()));
					if (config_test) {
						logger.info("TEST setEntry SP_HSL_ORDER_ALL");
					} else {
						logger.info("setEntry SP_HSL_ORDER_ALL");
						ctx.setEntry("SP_HSL_ORDER_ALL", entries.get(0).getEntryId(), entry, null, 0);
					}
				}

			}


			int[] intArray = new int[5];
			intArray[0] = 7;			// Stato
			intArray[1] = 800000023;	// Codice di attesa
			intArray[2] = 800000223;	// Dettaglio Codice Attesa
			intArray[3] = 800000005; 	// Codice di chiusura
			intArray[4] = 800000205;	// Dettaglio codice di chiusura

			boolean verifica = true;

			Entry entry = ctx.getEntry(getForm(workorder_da_processare.getTech()), workorder_da_processare.getIdentificativo(), intArray);

			// Verifica se c'� il cambio stato
			if(workorder_da_processare.getNuovo_stato() != null && !workorder_da_processare.getNuovo_stato().isEmpty()) {
				entry.put(intArray[0],new Value(workorder_da_processare.getNuovo_stato()));
			}

			// Verifica se c'� il cambio codice d'attesa
			if(workorder_da_processare.getNuovo_codice_attesa() != null && !workorder_da_processare.getNuovo_codice_attesa().isEmpty()) {
				entry.put(intArray[1],new Value(workorder_da_processare.getNuovo_codice_attesa()));
			}
			// Verifica se c'� il cambio dettaglio codice d'attesa
			if(workorder_da_processare.getNuovo_codice_attesa() != null && !workorder_da_processare.getNuovo_dett_codice_attesa().isEmpty()) {
				entry.put(intArray[2],new Value(workorder_da_processare.getNuovo_dett_codice_attesa()));
			}
			// Verifica se c'� il cambio codice di chiusura
			if(workorder_da_processare.getNuovo_codice_chiusura() != null && !workorder_da_processare.getNuovo_codice_chiusura().isEmpty())
			{
				entry.put(intArray[3],new Value(workorder_da_processare.getNuovo_codice_chiusura()));
				entry.put(intArray[1],new Value(""));
			}

			// Verifica se c'� il cambio dettaglio codice di chiusura
			if(workorder_da_processare.getNuovo_dett_codice_chiusura() != null && !workorder_da_processare.getNuovo_dett_codice_chiusura().isEmpty()) {
				entry.put(intArray[4],new Value(workorder_da_processare.getNuovo_dett_codice_chiusura()));
			}

			if (config_test) {
				logger.info("TEST setEntry cambio codice");
			} else {
				logger.info("setEntry cambio codice");
				ctx.setEntry(getForm(workorder_da_processare.getTech()), workorder_da_processare.getIdentificativo(), entry, null, 0);
			}
			if(modificaNotePM(workorder_da_processare))
			{

				if(workorder_da_processare.getTech().equalsIgnoreCase("SUBULL"))
				{
					creaTransizioneFTTS(workorder_da_processare);
					if(workorder_da_processare.getNuovo_stato() != null && !workorder_da_processare.getNuovo_stato().isEmpty() && workorder_da_processare.getNuovo_stato().equals("Chiuso"))
					{
						if(!creaWoSuccessivo(workorder_da_processare))
						{
							workorder_da_processare.setEsito(false);
							workorder_da_processare.setEsito_message("MOD_SP_KO");
							workorder_da_processare.setEsito_dettagli("WO successivo non creato. Verificare i parametri in db.");
							verifica = false;
						}
					}
				}

				// Verifica che tutti gli aggiornamenti, dove previsti, siano stati eseguiti correttamente.
				if(caricaDettagliWO(workorder_da_processare) && verifica)
				{

					if((workorder_da_processare.getNuovo_stato() != null && !workorder_da_processare.getNuovo_stato().isEmpty() && !workorder_da_processare.getNuovo_stato().equals(workorder_da_processare.getStato()))) {
						verifica = false;
					}
					if(workorder_da_processare.getNuovo_codice_attesa() != null && !workorder_da_processare.getNuovo_codice_attesa().isEmpty() && !workorder_da_processare.getNuovo_codice_attesa().equals(workorder_da_processare.getCodice_attesa())) {
						verifica = false;
					}
					if(workorder_da_processare.getNuovo_dett_codice_attesa() != null && !workorder_da_processare.getNuovo_dett_codice_attesa().isEmpty() && !workorder_da_processare.getNuovo_dett_codice_attesa().equals(workorder_da_processare.getDettaglio_codice_attesa())) {
						verifica = false;
					}
					if(workorder_da_processare.getNuovo_codice_chiusura() != null && !workorder_da_processare.getNuovo_dett_codice_attesa().isEmpty() && !workorder_da_processare.getNuovo_dett_codice_attesa().equals(workorder_da_processare.getCodice_chiusura())) {
						verifica = false;
					}
					if(workorder_da_processare.getNuovo_dett_codice_chiusura() != null && !workorder_da_processare.getNuovo_dett_codice_chiusura().isEmpty() && !workorder_da_processare.getNuovo_dett_codice_chiusura().equals(workorder_da_processare.getDettaglio_codice_chiusura())) {
						verifica = false;
					}
					// Se � entrato qu�, vuol dire che verifica era true, cio� gli altri passi sono stati effettuati senza eccezioni.
					// In questo caso verifica pu� essere false solo se non vengono rilevate le modifiche.
					if(verifica)
					{
						workorder_da_processare.setEsito(true);
						workorder_da_processare.setEsito_message("MOD_SP_OK");
						if(!workorder_da_processare.getEsito_dettagli().isEmpty()) {
							workorder_da_processare.setEsito_dettagli(workorder_da_processare.getEsito_dettagli() +" - "+"Ok verifica cambio stato.");
						}else {
							workorder_da_processare.setEsito_dettagli("Ok");
						}
					}
					else
					{
						workorder_da_processare.setEsito(false);
						workorder_da_processare.setEsito_message( "MOD_SP_KO");
						if(!workorder_da_processare.getEsito_dettagli().isEmpty()) {
							workorder_da_processare.setEsito_dettagli(workorder_da_processare.getEsito_dettagli() +" - "+"Errore rilevamento modifiche.");
						}else {
							workorder_da_processare.setEsito_dettagli("Errore rilevamento modifiche.");
						}

						logger.error("Errore rilevamento modifiche. ID: " +workorder_da_processare.getIdentificativo());
					}
				}
				else
				{
					workorder_da_processare.setEsito(false);
					workorder_da_processare.setEsito_message( "MOD_SP_KO");
					if(!workorder_da_processare.getEsito_dettagli().isEmpty()) {
						workorder_da_processare.setEsito_dettagli("Errore caricamento dettagli WO.");
					}
					return verifica;
				}
			}
			else
			{
				verifica = false;
			}


			return verifica;
		}
		catch (ARException e)
		{  
			workorder_da_processare.setEsito(false);
			workorder_da_processare.setEsito_message( "MOD_SP_KO");
			if(!workorder_da_processare.getEsito_dettagli().isEmpty()) {
				workorder_da_processare.setEsito_dettagli(e.getMessage());
			}
			logger.error(workorder_da_processare.getIdentificativo()+": "+e.getMessage());
			return false;
		}
	}

	//	///TODO) DA CANCELLARE 
	//	public  boolean creaTT(WorkOrderSP ticket ) {
	//        boolean enable_remedy = false;
	//        try {
	//
	//        	int[] intArray = new int[7];
	//			intArray[0] = 600030001; // Tipo Problema
	//			intArray[1] = 600030002; // Cateogria
	//			intArray[2] = 600030003; // Sottocategoria
	//			intArray[3] = 600030014; // Descrizione
	//			intArray[4] = 600000001; // Account
	//			intArray[5] = 112; // Assegnato al Gruppo DB
	//			intArray[6] = 4;  // Assegnato al Gruppo
	//			
	//			String queryString = "'Tipo Problema' = \"" +ticket.getTipo_problema()+"\" "
	//					+ "AND 'Categoria del Problema' = \"" +ticket.getCategoria_problema()+"\" "
	//					+ "AND 'Sottocategoria del Problema' = \"" +ticket.getSottocategoria_problema()+"\"";
	//			
	//			QualifierInfo qual = ctx.parseQualification(form_gruppi, queryString);   
	//			List<Entry> entries = ctx.getListEntryObjects(form_gruppi, qual, 0, 0, null, null, false, null); 
	//			
	//			Entry entryForm = ctx.getEntry(form_gruppi, entries.get(0).getEntryId(), null); 
	//			
	//			
	//			logger.info("Elaborazione Ticket su remedy");
	//					
	//			ticket.setElaborato(true);
	//
	//			if (enable_remedy) {
	//				Entry entry = new Entry();
	//	            
	//	            entry.put(intArray[0],new Value(ticket.getTipo_problema()));
	//				entry.put(intArray[1],new Value(ticket.getCategoria_problema()));
	//				entry.put(intArray[2],new Value(ticket.getSottocategoria_problema()));
	//				entry.put(intArray[3],new Value(ticket.getDescrizione()));
	//				entry.put(intArray[4],new Value(ticket.getAccount()));
	//				
	//				Value numeroGruppo = entryForm.get(112);
	//				entry.put(intArray[5],new Value(numeroGruppo.toString()));
	//				
	//				
	//				Value gruppo = entryForm.get(600030005);
	//				entry.put(intArray[6],new Value(gruppo.toString()));
	//				
	//	            ticket.setIdentificativo(ctx.createEntry(form, entry));  
	//				
	//				ticket.setEsito(true);
	//			}else {
	//                ticket.setIdentificativo("XXXXXX");  
	//				ticket.setEsito(true);
	//			}
	//            
	//
	//			logger.info("Ticket nr. "+ticket.getIdentificativo()+" creato correttamente.");
	//			
	//        } catch (ARException e) {
	//            logger.error(e.getMessage());
	//            ticket.setElaborato(true);
	//            ticket.setErrore(e.getMessage());
	//            return false;
	//        } catch(Exception e)
	//		{
	//			logger.error("IL TASK E' FERMO!!! Errore: " + e.getMessage()+" - Chiave processo master: "+ticket.getChiave_processo_master()+". Questa terna puo' essere aperta da RCO? In tal caso, controllare la sintassi della tripletta (es: maiuscole/minuscole..).");
	//			// Exit
	//			return false;
	//		}
	//        return true;
	//    }
}
