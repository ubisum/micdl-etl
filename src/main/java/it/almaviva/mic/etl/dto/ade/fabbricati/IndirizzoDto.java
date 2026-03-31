package it.almaviva.mic.etl.dto.ade.fabbricati;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class IndirizzoDto 
{
	private String toponimo;
	private String indirizzo;
	private String civico1;
	private String civico2;
	private String civico3;
	private String cod_strada;
}
