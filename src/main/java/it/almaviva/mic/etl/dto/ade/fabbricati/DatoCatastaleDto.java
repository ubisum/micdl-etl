package it.almaviva.mic.etl.dto.ade.fabbricati;

import it.almaviva.mic.etl.parsers.CsvPosition;
import it.almaviva.mic.etl.validation.ade.AdeEdificialita;
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
	private String foglio;
	
	@CsvPosition(2)
	private String numero;
	
	@CsvPosition(3)
	private String denominatore;
	
	@CsvPosition(4)
	private String subalterno;
	
	@CsvPosition(5)
	private String edificialita;
}
