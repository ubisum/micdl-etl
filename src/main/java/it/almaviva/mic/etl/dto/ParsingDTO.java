package it.almaviva.mic.etl.dto;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;

import it.almaviva.mic.etl.dto.ade.fabbricati.FabbricatoTipoRecord1Dto;
import it.almaviva.mic.etl.dto.ade.fabbricati.FabbricatoTipoRecord2Dto;
import it.almaviva.mic.etl.dto.ade.fabbricati.FabbricatoTipoRecord3Dto;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class ParsingDTO extends EsitoDTO
{
	/* campi comuni */
	private Integer recordLetti;
	
	@JsonInclude(JsonInclude.Include.NON_EMPTY)
	private Map<Integer, List<String>> reportRecord;
	
	private Integer recordNonInseriti;
	private Integer recordInseriti;	
	
	/* flusso FAB */
	@JsonIgnore
	private List<FabbricatoTipoRecord1Dto> unitaImmobiliari;
	
	@JsonIgnore
	private List<FabbricatoTipoRecord2Dto> datiCatastali;
	
	@JsonIgnore
	private List<FabbricatoTipoRecord3Dto> indirizzi;
}
