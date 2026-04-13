package it.almaviva.mic.etl.dao;

import java.math.BigDecimal;

import it.almaviva.mic.etl.enums.AdeEsitoBatchJob;

public interface GenericDAO 
{
	public void eseguiStoredProcedure(String procedure);
	public BigDecimal insertBatchJob(String fonte, String tipoCarico);
	public void updateBatchJob(BigDecimal idJob, AdeEsitoBatchJob esito);
}
