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
public class PorzioneDTO 
{
	@CsvPosition(0)
	@Size(max = 2, message = "100 - L'ID porzione non puo' avere lunghezza maggiore di 2")
	@NotBlank(message = "101 - Uno o piu' ID porzione sono mancanti")
	private String idPorzione;
	
	@CsvPosition(1)
	@Size(max = 3, message = "102 - Il campo qualita' non puo' avere lunghezza maggiore di 3")
	private String qualita;
	
	@CsvPosition(2)
	@Size(max = 2, message = "103 - Il campo classe non puo' avere lunghezza maggiore di 2")
	private String classe;
	
	@CsvPosition(3)
	@Size(max = 5, message = "104 - Il campo ettari non puo' avere lunghezza maggiore di 5")
	private String ettari;
	
	@CsvPosition(4)
	@Size(max = 2, message = "105 - Il campo are non puo' avere lunghezza maggiore di 2")
	private String are;
	
	@CsvPosition(5)
	@Size(max = 2, message = "106 - Il campo centiare non puo' avere lunghezza maggiore di 2")
	private String centiare;
	
	@CsvPosition(6)
	@Size(max = 11, message = "107 - Il campo reddito dominicale euro non puo' avere lunghezza maggiore di 11")
	private String redditoDominicaleEuro;
	
	@CsvPosition(7)
	@Size(max = 11, message = "108 - Il campo reddito agrario euro non puo' avere lunghezza maggiore di 11")
	private String redditoAgrarioEuro;
}
