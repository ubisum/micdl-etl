package it.almaviva.mic.etl.services;

import java.io.Reader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import it.almaviva.mic.etl.parsers.ParserInterface;

public class AdeETLFabServiceImpl implements MicDllEtlService 
{
	@Autowired
	@Qualifier("adeFabParserImpl")
	private ParserInterface parser;
	
	private static final Logger logger = LoggerFactory.getLogger(AdeETLFabServiceImpl.class);
	
	@Override
	public void parseAndStore(Reader csvReader) 
	{
		logger.info("Ingresso nel servizio di scansione e salvataggio dei file Ade");
		parser.parseFile(csvReader);
	}

}
