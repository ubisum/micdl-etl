package it.almaviva.mic.etl.services;

import java.io.Reader;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import it.almaviva.mic.etl.converters.ade.AdeConverter;
import it.almaviva.mic.etl.dao.ade.AdeFabDAO;
import it.almaviva.mic.etl.dto.ParsingDTO;
import it.almaviva.mic.etl.entities.ade.AdeUnitaImmHist;
import it.almaviva.mic.etl.parsers.ParserInterface;
import jakarta.transaction.Transactional;

@Service
public class AdeETLFabServiceImpl implements MicDllEtlService 
{
	@Autowired
	@Qualifier("adeFabParserImpl")
	private ParserInterface parser;
	
	@Autowired
	private AdeFabDAO fabDAO;
	
	private static final Logger logger = LoggerFactory.getLogger(AdeETLFabServiceImpl.class);
	
	@Override
	@Transactional
	public ParsingDTO parseAndStore(Reader csvReader) 
	{
		logger.info("Ingresso nel servizio di scansione e salvataggio dei file Ade");
		
		logger.info("Scansione del file...");
		ParsingDTO parsingResult = parser.parseFile(csvReader);
		
		logger.info("Estrazione delle entita' dai DTO...");
		List<AdeUnitaImmHist> listaUnita = parsingResult.getUnitaImmobiliari().stream()
				                           .map(unita -> AdeConverter.convertFABRec1FromDto(unita))
				                           .collect(Collectors.toList());
		
		logger.info("Richiesta di salvataggio su tabella di staging...");
		fabDAO.insertUnitaImm(listaUnita);
		
		return parsingResult;
		
	}

}
