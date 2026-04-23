package it.almaviva.mic.etl.dto.ade.fabbricati;

import it.almaviva.mic.etl.parsers.CsvPosition;
import it.almaviva.mic.etl.validation.ade.AdeEdificialita;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
@EqualsAndHashCode(callSuper = false)
@AdeEdificialita
public class DatoCatastaleDto 
{
	@CsvPosition(0)
	private String sezioneUrbana;
	
	@CsvPosition(1)
	@Pattern(regexp = "[A-Za-z0-9]{1,4}", message = "Formato del foglio non valido")
	private String foglio;
	
	@CsvPosition(2)
	@Pattern(regexp = "[.?A-Za-z0-9]{1,5}", message = "Formato del numero/particella non valido")
	private String numero;
	
	@CsvPosition(3)
	@Pattern(regexp = "[0-9]{0,4}", message = "Formato del denominatore non valido")
	private String denominatore;
	
	@CsvPosition(4)
	@Pattern(regexp = "[A-Za-z0-9]{0,4}", message = "Formato del subalterno non valido")
	private String subalterno;
	
	@CsvPosition(5)
	private String edificialita;
}
