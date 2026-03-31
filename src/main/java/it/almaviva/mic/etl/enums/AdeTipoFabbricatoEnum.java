package it.almaviva.mic.etl.enums;

public enum AdeTipoFabbricatoEnum 
{
	ADE_TIPO_CATASTO_FABBRICATI("F"),
	ADE_TIPO_CATASTO_TERRENI("T");
	
	private String tipoCatasto;

	private AdeTipoFabbricatoEnum(String tipoCatasto) {
		this.tipoCatasto = tipoCatasto;
	}

	public String getTipoCatasto() {
		return tipoCatasto;
	}
	
	
}
