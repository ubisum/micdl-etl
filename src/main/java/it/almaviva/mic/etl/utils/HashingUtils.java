package it.almaviva.mic.etl.utils;

import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;

import it.almaviva.mic.etl.exceptions.MicdlETLException;
import it.almaviva.mic.etl.parsers.CsvPosition;

public class HashingUtils 
{
	private static final Logger logger = LoggerFactory.getLogger(HashingUtils.class);
	
	/* metodo di calcolo della codifica hashing per una classe con campi annotati progressivamente */
	public static String getHashingForAnnotatedCols(Integer lowestIndex, Object dto)
	{
		/* ricerca di tutti campi */
		 Field[] fields = dto.getClass().getDeclaredFields();

	     String input =  Arrays.stream(fields)
	                /* si filtrano solo i campi che hanno l'annotazione progressiva maggiore di un certo valore
	                 * (questo ci permette di escludere i primi campi che di solito rappresentano una chiave 
	                 * e che non devono essere presi in considerazione dall'algoritmo di hashing) */
	                .filter(f -> f.isAnnotationPresent(CsvPosition.class)
	                        && (lowestIndex == null || f.getAnnotation(CsvPosition.class).value() > lowestIndex))

	                /* ordinaniamo i campi in base alla loro annotazione, in modo che l'algoritmo di hashing 
	                 * li consideri sempre nello stesso ordine */
	                .sorted(Comparator.comparingInt(f ->
	                        f.getAnnotation(CsvPosition.class).value()))

	                /* ciascun campo, contribuisce col proprio valore (sotto forma di stringa) o con la 
	                 * string NULL alla creazione di sequenza di caratteri iniziale da sottoporre 
	                 * all'algoritmo di hashing */
	                .map(f -> {
	                    f.setAccessible(true);
	                    try 
	                    {
	                        Object value = f.get(dto);
	                        return value != null ? value.toString() : "NULL";
	                    } 
	                    
	                    catch (IllegalAccessException e) 
	                    {
	                    	logger.error("Si e' verificata un'eccezione durante l'applicazione dell'algoritmo di hashing", e);
	                        throw new MicdlETLException("Si e' verificato un errore interno", HttpStatus.INTERNAL_SERVER_ERROR);
	                    }
	                })

	                /* si combinano i diversi contributi dividendoli con dei | */
	                .collect(Collectors.joining("|"));
	     
	     try
	     {
	    	 return getHashingCode(input);
	     }
	     
	     catch(Throwable ex)
	     {
	    	 logger.error("Si e' verificata un'eccezione durante l'applicazione dell'algoritmo di hashing", ex);
             throw new MicdlETLException("Si e' verificato un errore interno", HttpStatus.INTERNAL_SERVER_ERROR);
	     }
	     
	}
	
	private static String getHashingCode(String input) throws NoSuchAlgorithmException
	{
		/* introduzione del codificatore */
		MessageDigest digest = MessageDigest.getInstance("SHA-256");
		
		/* calcolo dell'hash */
		 byte[] hashBytes = digest.digest(input.getBytes(StandardCharsets.UTF_8));
		 
		 /* conversione in esadecimale (per rendere la stringa formata da caratteri leggibili) */
		 StringBuilder hexString = new StringBuilder();
         for (byte b : hashBytes) 
         {
             String hex = Integer.toHexString(0xff & b); /* ultimi 8 bit */
             if (hex.length() == 1) hexString.append('0'); /* padding (se necessario) */
             hexString.append(hex);
         }
         return hexString.toString();
	}
	
}
