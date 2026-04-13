package it.almaviva.mic.etl.enums;

public enum AdeEsitoBatchJob 
{
	ESITO_KO("KO"),
	ESITO_OK("OK");
	
	private String esito;

	private AdeEsitoBatchJob(String esito) 
	{
		this.esito = esito;
	}

	public String getEsito() 
	{
		return esito;
	}
	
}
