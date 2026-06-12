package it.almaviva.mic.etl.utils;

public class MicDlEtlConsts 
{
	/* errori parsing file */
	public static final String ERR_MISSING_ELEMS = "E001 - Il record non contiene abbastanza elementi"; 
	public static final String ERR_WRONG_TYPE = "E002 - Tipo record non corretto";
	public static final String ERR_VALIDATION = "E003 - Uno o piu' vincoli violati sul record";
	public static final String ERR_ESTATE_REG_NUM = "E004 - Numero dei campi dei dati catastali non corretto";
	public static final String ERR_ADDR_NUM = "E005 - Numero dei campi degli indirizzi non corretto";
	public static final String ERR_DED_MISSING_ELEMS = "E006 - Dati delle deduzioni non presenti";
	public static final String ERR_RIS_MISSING_ELEMS = "E007 - Dati delle riserve non presenti";
	public static final String ERR_POR_MISSING_ELEMS = "E008 - Dati delle porzioni non presenti";
	public static final String ERR_TIT_MISSING_ID_SOGGETTO = "E009 - ID soggetto non rilevato sul database";
	public static final String ERR_TIT_MISSING_SOGGETTO_RIF = "E010 - Soggetto di riferimento non rilevato sul database";
	public static final String ERR_TIT_MISSING_ID_IMMOBILE = "E011 - Identificativo dell'immobile non presente nel database";
	
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
	public static final String ADE_PORZIONE_TER_STAGING = "sql/ade_porzione_hist_staging.sql";
	public static final String ADE_PORZIONE_TER_STAGING_INSERT = "sql/ade_porzione_hist_staging_insert.sql";
	public static final String PROPRIETARIO_HIST_STAGING = "sql/proprietario_hist_staging.sql";
	public static final String PROPRIETARIO_HIST_STAGING_INSERT = "sql/proprietario_hist_staging_insert.sql";
	public static final String ADE_TITOLARITA_HIST_STAGING = "sql/ade_titolarita_hist_staging.sql";
	public static final String ADE_TITOLARITA_HIST_STAGING_INSERT = "sql/ade_titolarita_hist_staging_insert.sql";
	
	/* stored procedure */
	public static final String ADE_UNITA_IMM_SP = "sp_unita_imm_scd2_load"; 
	public static final String ADE_DATO_CASTALE_SP = "sp_dato_catastale_scd2_load"; 
	public static final String ADE_DATO_CASTALE_SIMPLE_SP = "sp_dato_catastale_simple_insert"; 
	public static final String ADE_INDIRIZZO_SP = "sp_indirizzo_scd2_load"; 
	public static final String ADE_INDIRIZZO_SIMPLE_SP = "sp_indirizzo_simple_insert"; 
	public static final String ADE_PARTICELLA_SP = "sp_particella_scd2_load";
	public static final String ADE_DEDUZIONE_SP = "sp_deduzione_scd2_load";
	public static final String ADE_RISERVA_SP = "sp_riserva_scd2_load";
	public static final String ADE_PORZIONE_SP = "sp_porzione_scd2_load";
	public static final String PROPRIETARIO_SP = "sp_proprietario_scd2_load";
	public static final String ADE_TITOLARITA_SP = "sp_titolarita_scd2_load";
	
}
