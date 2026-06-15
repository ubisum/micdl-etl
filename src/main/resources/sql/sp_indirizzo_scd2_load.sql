DROP PROCEDURE IF EXISTS sp_indirizzo_scd2_load;

DELIMITER $$

CREATE PROCEDURE sp_indirizzo_scd2_load(OUT totale_inseriti INT)
BEGIN
	DECLARE v_oggi_dt DATETIME;
	DECLARE v_count INT DEFAULT 0;
	DECLARE v_oggi DATE;
	SET v_oggi_dt = NOW();
	SET v_oggi = DATE(v_oggi_dt);
	
	-- ----------------------------------------------------------
	-- 1. CHIUSURA RECORD RELATIVI A INDIRIZZI PRESENTI
	-- ----------------------------------------------------------
	UPDATE ade_indirizzo_hist ind_hist
	INNER JOIN ade_unita_imm_hist imm on ind_hist.id_imm_hist = imm.id_imm_hist
	-- AND
	-- 	ind_hist.valid_to IS NULL
	INNER JOIN ADE_INDIRIZZO_HIST_STAGING staging
	ON 
		imm.cod_comune COLLATE utf8mb4_unicode_ci = staging.cod_comune COLLATE utf8mb4_unicode_ci
	AND 
		imm.sezione COLLATE utf8mb4_unicode_ci = staging.sezione COLLATE utf8mb4_unicode_ci
	AND 
		imm.id_imm_catasto COLLATE utf8mb4_unicode_ci = staging.id_imm_catasto COLLATE utf8mb4_unicode_ci
	AND 
		imm.tipo_catasto COLLATE utf8mb4_unicode_ci = staging.tipo_catasto COLLATE utf8mb4_unicode_ci 
	SET
		ind_hist.valid_to = v_oggi_dt,
		ind_hist.is_current = 0
	WHERE
		ind_hist.is_current = 1;
		
	-- ----------------------------------------------------------
	-- 2. ELIMINAZIONE DEI RIFERIMENTI NON STORICIZZATI
	-- ----------------------------------------------------------		
	DELETE FROM ade_indirizzo
	WHERE EXISTS
	(
		SELECT 1
		FROM 
			ade_indirizzo_hist
		WHERE 
			ade_indirizzo.id_ind_hist = ade_indirizzo_hist.id_ind_hist
		AND
			ade_indirizzo_hist.is_current = 0
	);
	
	-- ----------------------------------------------------------
	-- 3. INSERIMENTO DEI NUOVI RECORD DEGLI INDIRIZZI 
	-- ----------------------------------------------------------	
	INSERT INTO ade_indirizzo_hist
	(
		id_imm_hist,
		seq,
		toponimo,
		indirizzo,
		civico1,
		civico2,
		civico3,
		cod_strada,
		hash,
		valid_from,
		valid_to,
		is_current,
		batch_id
	)
	SELECT
		imm.id_imm_hist,
		staging.seq,
		staging.toponimo,
		staging.indirizzo,
		staging.civico1,
		staging.civico2,
		staging.civico3,
		staging.cod_strada,
		staging.hash,
		v_oggi_dt,
		NULL,
		1,
		staging.batch_id
	FROM ADE_INDIRIZZO_HIST_STAGING staging INNER JOIN ade_unita_imm_hist imm
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
	--		ade_indirizzo_hist test
	--	WHERE
	--		test.id_imm_hist = imm.id_imm_hist
	-- );
	
	SET v_count = v_count + ROW_COUNT();
	
	-- ----------------------------------------------------------
	-- 3. INSERIMENTO DEI NUOVI RECORD NON STORICIZZATI
	-- ----------------------------------------------------------	
	INSERT INTO ade_indirizzo
	(
		id_ind_hist,
		snapshot_ts
	)
	SELECT
		id_ind_hist,
		v_oggi_dt
	FROM
		ade_indirizzo_hist hist
	WHERE
		hist.is_current = 1
	AND NOT EXISTS
	(
		SELECT 1
		FROM
			ade_indirizzo test
		WHERE
			hist.id_ind_hist = test.id_ind_hist
	);
	
	SET totale_inseriti = v_count;
	
END$$

DELIMITER ;