package it.almaviva.mic.etl.dto.ade.fabbricati;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@EqualsAndHashCode(callSuper = false)
public class FabbricatoTipoRecord3Dto extends FabbricatoBaseDto
{
	private List<IndirizzoDto> array_id_indirizzi;
}
