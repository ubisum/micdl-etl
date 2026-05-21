package it.almaviva.mic.etl.dto.ade.terreni;

import java.util.List;

import it.almaviva.mic.etl.dto.ade.BeneImmobiliareDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class TerrenoTipoRecord2DTO extends BeneImmobiliareDTO
{
	private List<DeduzioneParticellaDTO> listaDeduzione;
}
