package it.almaviva.mic.etl.dao.ade;

import java.math.BigDecimal;
import java.util.List;

import it.almaviva.mic.etl.dto.ade.soggetti.ProprietarioDTO;

public interface AdeSogDAO 
{
	public Integer inserisciProprietari(List<ProprietarioDTO> proprietari, BigDecimal idBatch);
}
