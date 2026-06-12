package it.almaviva.mic.etl.services.ade;

import java.io.Reader;
import java.math.BigDecimal;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import it.almaviva.mic.etl.dao.ade.AdeTitDAO;
import it.almaviva.mic.etl.dto.ParsingDTO;
import it.almaviva.mic.etl.exceptions.MicdlETLException;
import it.almaviva.mic.etl.parsers.ParserInterface;
import it.almaviva.mic.etl.services.MicDllEtlService;
import jakarta.transaction.Transactional;

@Service
public class AdeETLTitServiceImpl implements MicDllEtlService 
{
	
	@Autowired
	@Qualifier("adeTitParserImpl")
	private ParserInterface parser;
	
	@Autowired
	private AdeTitDAO titDAO;
	
	private static final Logger logger = LoggerFactory.getLogger(AdeETLTitServiceImpl.class);

	@Override
	@Transactional
	public ParsingDTO parseAndStore(Reader csvReader, String filename, BigDecimal idBatch) 
	{
		logger.info("Ingresso nel servizio di scansione e salvataggio dei file Ade TIT");
		
		/* result */
		ParsingDTO parsingResult = new ParsingDTO();
		
		try
		{
			logger.info("Scansione del file...");
			parsingResult = parser.parseFile(csvReader);
			
			if(CollectionUtils.isNotEmpty(parsingResult.getTitolarita()))
			{
				logger.info("Salvataggio delle titolarita' sulla tabella di staging...");
				Integer recordInStage = titDAO.insertTitolarita(parsingResult.getTitolarita(), idBatch);
				parsingResult.setRecordInseritiInStaging(parsingResult.getRecordInseritiInStaging() != null ? 
				          parsingResult.getRecordInseritiInStaging() + recordInStage : recordInStage);
				
				logger.info("Avvio stored procedure per titolarita'...");
				Integer recordInseriti = titDAO.executeSCD2Procedure(parsingResult);
				parsingResult.setRecordInseriti(parsingResult.getRecordInseriti() != null ? 
						                        parsingResult.getRecordInseriti() + recordInseriti :
						                        recordInseriti);
			}
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
		
		// TODO Auto-generated method stub
		return parsingResult;
	}

}
