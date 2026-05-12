package it.almaviva.mic.etl.converters.ade;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;

import it.almaviva.mic.etl.converters.LocalDateTimeToStringConverter;
import it.almaviva.mic.etl.converters.StringToIntegerConverter;
import it.almaviva.mic.etl.dto.BatchJobDTO;
import it.almaviva.mic.etl.dto.ade.fabbricati.DatoCatastaleDto;
import it.almaviva.mic.etl.dto.ade.fabbricati.FabbricatoTipoRecord1Dto;
import it.almaviva.mic.etl.dto.ade.fabbricati.IndirizzoDto;
import it.almaviva.mic.etl.dto.ade.soggetti.ProprietarioDTO;
import it.almaviva.mic.etl.dto.ade.terreni.TerrenoTipoRecord1DTO;
import it.almaviva.mic.etl.entities.ade.AdeDatoCatastaleHist;
import it.almaviva.mic.etl.entities.ade.AdeIndirizzoHist;
import it.almaviva.mic.etl.entities.ade.AdeParticellaHist;
import it.almaviva.mic.etl.entities.ade.AdeUnitaImmHist;
import it.almaviva.mic.etl.entities.ade.BatchJob;
import it.almaviva.mic.etl.entities.ade.ProprietarioHist;
import it.almaviva.mic.etl.utils.HashingUtils;

public class AdeConverter 
{
	public static AdeUnitaImmHist convertFABRec1FromDto(FabbricatoTipoRecord1Dto fabbricato)
	{
		/* creazione del mapper */
		ModelMapper modelMapper = new ModelMapper();
		
		/* aggiunta dei convertitori necessari */
		modelMapper.addConverter(new StringToIntegerConverter());
		
		/* creazione dell'entita' con conversione dei campi standard */
		AdeUnitaImmHist destination = modelMapper.map(fabbricato, AdeUnitaImmHist.class);
		
		/* aggiunta del calcolo dell'hashing */
		destination.setHash(HashingUtils.getHashingForAnnotatedCols(5, fabbricato));
		
		return destination;
	}
	
	public static AdeDatoCatastaleHist convertDatoCatastaleFromDto(DatoCatastaleDto datoCatastale)
	{
		/* creazione del mapper */
		ModelMapper modelMapper = new ModelMapper();
		
		/* aggiunta dei convertitori necessari */
		modelMapper.addConverter(new StringToIntegerConverter());
		
		/* creazione dell'entita' con conversione dei campi standard */
		AdeDatoCatastaleHist destination = modelMapper.map(datoCatastale, AdeDatoCatastaleHist.class);
		
		/* aggiunta del calcolo dell'hashing */
		destination.setHash(HashingUtils.getHashingForAnnotatedCols(-1, datoCatastale));
		
		return destination;
	}
	
	public static AdeIndirizzoHist convertiIndirizzoFromDto(IndirizzoDto indirizzo)
	{
		/* creazione del mapper */
		ModelMapper modelMapper = new ModelMapper();
		
		/* aggiunta dei convertitori necessari */
		modelMapper.addConverter(new StringToIntegerConverter());
		
		/* creazione dell'entita' con conversione dei campi standard */
		AdeIndirizzoHist destination = modelMapper.map(indirizzo, AdeIndirizzoHist.class);
		
		/* aggiunta del calcolo dell'hashing */
		destination.setHash(HashingUtils.getHashingForAnnotatedCols(-1, indirizzo));
		
		return destination;
	}
	
	public static BatchJobDTO convertBatchJobFromEntity(BatchJob job)
	{
		/* creazione del mapper */
		ModelMapper modelMapper = new ModelMapper();
		
		/* aggiunta dei convertitori necessari */
		modelMapper.addConverter(new LocalDateTimeToStringConverter());
		
		/* conversione */
		return modelMapper.map(job, BatchJobDTO.class);
	}
	
	public static ProprietarioHist convertProprietarioFromDTO(ProprietarioDTO dto)
	{
		/* vista la particolare natura del flusso, non e' possibile usare 
		 * il ModelMapper come di consueto, quindi si procede manualmente */
		
		/* creazione di un'entita' vuota */
		ProprietarioHist proprietario = new ProprietarioHist();
		
		/* convertitore di date */
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("ddMMyyyy");
		
		
		/* inserimento dei campi ordinari */
		proprietario.setCodiceComune(dto.getCodAmm());
		proprietario.setSezione(dto.getSezione());
		proprietario.setIdSoggetto(dto.getIdSoggetto());
		proprietario.setTipoRecord(dto.getIdTipoSoggetto());
		
		/* caso persona fisica */
		if(StringUtils.isNotBlank(dto.getIdTipoSoggetto()) && dto.getIdTipoSoggetto().toUpperCase().equals("P"))
		{
			proprietario.setCognome(dto.getCognomeORDenominazione());
			proprietario.setNome(dto.getNomeORSede());
			proprietario.setSesso(dto.getSessoORCodiceFiscale());
			
			if(StringUtils.isNotBlank(dto.getDataNascita()))
				proprietario.setDataNascita(LocalDate.parse(dto.getDataNascita(), formatter));
			
			proprietario.setLuogoNascita(dto.getLuogoNascita());
			proprietario.setCodiceFiscale(dto.getCodiceFiscale());
			proprietario.setAltreInfo(dto.getAltreInfo());
		}
		
		/* caso persona giuridica */
		else if(StringUtils.isNotBlank(dto.getIdTipoSoggetto()) && dto.getIdTipoSoggetto().toUpperCase().equals("G"))
		{
			proprietario.setDenominazione(dto.getCognomeORDenominazione());
			proprietario.setSede(dto.getNomeORSede());
			proprietario.setCodiceFiscale(dto.getSessoORCodiceFiscale());
		}
		
		/* hashing */
		proprietario.setHash(HashingUtils.getHashingForAnnotatedCols(3, dto));
		
		return proprietario;
	}
	
	public static AdeParticellaHist convertParticellaFromDTO(TerrenoTipoRecord1DTO source)
	{
		/* creazione del mapper */
		ModelMapper modelMapper = new ModelMapper();
		
		/* aggiunta dei convertitori necessari */
		modelMapper.addConverter(new StringToIntegerConverter());
		
		/* creazione dell'entita' con conversione dei campi standard */
		AdeParticellaHist destination = modelMapper.map(source, AdeParticellaHist.class);
		
		/* aggiunta del calcolo dell'hashing */
		destination.setHash(HashingUtils.getHashingForAnnotatedCols(5, source));
		
		return destination;
	}
}
