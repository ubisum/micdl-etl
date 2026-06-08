package it.almaviva.mic.etl.dto.ade.fabbricati;

import it.almaviva.mic.etl.parsers.CsvPosition;
import it.almaviva.mic.etl.validation.ade.AdeEdificialita;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
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
	@Size(max = 3, message = "049 - Formato della sezione urbana non valido")
	private String sezioneUrbana;
	
	@CsvPosition(1)
	@Size(max = 4, message = "050 - Formato del foglio non valido")
	private String foglio;
	
	@CsvPosition(2)
	/* il codice 051 e' dichiarato all'interno dell'annotazione @AdeEdificialita e nelle classi correlate */
	private String numero;
	
	@CsvPosition(3)
	@Pattern(regexp = "[0-9]{1,4}", message = "052 - Formato del denominatore non valido")
	private String denominatore;
	
	@CsvPosition(4)
	@Size(max = 4, message = "053 - Formato del subalterno non valido")
	private String subalterno;
	
	@CsvPosition(5)
	/* il codice di errore correlato alla validazione di questo campo e' sempre 051 */
	private String edificialita;
}
