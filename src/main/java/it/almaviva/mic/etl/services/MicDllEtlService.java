package it.almaviva.mic.etl.services;

import java.io.Reader;

import it.almaviva.mic.etl.dto.ParsingDTO;

public interface MicDllEtlService 
{
	ParsingDTO parseAndStore(Reader csvReader);
}
