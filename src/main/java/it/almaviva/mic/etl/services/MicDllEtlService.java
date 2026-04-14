package it.almaviva.mic.etl.services;

import java.io.Reader;
import java.math.BigDecimal;

import it.almaviva.mic.etl.dto.ParsingDTO;

public interface MicDllEtlService 
{
	public ParsingDTO parseAndStore(Reader csvReader, String filename, BigDecimal idBatch);
}
