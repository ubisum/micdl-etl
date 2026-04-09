package it.almaviva.mic.etl.dao.ade;

import java.util.List;

import it.almaviva.mic.etl.entities.ade.AdeUnitaImmHist;

public interface AdeFabDAO 
{
	public Integer insertUnitaImm(List<AdeUnitaImmHist> unitaImmobiliari);
}
