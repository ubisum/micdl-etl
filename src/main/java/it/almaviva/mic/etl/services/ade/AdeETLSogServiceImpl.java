package it.almaviva.mic.etl.services.ade;

import java.io.Reader;
import java.math.BigDecimal;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import it.almaviva.mic.etl.dto.ParsingDTO;
import it.almaviva.mic.etl.exceptions.MicdlETLException;
import it.almaviva.mic.etl.parsers.ParserInterface;
import it.almaviva.mic.etl.services.MicDllEtlService;

@Service
public class AdeETLSogServiceImpl implements MicDllEtlService 
{
	@Autowired
	@Qualifier("adeSogParserImpl")
	private ParserInterface parser;
	
	private static final Logger logger = LoggerFactory.getLogger(AdeETLSogServiceImpl.class);
	
	@Override
	public ParsingDTO parseAndStore(Reader csvReader, String filename, BigDecimal idBatch) 
	{
		logger.info("Ingresso nel servizio di scansione e salvataggio dei file Ade SOG");
		
		/* result */
		ParsingDTO parsingResult = new ParsingDTO();
		
		try
		{
			logger.info("Scansione del file...");
			parsingResult = parser.parseFile(csvReader);
			
			logger.info("Salvataggio dei soggetti sulla tabella di staging...");
		}
		
		catch(MicdlETLException mee)
		{
			/* rilancio eccezione */
			throw new MicdlETLException(mee.getMessage(), mee.getStatus());
		}
		
		catch(Throwable ex)
		{
			/* rilancio eccezione */
			throw new MicdlETLException(StringUtils.isNoneBlank(ex.getMessage()) ? ex.getMessage() : 
				                        "Si e' verificato un errore interno", HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return parsingResult;
	}

}
