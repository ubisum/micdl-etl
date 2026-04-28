package it.almaviva.mic.etl.validation.ade;

import java.util.Arrays;

import org.apache.commons.lang3.StringUtils;

import it.almaviva.mic.etl.dto.ade.soggetti.SoggettoDTO;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class AdeSoggettoCFSessoValidator implements ConstraintValidator<AdeSoggettoCFSesso, SoggettoDTO> 
{

	@Override
	public boolean isValid(SoggettoDTO value, ConstraintValidatorContext context) 
	{	
		/* --------------------------- CASISTICHE --------------------------------------------------------------------- */
		
		/* 1. Persona fisica per la quale non e' stato fornito il sesso */
		if(StringUtils.isNotBlank(value.getIdTipoSoggetto())&& 
		   value.getIdTipoSoggetto().toUpperCase().equals("P") && 
		   StringUtils.isNotBlank(value.getSessoORCodiceFiscale()) && 
		   !Arrays.asList("1", "2").contains(value.getSessoORCodiceFiscale()))
			return false;
		
		/* 2. Persona giuridica per la quale non e' stato fornito correttamente un codice fiscale */
		else if(StringUtils.isNotBlank(value.getIdTipoSoggetto())&& 
				value.getIdTipoSoggetto().toUpperCase().equals("G") && 
				StringUtils.isNotBlank(value.getSessoORCodiceFiscale()) && 
				value.getSessoORCodiceFiscale().length() != 11)
			return false;
		
		else
			return true;
		
	}

}
