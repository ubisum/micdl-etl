package it.almaviva.mic.etl.dao;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import it.almaviva.mic.etl.dto.BatchJobDTO;
import it.almaviva.mic.etl.enums.AdeEsitoBatchJob;

public interface GenericDAO 
{
	public void eseguiStoredProcedure(String procedure);
	public Integer eseguiStoreProcedureContaRecord(String procedure);
	public BigDecimal insertBatchJob(String fonte, String tipoCarico);
	public void updateBatchJob(BigDecimal idJob, AdeEsitoBatchJob esito);
	public void inserisciDettagliBatchJob(Map<Integer, List<String>> errori, BigDecimal idJob, String filename);
	public BatchJobDTO findUltimoBatchJobAttivo();
}
