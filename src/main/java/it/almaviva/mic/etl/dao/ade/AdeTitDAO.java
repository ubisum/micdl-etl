package it.almaviva.mic.etl.dao.ade;

import java.math.BigDecimal;
import java.util.List;

import it.almaviva.mic.etl.dto.ParsingDTO;
import it.almaviva.mic.etl.dto.ade.titolarita.TitolaritaDTO;

public interface AdeTitDAO 
{
	public Integer insertTitolarita(List<TitolaritaDTO> listaTitolarita, BigDecimal batchId);
	public Integer executeSCD2Procedure(ParsingDTO result);
}
