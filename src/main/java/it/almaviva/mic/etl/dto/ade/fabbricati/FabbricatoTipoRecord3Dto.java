package it.almaviva.mic.etl.dto.ade.fabbricati;

import java.util.List;

import it.almaviva.mic.etl.dto.ade.NewBeneImmobiliareDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@EqualsAndHashCode(callSuper = false)
public class FabbricatoTipoRecord3Dto extends NewBeneImmobiliareDTO
{
	private List<IndirizzoDto> array_id_indirizzi;
}
