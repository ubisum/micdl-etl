package it.almaviva.mic.etl.dao.ade;

import java.math.BigDecimal;
import java.util.List;

import it.almaviva.mic.etl.dto.ade.terreni.TerrenoTipoRecord1DTO;
import it.almaviva.mic.etl.dto.ade.terreni.TerrenoTipoRecord2DTO;

public interface AdeTerDAO 
{
	public Integer insertParticelle(List<TerrenoTipoRecord1DTO> listaTerreni, BigDecimal idBatch);
	public Integer insertDeduzioni(List<TerrenoTipoRecord2DTO> listaDeduzioni, BigDecimal idBatch);
}
