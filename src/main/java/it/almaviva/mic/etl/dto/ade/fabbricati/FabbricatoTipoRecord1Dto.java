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
	private String zona_censuaria;
	
	@CsvPosition(7)
	private String categoria;
	
	@CsvPosition(8)
	private String classe;
	
	@CsvPosition(9)
	private String consistenza;
	
	@CsvPosition(10)
	private String superficie;
	
	@CsvPosition(11)
	private String rendita_lire;
	
	@CsvPosition(12)
	private String rendita_euro;
	
	/* ----------------------------- ubicazione immobile ----------------------------------------- */
	@CsvPosition(13)
	private String otto;
	
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
	private String data_efficacia;
	
	@Pattern(regexp = "^(0[1-9]|[12][0-9]|3[01])(0[1-9]|1[0-2])([0-9]{4})$", message = "La data registrazione atti dev'essere nel formato DDMMAAAA")
	@CsvPosition(23)
	private String data_registrazione_atti;
	
	@CsvPosition(24)
	private String tipo_nota;
	
	@CsvPosition(25)
	private String numero_nota;
	
	@CsvPosition(26)
	private String progressivo_nota;
	
	@Pattern(regexp = "^([0-9]{4})$", message = "La data nota dev'essere nel formato AAAA")
	@CsvPosition(27)
	private String anno_nota;
	
	/* conclusione atto unita' */
	@Pattern(regexp = "^(0[1-9]|[12][0-9]|3[01])(0[1-9]|1[0-2])([0-9]{4})$", message = "La data efficacia (conclusione atto) dev'essere nel formato DDMMAAAA")
	@CsvPosition(28)
	private String data_efficacia_concl;
	
	@Pattern(regexp = "^(0[1-9]|[12][0-9]|3[01])(0[1-9]|1[0-2])([0-9]{4})$", message = "La data registrazione (conclusione atto) dev'essere nel formato DDMMAAAA")
	@CsvPosition(29)
	private String data_registrazione_atti_concl;
	
	@CsvPosition(30)
	private String tipo_nota_concl;
	
	@CsvPosition(31)
	private String numero_nota_concl;
	
	@CsvPosition(32)
	private String progressivo_nota_concl;
	
	@CsvPosition(33)
	private String anno_nota_concl;
	
	@CsvPosition(34)
	private String partita;
	
	@CsvPosition(35)
	private String annotazione;
	
	@CsvPosition(36)
	private String id_mutazione_iniz;
	
	@CsvPosition(37)
	private String id_mutazione_fin;
	
	@CsvPosition(38)
	private String protocollo_notifica;
	
	@CsvPosition(39)
	private String data_notifica;
	
	/** DA DECOMMENTARE ALL'ARRIVO DEI NUOVI CAMPI NEL TRACCIATO 
	private String cd_causale_atto_generante;
	private String descrizione_atto_generante;
	private String cd_causale_atto_conclusivo;
	private String descrizione_atto_conclusivo;
	private String flag_classamento;
	**/

}
