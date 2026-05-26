DROP PROCEDURE IF EXISTS sp_riserva_scd2_load

DELIMITER $$

CREATE PROCEDURE sp_riserva_scd2_load(OUT totale_inseriti INT)
BEGIN
	DECLARE v_oggi_dt DATETIME;
    DECLARE v_oggi DATE;
	DECLARE v_count INT DEFAULT 0;
	
	SET v_oggi_dt = NOW();
	SET v_oggi = DATE(v_oggi_dt);
	
	-- ----------------------------------------------------------
	-- 1. CHIUSURA RECORD RELATIVI ALLE RISERVE 
	-- ----------------------------------------------------------
	UPDATE
		ade_riserva_ter_hist ris
	INNER JOIN 
		ade_particella_hist part
	ON 
		ris.id_part_hist = part.id_part_hist
	INNER JOIN 
		ADE_RISERVA_TER_STAGING staging
	ON
		staging.cod_comune COLLATE utf8mb4_unicode_ci = part.cod_comune COLLATE utf8mb4_unicode_ci 
	AND
		staging.sezione COLLATE utf8mb4_unicode_ci = part.sezione COLLATE utf8mb4_unicode_ci 
	AND
		staging.id_imm_catasto COLLATE utf8mb4_unicode_ci = part.id_imm_catasto COLLATE utf8mb4_unicode_ci 
	AND
		staging.tipo_catasto COLLATE utf8mb4_unicode_ci = part.tipo_catasto COLLATE utf8mb4_unicode_ci 
		
	SET
		ris.valid_to = v_oggi,
		ris.is_current = 0
	WHERE
		ris.is_current = 1;
	
	-- ----------------------------------------------------------
	-- 2. ELIMINAZIONE DEI RIFERIMENTI NON STORICIZZATI ---------
	-- ----------------------------------------------------------
	DELETE FROM ade_riserva_ter
	WHERE EXISTS
	(
		SELECT 1
		FROM 
			ade_riserva_ter_hist
		WHERE 
			ade_riserva_ter.id_ris_hist = ade_riserva_ter_hist.id_ris_hist
		AND
			ade_riserva_ter_hist.is_current = 0
	);
	
	-- ----------------------------------------------------------
	-- 3. INSERIMENTO DELLE NUOVE DEDUZIONI ---------------------
	-- ----------------------------------------------------------
	INSERT INTO ade_riserva_ter_hist
	(
		id_part_hist, 
		codice_riserva, 
		partita_iscrizione_riserva, 
		seq, 
		hash, 
		valid_from, 
		valid_to, 
		is_current,
		batch_id
	)
	SELECT
		part.id_part_hist,
		staging.codice_riserva,
		staging.partita_iscrizione_riserva,
		staging.seq,
		staging.hash,
		v_oggi,
		NULL,
		1,
		staging.batch_id
	FROM 
		ADE_RISERVA_TER_STAGING staging
	INNER JOIN
		ade_particella_hist part 
	ON
		staging.cod_comune COLLATE utf8mb4_unicode_ci = part.cod_comune COLLATE utf8mb4_unicode_ci 
	AND
		staging.sezione COLLATE utf8mb4_unicode_ci = part.sezione COLLATE utf8mb4_unicode_ci 
	AND
		staging.id_imm_catasto COLLATE utf8mb4_unicode_ci = part.id_imm_catasto COLLATE utf8mb4_unicode_ci 
	AND
		staging.tipo_catasto COLLATE utf8mb4_unicode_ci = part.tipo_catasto COLLATE utf8mb4_unicode_ci 
	-- AND
		-- staging.tipo_record COLLATE utf8mb4_unicode_ci = part.tipo_record COLLATE utf8mb4_unicode_ci 
	WHERE
		part.is_current = 1;
	-- AND NOT EXISTS
	-- (
	--	SELECT 1
	--	FROM ade_riserva_ter_hist test
	--	where test.id_part_hist = part.id_part_hist
	-- );
	
	-- CONTEGGIO RECORD INSERITI
	SET v_count = v_count + ROW_COUNT();
	
	-- ----------------------------------------------------------
	-- 3. INSERIMENTO DEI NUOVI RECORD NON STORICIZZATI
	-- ----------------------------------------------------------
	INSERT INTO ade_riserva_ter
	(
		id_ris_hist,
		snapshot_ts
	)
	SELECT
		id_ris_hist,
		v_oggi_dt
	FROM
		ade_riserva_ter_hist hist
	WHERE
		hist.is_current = 1
	AND NOT EXISTS
	(
		SELECT 1
		FROM
			ade_riserva_ter test
		where 
			hist.id_ris_hist = test.id_ris_hist
	);
	
	SET totale_inseriti = v_count;
	
END$$

DELIMITER ;