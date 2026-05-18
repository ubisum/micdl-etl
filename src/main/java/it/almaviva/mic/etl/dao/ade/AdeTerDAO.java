package it.almaviva.mic.etl.dao.ade;

import java.math.BigDecimal;
import java.util.List;

import it.almaviva.mic.etl.dto.ade.terreni.TerrenoTipoRecord1DTO;
import it.almaviva.mic.etl.dto.ade.terreni.TerrenoTipoRecord2DTO;
import it.almaviva.mic.etl.dto.ade.terreni.TerrenoTipoRecord3DTO;
import it.almaviva.mic.etl.dto.ade.terreni.TerrenoTipoRecord4DTO;

public interface AdeTerDAO 
{
	public Integer insertParticelle(List<TerrenoTipoRecord1DTO> listaTerreni, BigDecimal idBatch);
	public Integer insertDeduzioni(List<TerrenoTipoRecord2DTO> listaDeduzioni, BigDecimal idBatch);
	public Integer insertRiserve(List<TerrenoTipoRecord3DTO> listaRiserve, BigDecimal idBatch);
	public Integer insertPorzioni(List<TerrenoTipoRecord4DTO> listaPorzioni, BigDecimal idBatch);
}
