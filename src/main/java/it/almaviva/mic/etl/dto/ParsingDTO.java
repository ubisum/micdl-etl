package it.almaviva.mic.etl.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;

import it.almaviva.mic.etl.dto.ade.fabbricati.FabbricatoTipoRecord1Dto;
import it.almaviva.mic.etl.dto.ade.fabbricati.FabbricatoTipoRecord2Dto;
import it.almaviva.mic.etl.dto.ade.fabbricati.FabbricatoTipoRecord3Dto;
import it.almaviva.mic.etl.dto.ade.soggetti.ProprietarioDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class ParsingDTO extends EsitoDTO
{
	/* ******************************* CAMPI COMUNI *********************************************** */
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private String nomeFileRicevuto;
	
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private String tipoFlusso;
	
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private Integer recordLetti;
	
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private Integer recordInseritiInStaging;
	
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private Integer recordInseriti;	
	
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private Integer recordNonValidi; 
	
	@JsonInclude(JsonInclude.Include.NON_EMPTY)
	private Map<Integer, List<String>> reportRecord;
	
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private String inizioScansioneFile;
	
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private String fineScansioneFile;
	
	/* ************************************* FLUSSO FAB ******************************************** */
	@JsonIgnore
	private List<FabbricatoTipoRecord1Dto> unitaImmobiliari;
	
	@JsonIgnore
	private List<FabbricatoTipoRecord2Dto> datiCatastali;
	
	@JsonIgnore
	private List<FabbricatoTipoRecord3Dto> indirizzi;
	
	@JsonIgnore
	private List<FabbricatoTipoRecord2Dto> datiCatastaliSupplementari;
	
	@JsonIgnore
	private List<FabbricatoTipoRecord3Dto> indirizziSupplementari;
	
	/* ************************************* FLUSSO SOG ******************************************** */
	@JsonIgnore
	List<ProprietarioDTO> listaSoggetti = new ArrayList<>();
}
