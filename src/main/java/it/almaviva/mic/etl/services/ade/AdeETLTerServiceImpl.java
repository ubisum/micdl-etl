package it.almaviva.mic.etl.services.ade;

import java.io.Reader;
import java.math.BigDecimal;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import it.almaviva.mic.etl.dao.GenericDAO;
import it.almaviva.mic.etl.dao.ade.AdeTerDAO;
import it.almaviva.mic.etl.dto.ParsingDTO;
import it.almaviva.mic.etl.exceptions.MicdlETLException;
import it.almaviva.mic.etl.parsers.ParserInterface;
import it.almaviva.mic.etl.services.MicDllEtlService;
import it.almaviva.mic.etl.utils.MicDlEtlConsts;
import jakarta.transaction.Transactional;

@Service
public class AdeETLTerServiceImpl implements MicDllEtlService 
{
	@Autowired
	@Qualifier("adeTerParserImpl")
	private ParserInterface parser;
	
	@Autowired
	private AdeTerDAO adeTerDAo;
	
	@Autowired
	private GenericDAO genericDAO;
	
	private static final Logger logger = LoggerFactory.getLogger(AdeETLTerServiceImpl.class);
	
	@Override
	@Transactional
	public ParsingDTO parseAndStore(Reader csvReader, String filename, BigDecimal idBatch) 
	{
		logger.info("Ingresso nel servizio di scansione e salvataggio dei file Ade TER");
		
		/* result */
		ParsingDTO parsingResult = new ParsingDTO();
		
		try
		{
			logger.info("Scansione del file...");
			parsingResult = parser.parseFile(csvReader);
			
			if(CollectionUtils.isNotEmpty(parsingResult.getListaTerreni()))
			{
				logger.info("Tipi nota REG: {}", parsingResult.getListaTerreni().stream().map(m-> m.getTipoNotaReg()).collect(Collectors.toList()));
				logger.info("Tipi nota CON: {}", parsingResult.getListaTerreni().stream().map(m-> m.getTipoNotaConcl()).collect(Collectors.toList()));
				
				logger.info("Salvataggio delle particelle sulla tabella di staging...");
				Integer numeroRecordInseriti = adeTerDAo.insertParticelle(parsingResult.getListaTerreni(), idBatch);
				parsingResult.setRecordInseritiInStaging(parsingResult.getRecordInseritiInStaging() != null ? 
				          parsingResult.getRecordInseritiInStaging() + numeroRecordInseriti : numeroRecordInseriti);
				
				logger.info("Inserite {} particelle sulla tabella di staging", numeroRecordInseriti);
				
				if(numeroRecordInseriti > 0)
				{	
					logger.info("Esecuzione stored procedure per particelle...");
					Integer particelleInserite = genericDAO.eseguiStoredProcedureContaRecord(MicDlEtlConsts.ADE_PARTICELLA_SP);
					parsingResult.setRecordInseriti(parsingResult.getRecordInseriti() != null ? 
							                        parsingResult.getRecordInseriti() + particelleInserite : 
							                        	particelleInserite);
					
					logger.info("Esecuzione stored procedure {} terminata", MicDlEtlConsts.ADE_PARTICELLA_SP);
				}
			}
			
			if(CollectionUtils.isNotEmpty(parsingResult.getListaDeduzioni()))
			{
				logger.info("Salvataggio delle deduzioni sulla tabella di staging...");
				Integer numeroRecordInseriti = adeTerDAo.insertDeduzioni(parsingResult.getListaDeduzioni(), idBatch);
				parsingResult.setRecordInseritiInStaging(parsingResult.getRecordInseritiInStaging() != null ? 
				          parsingResult.getRecordInseritiInStaging() + numeroRecordInseriti : numeroRecordInseriti);
				
				logger.info("Inserite {} deduzioni sulla tabella di staging", numeroRecordInseriti);
				
				logger.info("Esecuzione stored procedure per particelle...");
				Integer deduzioniInserite = genericDAO.eseguiStoredProcedureContaRecord(MicDlEtlConsts.ADE_DEDUZIONE_SP);
				parsingResult.setRecordInseriti(parsingResult.getRecordInseriti() != null ? 
						                        parsingResult.getRecordInseriti() + deduzioniInserite : 
						                        	deduzioniInserite);
				
				logger.info("Esecuzione stored procedure {} terminata", MicDlEtlConsts.ADE_DEDUZIONE_SP);
			}
			
			if(CollectionUtils.isNotEmpty(parsingResult.getListaRiserve()))
			{
				logger.info("Salvataggio delle riserve sulla tabella di staging...");
				Integer numeroRecordInseriti = adeTerDAo.insertRiserve(parsingResult.getListaRiserve(), idBatch);
				parsingResult.setRecordInseritiInStaging(parsingResult.getRecordInseritiInStaging() != null ? 
				          parsingResult.getRecordInseritiInStaging() + numeroRecordInseriti : numeroRecordInseriti);
				
				logger.info("Inserite {} riserve sulla tabella di staging", numeroRecordInseriti);
				
				logger.info("Esecuzione stored procedure per riserve...");
				Integer riserveInserite = genericDAO.eseguiStoredProcedureContaRecord(MicDlEtlConsts.ADE_RISERVA_SP);
				parsingResult.setRecordInseriti(parsingResult.getRecordInseriti() != null ? 
						                        parsingResult.getRecordInseriti() + riserveInserite : 
						                        	riserveInserite);
				
				logger.info("Esecuzione stored procedure {} terminata", MicDlEtlConsts.ADE_RISERVA_SP);
			}
			
			if(CollectionUtils.isNotEmpty(parsingResult.getListaPorzioni()))
			{
				logger.info("Salvataggio delle porzioni sulla tabella di staging...");
				Integer numeroRecordInseriti = adeTerDAo.insertPorzioni(parsingResult.getListaPorzioni(), idBatch);
				parsingResult.setRecordInseritiInStaging(parsingResult.getRecordInseritiInStaging() != null ? 
				          parsingResult.getRecordInseritiInStaging() + numeroRecordInseriti : numeroRecordInseriti);
				
				logger.info("Inserite {} porzioni sulla tabella di staging", numeroRecordInseriti);
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
		
		
		
		return parsingResult;
	}

}
