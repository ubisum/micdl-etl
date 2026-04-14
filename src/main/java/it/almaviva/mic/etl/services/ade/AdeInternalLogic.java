package it.almaviva.mic.etl.services.ade;

import java.math.BigDecimal;
import java.util.List;

import it.almaviva.mic.etl.dto.ParsingDTO;
import it.almaviva.mic.etl.entities.ade.AdeUnitaImmHist;

public interface AdeInternalLogic 
{
	public void inserisciUnitaImmobiliari(BigDecimal idBatch, ParsingDTO parsingResult, List<AdeUnitaImmHist> listaUnita);
}
