package it.almaviva.mic.etl.parsers.ade;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import it.almaviva.mic.etl.dto.EsitoDTO;
import it.almaviva.mic.etl.dto.ade.fabbricati.DatoCatastaleDto;
import it.almaviva.mic.etl.dto.ade.fabbricati.FabbricatoTipoRecord1Dto;
import it.almaviva.mic.etl.dto.ade.fabbricati.FabbricatoTipoRecord2Dto;
import it.almaviva.mic.etl.dto.ade.fabbricati.FabbricatoTipoRecord3Dto;
import it.almaviva.mic.etl.dto.ade.fabbricati.IndirizzoDto;
import it.almaviva.mic.etl.enums.AdeTipoRecordEnum;
import it.almaviva.mic.etl.parsers.CsvMapper;
import it.almaviva.mic.etl.parsers.ParserInterface;
import it.almaviva.mic.etl.utils.MicDlEtlParsingConsts;
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
	public EsitoDTO parseFile(Reader reader) 
	{
		logger.info("Inizio parsing del file FAB...");
		
		/* preparazione alla lettura */
		 BufferedReader br = new BufferedReader(reader);
		 Map<Integer, List<String>> erroriRecord = new HashMap<>();
		 String line;
		
		 try 
		 {
			 /* contatore delle righe */
			 int rowCounter = 1;
			 
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
					logger.info(MicDlEtlParsingConsts.ERR_MISSING_ELEMS);
					aggiungiErrore(erroriRecord, rowCounter, Arrays.asList(MicDlEtlParsingConsts.ERR_MISSING_ELEMS));
					rowCounter++;
					break;
				}
				
				/* controllo tipo */
				String tipo = elementiRiga[5];
				if(StringUtils.isBlank(tipo) || !tipo.matches("^[0-9]{1}$"))
				{
					logger.info("Errore sul record {}", rowCounter);
					logger.info(MicDlEtlParsingConsts.ERR_WRONG_TYPE);
					aggiungiErrore(erroriRecord, rowCounter, Arrays.asList(MicDlEtlParsingConsts.ERR_MISSING_ELEMS));
					rowCounter++;
					
					break;
				}
				
				/* estrazione tipo */
				Integer tipoEstratto = Integer.valueOf(tipo);
				List<Integer> listaTipi = AdeTipoRecordEnum.listaValori();
				
				if(!listaTipi.contains(tipoEstratto))
				{
					logger.info("Errore sul record {}", rowCounter);
					logger.info(MicDlEtlParsingConsts.ERR_WRONG_TYPE);
					aggiungiErrore(erroriRecord, rowCounter, Arrays.asList(MicDlEtlParsingConsts.ERR_WRONG_TYPE));
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
							logger.error(MicDlEtlParsingConsts.ERR_VALIDATION);
							aggiungiErrore(erroriRecord, rowCounter, estraiDescrizioniErrori(violations1));
							
							rowCounter++;
							
							break;
						}
						
						rowCounter++;
						
						break;
						
					case ADE_TIPO_RECORD_2:
						
						/* si verifica che i campi non obbligatori siano in un numero multiplo di 6, cioe' 
						 * siano rappresentazioni valide di uno o piu' dati catastali */
						if(elementiRiga.length %6 != 0)
						{
							logger.info("Errore sul record {}", rowCounter);
							logger.error(MicDlEtlParsingConsts.ERR_ESTATE_REG_NUM);
							aggiungiErrore(erroriRecord, rowCounter, Arrays.asList(MicDlEtlParsingConsts.ERR_ESTATE_REG_NUM));
							
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
							logger.error(MicDlEtlParsingConsts.ERR_VALIDATION);
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
								logger.error(MicDlEtlParsingConsts.ERR_VALIDATION);
								aggiungiErrore(erroriRecord, rowCounter, estraiDescrizioniErrori(violationsDatoCatastale));
								
								rowCounter++;
								break;
							}
							
							/* inserimento del dato catastale nel record di tipo 2 */
							if(CollectionUtils.isEmpty(fabbricato2.getArray_id_dato_catastale()))
								fabbricato2.setArray_id_dato_catastale(Arrays.asList(datoCatastale));
							
							else
								fabbricato2.getArray_id_dato_catastale().add(datoCatastale);
							
							indiceIniziale += 6;
						}
						
						rowCounter++;
						
						break;
					case ADE_TIPO_RECORD_3:
						
						/* si verifica che i campi non obbligatori siano in un numero multiplo di 6, cioe' 
						 * siano rappresentazioni valide di uno o piu' indirizzi */
						if(elementiRiga.length %6 != 0)
						{
							logger.info("Errore sul record {}", rowCounter);
							logger.error(MicDlEtlParsingConsts.ERR_ADDR_NUM);
							aggiungiErrore(erroriRecord, rowCounter, Arrays.asList(MicDlEtlParsingConsts.ERR_ADDR_NUM));
							
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
							logger.error(MicDlEtlParsingConsts.ERR_VALIDATION);
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
								logger.error(MicDlEtlParsingConsts.ERR_VALIDATION);
								aggiungiErrore(erroriRecord, rowCounter, estraiDescrizioniErrori(violationsIndirizzo));
								
								rowCounter++;
								break;
							}
							
							/* aggiunta dell'indirizzo */
							if(CollectionUtils.isEmpty(fabbricato3.getArray_id_indirizzi()))
								fabbricato3.setArray_id_indirizzi(Arrays.asList(indirizzo));
							
							else
								fabbricato3.getArray_id_indirizzi().add(indirizzo);
							
							indiceInizialeAddr += 6;
						}
						
						rowCounter++;
						
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
		} 
		 
		 catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}

}
