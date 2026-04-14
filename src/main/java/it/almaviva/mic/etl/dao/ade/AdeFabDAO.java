package it.almaviva.mic.etl.dao.ade;

import java.math.BigDecimal;
import java.util.List;

import it.almaviva.mic.etl.entities.ade.AdeUnitaImmHist;

public interface AdeFabDAO 
{
	public Integer insertUnitaImm(List<AdeUnitaImmHist> unitaImmobiliari, BigDecimal idBatch);
}
