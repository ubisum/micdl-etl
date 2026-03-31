package it.almaviva.mic.etl.services;

import java.io.Reader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AdeETLServiceImpl implements MicDllEtlService {

	private static final Logger logger = LoggerFactory.getLogger(AdeETLServiceImpl.class);
	
	@Override
	public void parseAndStore(Reader csvReader) 
	{
		logger.info("Ingresso nel servizio di scansione e salvataggio dei file Ade");
	}

}
