DROP PROCEDURE IF EXISTS sp_dato_catastale_scd2_load;

DELIMITER $$

CREATE PROCEDURE sp_dato_catastale_scd2_load(OUT totale_inseriti INT)
BEGIN 
	DECLARE v_oggi_dt DATETIME;
    DECLARE v_oggi DATE;
	DECLARE v_count INT DEFAULT 0;
	
	SET v_oggi_dt = NOW();
	SET v_oggi = DATE(v_oggi_dt);

	-- ----------------------------------------------------------
	-- 1. CHIUSURA RECORD RELATIVI A DATI CATASTALI 
	-- ----------------------------------------------------------
	UPDATE ade_dato_catastale_hist dc_hist
	INNER JOIN ade_unita_imm_hist imm 
		ON dc_hist.id_imm_hist = imm.id_imm_hist
	INNER JOIN ADE_DATO_CATASTALE_HIST_STAGING staging
		ON imm.cod_comune COLLATE utf8mb4_unicode_ci = staging.cod_comune COLLATE utf8mb4_unicode_ci
	   AND imm.sezione COLLATE utf8mb4_unicode_ci = staging.sezione COLLATE utf8mb4_unicode_ci
	   AND imm.id_imm_catasto COLLATE utf8mb4_unicode_ci = staging.id_imm_catasto COLLATE utf8mb4_unicode_ci
	   AND imm.tipo_catasto COLLATE utf8mb4_unicode_ci = staging.tipo_catasto COLLATE utf8mb4_unicode_ci
	SET
		dc_hist.valid_to = v_oggi_dt,
		dc_hist.is_current = 0
	WHERE dc_hist.is_current = 1;
		
	-- ----------------------------------------------------------
	-- 2. ELIMINAZIONE DEI RIFERIMENTI NON STORICIZZATI
	-- ----------------------------------------------------------		
	DELETE FROM ade_dato_catastale
	WHERE EXISTS
	(
		SELECT 1
		FROM 
			ade_dato_catastale_hist
		WHERE 
			ade_dato_catastale.id_dc_hist = ade_dato_catastale_hist.id_dc_hist
		AND
			ade_dato_catastale_hist.is_current = 0
		
	);
	
	-- ----------------------------------------------------------
	-- 3. INSERIMENTO DEI NUOVI RECORD DEI DATI CATASTALI
	-- ----------------------------------------------------------	
	INSERT INTO ade_dato_catastale_hist
	(
		id_imm_hist,
		sezione_urbana,
		foglio,
		particella,
		denominatore,
		subalterno,
		edificialita,
		hash,
		valid_from,
		valid_to,
		is_current,
		batch_id
	)
	SELECT
		imm.id_imm_hist,
		staging.sezione_urbana,
		staging.foglio,
		staging.particella,
		staging.denominatore,
		staging.subalterno,
		staging.edificialita,
		staging.hash,
		v_oggi_dt,
		NULL,
		1,
		staging.batch_id
	FROM ADE_DATO_CATASTALE_HIST_STAGING staging INNER JOIN ade_unita_imm_hist imm 
	ON 
		imm.cod_comune COLLATE utf8mb4_unicode_ci = staging.cod_comune COLLATE utf8mb4_unicode_ci
	AND 
		imm.sezione COLLATE utf8mb4_unicode_ci = staging.sezione COLLATE utf8mb4_unicode_ci
	AND 
		imm.id_imm_catasto COLLATE utf8mb4_unicode_ci = staging.id_imm_catasto COLLATE utf8mb4_unicode_ci
	AND 
		imm.tipo_catasto COLLATE utf8mb4_unicode_ci = staging.tipo_catasto COLLATE utf8mb4_unicode_ci
	WHERE
		imm.is_current = 1;
	-- AND NOT EXISTS
	-- (
	--	SELECT 1
	--	FROM
	--		ade_dato_catastale_hist test
	--	WHERE
	--		test.id_imm_hist = imm.id_imm_hist
	-- );
		
	SET v_count = v_count + ROW_COUNT();
	
	-- ----------------------------------------------------------
	-- 3. INSERIMENTO DEI NUOVI RECORD NON STORICIZZATI
	-- ----------------------------------------------------------	
	INSERT INTO ade_dato_catastale
	(
		id_dc_hist,
		snapshot_ts
	)
	SELECT
		id_dc_hist,
		v_oggi_dt
	FROM 
		ade_dato_catastale_hist hist
	where 
		hist.is_current = 1
	and NOT EXISTS
	(
		SELECT 1
		FROM
			ade_dato_catastale test
		where
			hist.id_dc_hist = test.id_dc_hist
	);
	
	SET totale_inseriti = v_count;

END$$

DELIMITER ;
