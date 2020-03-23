package it.fastweb.cancellazioniSP.config;

import com.jcraft.jsch.ChannelSftp;

import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;

public class Utility {

	public static LocalDate convertVectorToLocalDate(ChannelSftp.LsEntry file) {

		String dateFile = file.getAttrs().getMtimeString();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEE MMM dd HH:mm:ss zzz yyyy", Locale.US);
		return LocalDate.parse(dateFile, formatter);
	}

	public static Date convertVectorToDate(ChannelSftp.LsEntry f) throws ParseException {

		String dateFile = f.getAttrs().getMtimeString();
		Date currentFileDate = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.US).parse(dateFile);
		return currentFileDate;
	}

	public static LocalDate convertDateToLocalDate(Date date) {

		Instant instant = Instant.ofEpochMilli(date.getTime());
		LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
		LocalDate localDate = localDateTime.toLocalDate();
		return localDate;
	}

	public String writeResourceToFile(String resourceName) throws URISyntaxException, IOException {

		URL resource;
		ClassLoader CLDR = getClass().getClassLoader();
//        logger.debug("Class loader = " + CLDR.toString());
//        logger.debug("Get class Name = " + getClass().getName());
//        logger.debug("Resource name = " + resourceName );
		resource = CLDR.getResource(resourceName);
//        logger.debug("Resource URI  = " + resource.toURI());
//        logger.debug("Reading path without using URI = " + resource.getPath());
//        logger.debug("Reading path using URI = " + resource.toURI().getPath() );

		InputStream configStream = CLDR.getResourceAsStream(resourceName);
		Path pth = Paths.get(resourceName);
		Files.deleteIfExists(pth);
		Files.copy(configStream, Files.createFile(pth), StandardCopyOption.REPLACE_EXISTING);
		return pth.toAbsolutePath().toString();
	}

}
