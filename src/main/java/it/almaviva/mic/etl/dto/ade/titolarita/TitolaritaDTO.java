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
	@NotBlank(message = "123 - Il campo codice amministrativo non puo' essere assente")
	@Size(max = 4, message = "124 - Il campo codice amministrativo non puo' superare i 4 caratteri")
	private String codAmm;
	
	@CsvPosition(1)
	@Size(max = 1, message = "125 - Il campo sezione puo' avere al massimo lunghezza 1")
	private String sezione;
	
	@CsvPosition(2)
	@NotBlank(message = "126 - L'ID soggetto non puo' essere assente")
	@Size(max = 15, message = "127 - Il campo soggetto non puo' superare i 15 caratteri")
	private String idSoggetto;
	
	@CsvPosition(3)
	@NotBlank(message = "128 - L'ID soggetto non puo' essere assente")
	@Pattern(regexp = "P|G", message = "129 - L'ID tipo soggetto puo' avere solo i valori P e G")
	private String idTipoSoggetto;
	
	@CsvPosition(4)
	@NotBlank(message = "130 - L'ID immobile catasto non puo' essere assente")
	@Size(max = 15, message = "131 - Il campo ID immobile catasto non puo' superare i 15 caratteri")
	private String idImmCatasto;
	
	@CsvPosition(5)
	@NotBlank(message = "132 - Il campo ID tipo immobile non puo' essere assente")
	@Pattern(regexp = "T|F", message = "133 - L'ID tipo immobile puo' avere solo i valori T ed F")
	private String idTipoImmobile;
	
	@CsvPosition(6)
	@Size(max = 3, message = "134 - Il codice diritto non puo' superare i 3 caratteri")
	private String codDiritto;
	
	@CsvPosition(7)
	@Size(max = 200, message = "135 - Il titolo non codificato non puo' superare i 200 caratteri")
	private String titoloNonCodificato;
	
	@CsvPosition(8)
	@Pattern(regexp = "^([0-9]{1,9})$", message = "136 - Formato della quota numeratore non valido")
	private String quotaNumeratore;
	
	@CsvPosition(9)
	@Pattern(regexp = "^([0-9]{1,9})$", message = "137 - Formato della quota denominatore non valido")
	private String quotaDenominatore;
	
	@CsvPosition(10)
	@Pattern(regexp = "C|D|P|S|[ ]+", message = "138 - Valore del regime non valido")
	private String regime;
	
	@CsvPosition(11)
	@Pattern(regexp = "^([0-9]{1,15})$", message = "139 - Formato soggetto riferimento non valido")
	private String soggettoRiferimento;
	
	@CsvPosition(12)
	@Pattern(regexp = "^(0[1-9]|[12][0-9]|3[01])(0[1-9]|1[0-2])([0-9]{4})$", message = "140 - La data di validita' (registrazione) dev'essere nel formato GGMMAAAA")
	private String dataValiditaReg;
	
	@CsvPosition(13)
	@Size(max = 1, message = "141 - Il campo tipo nota (registrazione) puo' avere al massimo lunghezza 1")
	private String tipoNotaReg;
	
	@CsvPosition(14)
	@Size(max = 6, message = "142 - Il campo numero nota (registrazione) puo' avere al massimo lunghezza 6")
	private String numeroNotaReg;
	
	@CsvPosition(15)
	@Size(max = 3, message = "143 - Il campo progressivo nota (registrazione) puo' avere al massimo lunghezza 3")
	private String progressivoNotaReg;
	
	@CsvPosition(16)
	@Pattern(regexp = "^([0-9]{4})$", message = "144 - Formato dell'anno nota (registrazione) non valido")
	private String annoNotaReg;
	
	@CsvPosition(17)
	@Pattern(regexp = "^(0[1-9]|[12][0-9]|3[01])(0[1-9]|1[0-2])([0-9]{4})$", message = "145 - La data registrazione atti dev'essere nel formato GGMMAAAA")
	private String dataRegistrazioneAttiReg;
	
	@CsvPosition(18)
	@Size(max = 7, message = "146 - Il campo partita (registrazione) puo' avere al massimo lunghezza 7")
	private String partitaReg;
	
	@CsvPosition(19)
	@Pattern(regexp = "^(0[1-9]|[12][0-9]|3[01])(0[1-9]|1[0-2])([0-9]{4})$", message = "147 - La data di validita' (conclusione) dev'essere nel formato GGMMAAAA")
	private String dataValiditaConcl;
	
	@CsvPosition(20)
	@Size(max = 1, message = "148 - Il campo tipo nota (conclusione) puo' avere al massimo lunghezza 1")
	private String tipoNotaConcl;
	
	@CsvPosition(21)
	@Size(max = 6, message = "149 - Il campo numero nota (conclusione) puo' avere al massimo lunghezza 6")
	private String numeroNotaConcl;
	
	@CsvPosition(22)
	@Size(max = 3, message = "150 - Il campo progressivo nota (conclusione) puo' avere al massimo lunghezza 3")
	private String progressivoNotaConcl;
	
	@CsvPosition(23)
	@Pattern(regexp = "^([0-9]{4})$", message = "151 - Formato dell'anno nota (conclusione) non valido")
	private String annoNotaConcl;
	
	@CsvPosition(24)
	@Pattern(regexp = "^(0[1-9]|[12][0-9]|3[01])(0[1-9]|1[0-2])([0-9]{4})$", message = "152 - La data registrazione atti (conclusione) dev'essere nel formato GGMMAAAA")
	private String dataRegistrazioneAttiConcl;
	
	@CsvPosition(25)
	@Pattern(regexp = "^([0-9]{1,9})$", message = "153 - Formato ID mutazione iniziale non valido")
	private String idMutazioneIniz;
	
	@CsvPosition(26)
	@Pattern(regexp = "^([0-9]{1,9})$", message = "154 - Formato ID mutazione finale non valido")
	private String idMutazioneFin;
	
	@CsvPosition(27)
	@Size(max = 15, message = "155 - L'identificativo titolarita' non puo' superare i 15 caratteri")
	private String idTitolarita;
	
	@CsvPosition(28)
	@Size(max = 50, message = "156 - Il campo codice causale atto generante puo' avere al massimo lunghezza 3")
	private String cdCausaleAttoGenerante;
	
	@CsvPosition(29)
	@Size(max = 100, message = "157 - Il campo descrizione atto generante puo' avere al massimo lunghezza 100")
	private String descrizioneAttoGenerante;
	
	@CsvPosition(30)
	@Size(max = 50, message = "158 - Il campo codice causale atto conclusivo puo' avere al massimo lunghezza 3")
	private String cdCausaleAttoConclusivo;
	
	@CsvPosition(31)
	@Size(max = 100, message = "159 - Il campo descrizione atto conclusivo puo' avere al massimo lunghezza 100")
	private String descrizioneAttoConclusivo;
	
	private Integer rowId;
	
}
