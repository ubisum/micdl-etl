package it.almaviva.mic.etl.parsers.ade;

import java.io.BufferedReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import it.almaviva.mic.etl.dto.ParsingDTO;
import it.almaviva.mic.etl.dto.ade.titolarita.TitolaritaDTO;
import it.almaviva.mic.etl.exceptions.MicdlETLException;
import it.almaviva.mic.etl.parsers.CsvMapper;
import it.almaviva.mic.etl.parsers.ParserInterface;
import it.almaviva.mic.etl.utils.MicDlEtlConsts;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;

@Component
public class AdeTitParserImpl implements ParserInterface 
{
	private static final Logger logger = LoggerFactory.getLogger(AdeTitParserImpl.class);
	private final Validator validator;
	
	public AdeTitParserImpl(Validator validator) {
		super();
		this.validator = validator;
	}

	@Override
	public ParsingDTO parseFile(Reader reader) 
	{
		logger.info("Inizio parsing del file TIT...");
		
		/* preparazione alla lettura */
		 BufferedReader br = new BufferedReader(reader);
		 String line;
		
		 /* strutture di appoggio */
		 Map<Integer, List<String>> erroriRecord = new HashMap<>();
		 List<TitolaritaDTO> listaTitolarita = new ArrayList<>();
		
		 /* output */
		 ParsingDTO output = new ParsingDTO();
		 
		 try 
		 {
			 /* contatore delle righe */
			 int rowCounter = 1;
			 
			 logger.info("Inizio analisi file...");
			 
			 while ((line = br.readLine()) != null)
			 {
				 /* controllo della presenza del carattere | finale */
				if(line.endsWith("|"))
					line = line.substring(0, line.length() - 1);
				
				/* suddivisione del record nelle sue parti */
				String[] elementiRiga = line.split("\\|", -1);
				
				if(elementiRiga.length < 6)
				{
					logger.info("Errore sul record {}", rowCounter);
					logger.info(MicDlEtlConsts.ERR_MISSING_ELEMS);
					aggiungiErrore(erroriRecord, rowCounter, Arrays.asList(MicDlEtlConsts.ERR_MISSING_ELEMS));
					rowCounter++;
					break;
				}
				
				/* conversione della riga in oggetto */
				TitolaritaDTO titolarita = CsvMapper.associaCampi(elementiRiga, TitolaritaDTO.class);
				

				/* validazione */
				Set<ConstraintViolation<TitolaritaDTO>> violations = null;
				violations = validator.validate(titolarita);
				
				/* controllo del risultato della validazione */
				if(CollectionUtils.isNotEmpty(violations))
				{
					logger.info("Errore sul record {}", rowCounter);
					logger.error(MicDlEtlConsts.ERR_VALIDATION);
					aggiungiErrore(erroriRecord, rowCounter, estraiDescrizioniErrori(violations));
				}
				
				else
				{
					titolarita.setRowId(rowCounter);
					listaTitolarita.add(titolarita);
				}
					
				
				rowCounter++;
			 }
			 
			 output.setRecordLetti(rowCounter - 1);
			 output.setReportRecord(erroriRecord);
			 output.setTitolarita(listaTitolarita);
		 }
		 
		 catch (Throwable e) 
		 {
			 logger.info("Si e' verificata un'eccezione durante il parsing del file", e);
			 throw new MicdlETLException("Si e' verificato un errore durante il parsing del file", HttpStatus.INTERNAL_SERVER_ERROR);
		 }
		 
		 
		 
		 
		// TODO Auto-generated method stub
		return output;
	}

}
