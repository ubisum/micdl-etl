package it.almaviva.mic.etl.validation.ade;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.ResolverStyle;

import org.apache.commons.lang3.StringUtils;

public class DataValidaValidator implements ConstraintValidator<DataValida, String> {

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("ddMMuuuu").withResolverStyle(ResolverStyle.STRICT);

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) 
    {
    	/* se la data non e' presente, si assume che rispetti le regole di conversione. 
    	 * La mancata presenza della data o il suo formato possono essere controllate
    	 * con le annotazioni standard
    	 * */
    	if(StringUtils.isBlank(value))
    		return true;
    	
        try 
        {
            LocalDate.parse(value, formatter);
            return true;
        } 
        
        catch (Exception e) 
        {
            return false;
        }
    }
}

