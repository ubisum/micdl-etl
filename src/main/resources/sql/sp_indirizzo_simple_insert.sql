DROP PROCEDURE IF EXISTS sp_indirizzo_simple_insert;

DELIMITER $$

CREATE PROCEDURE sp_indirizzo_simple_insert(OUT totale_inseriti INT)
BEGIN
	DECLARE v_oggi_dt DATETIME;
	DECLARE v_count INT DEFAULT 0;
	DECLARE v_oggi DATE;
	SET v_oggi_dt = NOW();
	SET v_oggi = DATE(v_oggi_dt);

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
		v_oggi,
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
		
	SET v_count = v_count + ROW_COUNT();
		
	-- ----------------------------------------------------------
	-- INSERIMENTO DEI NUOVI RECORD NON STORICIZZATI
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