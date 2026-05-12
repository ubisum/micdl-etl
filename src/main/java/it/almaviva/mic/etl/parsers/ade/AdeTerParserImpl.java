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
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import it.almaviva.mic.etl.dto.ParsingDTO;
import it.almaviva.mic.etl.dto.ade.terreni.DeduzioneParticellaDTO;
import it.almaviva.mic.etl.dto.ade.terreni.TerrenoTipoRecord1DTO;
import it.almaviva.mic.etl.dto.ade.terreni.TerrenoTipoRecord2DTO;
import it.almaviva.mic.etl.enums.AdeTipoRecordEnum;
import it.almaviva.mic.etl.exceptions.MicdlETLException;
import it.almaviva.mic.etl.parsers.CsvMapper;
import it.almaviva.mic.etl.parsers.ParserInterface;
import it.almaviva.mic.etl.utils.MicDlEtlConsts;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;

@Component
public class AdeTerParserImpl implements ParserInterface 
{
	private static final Logger logger = LoggerFactory.getLogger(AdeFabParserImpl.class);
	private final Validator validator;
	
	public AdeTerParserImpl(Validator validator) {
		super();
		this.validator = validator;
	}

	@Override
	public ParsingDTO parseFile(Reader reader) 
	{
		
		logger.info("Inizio parsing del file TER...");
		
		/* preparazione alla lettura */
		 BufferedReader br = new BufferedReader(reader);
		 String line;
		
		 /* strutture di appoggio */
		 Map<Integer, List<String>> erroriRecord = new HashMap<>();
		 List<TerrenoTipoRecord1DTO> listaTerreni = new ArrayList<>();
		 List<TerrenoTipoRecord2DTO> listaDeduzioni = new ArrayList<>();
		 TerrenoTipoRecord2DTO deduzioneTemp = null;
		 
		 /* output */
		 ParsingDTO output = new ParsingDTO();
		 

		 try 
		 {
			 /* contatore delle righe */
			 int rowCounter = 1;
			 
			 logger.info("Inizio analisi file...");
				
			/* ultimo tipo record rilevato */
			AdeTipoRecordEnum ultimoTipoRecord = null;
			
			while ((line = br.readLine()) != null) 
			{
				/* controllo della presenza del carattere | finale */
				if(line.endsWith("|"))
					line = line.substring(0, line.length() - 1);
				
				/* suddivisione del record nelle sue parti */
				String[] elementiRiga = line.split("\\|", -1);
				
				/* controllo presenza minima elementi */
				if(elementiRiga.length < 6)
				{
					logger.info("Errore sul record {}", rowCounter);
					logger.info(MicDlEtlConsts.ERR_MISSING_ELEMS);
					aggiungiErrore(erroriRecord, rowCounter, Arrays.asList(MicDlEtlConsts.ERR_MISSING_ELEMS));
					rowCounter++;
					break;
				}
				
				/* controllo tipo */
				String tipo = elementiRiga[5];
				if(StringUtils.isBlank(tipo) || !tipo.matches("^[0-9]{1}$"))
				{
					logger.info("Errore sul record {}", rowCounter);
					logger.info(MicDlEtlConsts.ERR_WRONG_TYPE);
					aggiungiErrore(erroriRecord, rowCounter, Arrays.asList(MicDlEtlConsts.ERR_WRONG_TYPE));
					rowCounter++;
					
					break;
				}
				
				/* estrazione tipo */
				Integer tipoEstratto = Integer.valueOf(tipo);
				List<Integer> listaTipi = AdeTipoRecordEnum.listaValori();
				
				if(!listaTipi.contains(tipoEstratto))
				{
					logger.info("Errore sul record {}", rowCounter);
					logger.info(MicDlEtlConsts.ERR_WRONG_TYPE);
					aggiungiErrore(erroriRecord, rowCounter, Arrays.asList(MicDlEtlConsts.ERR_WRONG_TYPE));
					rowCounter++;
					
					break;
				}
				
				/* ramificazione per tipo */
				AdeTipoRecordEnum tipoRecord = AdeTipoRecordEnum.getFromValue(tipoEstratto);
				
				switch(tipoRecord)
				{
					case ADE_TIPO_RECORD_1:
						/* costruzione del record di tipo 1 */
						TerrenoTipoRecord1DTO terreno1 = CsvMapper.associaCampi(elementiRiga, TerrenoTipoRecord1DTO.class);
						Set<ConstraintViolation<TerrenoTipoRecord1DTO>> violations1 = validator.validate(terreno1);
						
						/* incremento dell'indice di riga */
						rowCounter++;
						
						/* controllo del risultato della validazione */
						if(CollectionUtils.isNotEmpty(violations1))
						{
							logger.error(MicDlEtlConsts.ERR_VALIDATION);
							aggiungiErrore(erroriRecord, rowCounter - 1, estraiDescrizioniErrori(violations1));
							
							break;
						}
						
						listaTerreni.add(terreno1);
						ultimoTipoRecord = AdeTipoRecordEnum.ADE_TIPO_RECORD_1;
						
						break;
					case ADE_TIPO_RECORD_2:
						if(elementiRiga.length == 6)
						{
							/* dati delle dedudzioni delle particelle non presenti */
							aggiungiErrore(erroriRecord, rowCounter, Arrays.asList(MicDlEtlConsts.ERR_DED_MISSING_ELEMS));
							break;
						}
						
						/* creazione nuova deduzione */
						List<DeduzioneParticellaDTO> deduzioni = new ArrayList<>();
						
						for(int index = 6; index < elementiRiga.length; index++)
						{
							/* validazione della deduzione corrente */
							DeduzioneParticellaDTO ded = new DeduzioneParticellaDTO(elementiRiga[index]);
							Set<ConstraintViolation<DeduzioneParticellaDTO>> violations2 = validator.validate(ded);
							
							/* se una sola deduzione e' sbagliata, si elimina l'intero record */
							if(violations2.size() != 0)
							{
								aggiungiErrore(erroriRecord, rowCounter, estraiDescrizioniErrori(violations2));
								break;
							}
							
							deduzioni.add(ded);
						}
						
						/* record multipli */
						if(ultimoTipoRecord == AdeTipoRecordEnum.ADE_TIPO_RECORD_2)
						{
							deduzioneTemp.getListaDeduzione().addAll(deduzioni);
						}
						
						else
						{
							/* record singolo o primo di una sequenza */
							if(deduzioneTemp != null)
								listaDeduzioni.add(deduzioneTemp);
							
							deduzioneTemp = new TerrenoTipoRecord2DTO(deduzioni);
						}
						
						ultimoTipoRecord = AdeTipoRecordEnum.ADE_TIPO_RECORD_2;
							
						break;
					case ADE_TIPO_RECORD_3:
						break;
					case ADE_TIPO_RECORD_4:
						break;
					case ADE_TIPO_RECORD_5:
						break;
					default:
						break;
				
				}
			}
			
			output.setListaTerreni(listaTerreni);
			output.setReportRecord(erroriRecord);
		 }
		 
		 catch (Throwable e) 
		 {
			 logger.info("Si e' verificata un'eccezione durante il parsing del file", e);
			 throw new MicdlETLException("Si e' verificato un errore durante il parsing del file", HttpStatus.INTERNAL_SERVER_ERROR);
		 }
		
		
		return output;
	}

}
