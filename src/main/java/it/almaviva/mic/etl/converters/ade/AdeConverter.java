package it.almaviva.mic.etl.converters.ade;

import org.modelmapper.ModelMapper;

import it.almaviva.mic.etl.converters.StringToIntegerConverter;
import it.almaviva.mic.etl.dto.ade.fabbricati.FabbricatoTipoRecord1Dto;
import it.almaviva.mic.etl.entities.ade.AdeUnitaImmHist;
import it.almaviva.mic.etl.utils.HashingUtils;

public class AdeConverter 
{
	public static AdeUnitaImmHist convertFABFromDto(FabbricatoTipoRecord1Dto fabbricato)
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
}
