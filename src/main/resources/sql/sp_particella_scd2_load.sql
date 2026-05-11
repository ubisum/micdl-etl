DROP PROCEDURE IF EXISTS sp_particella_scd2_load;

DELIMITER $$

CREATE PROCEDURE sp_particella_scd2_load(OUT totale_inseriti INT)
BEGIN

	DECLARE v_oggi_dt DATETIME;
    DECLARE v_oggi DATE;
	DECLARE v_count INT DEFAULT 0;
	
	SET v_oggi_dt = NOW();
	SET v_oggi = DATE(v_oggi_dt);
	
	-- ---------------------------------------
    -- 1. CHIUSURA RECORD MODIFICATI
    -- ---------------------------------------
	UPDATE ade_particella_hist tgt
	INNER JOIN ADE_PARTICELLA_HIST_STAGING src
	ON
		tgt.cod_comune COLLATE utf8mb4_unicode_ci = src.cod_comune COLLATE utf8mb4_unicode_ci
	AND 
		tgt.sezione COLLATE utf8mb4_unicode_ci = src.sezione COLLATE utf8mb4_unicode_ci 
	AND
		tgt.id_imm_catasto COLLATE utf8mb4_unicode_ci = src.id_imm_catasto COLLATE utf8mb4_unicode_ci 
	AND
		tgt.tipo_catasto COLLATE utf8mb4_unicode_ci = src.tipo_catasto COLLATE utf8mb4_unicode_ci 
	AND 
		tgt.tipo_record COLLATE utf8mb4_unicode_ci = src.tipo_record COLLATE utf8mb4_unicode_ci 
	SET
		tgt.is_current = 0,
		tgt.valid_to = v_oggi
	WHERE
		tgt.is_current = 1
	AND 
		tgt.hash COLLATE utf8mb4_unicode_ci <> src.hash COLLATE utf8mb4_unicode_ci;
	
	-- ---------------------------------------
    -- 2. INSERT RECORD NUOVI O MODIFICATI
    -- ---------------------------------------
	INSERT INTO ade_particella_hist
	(cod_comune, sezione, id_imm_catasto, tipo_catasto, progressivo, tipo_record, foglio, particella, 
	denominatore, subalterno, edificialita, qualita, classe, ettari, `are`, 
	centiare, flag_reddito, flag_porzione, flag_deduzioni, reddito_dominicale_lire, 
	reddito_agrario_lire, reddito_dominicale_euro, reddito_agrario_euro, reg_data_efficacia, 
	reg_data_reg_atto, reg_tipo_nota, reg_numero_nota, reg_progressivo_nota, reg_anno_nota, 
	conc_data_efficacia, conc_data_reg_atto, conc_tipo_nota, conc_numero_nota, conc_progressivo_nota, 
	conc_anno_nota, partita, annotazione, conc_id_mut_iniz, conc_id_mut_fin, conc_cd_atto_generante, 
	conc_descr_atto_generante, conc_cd_atto_conclusivo, conc_descr_atto_conclusivo, hash, valid_from, 
	valid_to, is_current, batch_id)
	SELECT
		cod_comune,
		sezione,
		id_imm_catasto,
		tipo_catasto,
		progressivo,
		tipo_record,
		foglio,
		particella,
		denominatore,
		subalterno,
		edificialita,
		qualita,
		classe,
		ettari,
		`are`,
		centiare,
		flag_reddito,
		flag_porzione,
		flag_deduzioni,
		reddito_dominicale_lire,
		reddito_agrario_lire,
		reddito_dominicale_euro,
		reddito_agrario_euro,
		reg_data_efficacia,
		reg_data_reg_atto,
		reg_tipo_nota,
		reg_numero_nota,
		reg_progressivo_nota,
		reg_anno_nota,
		conc_data_efficacia,
		conc_data_reg_atto,
		conc_tipo_nota,
		conc_numero_nota,
		conc_progressivo_nota,
		conc_anno_nota,
		partita,
		annotazione,
		conc_id_mut_iniz,
		conc_id_mut_fin,
		conc_cd_atto_generante,
		conc_descr_atto_generante,
		conc_cd_atto_conclusivo,
		conc_descr_atto_conclusivo,
		hash,
		v_oggi,
		NULL,
		1,
		batch_id
	FROM ADE_PARTICELLA_HIST_STAGING src
	WHERE NOT EXISTS 
	(
		SELECT 1
		FROM ade_particella_hist tgt
		WHERE 
			tgt.cod_comune COLLATE utf8mb4_unicode_ci = src.cod_comune COLLATE utf8mb4_unicode_ci
		AND 
			tgt.sezione COLLATE utf8mb4_unicode_ci = src.sezione COLLATE utf8mb4_unicode_ci 
		AND
			tgt.id_imm_catasto COLLATE utf8mb4_unicode_ci = src.id_imm_catasto COLLATE utf8mb4_unicode_ci 
		AND
			tgt.tipo_catasto COLLATE utf8mb4_unicode_ci = src.tipo_catasto COLLATE utf8mb4_unicode_ci 
		AND 
			tgt.tipo_record COLLATE utf8mb4_unicode_ci = src.tipo_record COLLATE utf8mb4_unicode_ci 
		AND 
			tgt.is_current = 1
		AND 
			tgt.hash COLLATE utf8mb4_unicode_ci = src.hash COLLATE utf8mb4_unicode_ci
	);
	
	-- aggiornamento del numero di record inseriti
	SET v_count = v_count + ROW_COUNT();
	
	-- -----------------------------------------------------------------------
    -- 3. INSERT FK SU TABELLA PARTICELLE NON STORICIZZATE
    -- -----------------------------------------------------------------------	
	INSERT INTO ade_particella
	(
		id_part_hist,
		snapshot_ts
	)
	SELECT 
		new_par.id_part_hist,
		v_oggi_dt 
	FROM
		ade_particella_hist new_par
	WHERE 
		new_par.is_current = 1
	AND 
		new_par.valid_from = v_oggi
	AND NOT EXISTS
	(
		SELECT 1
		FROM ade_particella_hist old_par
		WHERE
			old_par.cod_comune = new_par.cod_comune
		AND
			old_par.sezione = new_par.sezione
		AND
			old_par.id_imm_catasto = new_par.id_imm_catasto
		AND
			old_par.tipo_catasto = new_par.tipo_catasto
		AND
			old_par.tipo_record = new_par.tipo_record
		AND  
			old_par.valid_from < v_oggi
	)
	AND NOT EXISTS 
	(
		SELECT 1
		FROM 
			ade_particella inspect_par
		INNER JOIN ade_particella_hist test ON inspect_par.id_part_hist = test.id_part_hist
		WHERE
			test.cod_comune = new_par.cod_comune
		AND 
			test.sezione = new_par.sezione
		AND 
			test.id_imm_catasto = new_par.id_imm_catasto
		AND 
			test.tipo_catasto = new_par.tipo_catasto
		AND 
			test.tipo_record = new_par.tipo_record
	);
	
	-- -----------------------------------------------------------------------
    -- 4. UPDATE FK SU TABELLA PARTICELLE NON STORICIZZATE
    -- -----------------------------------------------------------------------
	UPDATE ade_particella ap
	INNER JOIN 
		ade_particella_hist old_par
	ON 
		ap.id_part_hist = old_par.id_part_hist
	INNER JOIN
		ade_particella_hist new_par
	ON
		old_par.cod_comune = new_par.cod_comune
	AND
		old_par.sezione = new_par.sezione
	AND
		old_par.id_imm_catasto = new_par.id_imm_catasto
	AND
		old_par.tipo_catasto = new_par.tipo_catasto
	AND
		old_par.tipo_record = new_par.tipo_record
	SET
		ap.id_part_hist = new_par.id_part_hist,
		ap.snapshot_ts = v_oggi_dt
	WHERE
		old_par.is_current = 0
	AND
		new_par.is_current = 1
	AND 
		ap.id_part_hist <> new_par.id_part_hist;
		
	SET totale_inseriti = v_count;
	
END$$

DELIMITER ;