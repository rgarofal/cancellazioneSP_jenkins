package it.fastweb.cancellazioniSP.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Common_configuration {
	private static String  Configurazione_file;
	private static String  Configurazione_file_prod = "config.properties";
	private static String  Configurazione_file_test = "configtest.properties";

	private static final Logger log = LoggerFactory.getLogger(Common_configuration.class);

	public Common_configuration(boolean config_test ) {
		if (config_test) {
			log.info("TEST configurazione");;
			Configurazione_file = Configurazione_file_test;
		} else {
			log.info("Configurazione Produzione");;
			Configurazione_file = Configurazione_file_prod;
		}
	}
	public static String getConfigurazione_file() {
		return Configurazione_file;
	}
}