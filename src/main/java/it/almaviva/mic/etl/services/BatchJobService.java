package it.almaviva.mic.etl.services;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import it.almaviva.mic.etl.enums.AdeEsitoBatchJob;

public interface BatchJobService 
{
	public BigDecimal insertBatchJob(String fonte, String tipoCarico);
	public void updateBatchJob(BigDecimal idBatch, AdeEsitoBatchJob esito);
	public void inserisciDettagliBatchJob(Map<Integer, List<String>> errori, BigDecimal idJob, String filename) ;
}
