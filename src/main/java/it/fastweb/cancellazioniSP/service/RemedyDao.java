package it.fastweb.cancellazioniSP.service;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bmc.arsys.api.ARException;
import com.bmc.arsys.api.ARServerUser;
import com.bmc.arsys.api.AttachmentField;
import com.bmc.arsys.api.AttachmentValue;
import com.bmc.arsys.api.CharacterField;
import com.bmc.arsys.api.CurrencyField;
import com.bmc.arsys.api.CurrencyValue;
import com.bmc.arsys.api.DateTimeField;
import com.bmc.arsys.api.DiaryField;
import com.bmc.arsys.api.DiaryItem;
import com.bmc.arsys.api.DiaryListValue;
import com.bmc.arsys.api.Entry;
import com.bmc.arsys.api.EntryKey;
import com.bmc.arsys.api.EnumItem;
import com.bmc.arsys.api.Field;
import com.bmc.arsys.api.FuncCurrencyInfo;
import com.bmc.arsys.api.QualifierInfo;
import com.bmc.arsys.api.SelectionField;
import com.bmc.arsys.api.SelectionFieldLimit;
import com.bmc.arsys.api.StatusHistoryItem;
import com.bmc.arsys.api.StatusHistoryValue;
import com.bmc.arsys.api.Timestamp;
import com.bmc.arsys.api.Value;


import it.fastweb.cancellazioniSP.model.Elemento;


public class RemedyDao {

	private static ARServerUser ctx;
	private static Logger logger = LoggerFactory.getLogger(RemedyDao.class);
	private static Boolean config_test = false;

	public RemedyDao(ARServerUser session) {
		super();
		ctx = session;
	}


//	/*
//	 * In base alla tecnologia, si usa un form diverso, restituito da questo metodo.
//	 * 
//	 */
//	private String getForm(String tech)
//	{
//		if(tech.equalsIgnoreCase("ADSL"))
//			return "SP_DSL_WO_ALL";
//		else if(tech.equalsIgnoreCase("ADSL_WS"))
//			return "SP_HSL_WO_ALL";
//		else if(tech.equalsIgnoreCase("SUBULL"))
//			return "SP_SLU_WO";
//		else if(tech.equalsIgnoreCase("FIBRA"))
//			return "";
//
//		return "";
//	}
	
	/*
     * Per ottimizzare, si potrebbe passare semplicemente l'oggetto in cui tutti i campi da settare sono
     * mappati con chiave-valore, cos� da generalizzare e lasciare la logica a seconda del tipo di elemento e quindi
     * delle chiavi.
     * 
     */
    public  boolean cancellazione(Elemento elemento) 
    {
    	try {

    		if(elemento.getProcesso().equalsIgnoreCase("CANC_COMM") 
    				|| elemento.getProcesso().equalsIgnoreCase("CANC_TEC"))
    		{
        		String form = "OFTS Crea Eventi"; 

        		int[] intArray = new int[3];
        		intArray[0] = 800000200; 		// Invio Evento Cancellato
        		intArray[1] = 7;				// Stato 
        		intArray[2] = 800000005;		// Codice di chiusura

        		Entry entry = ctx.getEntry(form, elemento.getIdentificativo(), intArray);
    			entry.put(intArray[0],new Value("Invia cancellazione"));
    			ctx.setEntry(form, elemento.getIdentificativo(), entry, null, 0);
    			logger.info("Entry #" + elemento.getIdentificativo() +" settato a " +elemento.getProcesso()+ " con successo.");
    			return true;
    		}
    		else if(elemento.getProcesso().equalsIgnoreCase("CH_WO_SBLOCCO_ADSL")
    				|| elemento.getProcesso().equalsIgnoreCase("CH_WO_SBLOCCO_FIBRA")
    				|| elemento.getProcesso().equalsIgnoreCase("CH_WO_SBLOCCO_WS"))
    		{
        		String form = ""; 
        		
               	if(elemento.getProcesso().equalsIgnoreCase("CH_WO_SBLOCCO_ADSL"))
               		form = "SP_DSL_WO_ALL";
            	else if(elemento.getProcesso().equalsIgnoreCase("CH_WO_SBLOCCO_FIBRA"))
            		form = "OFTS WO";
            	else if(elemento.getProcesso().equalsIgnoreCase("CH_WO_SBLOCCO_WS"))
            		form = "SP_HSL_WO_ALL";

        		int[] intArray = new int[3];
        		intArray[0] = 7;				// Stato 
        		intArray[1] = 800000005;		// Codice di chiusura -> Ordine Cancellato
        		intArray[2] = 800000205;		// Dettaglio codice chiusura

        		Entry entry = ctx.getEntry(form, elemento.getIdentificativo(), intArray);
        		
        		entry.put(intArray[0],new Value("Chiusa"));
    			entry.put(intArray[1],new Value("Ordine Cancellato"));
    			entry.put(intArray[2],new Value(elemento.getDettaglio_codice_chiusura()));
    		
    			if(elemento.is_not_set_Dettaglio_codice_chiusura())
    			{
    				logger.error("Dettaglio codice chiusura non presente. INFO: id: " +elemento.getIdentificativo());
    				return false;
    			}
    			    			
    			ctx.setEntry(form, elemento.getIdentificativo(), entry, null, 0);
    			logger.info("Entry #" + elemento.getIdentificativo() +" settato a " +elemento.getProcesso()+ " con successo.");
    			return true;
    		}
    		else if(elemento.getProcesso().equalsIgnoreCase("CH_WO_FTTS_POST")
    				|| elemento.getProcesso().equalsIgnoreCase("CH_WO_FTTS_PRE")
    				|| elemento.getProcesso().equalsIgnoreCase("CH_WO_FTTS_PRE_OLO"))
    		{
    			String form = "";
    			
    			if(elemento.getProcesso().equalsIgnoreCase("CH_WO_FTTS_POST")
    					|| elemento.getProcesso().equalsIgnoreCase("CH_WO_FTTS_PRE")
    					|| elemento.getProcesso().equalsIgnoreCase("CH_WO_FTTS_PRE_OLO"))
            		form = "SP_SLU_WO";
    			
        		int[] intArray = new int[16];
        		intArray[0] = 7;			// Stato 
        		intArray[1] = 800000005;	// Codice di chiusura
        		intArray[2] = 800000205;	// Dettaglio codice chiusura
        		intArray[3] = 800000023;	// Codice di attesa
        		intArray[4] = 800000223;	// Dettaglio codice di attesa
        		intArray[5] = 800000776;	// WO Chiuso da
        		intArray[6] = 800000774;	// Variazione eseguita da
        		intArray[7] = 700000222;	// Shadow.Codice di attesa -> Cancellazione Tecnica
        		intArray[8] = 536870922;	// Account no
        		intArray[9] = 700000013;	// Process CFG ID
        		intArray[10] = 700000082;	// FO ID
        		intArray[11] = 700000087;	// Creato Da WO
        		intArray[12] = 800000029;	// Process ID
    			intArray[13] = 800000041;	// Processo
    			intArray[14] = 802000001;	// Order ID			
    			intArray[15] = 802000002;	// Site ID

        		Entry entry = ctx.getEntry(form, elemento.getIdentificativo(), intArray);

        		entry.put(intArray[0],new Value("Chiuso"));
        		entry.put(intArray[1],new Value("Ordine Cancellato"));
        		entry.put(intArray[2],new Value(elemento.getDettaglio_codice_chiusura()));
        		entry.put(intArray[3],new Value(""));
        		entry.put(intArray[4],new Value(""));
        		entry.put(intArray[5],new Value(elemento.getAutoma()));
        		entry.put(intArray[6],new Value(elemento.getAutoma()));  			
    			entry.put(intArray[7],new Value("Cancellazione Tecnica"));
    			
    			if(elemento.is_not_set_Dettaglio_codice_chiusura())
    			{
    				logger.error("Dettaglio codice chiusura non presente. INFO: id: " +elemento.getIdentificativo());
    				return false;
    			}
    				
    			ctx.setEntry(form, elemento.getIdentificativo(), entry, null, 0);
    			
    			creaTransizione(elemento, "Chiusura");
    			
    			creaWoOrdineCancellazione(elemento);
    				
    			logger.info("Entry #" + elemento.getIdentificativo() +" settato a " +elemento.getProcesso()+ " con successo.");
    			return true;
    		}
    		else
    		{
    			logger.error("Tipo processo non gestito.");
    			return false;
    		}
    	}
    	catch(ARException e) {
    		logger.error(e.getMessage() +" INFO: identificativo: " +elemento.getIdentificativo());
    		e.printStackTrace();
    		return false;
    	}
    }
    
    private  String creaTransizione(Elemento elemento, String tipologia)
	{
		try
		{
			String schemaName = "SP_SLU_WO_TRANSIZIONI";
			Entry entry = new Entry();
			
			if(tipologia.equals("Chiusura"))
			{
				int[] intArray = new int[8];
				intArray[0] = 7;	// Stato -> Chiuso
				intArray[1] = 2;	// Utente
				intArray[2] = 8;	// Nota -> .
				intArray[3] = 700000086;	// WO ID -> identificativo
				intArray[4]	= 800000005;	// Codice di Chiusura -> Ordine Cancellato
				intArray[5]	= 800000205;	// Dettaglio Codice Chiusura
				intArray[6]	= 802000001;	// Order ID
				intArray[7]	= 800000542;	// GUID


				entry.put(intArray[0],new Value("Chiuso"));
				entry.put(intArray[1],new Value(elemento.getAutoma()));
				entry.put(intArray[2],new Value("."));
				entry.put(intArray[3],new Value(elemento.identificativo));
				entry.put(intArray[4],new Value("Ordine Cancellato"));
				entry.put(intArray[5],new Value(elemento.getDettaglio_codice_chiusura()));
				entry.put(intArray[6],new Value(elemento.getOrder_ID()));
				entry.put(intArray[7],new Value(elemento.getGuid()));	
			}
			
			elemento.setCodice_transizione(ctx.createEntry(schemaName, entry));
			logger.info("Transizione creata: " +elemento.getCodice_transizione()+ ". Identificativo WO: " +elemento.identificativo); 
			
		}
			catch (ARException e) {   
			logger.error(e.getMessage() +" INFO: identificativo: " +elemento.identificativo);
		} 
    		
		return elemento.getCodice_transizione();	
	}
	
    
	
    
    private  Elemento creaWoOrdineCancellazione(Elemento elemento_generatore)
	{
		Elemento elemento = new Elemento("",elemento_generatore.getProcesso(), elemento_generatore.getId(), elemento_generatore.getAutoma());

		try
		{
			int[] intArray = new int[16];
			intArray[0] = 536870922;	// Account no
			intArray[1] = 700000013;	// Process CFG ID
			intArray[2] = 7;			// Stato -> In attesa
			intArray[3] = 700000082;	// FO ID
			intArray[4] = 700000087;	// Creato Da WO
			intArray[5] = 700000221;	// Codice di attesa precedente -> Approvazione Cancellazione
			intArray[6] = 700000222;	// Shadow.Codice di attesa -> Approvazione Cancellazione
			intArray[7] = 800000001;	// Nome_WO -> ORDINE CANCELLATO
			intArray[8] = 800000007;	// Nome FO
			intArray[9] = 800000023;	// Codice di attesa -> Approvazione Cancellazione
			intArray[10] = 800000029;	// Process ID
			intArray[11] = 800000041;	// Processo
			intArray[12] = 800000223;	// Dettaglio Codice Attesa
			intArray[13] = 800000778;	// Operatore Ultima Modifica
			intArray[14] = 802000001;	// Order ID			
			intArray[15] = 802000002;	// Site ID
			
			// Passaggio di tutti i parametri (anche se pu� essere superfluo)
			elemento.setAccount_no(elemento_generatore.getAccount_no());
			elemento.setProcess_CFG_ID(elemento_generatore.getProcess_CFG_ID());
			elemento.setFo_ID(elemento_generatore.getFo_ID());
			elemento.setNome_FO(elemento_generatore.getNome_FO());
			elemento.setProcess_ID(elemento_generatore.getProcess_ID());
			elemento.setProcesso_wo(elemento_generatore.getProcesso_wo());
			elemento.setDettaglio_codice_attesa(elemento_generatore.getDettaglio_codice_chiusura());
			elemento.setAutoma(elemento_generatore.getAutoma());
			elemento.setOrder_ID(elemento_generatore.getOrder_ID());
			elemento.setSite_ID(elemento_generatore.getSite_ID());
			
			String schemaName = "";
			
        	if(elemento.getProcesso().equalsIgnoreCase("CH_WO_FTTS_POST")
        			|| elemento.getProcesso().equalsIgnoreCase("CH_WO_FTTS_PRE")
        			|| elemento.getProcesso().equalsIgnoreCase("CH_WO_FTTS_PRE_OLO"))
        		schemaName = "SP_SLU_WO";
        	
        	Entry entry = new Entry();
			entry.put(intArray[0],new Value(elemento.getAccount_no()));
			entry.put(intArray[1],new Value(elemento.getProcess_CFG_ID()));
			entry.put(intArray[2],new Value("In attesa"));
			entry.put(intArray[3],new Value(elemento.getFo_ID()));
			
			if(elemento.getProcesso().equalsIgnoreCase("CH_WO_FTTS_POST"))
				entry.put(intArray[4],new Value("SBLOCCO CLT POST INVIO"));
			else if(elemento.getProcesso().equalsIgnoreCase("CH_WO_FTTS_PRE"))
				entry.put(intArray[4],new Value("SBLOCCO CLT PRE INVIO"));
			else if(elemento.getProcesso().equalsIgnoreCase("CH_WO_FTTS_PRE_OLO"))
				entry.put(intArray[4],new Value("SBLOCCO CLT PRE INVIO OLO"));
			
			entry.put(intArray[5],new Value("Approvazione Cancellazione"));
			entry.put(intArray[6],new Value("Approvazione Cancellazione"));
			entry.put(intArray[7],new Value("ORDINE CANCELLATO"));
			entry.put(intArray[8],new Value(elemento.getNome_FO()));
			entry.put(intArray[9],new Value("Approvazione Cancellazione"));
			entry.put(intArray[10],new Value(elemento.getProcess_ID()));
			entry.put(intArray[11],new Value(elemento.getProcesso_wo()));
			entry.put(intArray[12],new Value(elemento.getDettaglio_codice_attesa()));
			entry.put(intArray[13],new Value(elemento.getAutoma()));
			entry.put(intArray[14],new Value(elemento.getOrder_ID()));
			entry.put(intArray[15],new Value(elemento.getSite_ID()));
			
			elemento.setIdentificativo(ctx.createEntry(schemaName, entry));  
			
			logger.info("WO Ordine di cancellazione creato: " +elemento.getIdentificativo());
			
			// Una volta creato l'elemento, � necessario reperire i campi impostati, come il GUID
			getDettagliWO(elemento);
			
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
			intArrayTransizione[12] = 800000023;	// Codice di attesa -> Approvazione Cancellazione
			intArrayTransizione[13] = 800000223;	// Dettaglio Codice Attesa -> dettaglio_codice_attesa
			intArrayTransizione[14] = 800000542;	// GUID
			intArrayTransizione[15] = 802000001;	// Order ID
			
					
			String form = "SP_SLU_WO_TRANSIZIONI";
			
			String queryString = "'WO ID' = \"" + elemento.getIdentificativo() +"\"";  
			QualifierInfo qual = ctx.parseQualification(form, queryString);   
	        List<Entry> entries = ctx.getListEntryObjects(form, qual, 0, 0, null, null, false, null); 	
	        Entry transizione = ctx.getEntry(form, entries.get(0).getEntryId(), intArray);
			
	        
	        transizione.put(intArrayTransizione[0],new Value(elemento.getAutoma()));
	        transizione.put(intArrayTransizione[1],new Value("In attesa"));
	        transizione.put(intArrayTransizione[2],new Value("."));
	        transizione.put(intArrayTransizione[3],new Value(elemento.getIdentificativo()));
	        transizione.put(intArrayTransizione[4],new Value("Ordine Cancellato"));
	        transizione.put(intArrayTransizione[5],new Value(elemento.getDettaglio_codice_attesa()));
	        transizione.put(intArrayTransizione[6],new Value(elemento.getFo_ID()));
	        transizione.put(intArrayTransizione[7],new Value(elemento.getProcess_ID()));
	        transizione.put(intArrayTransizione[8],new Value(elemento.getNome_FO()));
	        transizione.put(intArrayTransizione[9],new Value(elemento.getProcesso_wo()));
	        
	        if(elemento.getProcesso().equalsIgnoreCase("CH_WO_FTTS_POST"))
	        	transizione.put(intArrayTransizione[10],new Value("SBLOCCO CLT POST INVIO"));
	        else if(elemento.getProcesso().equalsIgnoreCase("CH_WO_FTTS_PRE"))
	        	transizione.put(intArrayTransizione[10],new Value("SBLOCCO CLT PRE INVIO"));
	        else if(elemento.getProcesso().equalsIgnoreCase("CH_WO_FTTS_PRE_OLO"))
	        	transizione.put(intArrayTransizione[10],new Value("SBLOCCO CLT PRE INVIO OLO"));
	                
	        transizione.put(intArrayTransizione[11],new Value("Chiuso"));
	        transizione.put(intArrayTransizione[12],new Value("Approvazione Cancellazione"));
			transizione.put(intArrayTransizione[13],new Value(elemento.getDettaglio_codice_attesa()));
			transizione.put(intArrayTransizione[14],new Value(elemento.getGuid()));
			transizione.put(intArrayTransizione[15],new Value(elemento.getOrder_ID()));		
			
			ctx.setEntry(form, entries.get(0).getEntryId(), transizione, null, 0);
		}
		catch (ARException e) {   
		logger.error(e.getMessage() +" INFO: identificativo generatore: " +elemento_generatore.getIdentificativo());
	} 
		
		return elemento;
	}

	
	public  boolean verificaStatoCancellazione(Elemento elemento) throws ARException
	{
		try {  
			int[] intArray = new int[1];
			intArray[0] = 800000200;	// Invio Evento Cancellato

			String schemaName = "OFTS Crea Eventi";  
			Entry entry = new Entry(); 
			entry = ctx.getEntry(schemaName, elemento.getIdentificativo(), intArray); 

			for (Integer fieldID : entry.keySet()) {  
				Value val = entry.get(fieldID);  
				Field field = ctx.getField(schemaName, fieldID.intValue());  

				if (field instanceof SelectionField) {  
					SelectionFieldLimit sFieldLimit = (SelectionFieldLimit)field.getFieldLimit();  
					if (sFieldLimit != null) {  
						List<EnumItem> eItemList = sFieldLimit.getValues();  
						for (EnumItem eItem : eItemList) {  
							if (eItem.getEnumItemNumber() == Integer.parseInt(val.toString()))
							{
								if(eItem.getEnumItemName().equals("Invia cancellazione"))
									return false;
								else 
									return true;
							}  
						}  
					}  
				}
			}
		} catch (ARException e) {  
			logger.error(e.getMessage());
			return false;
		} 
		return false;
	}

	public void getDettagliWO(Elemento elemento)
	{
		try {  
			// Per le categorie FTTS
			int[] intArray = new int[10];
			intArray[0] = 800000542;	// GUID
			intArray[1] = 802000001;	// Order ID
			intArray[2] = 536870922;	// Account no
			intArray[3] = 700000013;	// Process CFG ID
			intArray[4] = 700000082;	// FO ID
			intArray[5] = 800000029;	// Process ID
			intArray[6] = 800000007;	// Nome FO
			intArray[7] = 800000041;	// Processo
			intArray[8] = 800000223;	// Dettaglio Codice Attesa
			intArray[9] = 802000002;	// Site ID
			
			// Per le categorie SBLOCCO_ADSL/FIBRA/WS
			int[] intArray_SBLOCCO = new int[1];
			intArray_SBLOCCO[0] = 800000223;	// Dettaglio Codice Attesa

        	String schemaName = "";
        	Entry entry = new Entry();
        	
        	if(elemento.getProcesso().equalsIgnoreCase("CH_WO_SBLOCCO_ADSL"))
        	{
           		schemaName = "SP_DSL_WO_ALL";
           		entry = ctx.getEntry(schemaName, elemento.getIdentificativo(), intArray_SBLOCCO);
        	}
        	else if(elemento.getProcesso().equalsIgnoreCase("CH_WO_SBLOCCO_FIBRA"))
        	{
        		schemaName = "OFTS WO";
        		entry = ctx.getEntry(schemaName, elemento.getIdentificativo(), intArray_SBLOCCO);
        	}
        	else if(elemento.getProcesso().equalsIgnoreCase("CH_WO_SBLOCCO_WS"))
        	{
        		schemaName = "SP_HSL_WO_ALL";
        		entry = ctx.getEntry(schemaName, elemento.getIdentificativo(), intArray_SBLOCCO);
        	}
        	else if(elemento.getProcesso().equalsIgnoreCase("CH_WO_FTTS_POST")
        			|| elemento.getProcesso().equalsIgnoreCase("CH_WO_FTTS_PRE")
        			|| elemento.getProcesso().equalsIgnoreCase("CH_WO_FTTS_PRE_OLO"))
        	{
        		schemaName = "SP_SLU_WO";
        		entry = ctx.getEntry(schemaName, elemento.getIdentificativo(), intArray);
        	}
        		

			for (Integer fieldID : entry.keySet())
			{  
				Value val = entry.get(fieldID);  
				Field field = ctx.getField(schemaName, fieldID); 
				if (val.toString() != null)
				{
					if (field instanceof CharacterField)
					{    
						if(field.getName().equals("GUID"))
							elemento.setGuid(val.toString());
						else if(field.getName().equals("Account no"))
							elemento.setAccount_no(val.toString());
						else if(field.getName().equals("Process CFG ID"))
							elemento.setProcess_CFG_ID(val.toString());
						else if(field.getName().equals("FO ID"))
							elemento.setFo_ID(val.toString());
						else if(field.getName().equals("Process ID"))
							elemento.setProcess_ID(val.toString());
						else if(field.getName().equals("Nome FO"))
							elemento.setNome_FO(val.toString());
						else if(field.getName().equals("Processo"))
							elemento.setProcesso_wo(val.toString());
						else if(field.getName().equals("Dettaglio Codice Attesa"))
						{
							elemento.setDettaglio_codice_attesa(val.toString());
							elemento.setDettaglio_codice_chiusura(elemento.getDettaglio_codice_attesa());
						}				
					}
					else 
					{  
						if(field.getName().equals("Order ID"))
							elemento.setOrder_ID(val.toString());
						else if(field.getName().equals("Site ID"))
							elemento.setSite_ID(val.toString());
                    }  
				}
			}
		} catch (ARException e) {   
			logger.error(e.getMessage() +" INFO: identificativo: " +elemento.getIdentificativo());
		} 
	}
	
	public  boolean verificaStatoWO(Elemento elemento) throws ARException
    {
    	try {  
    		int[] intArray = new int[1];
    		intArray[0] = 7;	// Stato work order

    		String schemaName = "";
    		
           	if(elemento.getProcesso().equalsIgnoreCase("CH_WO_SBLOCCO_ADSL"))
        		schemaName = "SP_DSL_WO_ALL";
        	else if(elemento.getProcesso().equalsIgnoreCase("CH_WO_SBLOCCO_FIBRA"))
        		schemaName = "OFTS WO";
        	else if(elemento.getProcesso().equalsIgnoreCase("CH_WO_SBLOCCO_WS"))
        		schemaName = "SP_HSL_WO_ALL";
        	else if(elemento.getProcesso().equalsIgnoreCase("CH_WO_FTTS_POST")
        			|| elemento.getProcesso().equalsIgnoreCase("CH_WO_FTTS_PRE")
        			|| elemento.getProcesso().equalsIgnoreCase("CH_WO_FTTS_PRE_OLO"))
        		schemaName = "SP_SLU_WO";
    		
    		Entry entry = new Entry(); 
    		entry = ctx.getEntry(schemaName, elemento.getIdentificativo(), intArray); 

    		for (Integer fieldID : entry.keySet()) {  
    			Value val = entry.get(fieldID);  
    			Field field = ctx.getField(schemaName, fieldID.intValue());  

    			if (field instanceof SelectionField) {  
    				SelectionFieldLimit sFieldLimit = (SelectionFieldLimit)field.getFieldLimit();  
    				if (sFieldLimit != null) {  
    					List<EnumItem> eItemList = sFieldLimit.getValues();  
    					for (EnumItem eItem : eItemList) {  
    						if (eItem.getEnumItemNumber() == Integer.parseInt(val.toString()))
    						{
    							if(elemento.getProcesso().equals("CH_WO_FTTS_POST")
    									|| elemento.getProcesso().equals("CH_WO_FTTS_PRE")
    									|| elemento.getProcesso().equals("CH_WO_FTTS_PRE_OLO"))
    							{
    								if(eItem.getEnumItemName().equals("Chiuso"))
    									return false;
    								else
    									return true;
    							}
    							else
    							{  
    								if(eItem.getEnumItemName().equals("Chiusa"))
    									return false;
    								else 
    									return true;
    							}
    						}  
    					}  
    				}  
    			}
    		}
    	} catch (ARException e) {  
    		logger.error(e.getMessage() +" INFO: id: " +elemento.getIdentificativo());
    		return false;
    	} 
    	return false;
    }

	/*
     * Serve solo per tirare fuori informazioni del form e dell'entry richiesto.
     * Se si vuole vedere TUTTI i campi dell'entry, con la relativa descrizione,
     * basta mettere null come terzo parametro del metodo getEntry, al posto di intArray.
     * 
     */
	public  void getTicketConCampi(Elemento elemento, boolean tuttiCampi) {  
        try {  
        	
        	int[] intArray = new int[5];

        	intArray[0] = 1;			// Stato
        	intArray[1] = 800000200;	// Invio Evento Cancellato
        	intArray[2] = 7;	// Stato work order
        	intArray[3] = 800000223;	// Dettaglio codice attesa
        	intArray[4] = 800000205;	// Dettaglio codice chiusura
        	
        	String schemaName = "";
        	
        	if(elemento.getProcesso().equalsIgnoreCase("CANC_COMM") || elemento.getProcesso().equalsIgnoreCase("CANC_TEC"))
        		schemaName = "OFTS Crea Eventi";
        	else if(elemento.getProcesso().equalsIgnoreCase("CH_WO_SBLOCCO_ADSL"))
        		schemaName = "SP_DSL_WO_ALL";
        	else if(elemento.getProcesso().equalsIgnoreCase("CH_WO_SBLOCCO_FIBRA"))
        		schemaName = "OFTS WO";
        	else if(elemento.getProcesso().equalsIgnoreCase("CH_WO_SBLOCCO_WS"))
        		schemaName = "SP_HSL_WO_ALL";
        	else if(elemento.getProcesso().equalsIgnoreCase("CH_WO_FTTS_POST")
        			|| elemento.getProcesso().equalsIgnoreCase("CH_WO_FTTS_PRE")
        			|| elemento.getProcesso().equalsIgnoreCase("CH_WO_FTTS_PRE_OLO"))
        		schemaName = "SP_SLU_WO";
        	else if(elemento.getProcesso().equalsIgnoreCase("TESTWO"))
        		schemaName = "SP_SLU_WO";
        	else if(elemento.getProcesso().equalsIgnoreCase("TESTTR"))
        		schemaName = "SP_SLU_WO_TRANSIZIONI";
            
            
            Entry entry = new Entry(); 
            if(tuttiCampi)
            	entry = ctx.getEntry(schemaName, elemento.getIdentificativo(), null);
            else
            	entry = ctx.getEntry(schemaName, elemento.getIdentificativo(), intArray);  
            
            for (Integer fieldID : entry.keySet()) {  
                Value val = entry.get(fieldID);  
                Field field = ctx.getField(schemaName, fieldID.intValue());  
                if (val.toString() != null) { 
                	logger.info("Campo numero: " +fieldID + " : ");
                    if (field instanceof CharacterField) {  
                        if (fieldID.intValue() != 99) {  
                        	logger.info("Field ID diverso da 99 : " +field.getName() + ": " + val.toString() + "\n"); 
                        } else {  
                            StatusHistoryValue shVal = StatusHistoryValue.decode(val.getValue().toString());  
                            StringBuilder shBuilder = new StringBuilder();  
                            if (shVal != null) {  
                                for (StatusHistoryItem shItem : shVal) {  
                                    if (shItem != null) {  
                                        shBuilder.append(shItem.getTimestamp().toDate().toString());  
                                        shBuilder.append("\u0004").append(shItem.getUser()).append("\u0003");  
                                    } else {  
                                        shBuilder.append("\u0003");  
                                    }  
                                }  
                                logger.info(field.getName() + ": " + shBuilder.toString());  
                            }  
                        }  
                    } else if (field instanceof SelectionField) {  
                        SelectionFieldLimit sFieldLimit = (SelectionFieldLimit)field.getFieldLimit();  
                        if (sFieldLimit != null) {  
                            List<EnumItem> eItemList = sFieldLimit.getValues();  
                            for (EnumItem eItem : eItemList) {  
                                if (eItem.getEnumItemNumber() == Integer.parseInt(val.toString())) {  
                                	logger.info(field.getName() + ": " + eItem.getEnumItemName());  
                                }  
                            }  
                        }  
                    } else if (field instanceof CurrencyField) {  
                        CurrencyValue cCurValue = (CurrencyValue) val.getValue();  
                        if (cCurValue != null) {  
                            for (FuncCurrencyInfo curInfo : cCurValue.getFuncCurrencyList()) {  
                            	logger.info(field.getName() + ": " + curInfo.getValue() + " " + curInfo.getCurrencyCode());  
                            }  
                        }  
                    } else if (field instanceof DateTimeField) {  
                        Timestamp callDateTimeTS = (Timestamp) val.getValue();  
                        if (callDateTimeTS != null) {  
                        	logger.info(field.getName() + ": " + callDateTimeTS.toDate().toString());  
                        }  
                    } else if (field instanceof DiaryField) {  
                        for (DiaryItem dlv : (DiaryListValue)val.getValue()) {  
                        	logger.info(field.getName() + ": " + "Created by " + dlv.getUser() + " @ " + dlv.getTimestamp().toDate().toString() + " " + dlv.getText());  
                        }  
                    } else if (field instanceof AttachmentField) {  
                        AttachmentValue aVal = (AttachmentValue) val.getValue();  
                        if (aVal != null) {  
                            String aName;  
                            String[] aDetails = aVal.getValueFileName().split("\\.(?=[^\\.]+$)");  
                            if (aDetails.length == 2) {  
                                aName = aDetails[0] + "." + aDetails[1];  
                            } else {  
                                aName = aDetails[0];  
                            }  
                            int lastPos = aName.lastIndexOf('\\');  
                            String aNameShort = (lastPos < 0) ? aName : aName.substring(lastPos + 1);  
                            FileOutputStream fos = new FileOutputStream(aNameShort);  
                            byte[] attach = ctx.getEntryBlob(schemaName, elemento.getIdentificativo(), fieldID.intValue());  
                            fos.write(attach);  
                            fos.close();  
                        }  
                    } else {  
                    	logger.info(field.getName() + ": " + val.toString());  
                    }  
                }  
            }  
        } catch (ARException e) {  
            logger.error(e.getMessage() +" INFO: identificativo: " +elemento.getIdentificativo());
        } catch (FileNotFoundException e) {   
            logger.error(e.getMessage());
        } catch (IOException e) {   
            logger.error(e.getMessage());
        }  
    }  

}
