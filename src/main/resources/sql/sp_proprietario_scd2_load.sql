DROP PROCEDURE IF EXISTS sp_proprietario_scd2_load;

DELIMITER $$

CREATE PROCEDURE sp_proprietario_scd2_load(OUT totale_inseriti INT)
BEGIN
	DECLARE v_oggi_dt DATETIME;
	DECLARE v_oggi DATE;
	DECLARE v_count INT DEFAULT 0;
	
	SET v_oggi_dt = NOW();
	SET v_oggi = DATE(v_oggi_dt);
	
	-- ---------------------------------------
    -- 1. CHIUSURA RECORD MODIFICATI
    -- ---------------------------------------
	UPDATE proprietario_hist tgt
	INNER JOIN PROPRIETARIO_HIST_STAGING src
	ON 
		tgt.cod_comune COLLATE utf8mb4_unicode_ci = src.cod_comune COLLATE utf8mb4_unicode_ci
	AND 
		tgt.sezione COLLATE utf8mb4_unicode_ci = src.sezione COLLATE utf8mb4_unicode_ci
	AND 
		tgt.id_soggetto COLLATE utf8mb4_unicode_ci = src.id_soggetto COLLATE utf8mb4_unicode_ci
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
	INSERT INTO proprietario_hist
	(
		cod_comune,
		sezione,
		id_soggetto,
		tipo_record,
		cod_fiscale,
		cognome,
		nome,
		sesso,
		data_nascita,
		luogo_nascita,
		altre_info,
		denominazione,
		sede,
		hash,
		valid_from,
		valid_to,
		is_current,
		batch_id
	)
	SELECT
		cod_comune,
		sezione,
		id_soggetto,
		tipo_record,
		cod_fiscale,
		cognome,
		nome,
		sesso,
		data_nascita,
		luogo_nascita,
		altre_info,
		denominazione,
		sede,
		hash,
		v_oggi,
		NULL,
		1,
		batch_id
	FROM PROPRIETARIO_HIST_STAGING src
	WHERE NOT EXISTS
	(
		SELECT 1
		FROM proprietario_hist tgt 
		WHERE
			tgt.cod_comune COLLATE utf8mb4_unicode_ci = src.cod_comune COLLATE utf8mb4_unicode_ci
		AND 
			tgt.sezione COLLATE utf8mb4_unicode_ci = src.sezione COLLATE utf8mb4_unicode_ci
		AND 
			tgt.id_soggetto COLLATE utf8mb4_unicode_ci = src.id_soggetto COLLATE utf8mb4_unicode_ci
		AND 
			tgt.tipo_record COLLATE utf8mb4_unicode_ci = src.tipo_record COLLATE utf8mb4_unicode_ci
		AND 
			tgt.is_current = 1
		AND 
			tgt.hash COLLATE utf8mb4_unicode_ci = src.hash COLLATE utf8mb4_unicode_ci
	);
	
	SET v_count = v_count + ROW_COUNT();
	
	-- -----------------------------------------------------------------------
    -- 3. UPDATE FK SU TABELLA PROPRIETARI NON STORICIZZATI
    -- -----------------------------------------------------------------------
	UPDATE 
		proprietario prop
	INNER JOIN 
		proprietario_hist old_prop
	ON
		prop.id_proprietario_hist = old_prop.id_proprietario_hist
	INNER JOIN
		proprietario_hist new_prop
	ON 
		old_prop.cod_comune = new_prop.cod_comune
	AND 
		old_prop.sezione = new_prop.sezione
	AND 
		old_prop.id_soggetto = new_prop.id_soggetto
	AND 
		old_prop.tipo_record = new_prop.tipo_record
	SET
		prop.id_proprietario_hist = new_prop.id_proprietario_hist,
		prop.snapshot_ts = v_oggi_dt
	WHERE
		old_prop.is_current = 0
	AND
		new_prop.is_current = 1
	AND
		prop.id_proprietario_hist <> new_prop.id_proprietario_hist;
		
	SET totale_inseriti = v_count;
	
	-- -----------------------------------------------------------------------
    -- 4. INSERIMENTI SU TABELLA PROPRIETARI NON STORICIZZATI
    -- -----------------------------------------------------------------------
	INSERT INTO proprietario
	(
		id_proprietario_hist,
		snapshot_ts
	)
	
	SELECT
		new_prop.id_proprietario_hist,
		v_oggi_dt
	FROM 	
		proprietario_hist new_prop
	WHERE 
		new_prop.is_current = 1
	AND
		new_prop.valid_from = v_oggi
	AND NOT EXISTS
	(
		SELECT 
			1
		FROM 
			proprietario_hist old_prop
		WHERE 
			old_prop.cod_comune = new_prop.cod_comune
		AND 
			old_prop.sezione = new_prop.sezione
		AND 
			old_prop.id_soggetto = new_prop.id_soggetto
		AND 
			old_prop.tipo_record = new_prop.tipo_record
		AND 
			old_prop.valid_from < v_oggi
	)
	AND NOT EXISTS 
	(
		SELECT 
			1
		FROM 
			proprietario inspect_prop
		INNER JOIN 
			proprietario_hist test_prop
		ON
			inspect_prop.id_proprietario_hist = test_prop.id_proprietario_hist
		WHERE 
			test_prop.cod_comune = new_prop.cod_comune
		AND 
			test_prop.sezione = new_prop.sezione
		AND 
			test_prop.id_soggetto = new_prop.id_soggetto
		AND 
			test_prop.tipo_record = new_prop.tipo_record
	);
	
END$$

DELIMITER ;