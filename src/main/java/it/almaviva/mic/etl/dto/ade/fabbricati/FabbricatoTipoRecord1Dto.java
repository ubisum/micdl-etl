package it.almaviva.mic.etl.dto.ade.fabbricati;

import it.almaviva.mic.etl.dto.ade.BeneImmobiliareDTO;
import it.almaviva.mic.etl.parsers.CsvPosition;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
@EqualsAndHashCode(callSuper = false)
public class FabbricatoTipoRecord1Dto extends BeneImmobiliareDTO 
{
	/* ---------------------------- classe dell'unita' immobiliare ---------------------------------- */
	@CsvPosition(6)
	@Size(max = 3, message = "010 - Formato della zona censuaria non valido")
	private String zonaCensuaria;
	
	@CsvPosition(7)
	@Size(max = 4, message = "011 - Formato della categoria non valido")
	private String categoria;
	
	@CsvPosition(8)
	@Size(max = 2, message = "012 - Formato della classe non valido")
	private String classe;
	
	@CsvPosition(9)
	@Size(max = 7, message = "013 - Formato della consistenza non valido")
	private String consistenza;
	
	@CsvPosition(10)
	@Pattern(regexp = "^([0-9]{1,5})$", message = "014 - Formato della superficie non valido")
	private String superficie;
	
	@CsvPosition(11)
	@Pattern(regexp = "^(?=.{1,15}$)\\d+(,\\d{1,3})?$", message = "015 - Formato della rendita in lire non valido")
	private String renditaLire;
	
	@CsvPosition(12)
	@Pattern(regexp = "^(?=.{1,18}$)\\d+(,\\d{1,3})?$", message = "016 - Formato della rendita in euro non valido")
	private String renditaEuro;
	
	/* ----------------------------- ubicazione immobile ----------------------------------------- */
	@CsvPosition(13)
	@Size(max = 2, message = "017 - Formato del lotto non valido")
	private String lotto;
	
	@CsvPosition(14)
	@Size(max = 2, message = "018 - Formato dell'edificio non valido")
	private String edificio;
	
	@CsvPosition(15)
	@Size(max = 2, message = "019 - Formato della scala non valido")
	private String scala;
	
	@CsvPosition(16)
	@Size(max = 3, message = "020 - Formato dell'interno 1 non valido")
	private String interno1;
	
	@CsvPosition(17)
	@Size(max = 3, message = "021 - Formato dell'interno 2 non valido")
	private String interno2;
	
	@CsvPosition(18)
	@Size(max = 4, message = "022 - Formato del piano 1 non valido")
	private String piano1;
	
	@CsvPosition(19)
	@Size(max = 4, message = "023 - Formato del piano 2 non valido")
	private String piano2;
	
	@CsvPosition(20)
	@Size(max = 4, message = "024 - Formato del piano 3 non valido")
	private String piano3;
	
	@CsvPosition(21)
	@Size(max = 4, message = "025 - Formato del piano 4 non valido")
	private String piano4;
	
	/* -------------------------------- registrazione atto unita' ------------------------------- */
	@Pattern(regexp = "^(0[1-9]|[12][0-9]|3[01])(0[1-9]|1[0-2])([0-9]{4})$", message = "026 - La data efficacia dev'essere nel formato DDMMAAAA")
	@CsvPosition(22)
	private String regDataEfficiacia;
	
	@Pattern(regexp = "^(0[1-9]|[12][0-9]|3[01])(0[1-9]|1[0-2])([0-9]{4})$", message = "027 - La data registrazione atti dev'essere nel formato DDMMAAAA")
	@CsvPosition(23)
	private String regDataregAtto;
	
	@CsvPosition(24)
	@Size(max = 1, message = "028 - Formato del tipo nota non valido")
	private String regTipoNota;
	
	@CsvPosition(25)
	@Size(max = 6, message = "029 - Formato del numero nota non valido")
	private String regNumeroNota;
	
	@CsvPosition(26)
	@Size(max = 3, message = "030 - Formato del progressivo nota non valido")
	private String regProgressivoNota;
	
	@Pattern(regexp = "^([0-9]{4})$", message = "031 - L'anno nota dev'essere nel formato AAAA")
	@CsvPosition(27)
	private String regAnnoNota;
	
	/* conclusione atto unita' */
	@Pattern(regexp = "^(0[1-9]|[12][0-9]|3[01])(0[1-9]|1[0-2])([0-9]{4})$", message = "032 - La data efficacia (conclusione atto) dev'essere nel formato DDMMAAAA")
	@CsvPosition(28)
	private String concDataEfficacia;
	
	@Pattern(regexp = "^(0[1-9]|[12][0-9]|3[01])(0[1-9]|1[0-2])([0-9]{4})$", message = "033 - La data registrazione (conclusione atto) dev'essere nel formato DDMMAAAA")
	@CsvPosition(29)
	private String concDataRegAtto;
	
	@CsvPosition(30)
	@Size(max = 1, message = "034 - Formato del tipo nota (conclusione) non valido")
	private String concTipoNota;
	
	@CsvPosition(31)
	@Size(max = 6, message = "035 - Formato del numero nota (conclusione) non valido")
	private String concNumeroNota;
	
	@CsvPosition(32)
	@Size(max = 3, message = "036 - Formato del progressivo nota (conclusione) non valido")
	private String concProgressivoNota;
	
	@Pattern(regexp = "^([0-9]{4})$", message = "037 - L'anno nota (conslusione atto) dev'essere nel formato AAAA")
	@CsvPosition(33)
	private String concAnnoNota;
	
	@CsvPosition(34)
	@Size(max = 7, message = "038 - Formato della partita (conclusione) non valido")
	private String concPartita;
	
	@CsvPosition(35)
	@Size(max = 200, message = "039 - Formato dell'annotazione (conclusione) non valido")
	private String concAnnotazione;
	
	@CsvPosition(36)
	@Pattern(regexp = "[0-9]{1,9}", message = "040 - Formato dell'identificazione mutazione iniziale non valido")
	private String concIdMutIniz;
	
	@CsvPosition(37)
	@Pattern(regexp = "[0-9]{1,9}", message = "041 - Formato dell'identificazione mutazione finale non valido")
	private String concIdMutFin;
	
	@CsvPosition(38)
	@Size(max = 18, message = "042 - Formato del protocollo notifica non valido")
	private String concProtocolloNotifica;
	
	@CsvPosition(39)
	@Pattern(regexp = "^(0[1-9]|[12][0-9]|3[01])(0[1-9]|1[0-2])([0-9]{4})$", message = "043 - La data notifica (conclusione atto) dev'essere nel formato DDMMAAAA")
	private String concDataNotifica;
	
	@CsvPosition(40)
	@Size(max = 3, message = "044 - Formato del codice causale atto generante non valido")
	private String concCdAttoGenerante;
	
	@CsvPosition(41)
	@Size(max = 100, message = "045 - Formato della descrizione dell'atto generante non valido")
	private String concDescrAttoGenerante;
	
	@CsvPosition(42)
	@Size(max = 3, message = "046 - Formato del codice causale atto coclusivo non valido")
	private String concCdAttoConclusivo;
	
	@CsvPosition(43)
	@Size(max = 100, message = "047 - Formato della descrizione dell'atto conclusivo non valido")
	private String concDescrAttoConclusivo;
	
	@CsvPosition(44)
	@Pattern(regexp = "0|1|2|3|4|5", message = "048 - Formato del flag classamento non valido")
	private String concFlagClassamento;

}
