package it.almaviva.mic.etl.dto.ade.fabbricati;

import it.almaviva.mic.etl.parsers.CsvPosition;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class IndirizzoDto 
{
	@CsvPosition(0)
	private String toponimo;
	
	@CsvPosition(1)
	private String indirizzo;
	
	@CsvPosition(2)
	private String civico1;
	
	@CsvPosition(3)
	private String civico2;
	
	@CsvPosition(4)
	private String civico3;
	
	@CsvPosition(5)
	private String codStrada;
}
