package it.almaviva.mic.etl.services;

import java.util.Map;

import org.springframework.stereotype.Component;

@Component
public class ServiceFactory 
{
	private final Map<String, MicDllEtlService> services;
	
	public ServiceFactory(Map<String, MicDllEtlService> services) 
	{
		super();
		this.services = services;
	}
	
	/* generazione dell'interfaccia sulla base del tipo di file */
	public MicDllEtlService getService(String tipo)
	{
		if(services == null || services.size() == 0)
			return null;
		
		switch(tipo.toUpperCase())
		{
			case "FAB":
				return services.get("adeETLFabServiceImpl");
				
			case "SOG":
				return services.get("adeETLSogServiceImpl");
				
			default:
				return null;
		}
	}
}
