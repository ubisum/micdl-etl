package it.almaviva.mic.etl.dto.ade.terreni;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
@AllArgsConstructor
public class TerrenoTipoRecord2DTO 
{
	private List<DeduzioneParticellaDTO> listaDeduzione;
}
