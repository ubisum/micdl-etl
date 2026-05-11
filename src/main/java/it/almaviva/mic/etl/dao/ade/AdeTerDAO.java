package it.almaviva.mic.etl.dao.ade;

import java.util.List;

import it.almaviva.mic.etl.dto.ade.terreni.TerrenoTipoRecord1Dto;

public interface AdeTerDAO 
{
	public Integer insertParticelle(List<TerrenoTipoRecord1Dto> listaTerreni);
}
