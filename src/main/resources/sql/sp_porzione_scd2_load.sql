DROP PROCEDURE IF EXISTS sp_porzione_scd2_load

DELIMITER $$

CREATE PROCEDURE sp_porzione_scd2_load(OUT totale_inseriti INT)
BEGIN
	DECLARE v_oggi_dt DATETIME;
    DECLARE v_oggi DATE;
	DECLARE v_count INT DEFAULT 0;
	
	SET v_oggi_dt = NOW();
	SET v_oggi = DATE(v_oggi_dt);
	
	-- ----------------------------------------------------------
	-- 1. CHIUSURA RECORD RELATIVI ALLE PORZIONI 
	-- ----------------------------------------------------------
	UPDATE
		ade_porzione_ter_hist por
	INNER JOIN 
		ade_particella_hist part
	ON 
		por.id_part_hist = part.id_part_hist
	AND 
		por.is_current = 1
	AND
		part.is_current = 0
	SET
		por.valid_to = v_oggi,
		por.is_current = 0;
		
	-- ----------------------------------------------------------
	-- 2. ELIMINAZIONE DEI RIFERIMENTI NON STORICIZZATI ---------
	-- ----------------------------------------------------------
	DELETE FROM ade_porzione_ter
	WHERE EXISTS
	(
		SELECT 1
		FROM 
			ade_porzione_ter_hist
		WHERE 
			ade_porzione_ter.id_por_hist = ade_porzione_ter_hist.id_por_hist
		AND
			ade_porzione_ter_hist.is_current = 0
	);
		
	-- ----------------------------------------------------------
	-- 3. INSERIMENTO DELLE NUOVE PORZIONI ---------------------
	-- ----------------------------------------------------------
	INSERT INTO ade_porzione_ter_hist 
	(
		id_part_hist,
		id_porzione,
		qualita,
		classe,
		ettari,
		`are`,
		centiare,
		reddito_dominicale_euro,
		reddito_agrario_euro,
		hash,
		valid_from,
		valid_to,
		is_current,
		batch_id
	)
	SELECT 
		part.id_part_hist,
		staging.id_porzione,
		staging.qualita,
		staging.classe,
		staging.ettari,
		staging.`are`,
		staging.centiare,
		staging.reddito_dominicale_euro,
		staging.reddito_agrario_euro,
		staging.hash,
		v_oggi,
		NULL,
		1,
		staging.batch_id
	FROM
		ADE_PORZIONE_HIST_STAGING staging
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
	WHERE
		part.is_current = 1
	AND NOT EXISTS
	(
		SELECT 1
		FROM 
			ade_porzione_ter_hist test
		WHERE 
			test.id_part_hist = part.id_part_hist
	);
	
	-- CONTEGGIO RECORD INSERITI
	SET v_count = v_count + ROW_COUNT();
	
	-- ----------------------------------------------------------
	-- 3. INSERIMENTO DEI NUOVI RECORD NON STORICIZZATI
	-- ----------------------------------------------------------
	INSERT INTO ade_porzione_ter
	(
		id_por_hist,
		snapshot_ts
	)
	SELECT
		id_por_hist,
		v_oggi_dt
	FROM
		ade_porzione_ter_hist hist
	WHERE
		hist.is_current = 1
	AND NOT EXISTS
	(
		SELECT 1
		FROM
			ade_porzione_ter test
		WHERE
			hist.id_por_hist = test.id_por_hist
	);
	
	SET totale_inseriti = v_count;
	
END$$

DELIMITER ;