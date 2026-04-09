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
import it.almaviva.mic.etl.dto.ade.fabbricati.DatoCatastaleDto;
import it.almaviva.mic.etl.dto.ade.fabbricati.FabbricatoTipoRecord1Dto;
import it.almaviva.mic.etl.dto.ade.fabbricati.FabbricatoTipoRecord2Dto;
import it.almaviva.mic.etl.dto.ade.fabbricati.FabbricatoTipoRecord3Dto;
import it.almaviva.mic.etl.dto.ade.fabbricati.IndirizzoDto;
import it.almaviva.mic.etl.enums.AdeTipoRecordEnum;
import it.almaviva.mic.etl.exceptions.MicdlETLException;
import it.almaviva.mic.etl.parsers.CsvMapper;
import it.almaviva.mic.etl.parsers.ParserInterface;
import it.almaviva.mic.etl.utils.MicDlEtlConsts;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;

@Component
public class AdeFabParserImpl implements ParserInterface {

	private static final Logger logger = LoggerFactory.getLogger(AdeFabParserImpl.class);
	private final Validator validator;
	
	 public AdeFabParserImpl(Validator validator) 
	 {
	        this.validator = validator;
	 }
	
	@Override
	public ParsingDTO parseFile(Reader reader) 
	{
		logger.info("Inizio parsing del file FAB...");
		
		/* preparazione alla lettura */
		 BufferedReader br = new BufferedReader(reader);
		 String line;
		 
		 /* strutture di appoggio */
		 Map<Integer, List<String>> erroriRecord = new HashMap<>();
		 List<FabbricatoTipoRecord1Dto> listaFabbricati = new ArrayList<>();
		 List<FabbricatoTipoRecord2Dto> listaDatiCatastali = new ArrayList<>();
		 List<FabbricatoTipoRecord3Dto> listaIndirizzi = new ArrayList<>();
		 
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
						FabbricatoTipoRecord1Dto fabbricato1 = CsvMapper.associaCampi(elementiRiga, FabbricatoTipoRecord1Dto.class);
						Set<ConstraintViolation<FabbricatoTipoRecord1Dto>> violations1 = null;
						violations1 = validator.validate(fabbricato1);
						
						/* controllo del risultato della validazione */
						if(CollectionUtils.isNotEmpty(violations1))
						{
							logger.error(MicDlEtlConsts.ERR_VALIDATION);
							aggiungiErrore(erroriRecord, rowCounter, estraiDescrizioniErrori(violations1));
							
							rowCounter++;
							
							break;
						}
						
						rowCounter++;
						listaFabbricati.add(fabbricato1);
						
						break;
						
					case ADE_TIPO_RECORD_2:
						
						/* si verifica che i campi non obbligatori siano in un numero multiplo di 6, cioe' 
						 * siano rappresentazioni valide di uno o piu' dati catastali */
						if(elementiRiga.length %6 != 0)
						{
							logger.info("Errore sul record {}", rowCounter);
							logger.error(MicDlEtlConsts.ERR_ESTATE_REG_NUM);
							aggiungiErrore(erroriRecord, rowCounter, Arrays.asList(MicDlEtlConsts.ERR_ESTATE_REG_NUM));
							
							rowCounter++;
							
							break;
						}
						
						/* creazione tipo record 2 */
						FabbricatoTipoRecord2Dto fabbricato2 = CsvMapper.associaCampi(Arrays.copyOfRange(elementiRiga, 0, 6), 
								                                                      FabbricatoTipoRecord2Dto.class);
						
						/* validazione */
						Set<ConstraintViolation<FabbricatoTipoRecord2Dto>> violations2 = null;
						violations2 = validator.validate(fabbricato2);
						
						/* controllo del risultato della validazione */
						if(CollectionUtils.isNotEmpty(violations2))
						{
							logger.info("Errore sul record {}", rowCounter);
							logger.error(MicDlEtlConsts.ERR_VALIDATION);
							aggiungiErrore(erroriRecord, rowCounter, estraiDescrizioniErrori(violations2));
							
							rowCounter++;
							break;
						}
						
						/* estrazione dei dati catastali */
						int indiceIniziale = 6;
						while(indiceIniziale < elementiRiga.length)
						{
							/* estrazione del dato catastale corrente */
							DatoCatastaleDto datoCatastale = CsvMapper.associaCampi(Arrays.copyOfRange(elementiRiga, 
									                                                indiceIniziale, indiceIniziale + 6), 
									                                                DatoCatastaleDto.class);
							
							/* validazione del dato catastale */
							Set<ConstraintViolation<DatoCatastaleDto>> violationsDatoCatastale = validator.validate(datoCatastale);
							if(CollectionUtils.isNotEmpty(violationsDatoCatastale))
							{
								logger.info("Errore sul record {}", rowCounter);
								logger.error(MicDlEtlConsts.ERR_VALIDATION);
								aggiungiErrore(erroriRecord, rowCounter, estraiDescrizioniErrori(violationsDatoCatastale));
								
								rowCounter++;
								break;
							}
							
							/* inserimento del dato catastale nel record di tipo 2 */
							if(CollectionUtils.isEmpty(fabbricato2.getArray_id_dato_catastale()))
							{
								List<DatoCatastaleDto> nuovaListaCatasto = new ArrayList<>();
								nuovaListaCatasto.add(datoCatastale);
								fabbricato2.setArray_id_dato_catastale(nuovaListaCatasto);
							}
							
							else
								fabbricato2.getArray_id_dato_catastale().add(datoCatastale);
							
							indiceIniziale += 6;
						}
						
						rowCounter++;
						listaDatiCatastali.add(fabbricato2);
						
						break;
					case ADE_TIPO_RECORD_3:
						
						/* si verifica che i campi non obbligatori siano in un numero multiplo di 6, cioe' 
						 * siano rappresentazioni valide di uno o piu' indirizzi */
						if(elementiRiga.length %6 != 0)
						{
							logger.info("Errore sul record {}", rowCounter);
							logger.error(MicDlEtlConsts.ERR_ADDR_NUM);
							aggiungiErrore(erroriRecord, rowCounter, Arrays.asList(MicDlEtlConsts.ERR_ADDR_NUM));
							
							rowCounter++;
							break;
						}
						
						/* creazione tipo record 3 */
						FabbricatoTipoRecord3Dto fabbricato3 = CsvMapper.associaCampi(Arrays.copyOfRange(elementiRiga, 0, 6), 
								                                                      FabbricatoTipoRecord3Dto.class);
						
						Set<ConstraintViolation<FabbricatoTipoRecord3Dto>> violations3 = null;
						violations3 = validator.validate(fabbricato3);
						
						/* controllo del risultato della validazione */
						if(CollectionUtils.isNotEmpty(violations3))
						{
							logger.info("Errore sul record {}", rowCounter);
							logger.error(MicDlEtlConsts.ERR_VALIDATION);
							aggiungiErrore(erroriRecord, rowCounter, estraiDescrizioniErrori(violations3));
							
							rowCounter++;
							break;
						}
						
						/* estrazione dei dati degli indirizzi */
						int indiceInizialeAddr = 6;
						while(indiceInizialeAddr < elementiRiga.length)
						{
							/* estrazione dell'indirizzo corrente */
							IndirizzoDto indirizzo = CsvMapper.associaCampi(Arrays.copyOfRange(elementiRiga, 
									                                        indiceInizialeAddr, 
									                                        indiceInizialeAddr + 6), 
									                                        IndirizzoDto.class);
							/* validazione dell'indirizzo */
							Set<ConstraintViolation<IndirizzoDto>> violationsIndirizzo = validator.validate(indirizzo);
							if(CollectionUtils.isNotEmpty(violationsIndirizzo))
							{
								logger.info("Errore sul record {}", rowCounter);
								logger.error(MicDlEtlConsts.ERR_VALIDATION);
								aggiungiErrore(erroriRecord, rowCounter, estraiDescrizioniErrori(violationsIndirizzo));
								
								rowCounter++;
								break;
							}
							
							/* aggiunta dell'indirizzo */
							if(CollectionUtils.isEmpty(fabbricato3.getArray_id_indirizzi()))
							{
								List<IndirizzoDto> nuovaListaIndirizzi = new ArrayList<>();
								nuovaListaIndirizzi.add(indirizzo);
								fabbricato3.setArray_id_indirizzi(nuovaListaIndirizzi);
							}
							
							else
								fabbricato3.getArray_id_indirizzi().add(indirizzo);
							
							indiceInizialeAddr += 6;
						}
						
						rowCounter++;
						listaIndirizzi.add(fabbricato3);
						
						break;
					case ADE_TIPO_RECORD_4:
						/* da implementare in futuro */
						
						rowCounter++;
						break;
						
					case ADE_TIPO_RECORD_5:
						/* da implementare in futuro */
						
						rowCounter++;
						
						break;
					default:
						break;
				
				}
				
			 }
			
			/* aggiornamento output */
			output.setUnitaImmobiliari(listaFabbricati);
			output.setDatiCatastali(listaDatiCatastali);
			output.setIndirizzi(listaIndirizzi);
			output.setRecordLetti(rowCounter - 1);
			output.setReportRecord(erroriRecord);
			
			logger.info("Effettuata la lettura di {} record", rowCounter - 1);
			logger.info("Dati derivati dalla lettura dei record validi: {} unita' immobiliari, {} dati catastali, {} indirizzi", 
					    listaFabbricati.size(),
					    listaDatiCatastali.size(),
					    listaIndirizzi.size());
			logger.info("Record interessati da errori di validazione: {}", erroriRecord.size());
			logger.info("Totale record validi: {}", rowCounter - 1 - erroriRecord.size());
		} 
		 
		 catch (Throwable e) 
		 {
			 logger.info("Si e' verificata un'eccezione durante il parsing del file", e);
			 throw new MicdlETLException("Si e' verificato un errore durante il parsing del file", HttpStatus.INTERNAL_SERVER_ERROR);
		 }
		
		return output;
	}

}
