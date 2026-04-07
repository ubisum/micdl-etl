package it.almaviva.mic.etl.utils;

public class MicDlEtlConsts 
{
	/* errori parsing file */
	public static final String ERR_MISSING_ELEMS = "Il record non contiene abbastanza elementi"; 
	public static final String ERR_WRONG_TYPE = "Tipo record non corretto";
	public static final String ERR_VALIDATION = "Uno o piu' vincoli violati sul record";
	public static final String ERR_ESTATE_REG_NUM = "Numero dei campi dei dati catastali non corretto";
	public static final String ERR_ADDR_NUM = "Numero dei campi degli indirizzi non corretto";
	
	/* file SQL */
	public static final String ADE_UNITA_IMM_CREATE_STAGING = "sql/ade_unita_imm_hist_staging.sql";
	
}
