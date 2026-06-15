DROP PROCEDURE IF EXISTS sp_dato_catastale_simple_insert;

DELIMITER $$
CREATE PROCEDURE sp_dato_catastale_simple_insert(OUT totale_inseriti INT)
BEGIN
	DECLARE v_oggi_dt DATETIME;
	-- DECLARE v_oggi DATE;
	DECLARE v_count INT DEFAULT 0;
	SET v_oggi_dt = NOW();
	-- SET v_oggi = DATE(v_oggi_dt);

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
		
	SET v_count = v_count + ROW_COUNT();
		
	-- ----------------------------------------------------------
	-- INSERIMENTO DEI NUOVI RECORD NON STORICIZZATI
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