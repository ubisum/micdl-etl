package it.almaviva.mic.etl.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EsitoDTO 
{
	private Integer codice;
	private String messaggio;
	private Object content;
}
