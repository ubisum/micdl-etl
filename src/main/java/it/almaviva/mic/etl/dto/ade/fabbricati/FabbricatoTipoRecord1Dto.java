package it.almaviva.mic.etl.dto.ade.fabbricati;

import jakarta.validation.constraints.Pattern;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
@EqualsAndHashCode(callSuper = false)
public class FabbricatoTipoRecord1Dto extends FabbricatoBaseDto 
{
	/* classe dell'unita' immobiliare */
	private String zona_censuaria;
	private String categoria;
	private String classe;
	private String consistenza;
	private String superficie;
	private String rendita_lire;
	private String rendita_euro;
	
	/* ubicazione immobile */
	private String lotto;
	private String edificio;
	private String scala;
	private String interno1;
	private String interno2;
	private String piano1;
	private String piano2;
	private String piano3;
	private String piano4;
	
	/* registrazione atto unita' */
	@Pattern(regexp = "^(0[1-9]|[12][0-9]|3[01])(0[1-9]|1[0-2])([0-9]{4})$", message = "La data efficacia dev'essere nel formato DDMMAAAA")
	private String data_efficacia;
	
	@Pattern(regexp = "^(0[1-9]|[12][0-9]|3[01])(0[1-9]|1[0-2])([0-9]{4})$", message = "La data registrazione atti dev'essere nel formato DDMMAAAA")
	private String data_registrazione_atti;
	private String tipo_nota;
	private String numero_nota;
	private String progressivo_nota;
	
	@Pattern(regexp = "^([0-9]{4})$", message = "La data nota dev'essere nel formato AAAA")
	private String anno_nota;
	
	/* conclusione atto unita' */
	@Pattern(regexp = "^(0[1-9]|[12][0-9]|3[01])(0[1-9]|1[0-2])([0-9]{4})$", message = "La data efficacia (conclusione atto) dev'essere nel formato DDMMAAAA")
	private String data_efficacia_concl;
	
	@Pattern(regexp = "^(0[1-9]|[12][0-9]|3[01])(0[1-9]|1[0-2])([0-9]{4})$", message = "La data registrazione (conclusione atto) dev'essere nel formato DDMMAAAA")
	private String data_registrazione_atti_concl;
	
	private String tipo_nota_concl;
	private String numero_nota_concl;
	private String progressivo_nota_concl;
	private String anno_nota_concl;
	private String partita;
	private String annotazione;
	private String id_mutazione_iniz;
	private String id_mutazione_fin;
	
	private String protocollo_notifica;
	private String data_notifica;
	
	/** DA DECOMMENTARE ALL'ARRIVO DEI NUOVI CAMPI NEL TRACCIATO 
	private String cd_causale_atto_generante;
	private String descrizione_atto_generante;
	private String cd_causale_atto_conclusivo;
	private String descrizione_atto_conclusivo;
	private String flag_classamento;
	**/

}
