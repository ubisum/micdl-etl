package it.almaviva.mic.etl.services.ade;

import java.io.Reader;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
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
		logger.info("Ingresso nel servizio di scansione e salvataggio dei file Ade FAB");
		
		/* result */
		ParsingDTO parsingResult = new ParsingDTO();
		
		try
		{
			logger.info("Scansione del file...");
			parsingResult = parser.parseFile(csvReader);
			
			logger.info("Estrazione delle entita' immobiliari dai DTO...");
			List<AdeUnitaImmHist> listaUnita = parsingResult.getUnitaImmobiliari().stream()
					                           .map(unita -> AdeConverter.convertFABRec1FromDto(unita))
					                           .collect(Collectors.toList());
			
			
//			logger.info("Categorie: {}", listaUnita.stream().map(m -> m.getCategoria()).distinct().toList());
//			logger.info("Conc. flag. classamento: {}", listaUnita.stream().map(m -> m.getConcFlagClassamento()).distinct().toList());
//			logger.info("Zone censuarie: {}", listaUnita.stream().map(m -> m.getZonaCensuaria()).distinct().toList());
//			logger.info("Tipo nota: {}", listaUnita.stream().map(m -> m.getRegTipoNota()).distinct().toList());
//			logger.info("Conc.tipo nota: {}", listaUnita.stream().map(m -> m.getConcTipoNota()).distinct().toList());
//			
			if(CollectionUtils.isNotEmpty(listaUnita))
			{
				logger.info("Salvataggio su tabella di staging delle unita' immobiliari...");
				Integer numeroRecordInseriti = fabDAO.insertUnitaImm(listaUnita, idBatch);
				parsingResult.setRecordInseritiInStaging(parsingResult.getRecordInseritiInStaging() != null ? 
				          parsingResult.getRecordInseritiInStaging() + numeroRecordInseriti : numeroRecordInseriti);
				
				logger.info("Inseriti {} record sulla tabella di staging", numeroRecordInseriti);
			
				if(numeroRecordInseriti > 0)
				{
					logger.info("Esecuzione stored procedure per tabella unita' immobiliari...");
					Integer unitaInserite = genericDAO.eseguiStoreProcedureContaRecord(MicDlEtlConsts.ADE_UNITA_IMM_SP);
					parsingResult.setRecordInseriti(parsingResult.getRecordInseriti() != null ? 
							                        parsingResult.getRecordInseriti() + unitaInserite : 
							                        unitaInserite);
					
					logger.info("Esecuzione stored procedure {} terminata", MicDlEtlConsts.ADE_UNITA_IMM_SP);
				}
				
			}
			
			else
				logger.info("Nessuna unita' immobiliare rilevata nel file analizzato");
						
			
			logger.info("Salvataggio dei dati catastali sulla tabella di staging...");
			Integer datiCatastaliInseriti = fabDAO.insertDatiCatastali(parsingResult.getDatiCatastali(), idBatch);
			parsingResult.setRecordInseritiInStaging(parsingResult.getRecordInseritiInStaging() != null ? 
			                                         parsingResult.getRecordInseritiInStaging() + datiCatastaliInseriti : 
			        	                             datiCatastaliInseriti);
			
			logger.info("Inseriti {} record sulla tabella di staging", datiCatastaliInseriti);
			
			if(datiCatastaliInseriti != 0)
			{
				logger.info("Esecuzione stored procedure per tabella dati catastali...");
				Integer datiSP = genericDAO.eseguiStoreProcedureContaRecord(MicDlEtlConsts.ADE_DATO_CASTALE_SP);
				parsingResult.setRecordInseriti(parsingResult.getRecordInseriti() != null ?
						                        parsingResult.getRecordInseriti() + datiSP :
						                        datiSP);
				
				
				logger.info("Esecuzione stored procedure {} terminata", MicDlEtlConsts.ADE_DATO_CASTALE_SP);
			}
			
			if(CollectionUtils.isNotEmpty(parsingResult.getDatiCatastaliSupplementari()))
			{
				logger.info("Inserimento dei dati catastali supplementari sulla tabella di staging...");
				Integer datiSupplementari = fabDAO.insertDatiCatastali(parsingResult.getDatiCatastaliSupplementari(), idBatch);
				if(datiSupplementari > 0)
				{
					logger.info("Salvataggio dei dati catastali supplementari...");
					Integer numDati = genericDAO.eseguiStoreProcedureContaRecord(MicDlEtlConsts.ADE_DATO_CASTALE_SIMPLE_SP);
					parsingResult.setRecordInseriti(parsingResult.getRecordInseriti() != null ?
							                        parsingResult.getRecordInseriti() + numDati :
							                        numDati);
				}
				
				logger.info("Inseriti {} record supplementari sulla tabella di staging", datiSupplementari);
			}
			
			logger.info("Salvataggio degli indirizzi sulla tabella di staging...");
			Integer indirizziInseriti = fabDAO.insertIndirizzi(parsingResult.getIndirizzi(), idBatch);
			parsingResult.setRecordInseritiInStaging(parsingResult.getRecordInseritiInStaging() != null ? 
			          parsingResult.getRecordInseritiInStaging() + indirizziInseriti : indirizziInseriti);
			
			logger.info("Inseriti {} record sulla tabella di staging", indirizziInseriti);
			
			if(indirizziInseriti != 0)
			{
				logger.info("Esecuzione stored procedure per tabella indirizzi...");
				Integer indirizziSP = genericDAO.eseguiStoreProcedureContaRecord(MicDlEtlConsts.ADE_INDIRIZZO_SP);
				parsingResult.setRecordInseriti(parsingResult.getRecordInseriti() != null ?
						                                 parsingResult.getRecordInseriti() + indirizziSP :
						                                 indirizziSP);
				
				
				logger.info("Esecuzione stored procedure {} terminata", MicDlEtlConsts.ADE_INDIRIZZO_SP);
			}
			
			if(CollectionUtils.isNotEmpty(parsingResult.getIndirizziSupplementari()))
			{
				logger.info("Inserimento degli indirizzi supplementari sulla tabella di staging...");
				Integer indirizziSupplementari = fabDAO.insertIndirizzi(parsingResult.getIndirizziSupplementari(), idBatch);
				if(indirizziSupplementari > 0)
				{
					logger.info("Salvataggio degli indirizzi supplementari...");
					Integer indSupplSP = genericDAO.eseguiStoreProcedureContaRecord(MicDlEtlConsts.ADE_INDIRIZZO_SIMPLE_SP);
					parsingResult.setRecordInseriti(parsingResult.getRecordInseriti() != null ?
                            						parsingResult.getRecordInseriti() + indSupplSP :
                    								indSupplSP);
				}
				
				logger.info("Inseriti {} record supplementari sulla tabella di staging", indirizziSupplementari);
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
