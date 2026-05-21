package it.almaviva.mic.etl.validation.ade;

import org.apache.commons.lang3.StringUtils;

import it.almaviva.mic.etl.dto.ade.terreni.TerrenoTipoRecord1DTO;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class AdeEdificialitaTerreniValidator implements ConstraintValidator<AdeEdificialitaTerreni, TerrenoTipoRecord1DTO>
{
	@Override
	public boolean isValid(TerrenoTipoRecord1DTO value, ConstraintValidatorContext context) 
	{
		/* controllo della presenza dell'edificialita' */
		if(StringUtils.isBlank(value.getEdificialita()))
			return true;
		
		/* controllo che l'edificialita', se presente, sia uguale ad E OR numero ed edificialita siano coerenti */
		else if(StringUtils.isNotBlank(value.getEdificialita()) && !value.getEdificialita().equals("E"))
		{
			/* rimozione del messaggio di default */
			context.disableDefaultConstraintViolation();
			
			/*costruzione del nuovo vincolo violato */
			context.buildConstraintViolationWithTemplate("Se l'edificialita' e' presente, puo' assumere il solo valore E")
			                                            .addPropertyNode("numero")
			                                            .addConstraintViolation();
			
			return false;
		}
				
		else if(StringUtils.isNotBlank(value.getEdificialita()) && value.getEdificialita().equals("E") &&
			   (StringUtils.isBlank(value.getNumero()) || !value.getNumero().matches("^\\.[0-9]{4}$")))
		{
			/* rimozione del messaggio di default */
			context.disableDefaultConstraintViolation();
			
			/*costruzione del nuovo vincolo violato */
			context.buildConstraintViolationWithTemplate("Se l'edificialita' vale E, il numero dev'essere nella forma .[0-9]{4}")
			                                            .addPropertyNode("numero")
			                                            .addConstraintViolation();
					                                     
			return false;
		}
		
		/* controllo superato */
		else
			return true;
		
		
	}
}
