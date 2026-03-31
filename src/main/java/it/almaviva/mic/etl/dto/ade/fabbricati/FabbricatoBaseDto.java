package it.almaviva.mic.etl.dto.ade.fabbricati;

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
	private String codiceComune;
	
	private String sezione;
	private String id_imm_catasto;
	
	@NotNull(message = "Il tipo catasto non puo' essere nullo")
	private String tipo_catasto;
	
	private String progressivo;
	
	@Pattern(regexp = "T|F", message = "Il valore del tipo dev'essere 'T' o 'F'")
	private String tipo; 

	@Pattern(regexp = "1|2|3|4|5", message = "Il valore del tipo record dev'essere compreso tra 1 e 5")
	private String tipo_record;
}
