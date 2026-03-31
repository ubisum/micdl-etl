package it.almaviva.mic.etl.enums;

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
	
	
}
