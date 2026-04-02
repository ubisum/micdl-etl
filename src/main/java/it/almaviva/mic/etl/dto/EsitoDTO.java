package it.almaviva.mic.etl.dto;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EsitoDTO 
{
	private Integer codice;
	private String messaggio;
	private Object content;
	private Map<Integer, List<String>> reportRecord;
	private Integer totaleRecord;
	private Integer recordNonInseriti;
	private String recordInseriti;
	
}
