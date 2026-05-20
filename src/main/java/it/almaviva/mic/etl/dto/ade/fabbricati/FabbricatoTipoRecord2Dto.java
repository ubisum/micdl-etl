package it.almaviva.mic.etl.dto.ade.fabbricati;

import java.util.List;

import it.almaviva.mic.etl.dto.ade.NewBeneImmobiliareDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
@EqualsAndHashCode(callSuper = false)
public class FabbricatoTipoRecord2Dto extends NewBeneImmobiliareDTO
{
	private List<DatoCatastaleDto> array_id_dato_catastale;
}
