package it.almaviva.mic.etl.utils;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;

import it.almaviva.mic.etl.exceptions.MicdlETLException;

public class MicdlEtlUtils 
{
	private static final Logger logger = LoggerFactory.getLogger(MicdlEtlUtils.class);
	
	public static String readContentFromFile(String filename)
	{
		logger.info("Lettura contenuto dal file {}...", filename);
		
		/* stringa di appoggio */
		 String createSql;
		 
		 try 
		 {
			 /* lettura risorsa */
			 InputStream is = MicdlEtlUtils.class.getClassLoader().getResourceAsStream(filename);
			 if(is == null)
			 {
				 logger.info("Impossibile trovare la risorsa {}", filename);
				 throw new MicdlETLException("Impossibile trovate la risorsa " + filename, HttpStatus.INTERNAL_SERVER_ERROR);
			 }
			 
			 /* lettura file */
			 return new String(is.readAllBytes(), StandardCharsets.UTF_8);
		 }
		 
		 catch (Throwable ex) 
		{
			 logger.info("Si e' verificato un errore", ex);
			 throw new MicdlETLException(ex instanceof MicdlETLException ? ex.getMessage() : "Si e' verificato un errore interno", 
					                     HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
