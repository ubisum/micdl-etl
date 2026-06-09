package it.almaviva.mic.etl.dto.ade.terreni;

import it.almaviva.mic.etl.parsers.CsvPosition;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
@AllArgsConstructor
public class RiservaParticellaDTO 
{
	@CsvPosition(0)
	@NotBlank(message = "097 - Uno o piu' codici riserva non sono presenti")
	@Size(max = 1, message = "098 - Uno o piu' codici riserva superano la lunghezza 1")
	private String codiceRiserva;
	
	@CsvPosition(1)
	@Size(max = 7, message = "099 - Uno o piu' partite iscrizione riserva superano la lunghezza 7")
	private String partitaIscrizioneRiserva;
}
