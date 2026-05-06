package it.almaviva.mic.etl.dto.ade.titolarita;

import it.almaviva.mic.etl.parsers.CsvPosition;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class TitolaritaDTO 
{
	@CsvPosition(0)
	@NotBlank(message = "Il campo codice amministrativo non puo' essere assente")
	@Size(max = 4, message = "Il campo codice amministrativo non puo' superare i 4 caratteri")
	private String codAmm;
	
	@CsvPosition(1)
	@Size(max = 1, message = "Il campo sezione puo' avere al massimo lunghezza 1")
	private String sezione;
	
	@CsvPosition(2)
	@Size(max = 15, message = "Il campo soggetto non puo' superare i 15 caratteri")
	private String idSoggetto;
	
	@CsvPosition(3)
	@Pattern(regexp = "P|G", message = "L'ID tipo soggetto puo' avere solo i valori P e G")
	private String idTipoSoggetto;
	
	@CsvPosition(4)
	@Size(max = 15, message = "Il campo ID immobile catasto non puo' superare i 15 caratteri")
	private String idImmCatasto;
	
	@CsvPosition(5)
	@NotBlank(message = "Il campo ID tipo immobile non puo' essere assente")
	@Pattern(regexp = "T|F", message = "L'ID tipo immobile puo' avere solo i valori T ed F")
	private String idTipoImmobile;
	
	@CsvPosition(6)
	@Size(max = 3, message = "Il codice diritto non puo' superare i 3 caratteri")
	private String codDiritto;
	
	@CsvPosition(7)
	@Size(max = 200, message = "Il titolo non codificato non puo' superare i 200 caratteri")
	private String titoloNonCodificato;
	
	@CsvPosition(8)
	@Pattern(regexp = "^([0-9]{1,9})$", message = "Formato della quota numeratore non valido")
	private String quotaNumeratore;
	
	@CsvPosition(9)
	@Pattern(regexp = "^([0-9]{1,9})$", message = "Formato della quota denominatore non valido")
	private String quotaDenominatore;
	
	@CsvPosition(10)
	@Pattern(regexp = "C|D|P|S", message = "Valore del regime non valido")
	private String regime;
	
	@CsvPosition(11)
	@Pattern(regexp = "^([0-9]{1,15})$", message = "Formato soggetto riferimento non valido")
	private String soggettoRiferimento;
	
	@CsvPosition(12)
	@Pattern(regexp = "^(0[1-9]|[12][0-9]|3[01])(0[1-9]|1[0-2])([0-9]{4})$", message = "La data di validita' (registrazione) dev'essere nel formato GGMMAAAA")
	private String dataValiditaReg;
	
	@CsvPosition(13)
	@Size(max = 1, message = "Il campo tipo nota (registrazione) puo' avere al massimo lunghezza 1")
	private String tipoNotaReg;
	
	@CsvPosition(14)
	@Size(max = 6, message = "Il campo numero nota (registrazione) puo' avere al massimo lunghezza 6")
	private String numeroNotaReg;
	
	@CsvPosition(15)
	@Size(max = 3, message = "Il campo progressivo nota (registrazione) puo' avere al massimo lunghezza 3")
	private String progressivoNotaReg;
	
	@CsvPosition(16)
	@Pattern(regexp = "^([0-9]{4})$", message = "Formato dell'anno nota (registrazione) non valido")
	private String annoNotaReg;
	
	@CsvPosition(17)
	@Pattern(regexp = "^(0[1-9]|[12][0-9]|3[01])(0[1-9]|1[0-2])([0-9]{4})$", message = "La data registrazione atti dev'essere nel formato GGMMAAAA")
	private String dataRegistrazioneAttiReg;
	
	@CsvPosition(18)
	@Size(max = 7, message = "Il campo partita (registrazione) puo' avere al massimo lunghezza 7")
	private String partitaReg;
	
	@CsvPosition(19)
	@Pattern(regexp = "^(0[1-9]|[12][0-9]|3[01])(0[1-9]|1[0-2])([0-9]{4})$", message = "La data di validita' (conclusione) dev'essere nel formato GGMMAAAA")
	private String dataValiditaConcl;
	
	@CsvPosition(20)
	@Size(max = 1, message = "Il campo tipo nota (conclusione) puo' avere al massimo lunghezza 1")
	private String tipoNotaConcl;
	
	@CsvPosition(21)
	@Size(max = 6, message = "Il campo numero nota (conclusione) puo' avere al massimo lunghezza 6")
	private String numeroNotaConcl;
	
	@CsvPosition(22)
	@Size(max = 3, message = "Il campo progressivo nota (conclusione) puo' avere al massimo lunghezza 3")
	private String progressivoNotaConcl;
	
	@CsvPosition(23)
	@Pattern(regexp = "^([0-9]{4})$", message = "Formato dell'anno nota (conclusione) non valido")
	private String annoNotaConcl;
	
	@CsvPosition(24)
	@Pattern(regexp = "^(0[1-9]|[12][0-9]|3[01])(0[1-9]|1[0-2])([0-9]{4})$", message = "La data registrazione atti (conclusione) dev'essere nel formato GGMMAAAA")
	private String dataRegistrazioneAttiConcl;
	
	@CsvPosition(25)
	@Pattern(regexp = "^([0-9]{1,9})$", message = "Formato ID mutazione iniziale non valido")
	private String idMutazioneIniz;
	
	@CsvPosition(26)
	@Pattern(regexp = "^([0-9]{1,9})$", message = "Formato ID mutazione finale non valido")
	private String idMutazioneFin;
	
	@CsvPosition(27)
	@Size(max = 3, message = "Il campo codice causale atto generante puo' avere al massimo lunghezza 3")
	private String cdCausaleAttoGenerante;
	
	@CsvPosition(28)
	@Size(max = 100, message = "Il campo descrizione atto generante puo' avere al massimo lunghezza 100")
	private String descrizioneAttoGenerante;
	
	@CsvPosition(29)
	@Size(max = 3, message = "Il campo codice causale atto conclusivo puo' avere al massimo lunghezza 3")
	private String cdCausaleAttoConclusivo;
	
	@CsvPosition(30)
	@Size(max = 100, message = "Il campo descrizione atto conclusivo puo' avere al massimo lunghezza 100")
	private String descrizioneAttoConclusivo;
	
}
