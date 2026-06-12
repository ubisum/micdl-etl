DROP PROCEDURE IF EXISTS sp_titolarita_scd2_load;

DELIMITER $$

CREATE PROCEDURE sp_titolarita_scd2_load(OUT totale_inseriti INT)
BEGIN
	DECLARE v_oggi_dt DATETIME;
	DECLARE v_oggi DATE;
	DECLARE v_count INT DEFAULT 0;
	
	SET v_oggi_dt = NOW();
	SET v_oggi = DATE(v_oggi_dt);
	
	-- ---------------------------------------
    -- 0. INDIVIDUAZIONE RECORD VALIDI
    -- ---------------------------------------
	DROP TEMPORARY TABLE IF EXISTS RECORD_VALIDI;
	CREATE TEMPORARY TABLE RECORD_VALIDI AS
	SELECT 
		staging.*, 
		prop_soggetto.id_proprietario_hist AS sog_id,
		prop_rif.id_proprietario_hist AS rif_id,
		unita.id_imm_hist AS unita_id,
		particella.id_part_hist AS particella_id
	FROM 
		ADE_TITOLARITA_HIST_STAGING staging
	LEFT JOIN 
		proprietario_hist prop_soggetto
	ON
		staging.id_soggetto COLLATE utf8mb4_unicode_ci = prop_soggetto.id_soggetto COLLATE utf8mb4_unicode_ci
	LEFT JOIN 
		proprietario_hist prop_rif
	ON
		staging.soggetto_riferimento COLLATE utf8mb4_unicode_ci = prop_rif.id_soggetto COLLATE utf8mb4_unicode_ci
	LEFT JOIN 
		ade_unita_imm_hist unita
	ON
		staging.cod_comune  COLLATE utf8mb4_unicode_ci = unita.cod_comune COLLATE utf8mb4_unicode_ci 
	AND
		staging.sezione COLLATE utf8mb4_unicode_ci = unita.sezione COLLATE utf8mb4_unicode_ci 
	AND
		staging.id_imm_catasto COLLATE utf8mb4_unicode_ci = unita.id_imm_catasto COLLATE utf8mb4_unicode_ci 
	AND 
		staging.tipo_catasto COLLATE utf8mb4_unicode_ci = unita.tipo_catasto COLLATE utf8mb4_unicode_ci 
	AND
		unita.is_current = 1
	LEFT JOIN
		ade_particella_hist particella
	ON
		staging.cod_comune COLLATE utf8mb4_unicode_ci = particella.cod_comune COLLATE utf8mb4_unicode_ci 
	AND
		staging.sezione COLLATE utf8mb4_unicode_ci = particella.sezione COLLATE utf8mb4_unicode_ci 
	AND
		staging.id_imm_catasto COLLATE utf8mb4_unicode_ci = particella.id_imm_catasto COLLATE utf8mb4_unicode_ci 
	AND 
		staging.tipo_catasto COLLATE utf8mb4_unicode_ci = particella.tipo_catasto COLLATE utf8mb4_unicode_ci
	AND
		particella.is_current = 1;
		
	-- ---------------------------------------
    -- 1. CHIUSURA RECORD MODIFICATI
    -- ---------------------------------------
	UPDATE 
		ade_titolarita_hist tgt
	INNER JOIN 
		RECORD_VALIDI src
	ON
		tgt.cod_comune COLLATE utf8mb4_unicode_ci  = src.cod_comune COLLATE utf8mb4_unicode_ci
	AND
		tgt.sezione COLLATE utf8mb4_unicode_ci = src.sezione COLLATE utf8mb4_unicode_ci
	AND
		tgt.id_soggetto COLLATE utf8mb4_unicode_ci = src.id_soggetto COLLATE utf8mb4_unicode_ci
	AND
		tgt.tipo_record COLLATE utf8mb4_unicode_ci = src.tipo_record COLLATE utf8mb4_unicode_ci
	AND
		tgt.id_imm_catasto COLLATE utf8mb4_unicode_ci = src.id_imm_catasto COLLATE utf8mb4_unicode_ci
	AND
		tgt.tipo_catasto COLLATE utf8mb4_unicode_ci = src.tipo_catasto COLLATE utf8mb4_unicode_ci
	AND
		tgt.cod_diritto COLLATE utf8mb4_unicode_ci = src.cod_diritto COLLATE utf8mb4_unicode_ci
	AND
		tgt.quota_numeratore COLLATE utf8mb4_unicode_ci = src.quota_numeratore COLLATE utf8mb4_unicode_ci
	AND
		tgt.quota_denominatore COLLATE utf8mb4_unicode_ci = src.quota_denominatore COLLATE utf8mb4_unicode_ci
	SET
		tgt.is_current = 0,
		tgt.valid_to = v_oggi
	WHERE
		src.sog_id IS NOT NULL
	AND
	(
		src.soggetto_riferimento IS NULL
		OR src.rif_id IS NOT NULL
	)
	AND
	(
		src.unita_id IS NOT NULL OR src.particella_id IS NOT NULL
	)
	AND
		tgt.hash COLLATE utf8mb4_unicode_ci <> src.hash COLLATE utf8mb4_unicode_ci;
		
	-- ---------------------------------------
    -- 2. INSERT RECORD NUOVI O MODIFICATI
    -- ---------------------------------------
	INSERT INTO ade_titolarita_hist 
	(
		cod_comune,
		sezione,
		id_soggetto,
		tipo_record,
		id_imm_catasto,
		tipo_catasto,
		cod_diritto,
		titolo_non_codificato,
		quota_numeratore,
		quota_denominatore,
		regime,
		soggetto_riferimento,
		reg_data_validita,
		reg_tipo_nota,
		reg_numero_nota,
		reg_progressivo_nota,
		reg_anno_nota,
		reg_data_reg_atto,
		partita,
		conc_data_validita,
		conc_tipo_nota,
		conc_numero_nota,
		conc_progressivo_nota,
		conc_anno_nota,
		conc_data_registrazione_atti,
		conc_id_mutazione_iniz,
		conc_id_mutazione_fin,
		conc_cd_causale_atto_generante,
		conc_descrizione_atto_generante,
		conc_cd_causale_atto_conclusivo,
		conc_descrizione_atto_conclusivo,
		hash,
		valid_from,
		valid_to,
		is_current,
		batch_id,
		id_titolarita
	)
	SELECT
		cod_comune,
		sezione,
		id_soggetto,
		tipo_record,
		id_imm_catasto,
		tipo_catasto,
		cod_diritto,
		titolo_non_codificato,
		quota_numeratore,
		quota_denominatore,
		regime,
		soggetto_riferimento,
		reg_data_validita,
		reg_tipo_nota,
		reg_numero_nota,
		reg_progressivo_nota,
		reg_anno_nota,
		reg_data_reg_atto,
		partita,
		conc_data_validita,
		conc_tipo_nota,
		conc_numero_nota,
		conc_progressivo_nota,
		conc_anno_nota,
		conc_data_registrazione_atti,
		conc_id_mutazione_iniz,
		conc_id_mutazione_fin,
		conc_cd_causale_atto_generante,
		conc_descrizione_atto_generante,
		conc_cd_causale_atto_conclusivo,
		conc_descrizione_atto_conclusivo,
		hash,
		v_oggi,
		NULL,
		1,
		batch_id,
		id_titolarita
	FROM RECORD_VALIDI src
	WHERE NOT EXISTS
	(
		SELECT 1
		FROM
			ade_titolarita_hist tgt
		WHERE
			tgt.cod_comune COLLATE utf8mb4_unicode_ci  = src.cod_comune COLLATE utf8mb4_unicode_ci
		AND
			tgt.sezione COLLATE utf8mb4_unicode_ci = src.sezione COLLATE utf8mb4_unicode_ci
		AND
			tgt.id_soggetto COLLATE utf8mb4_unicode_ci = src.id_soggetto COLLATE utf8mb4_unicode_ci
		AND
			tgt.tipo_record COLLATE utf8mb4_unicode_ci = src.tipo_record COLLATE utf8mb4_unicode_ci
		AND
			tgt.id_imm_catasto COLLATE utf8mb4_unicode_ci = src.id_imm_catasto COLLATE utf8mb4_unicode_ci
		AND
			tgt.tipo_catasto COLLATE utf8mb4_unicode_ci = src.tipo_catasto COLLATE utf8mb4_unicode_ci
		AND
			tgt.cod_diritto COLLATE utf8mb4_unicode_ci = src.cod_diritto COLLATE utf8mb4_unicode_ci
		AND
			tgt.quota_numeratore COLLATE utf8mb4_unicode_ci = src.quota_numeratore COLLATE utf8mb4_unicode_ci
		AND
			tgt.quota_denominatore COLLATE utf8mb4_unicode_ci = src.quota_denominatore COLLATE utf8mb4_unicode_ci
		AND
			tgt.is_current = 1
		AND
			tgt.hash COLLATE utf8mb4_unicode_ci = src.hash COLLATE utf8mb4_unicode_ci
	)
	AND 
		src.sog_id IS NOT NULL
	AND
	(
		src.soggetto_riferimento IS NULL
		OR src.rif_id IS NOT NULL
	)
	AND
	(
		src.unita_id IS NOT NULL OR src.particella_id IS NOT NULL
	);
	
	SET v_count = v_count + ROW_COUNT();
	
	-- -----------------------------------------------------------------------
    -- 3. UPDATE FK SU TABELLA PROPRIETARI NON STORICIZZATI
    -- -----------------------------------------------------------------------
	UPDATE
		ade_titolarita tit
	INNER JOIN
		ade_titolarita_hist old_tit
	ON
		tit.id_titolarita_hist = old_tit.id_titolarita_hist
	INNER JOIN
		ade_titolarita_hist new_tit
	ON
		old_tit.cod_comune = new_tit.cod_comune
	AND
		old_tit.sezione = new_tit.sezione
	AND
		old_tit.id_soggetto = new_tit.id_soggetto
	AND
		old_tit.tipo_record = new_tit.tipo_record
	AND
		old_tit.id_imm_catasto = new_tit.id_imm_catasto
	AND
		old_tit.tipo_catasto = new_tit.tipo_catasto
	AND
		old_tit.cod_diritto = new_tit.cod_diritto
	AND
		old_tit.quota_numeratore = new_tit.quota_numeratore
	AND
		old_tit.quota_denominatore = new_tit.quota_denominatore
	SET
		tit.id_titolarita_hist = new_tit.id_titolarita_hist,
		tit.snapshot_ts = v_oggi_dt
	WHERE
		old_tit.is_current = 0
	AND
		new_tit.is_current = 1
	AND
		tit.id_titolarita_hist <> new_tit.id_titolarita_hist;
		
	-- -----------------------------------------------------------------------
    -- 4. INSERIMENTI SU TABELLA PROPRIETARI NON STORICIZZATI
    -- -----------------------------------------------------------------------
	INSERT INTO ade_titolarita
	(
		id_titolarita_hist,
		snapshot_ts
	)
	SELECT
		new_tit.id_titolarita_hist,
		v_oggi_dt
	FROM
		ade_titolarita_hist new_tit
	WHERE
		new_tit.is_current = 1
	AND
		new_tit.valid_from = v_oggi
	AND NOT EXISTS
	(
		SELECT 1
		FROM
			ade_titolarita_hist old_tit
		WHERE
			old_tit.cod_comune = new_tit.cod_comune
		AND
			old_tit.sezione = new_tit.sezione
		AND
			old_tit.id_soggetto = new_tit.id_soggetto
		AND
			old_tit.tipo_record = new_tit.tipo_record
		AND
			old_tit.id_imm_catasto = new_tit.id_imm_catasto
		AND
			old_tit.tipo_catasto = new_tit.tipo_catasto
		AND
			old_tit.cod_diritto = new_tit.cod_diritto
		AND
			old_tit.quota_numeratore = new_tit.quota_numeratore
		AND
			old_tit.quota_denominatore = new_tit.quota_denominatore
		AND
			old_tit.valid_from < v_oggi
	)
	AND NOT EXISTS
	(
		SELECT 1
		FROM 
			ade_titolarita inspect_tit
		INNER JOIN
			ade_titolarita_hist test_tit
		ON
			inspect_tit.id_titolarita_hist = test_tit.id_titolarita_hist
		WHERE
			test_tit.cod_comune = new_tit.cod_comune
		AND
			test_tit.sezione = new_tit.sezione
		AND
			test_tit.id_soggetto = new_tit.id_soggetto
		AND
			test_tit.tipo_record = new_tit.tipo_record
		AND
			test_tit.id_imm_catasto = new_tit.id_imm_catasto
		AND
			test_tit.tipo_catasto = new_tit.tipo_catasto
		AND
			test_tit.cod_diritto = new_tit.cod_diritto
		AND
			test_tit.quota_numeratore = new_tit.quota_numeratore
		AND
			test_tit.quota_denominatore = new_tit.quota_denominatore
	);
		
	-- -----------------------------------------------------------------------
    -- 5. SELEZIONE RECORD NON VALIDI
    -- -----------------------------------------------------------------------
	SELECT
		rd.row_index,
		CASE WHEN rd.sog_id IS NULL THEN 1 ELSE 0 END,
		CASE WHEN rd.soggetto_riferimento IS NOT NULL AND rd.rif_id IS NULL THEN 1 ELSE 0 END,
		CASE WHEN rd.unita_id IS NULL AND rd.particella_id IS NULL THEN 1 ELSE 0 END
	FROM
		RECORD_VALIDI rd
	WHERE
		rd.sog_id IS null OR rd.rif_id IS NULL OR (rd.unita_id IS NULL AND rd.particella_id IS NULL);
		
	DROP TEMPORARY TABLE RECORD_VALIDI;
	
	SET totale_inseriti = v_count;
	
END$$

DELIMITER ;	