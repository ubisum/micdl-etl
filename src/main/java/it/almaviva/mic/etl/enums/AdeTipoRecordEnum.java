package it.almaviva.mic.etl.enums;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public enum AdeTipoRecordEnum 
{
	ADE_TIPO_RECORD_1(1),
	ADE_TIPO_RECORD_2(2),
	ADE_TIPO_RECORD_3(3),
	ADE_TIPO_RECORD_4(4),
	ADE_TIPO_RECORD_5(5);
	
	private Integer tipoRecord;

	private AdeTipoRecordEnum(Integer tipoRecord) 
	{
		this.tipoRecord = tipoRecord;
	}

	public Integer getTipoRecord() {
		return tipoRecord;
	}
	
	public static List<Integer> listaValori()
	{
		return Arrays.asList(AdeTipoRecordEnum.values()).stream().map(t -> t.getTipoRecord()).collect(Collectors.toList());
	}
	
	public static AdeTipoRecordEnum getFromValue(Integer value)
	{
		AdeTipoRecordEnum[] values = AdeTipoRecordEnum.values();
		AdeTipoRecordEnum returnValue = null;
		
		for(AdeTipoRecordEnum tipo : values)
		{
			if(tipo.getTipoRecord().equals(value))
				returnValue = tipo;
		}
		
		return returnValue;
			
	}
	
	
}
