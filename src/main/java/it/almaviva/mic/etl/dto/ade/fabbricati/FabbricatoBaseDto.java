package it.almaviva.mic.etl.dto.ade.fabbricati;

import it.almaviva.mic.etl.parsers.CsvPosition;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class FabbricatoBaseDto 
{
	@NotNull(message = "Il codice comune non puo' essere nullo")
	@Pattern(regexp = "[A-Z]{1}[0-9]{3}", message = "Formato del codice comune non corretto")
	@CsvPosition(0)
	private String codComune;
	
	@CsvPosition(1)
	@Pattern(regexp = "[A-Za-z0-9 ]{1}", message = "Formato della sezione non valido")
	private String sezione;
	
	@CsvPosition(2)
	private String idImmCatasto;
	
	@NotNull(message = "Il tipo catasto non puo' essere nullo")
	@Pattern(regexp = "T|F", message = "Il valore del tipo dev'essere 'T' o 'F'")
	@CsvPosition(3)
	private String tipoCatasto;
	
	@CsvPosition(4)
	private String progressivo;
	
	@Pattern(regexp = "1|2|3|4|5", message = "Il valore del tipo record dev'essere compreso tra 1 e 5")
	@CsvPosition(5)
	private String tipoRecord;
}
