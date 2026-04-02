package it.almaviva.mic.etl.parsers;

import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import jakarta.validation.ConstraintViolation;

public interface ParserInterface 
{
	public Object[] parseFile(Reader reader);
	
	/* il metodo aggiunge tutti gli errori trovati in corrispondenza 
	 * dell'isimo record */
	default void aggiungiErrore(Map<Integer, List<String>> mappaErrori, Integer indice, List<String> errori)
	{
		/* controllo della mappa */
		if(mappaErrori == null)
			mappaErrori = new HashMap<>();
		
		/* controllo della coppia chiave - valore */
		if(indice == null || errori == null)
			return;
		
		/* primo inserimento della chiave */
		if(!mappaErrori.keySet().contains(indice))
		{
			List<String> listaErrori = new ArrayList<>();
			listaErrori.addAll(errori);
			mappaErrori.put(indice, listaErrori);
		}
		
		else
			mappaErrori.get(indice).addAll(errori);
	}
	
	/* A partire dagli eventuali errori di validazione, si estraggono le relative descrizioni */
	default <T> List<String> estraiDescrizioniErrori(Set<ConstraintViolation<T>> violations)
	{
		/* lista finale */
		List<String> listaDescrizioni = new ArrayList<>();
		
		/* popolamento lista */
		if (!violations.isEmpty()) 
		{
            for (ConstraintViolation<T> errore : violations) 
            	listaDescrizioni.add(errore.getMessage());
		}
		
		return listaDescrizioni;
	}
}
