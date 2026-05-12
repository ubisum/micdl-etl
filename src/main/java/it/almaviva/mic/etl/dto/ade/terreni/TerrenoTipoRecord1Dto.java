package it.almaviva.mic.etl.dto.ade.terreni;

import it.almaviva.mic.etl.dto.ade.BeneImmobiliareDto;
import it.almaviva.mic.etl.parsers.CsvPosition;
import it.almaviva.mic.etl.validation.ade.AdeEdificialitaTerreni;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
@EqualsAndHashCode(callSuper = false)
@AdeEdificialitaTerreni
public class TerrenoTipoRecord1DTO extends BeneImmobiliareDto 
{
	@CsvPosition(6)
	@Pattern(regexp = "^([0-9]{1,5})$", message = "Formato del foglio non valido")
	private String foglio;
	
	@CsvPosition(7)
	@Size(max = 5, message = "La lunghezza del campo numero non puo' superare i 5 caratteri")
	private String numero;
	
	@CsvPosition(8)
	@Size(max = 4, message = "La lunghezza del campo denominatore non puo' superare i 4 caratteri")
	private String denominatore;
	
	@CsvPosition(9)
	@Size(max = 4, message = "La lunghezza del campo subalterno non puo' superare i 4 caratteri")
	private String subalterno;
	
	@CsvPosition(10)
	@Size(max = 1, message = "La lunghezza del campo edificialita' non puo' superare 1 carattere")
	private String edificialita;
	
	@CsvPosition(11)
	@Size(max = 3, message = "La lunghezza del campo qualita' non puo' superare i 3 caratteri")
	private String qualita;
	
	@CsvPosition(12)
	@Size(max = 2, message = "La lunghezza del campo classe non puo' superare i 2 caratteri")
	private String classe;
	
	@CsvPosition(13)
	@Pattern(regexp = "^([0-9]{1,5})$", message = "Formato degli ettari non valido")
	private String ettari;
	
	@CsvPosition(14)
	@Pattern(regexp = "^([0-9]{1,2})$", message = "Formato delle are non valido")
	private String are;
	
	@CsvPosition(15)
	@Pattern(regexp = "^([0-9]{1,2})$", message = "Formato delle centiare non valido")
	private String centiare;
	
	@CsvPosition(16)
	@Size(max = 1, message = "La lunghezza del campo flag reddito non puo' superare 1 carattere")
	private String flagReddito;
	
	@CsvPosition(17)
	@Size(max = 1, message = "La lunghezza del campo flag porzione non puo' superare 1 carattere")
	private String flagPorzione;
	
	@CsvPosition(18)
	@Size(max = 1, message = "La lunghezza del campo flag deduzioni' non puo' superare 1 carattere")
	private String flagDeduzioni;
	
	@CsvPosition(19)
	@Pattern(regexp = "^([0-9]{1,9})$", message = "Formato del reddito dominicale (lire) non valido")
	private String redditoDominicaleLire;
	
	@CsvPosition(20)
	@Pattern(regexp = "^([0-9]{1,8})$", message = "Formato del reddito agrario (lire) non valido")
	private String redditoAgrarioLire;
	
	@CsvPosition(21)
	@Pattern(regexp = "^([,0-9]{1,12})$", message = "Formato del reddito dominicale (euro) non valido")
	private String redditoDominicaleEuro;
	
	@CsvPosition(22)
	@Pattern(regexp = "^([,0-9]{1,11})$", message = "Formato del reddito agrario (euro) non valido")
	private String redditoAgrarioEuro;
	
	@CsvPosition(23)
	@Pattern(regexp = "^(0[1-9]|[12][0-9]|3[01])(0[1-9]|1[0-2])([0-9]{4})$", message = "La data di efficacia (registrazione) dev'essere nel formato GGMMAAAA")
	private String dataEfficaciaReg;
	
	@CsvPosition(24)
	@Pattern(regexp = "^(0[1-9]|[12][0-9]|3[01])(0[1-9]|1[0-2])([0-9]{4})$", message = "La data di registrazione atti (registrazione) dev'essere nel formato GGMMAAAA")
	private String dataRegistrazioneAttiReg;
	
	@CsvPosition(25)
	@Size(max = 1, message = "La lunghezza del campo tipo nota (registrazione) non puo' superare 1 carattere")
	private String tipoNotaReg;
	
	@CsvPosition(26)
	@Size(max = 6, message = "La lunghezza del campo numero nota (registrazione) non puo' superare i 6 caratteri")
	private String numeroNotaReg;
	
	@CsvPosition(27)
	@Size(max = 3, message = "La lunghezza del campo progressivo (registrazione) non puo' superare i 3 caratteri")
	private String progressivoNotaReg;
	
	@CsvPosition(28)
	@Pattern(regexp = "^([0-9]{4})$", message = "Formato dell'anno nota (registrazione) non valido")
	private String annoNotaReg;
	
	@CsvPosition(29)
	@Pattern(regexp = "^(0[1-9]|[12][0-9]|3[01])(0[1-9]|1[0-2])([0-9]{4})$", message = "La data di efficacia (conclusione) dev'essere nel formato GGMMAAAA")
	private String dataEfficaciaConcl;
	
	@CsvPosition(30)
	@Pattern(regexp = "^(0[1-9]|[12][0-9]|3[01])(0[1-9]|1[0-2])([0-9]{4})$", message = "La data di registrazione atti (conclusione) dev'essere nel formato GGMMAAAA")
	private String dataRegistrazioneAttiConcl;
	
	@CsvPosition(31)
	@Size(max = 1, message = "La lunghezza del campo tipo nota (conclusione) non puo' superare 1 carattere")
	private String tipoNotaConcl;
	
	@CsvPosition(32)
	@Size(max = 6, message = "La lunghezza del campo numero nota (conclusione) non puo' superare i 6 caratteri")
	private String numeroNotaConcl;
	
	@CsvPosition(33)
	@Size(max = 3, message = "La lunghezza del campo progressivo (conclusione) non puo' superare i 3 caratteri")
	private String progressivoNotaConcl;
	
	@CsvPosition(34)
	@Pattern(regexp = "^([0-9]{4})$", message = "Formato dell'anno nota (conclusione) non valido")
	private String annoNotaConcl;
	
	@CsvPosition(35)
	@Size(max = 7, message = "La lunghezza del campo partita non puo' superare i 7 caratteri")
	private String partita;
	
	@CsvPosition(36)
	@Size(max = 200, message = "La lunghezza del campo annotazione non puo' superare i 200 caratteri")
	private String annotazione;
	
	@CsvPosition(37)
	@Pattern(regexp = "^([0-9]{1,9})$", message = "Formato del campo ID mutazione iniziale non valido")
	private String idMutazioneIniziale;
	
	@CsvPosition(38)
	@Pattern(regexp = "^([0-9]{1,9})$", message = "Formato del campo ID mutazione finale non valido")
	private String idMutazioneFinale;
	
	@CsvPosition(39)
	@Size(max = 3, message = "La lunghezza del campo codice causale atto generante non puo' superare i 3 caratteri")
	private String cdCausaleAttoGenerante;
	
	@CsvPosition(40)
	@Size(max = 100, message = "La lunghezza del campo descrizione atto generante non puo' superare i 100 caratteri")
	private String descrizioneAttoGenerante;
	
	@CsvPosition(41)
	@Size(max = 3, message = "La lunghezza del campo codice causale atto conclusivo non puo' superare i 3 caratteri")
	private String cdcausaleAttoConclusivo;
	
	@CsvPosition(42)
	@Size(max = 100, message = "La lunghezza del campo descrizione atto conclusivo non puo' superare i 100 caratteri")
	private String descrizioneAttoConclusivo;
}
