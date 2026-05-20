package it.almaviva.mic.etl.dto.ade.terreni;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

import it.almaviva.mic.etl.dto.ade.NewBeneImmobiliareDTO;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TerrenoTipoRecord4DTO extends NewBeneImmobiliareDTO
{
	private List<PorzioneDTO> listaPorzioni;
}
