package it.almaviva.mic.etl.dto.ade.terreni;

import it.almaviva.mic.etl.dto.ade.NewBeneImmobiliareDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@Data
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class TerrenoTipoRecord3DTO extends NewBeneImmobiliareDTO 
{
	private List<RiservaParticellaDTO> listaRiserve;
}
