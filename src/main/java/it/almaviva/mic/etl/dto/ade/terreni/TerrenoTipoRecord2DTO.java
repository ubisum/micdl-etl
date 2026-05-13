package it.almaviva.mic.etl.dto.ade.terreni;

import java.util.List;

import it.almaviva.mic.etl.dto.ade.BeneImmobiliareDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
@AllArgsConstructor
public class TerrenoTipoRecord2DTO extends BeneImmobiliareDTO
{
	private List<DeduzioneParticellaDTO> listaDeduzione;
}
