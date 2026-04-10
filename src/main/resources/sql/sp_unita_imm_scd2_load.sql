DROP PROCEDURE IF EXISTS sp_unita_imm_scd2_load;

DELIMITER $$

CREATE PROCEDURE sp_unita_imm_scd2_load()
BEGIN

	DECLARE v_oggi_dt DATETIME;
    DECLARE v_oggi DATE;
	
	SET v_oggi_dt = NOW();
	SET v_oggi = DATE(v_oggi_dt);
	
    -- ---------------------------------------
    -- 1. CHIUSURA RECORD MODIFICATI
    -- ---------------------------------------
    UPDATE ade_unita_imm_hist tgt
    INNER JOIN ADE_UNITA_IMM_HIST_STAGING src
       ON tgt.cod_comune COLLATE utf8mb4_unicode_ci = src.cod_comune COLLATE utf8mb4_unicode_ci 
       AND tgt.sezione COLLATE utf8mb4_unicode_ci = src.sezione COLLATE utf8mb4_unicode_ci
       AND tgt.id_imm_catasto COLLATE utf8mb4_unicode_ci = src.id_imm_catasto COLLATE utf8mb4_unicode_ci
       AND tgt.tipo_catasto COLLATE utf8mb4_unicode_ci = src.tipo_catasto COLLATE utf8mb4_unicode_ci
       AND tgt.progressivo COLLATE utf8mb4_unicode_ci = src.progressivo COLLATE utf8mb4_unicode_ci
       AND tgt.tipo_record COLLATE utf8mb4_unicode_ci = src.tipo_record COLLATE utf8mb4_unicode_ci
    SET 
        tgt.is_current = 0,
        tgt.valid_to = v_oggi
    WHERE tgt.is_current = 1
      AND tgt.hash COLLATE utf8mb4_unicode_ci <> src.hash COLLATE utf8mb4_unicode_ci;

    -- ---------------------------------------
    -- 2. INSERT RECORD NUOVI O MODIFICATI
    -- ---------------------------------------
    INSERT INTO ade_unita_imm_hist (
		cod_comune, sezione, id_imm_catasto, tipo_catasto, progressivo,
		tipo_record, zona_censuaria, categoria, classe, consistenza,
		superficie, rendita_lire, rendita_euro, lotto, edificio,
		scala, interno1, interno2, piano1, piano2,
		piano3, piano4, reg_data_efficacia, reg_data_reg_atto, reg_tipo_nota,
		reg_numero_nota, reg_progressivo_nota, reg_anno_nota,
		conc_data_efficacia, conc_data_reg_atto, conc_tipo_nota,
		conc_numero_nota, conc_progressivo_nota, conc_anno_nota,
		conc_partita, conc_annotazione, conc_id_mut_iniz, conc_id_mut_fin,
		conc_protocollo_notifica, conc_data_notifica,
		conc_cd_atto_generante, conc_descr_atto_generante,
		conc_cd_atto_conclusivo, conc_descr_atto_conclusivo,
		conc_flag_classamento,
		hash, valid_from, valid_to, is_current, batch_id
    )
    SELECT 
		cod_comune, sezione, id_imm_catasto, tipo_catasto, progressivo,
		tipo_record, zona_censuaria, categoria, classe, consistenza,
		superficie, rendita_lire, rendita_euro, lotto, edificio,
		scala, interno1, interno2, piano1, piano2,
		piano3, piano4, reg_data_efficacia, reg_data_reg_atto, reg_tipo_nota,
		reg_numero_nota, reg_progressivo_nota, reg_anno_nota,
		conc_data_efficacia, conc_data_reg_atto, conc_tipo_nota,
		conc_numero_nota, conc_progressivo_nota, conc_anno_nota,
		conc_partita, conc_annotazione, conc_id_mut_iniz, conc_id_mut_fin,
		conc_protocollo_notifica, conc_data_notifica,
		conc_cd_atto_generante, conc_descr_atto_generante,
		conc_cd_atto_conclusivo, conc_descr_atto_conclusivo,
		conc_flag_classamento,
		hash,
        v_oggi,
        NULL,
        1, 
		1
    FROM ADE_UNITA_IMM_HIST_STAGING src
    WHERE NOT EXISTS (
        SELECT 1
        FROM ade_unita_imm_hist tgt
        WHERE 
			tgt.cod_comune COLLATE utf8mb4_unicode_ci = src.cod_comune COLLATE utf8mb4_unicode_ci
			AND tgt.sezione COLLATE utf8mb4_unicode_ci = src.sezione COLLATE utf8mb4_unicode_ci
			AND tgt.id_imm_catasto COLLATE utf8mb4_unicode_ci = src.id_imm_catasto COLLATE utf8mb4_unicode_ci
			AND tgt.tipo_catasto COLLATE utf8mb4_unicode_ci = src.tipo_catasto COLLATE utf8mb4_unicode_ci  
			AND tgt.progressivo COLLATE utf8mb4_unicode_ci = src.progressivo COLLATE utf8mb4_unicode_ci
			AND tgt.tipo_record COLLATE utf8mb4_unicode_ci = src.tipo_record COLLATE utf8mb4_unicode_ci
			AND tgt.is_current = 1
			AND tgt.hash COLLATE utf8mb4_unicode_ci = src.hash COLLATE utf8mb4_unicode_ci
    );

    -- ---------------------------------------
    -- 3. UPDATE FK SU TABELLA DATI CATASTALI
    -- ---------------------------------------
    UPDATE ade_dato_catastale_hist dc
    INNER JOIN ade_unita_imm_hist old_imm
        ON dc.id_imm_hist = old_imm.id_imm_hist
    INNER JOIN ade_unita_imm_hist new_imm
		ON old_imm.cod_comune = new_imm.cod_comune
		AND old_imm.sezione = new_imm.sezione
		AND old_imm.id_imm_catasto = new_imm.id_imm_catasto
		AND old_imm.tipo_catasto = new_imm.tipo_catasto
		AND old_imm.progressivo = new_imm.progressivo
		AND old_imm.tipo_record = new_imm.tipo_record
    SET dc.id_imm_hist = new_imm.id_imm_hist
    WHERE old_imm.is_current = 0
      AND new_imm.is_current = 1
      AND dc.id_imm_hist <> new_imm.id_imm_hist;
	
	-- -----------------------------------------------------------------------
    -- 4. INSERT FK SU TABELLA DATI UNITA' IMMOBILIARI NON STORICIZZATI
    -- -----------------------------------------------------------------------	
	  
	INSERT INTO ade_unita_imm 
	(
		id_imm_hist,
		snapshot_ts
	)
	SELECT 
		new_imm.id_imm_hist,
		v_oggi_dt
	FROM 
		ade_unita_imm_hist new_imm
	WHERE 
		new_imm.is_current = 1
	AND 
		new_imm.valid_from = v_oggi
	AND NOT EXISTS 
	(
      SELECT 1
      FROM ade_unita_imm_hist old_imm
      WHERE 
		old_imm.cod_comune = new_imm.cod_comune
       AND 
		old_imm.sezione = new_imm.sezione
       AND 
		old_imm.id_imm_catasto = new_imm.id_imm_catasto
       AND 
		old_imm.tipo_catasto = new_imm.tipo_catasto
       AND 
		old_imm.progressivo = new_imm.progressivo
       AND 
		old_imm.tipo_record = new_imm.tipo_record
       AND 
		old_imm.valid_from < v_oggi
	)
	AND NOT EXISTS 
	(
		SELECT 1
		FROM ade_unita_imm inspect_aui
		INNER JOIN ade_unita_imm_hist test_imm ON test_imm.id_imm_hist = inspect_aui.id_imm_hist
		WHERE 
		test_imm.cod_comune = new_imm.cod_comune
       AND 
		test_imm.sezione = new_imm.sezione
       AND 
		test_imm.id_imm_catasto = new_imm.id_imm_catasto
       AND 
		test_imm.tipo_catasto = new_imm.tipo_catasto
       AND 
		test_imm.progressivo = new_imm.progressivo
       AND 
		test_imm.tipo_record = new_imm.tipo_record
	);

    -- -----------------------------------------------------------------------
    -- 5. UPDATE FK SU TABELLA DATI UNITA' IMMOBILIARI NON STORICIZZATI
    -- -----------------------------------------------------------------------
	UPDATE ade_unita_imm aui
    INNER JOIN ade_unita_imm_hist old_imm
        ON aui.id_imm_hist = old_imm.id_imm_hist
    INNER JOIN ade_unita_imm_hist new_imm
		ON old_imm.cod_comune = new_imm.cod_comune
		AND old_imm.sezione = new_imm.sezione
		AND old_imm.id_imm_catasto = new_imm.id_imm_catasto
		AND old_imm.tipo_catasto = new_imm.tipo_catasto
		AND old_imm.progressivo = new_imm.progressivo
		AND old_imm.tipo_record = new_imm.tipo_record
    SET 
		aui.id_imm_hist = new_imm.id_imm_hist,
		aui.snapshot_ts = v_oggi_dt
    WHERE old_imm.is_current = 0
      AND new_imm.is_current = 1
      AND aui.id_imm_hist <> new_imm.id_imm_hist;

END$$

DELIMITER ;
