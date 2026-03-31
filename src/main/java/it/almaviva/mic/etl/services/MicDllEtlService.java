package it.almaviva.mic.etl.services;

import java.io.Reader;

public interface MicDllEtlService 
{
	void parseAndStore(Reader csvReader);
}
