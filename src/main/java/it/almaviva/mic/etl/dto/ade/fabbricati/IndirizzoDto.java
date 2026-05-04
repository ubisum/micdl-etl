package it.almaviva.mic.etl.dto.ade.fabbricati;

import it.almaviva.mic.etl.parsers.CsvPosition;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class IndirizzoDto 
{
	@CsvPosition(0)
	@Pattern(regexp = "[0-9]{1,3}", message = "Formato del toponimo non corretto")
	private String toponimo;
	
	@CsvPosition(1)
	@Size(max = 50, message = "Formato dell'indirizzo non corretto")
	private String indirizzo;
	
	@CsvPosition(2)
	@Size(max = 6, message = "Formato del civico 1 non corretto")
	private String civico1;
	
	@CsvPosition(3)
	@Size(max = 6, message = "Formato del civico 2 non corretto")
	private String civico2;
	
	@CsvPosition(4)
	@Size(max = 6, message = "Formato del civico 3 non corretto")
	private String civico3;
	
	@CsvPosition(5)
	@Pattern(regexp = "[0-9]{1,5}", message = "Formato del codice strada non corretto")
	private String codStrada;
}
