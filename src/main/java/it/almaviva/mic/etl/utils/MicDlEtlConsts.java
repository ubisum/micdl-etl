package it.almaviva.mic.etl.utils;

public class MicDlEtlConsts 
{
	/* errori parsing file */
	public static final String ERR_MISSING_ELEMS = "Il record non contiene abbastanza elementi"; 
	public static final String ERR_WRONG_TYPE = "Tipo record non corretto";
	public static final String ERR_VALIDATION = "Uno o piu' vincoli violati sul record";
	public static final String ERR_ESTATE_REG_NUM = "Numero dei campi dei dati catastali non corretto";
	public static final String ERR_ADDR_NUM = "Numero dei campi degli indirizzi non corretto";
	public static final String ERR_DED_MISSING_ELEMS = "Dati delle deduzioni non presenti";
	public static final String ERR_RIS_MISSING_ELEMS = "Dati delle riserve non presenti";
	
	/* file SQL per operazioni CRUD */
	public static final String ADE_UNITA_IMM_CREATE_STAGING = "sql/ade_unita_imm_hist_staging.sql";
	public static final String ADE_UNITA_IMM_CREATE_STAGING_INSERT = "sql/ade_unita_imm_hist_staging_insert.sql";
	public static final String ADE_DATO_CATASTALE_CREATE_STAGING = "sql/ade_dato_catastale_hist_staging.sql";
	public static final String ADE_DATO_CATASTALE_CREATE_STAGING_INSERT = "sql/ade_dato_catastale_hist_staging_insert.sql";
	public static final String ADE_INDIRIZZO_CREATE_STAGING = "sql/ade_indirizzo_hist_staging.sql";
	public static final String ADE_INDIRIZZO_CREATE_STAGING_INSERT = "sql/ade_indirizzo_hist_staging_insert.sql";
	public static final String ADE_PARTICELLA_CREATE_STAGING = "sql/ade_particella_hist_staging.sql";
	public static final String ADE_PARTICELLA_CREATE_STAGING_INSERT = "sql/ade_particella_hist_insert.sql";
	public static final String ADE_DEDUZIONE_TER_STAGING = "sql/ade_deduzione_ter_staging.sql";
	public static final String ADE_DEDUZIONE_TER_STAGING_INSERT = "sql/ade_deduzione_ter_staging_insert.sql";
	public static final String ADE_RISERVA_TER_STAGING = "sql/ade_riserva_ter_staging.sql";
	public static final String ADE_RISERVA_TER_STAGING_INSERT = "sql/ade_riserva_ter_staging_insert.sql";
	public static final String PROPRIETARIO_HIST_STAGING = "sql/proprietario_hist_staging.sql";
	public static final String PROPRIETARIO_HIST_STAGING_INSERT = "sql/proprietario_hist_staging_insert.sql";
	
	/* stored procedure */
	public static final String ADE_UNITA_IMM_SP = "sp_unita_imm_scd2_load"; 
	public static final String ADE_DATO_CASTALE_SP = "sp_dato_catastale_scd2_load"; 
	public static final String ADE_DATO_CASTALE_SIMPLE_SP = "sp_dato_catastale_simple_insert"; 
	public static final String ADE_INDIRIZZO_SP = "sp_indirizzo_scd2_load"; 
	public static final String ADE_INDIRIZZO_SIMPLE_SP = "sp_indirizzo_simple_insert"; 
	public static final String ADE_PARTICELLA_SP = "sp_particella_scd2_load";
	public static final String ADE_DEDUZIONE_SP = "sp_deduzione_scd2_load";
	public static final String ADE_RISERVA_SP = "sp_riserva_scd2_load";
	public static final String PROPRIETARIO_SP = "sp_proprietario_scd2_load";
	
}
