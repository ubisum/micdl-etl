package it.almaviva.mic.etl.dto.ade.fabbricati;

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
	private String sezione_rbana;
	private String foglio;
	private String numero;
	private String denominatore;
	private String subalterno;
	private String edificialita;
}
