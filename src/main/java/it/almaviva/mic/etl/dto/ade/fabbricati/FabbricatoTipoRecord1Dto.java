package it.almaviva.mic.etl.dto.ade.fabbricati;

import it.almaviva.mic.etl.parsers.CsvPosition;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
@EqualsAndHashCode(callSuper = false)
public class FabbricatoTipoRecord1Dto extends FabbricatoBaseDto 
{
	/* ---------------------------- classe dell'unita' immobiliare ---------------------------------- */
	@CsvPosition(6)
	private String zonaCensuaria;
	
	@CsvPosition(7)
	private String categoria;
	
	@CsvPosition(8)
	private String classe;
	
	@CsvPosition(9)
	private String consistenza;
	
	@CsvPosition(10)
	private String superficie;
	
	@CsvPosition(11)
	private String renditaLire;
	
	@CsvPosition(12)
	private String renditaEuro;
	
	/* ----------------------------- ubicazione immobile ----------------------------------------- */
	@CsvPosition(13)
	private String lotto;
	
	@CsvPosition(14)
	private String edificio;
	
	@CsvPosition(15)
	private String scala;
	
	@CsvPosition(16)
	private String interno1;
	
	@CsvPosition(17)
	private String interno2;
	
	@CsvPosition(18)
	private String piano1;
	
	@CsvPosition(19)
	private String piano2;
	
	@CsvPosition(20)
	private String piano3;
	
	@CsvPosition(21)
	private String piano4;
	
	/* -------------------------------- registrazione atto unita' ------------------------------- */
	@Pattern(regexp = "^(0[1-9]|[12][0-9]|3[01])(0[1-9]|1[0-2])([0-9]{4})$", message = "La data efficacia dev'essere nel formato DDMMAAAA")
	@CsvPosition(22)
	private String regDataEfficiacia;
	
	@Pattern(regexp = "^(0[1-9]|[12][0-9]|3[01])(0[1-9]|1[0-2])([0-9]{4})$", message = "La data registrazione atti dev'essere nel formato DDMMAAAA")
	@CsvPosition(23)
	private String regDataregAtto;
	
	@CsvPosition(24)
	private String regTipoNota;
	
	@CsvPosition(25)
	private String regNumeroNota;
	
	@CsvPosition(26)
	private String regProgressivoNota;
	
	@Pattern(regexp = "^([0-9]{4})$", message = "L'anno nota dev'essere nel formato AAAA")
	@CsvPosition(27)
	private String regAnnoNota;
	
	/* conclusione atto unita' */
	@Pattern(regexp = "^(0[1-9]|[12][0-9]|3[01])(0[1-9]|1[0-2])([0-9]{4})$", message = "La data efficacia (conclusione atto) dev'essere nel formato DDMMAAAA")
	@CsvPosition(28)
	private String concDataEfficacia;
	
	@Pattern(regexp = "^(0[1-9]|[12][0-9]|3[01])(0[1-9]|1[0-2])([0-9]{4})$", message = "La data registrazione (conclusione atto) dev'essere nel formato DDMMAAAA")
	@CsvPosition(29)
	private String concDataRegAtto;
	
	@CsvPosition(30)
	private String concTipoNota;
	
	@CsvPosition(31)
	private String concNumeroNota;
	
	@CsvPosition(32)
	private String concProgressivoNota;
	
	@Pattern(regexp = "^([0-9]{4})$", message = "L'anno nota (conslusione atto) dev'essere nel formato AAAA")
	@CsvPosition(33)
	private String concAnnoNota;
	
	@CsvPosition(34)
	private String concPartita;
	
	@CsvPosition(35)
	private String concAnnotazione;
	
	@CsvPosition(36)
	private String concIdMutIniz;
	
	@CsvPosition(37)
	private String concIdMutFin;
	
	@CsvPosition(38)
	private String concProtocolloNotifica;
	
	@CsvPosition(39)
	private String concDataNotifica;
	
	@CsvPosition(40)
	private String concCdAttoGenerante;
	
	@CsvPosition(41)
	private String concDescrAttoGenerante;
	
	@CsvPosition(42)
	private String concCdAttoConclusivo;
	
	@CsvPosition(43)
	private String concDescrAttoConclusivo;
	
	@CsvPosition(44)
	private String concFlagClassamento;

}
