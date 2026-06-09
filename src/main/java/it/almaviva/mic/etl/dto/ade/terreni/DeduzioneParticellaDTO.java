package it.almaviva.mic.etl.dto.ade.terreni;

import it.almaviva.mic.etl.parsers.CsvPosition;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
@AllArgsConstructor
public class DeduzioneParticellaDTO 
{
	@CsvPosition(0)
	@Size(max = 6, message = "096 - Uno o piu' simboli deduzione superano la lunghezza 6")
	private String simboloDeduzione;
}
