package it.almaviva.mic.etl.services.ade;

import java.io.Reader;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import it.almaviva.mic.etl.converters.ade.AdeConverter;
import it.almaviva.mic.etl.dao.GenericDAO;
import it.almaviva.mic.etl.dao.ade.AdeFabDAO;
import it.almaviva.mic.etl.dto.ParsingDTO;
import it.almaviva.mic.etl.entities.ade.AdeUnitaImmHist;
import it.almaviva.mic.etl.enums.AdeEsitoBatchJob;
import it.almaviva.mic.etl.exceptions.MicdlETLException;
import it.almaviva.mic.etl.parsers.ParserInterface;
import it.almaviva.mic.etl.services.MicDllEtlService;
import it.almaviva.mic.etl.utils.MicDlEtlConsts;
import jakarta.transaction.Transactional;

@Service
public class AdeETLFabServiceImpl implements MicDllEtlService 
{
	@Autowired
	@Qualifier("adeFabParserImpl")
	private ParserInterface parser;

	@Autowired
	private GenericDAO genericDAO;
	
	@Autowired
	private AdeFabDAO fabDAO;
	
	private static final Logger logger = LoggerFactory.getLogger(AdeETLFabServiceImpl.class);
	
	@Override
	@Transactional
	public ParsingDTO parseAndStore(Reader csvReader, String filename, BigDecimal idBatch) 
	{
		logger.info("Ingresso nel servizio di scansione e salvataggio dei file Ade");
		
		/* result */
		ParsingDTO parsingResult = null;
		
		try
		{
			logger.info("Scansione del file...");
			parsingResult = parser.parseFile(csvReader);
			
			logger.info("Estrazione delle entita' immobiliari dai DTO...");
			List<AdeUnitaImmHist> listaUnita = parsingResult.getUnitaImmobiliari().stream()
					                           .map(unita -> AdeConverter.convertFABRec1FromDto(unita))
					                           .collect(Collectors.toList());
			
			/*
			logger.info("Categorie: {}", listaUnita.stream().map(m -> m.getCategoria()).distinct().toList());
			logger.info("Conc. flag. classamento: {}", listaUnita.stream().map(m -> m.getConcFlagClassamento()).distinct().toList());
			logger.info("Zone censuarie: {}", listaUnita.stream().map(m -> m.getZonaCensuaria()).distinct().toList());
			logger.info("Conc.tipo nota: {}", listaUnita.stream().map(m -> m.getConcTipoNota()).distinct().toList());
			*/
						
			logger.info("Richiesta di salvataggio su tabella di staging delle unita' immobiliari...");
			Integer numeroRecordInseriti = fabDAO.insertUnitaImm(listaUnita, idBatch);
			parsingResult.setRecordInseriti(parsingResult.getRecordInseriti() != null ? 
			          parsingResult.getRecordInseriti() + numeroRecordInseriti : numeroRecordInseriti);
			
			logger.info("Inseriti {} record sulla tabella di staging", numeroRecordInseriti);
		
			logger.info("Esecuzione stored procedure per tabella unita' immobiliari...");
			genericDAO.eseguiStoredProcedure(MicDlEtlConsts.ADE_UNITA_IMM_SP);
			logger.info("Esecuzione stored procedure {} terminata", MicDlEtlConsts.ADE_UNITA_IMM_SP);
			
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
