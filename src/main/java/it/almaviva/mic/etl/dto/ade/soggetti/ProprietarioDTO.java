package it.almaviva.mic.etl.dto.ade.soggetti;

import it.almaviva.mic.etl.parsers.CsvPosition;
import it.almaviva.mic.etl.validation.ade.AdeSoggettoCFSesso;
import it.almaviva.mic.etl.validation.ade.DataValida;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AdeSoggettoCFSesso
public class ProprietarioDTO 
{
	@CsvPosition(0)
	@NotBlank(message = "109 - Il campo codice amministrativo non puo' essere assente")
	@Size(max = 4, message = " 110 - Il campo codice amministrativo non puo' superare i 4 caratteri")
	private String codAmm;
	
	@CsvPosition(1)
	@Size(max = 1, message = "111 - Il campo sezione puo' avere al massimo lunghezza 1")
	private String sezione;
	
	@CsvPosition(2)
	@Size(max = 15, message = "112 - Il campo soggetto non puo' superare i 15 caratteri")
	private String idSoggetto;
	
	@CsvPosition(3)
	@NotBlank(message = "113 - Il campo ID tipo soggetto non puo' essere assente")
	@Pattern(regexp = "P|G", message = "114 - L'ID tipo soggetto puo' avere solo i valori P e G")
	private String idTipoSoggetto;
	
	@CsvPosition(4)
	@Size(max = 150, message = "115  - Il campo cognome/denominazione non puo' superare i 150 caratteri")
	private String cognomeORDenominazione;
	
	@CsvPosition(5)
	@Size(max = 50, message = "116 - Il campo nome/sede non puo' superare i 50 caratteri")
	private String nomeORSede;
	
	@CsvPosition(6)
	@Size(max = 11, message = "117 - Il campo sesso/codice fiscale non puo' avere piu' di 11 caratteri")
	private String sessoORCodiceFiscale;
	
	@CsvPosition(7)
	@DataValida(message = "118 - La data di nascita potrebbe essere nel giusto fomato ma essere non valida")
	@Pattern(regexp = "^(0[1-9]|[12][0-9]|3[01])(0[1-9]|1[0-2])([0-9]{4})$", message = "119 - La data di nascita dev'essere nel formato GGMMAAAA")
	private String dataNascita;
	
	@CsvPosition(8)
	@Size(max = 4, message = "120- Il campo luogo nascita non puo' superare i 4 caratteri")
	private String luogoNascita;
	
	@CsvPosition(9)
	@Size(max = 16, message = "121 - Il campo codice fiscale (persona fisica) non puo' superare i 16 caratteri")
	private String codiceFiscale;
	
	@CsvPosition(10)
	@Size(max = 100, message = "122 - Il campo altre info non puo' superare i 100 caratteri")
	private String altreInfo;
	
}
