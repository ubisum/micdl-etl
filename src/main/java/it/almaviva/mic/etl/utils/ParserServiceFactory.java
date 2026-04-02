package it.almaviva.mic.etl.utils;

import java.util.Map;

import org.springframework.stereotype.Component;

import it.almaviva.mic.etl.parsers.ParserInterface;

@Component
public class ParserServiceFactory 
{
	private final Map<String, ParserInterface> services;

	public ParserServiceFactory(Map<String, ParserInterface> services) 
	{
		super();
		this.services = services;
	}
	
	/* generazione dell'interfaccia sulla base del tipo di file */
	public ParserInterface getParserInterface(String tipo)
	{
		if(services == null || services.size() == 0)
			return null;
		
		switch(tipo.toUpperCase())
		{
			case "FAB":
				return services.get("adeFabParserImpl");
				
			default:
				return null;
		}
	}
	
	
}
