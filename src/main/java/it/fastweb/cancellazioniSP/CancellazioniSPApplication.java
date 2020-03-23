package it.fastweb.editSP;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bmc.arsys.api.ARException;

import it.fastweb.editSP.config.DataConfig;
import it.fastweb.editSP.config.SessionRemedyConfiguration;
import it.fastweb.editSP.model.WorkOrderSP;
import it.fastweb.editSP.service.RemedyService;

public class EditSPApplication {

	private static  Logger logger = LoggerFactory.getLogger(EditSPApplication.class);
	private static void close_all_session(SessionRemedyConfiguration remedy_conf, DataConfig db ) {
		try {
			remedy_conf.logoutSessionRemedy();
			
		} catch (ARException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			logger.error("Errore chiusura sessione Remedy " + e1.getMessage());
		}finally {
			try {
				db.close_conn();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				logger.error("Errore chiusura sessione DB " + e.getMessage());
			}
		}
	}
    public static void main( String[] args ) throws FileNotFoundException, IOException, SQLException
    {
    	logger.info("EditSP start");
    	DataConfig db = new DataConfig();
        logger.info("Aperta la sessione verso il database");
    	SessionRemedyConfiguration remedy_conf = new SessionRemedyConfiguration();
    	logger.info("Configurata la sessione verso Remedy");
    	RemedyService remedy_service = new RemedyService(remedy_conf);
    	logger.info("Aperta la sessione verso Remedy");
       
        ArrayList<WorkOrderSP> lista_wo_sp = db.read_data_wo_sp();
        logger.info("Estrazione workorder da editare");
        for (WorkOrderSP work_order_da_editare : lista_wo_sp){
        	try {
        		work_order_da_editare = remedy_service.process_WO_SP(work_order_da_editare);
        		db.update_stato_workorder_elaborato(work_order_da_editare);
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("Errore elaborazione Work Order SP su remedy" + e.getMessage());
				close_all_session(remedy_conf,db);
			}
        }
        logger.info("Fine workorder da editare");
        close_all_session(remedy_conf,db);
    }
}
