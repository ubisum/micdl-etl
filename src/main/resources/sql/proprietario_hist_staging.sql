CREATE TEMPORARY TABLE PROPRIETARIO_HIST_STAGING
(
	cod_comune VARCHAR(4),
	sezione VARCHAR(1),
	id_soggetto VARCHAR(10),
	tipo_record VARCHAR(1),
	cod_fiscale VARCHAR(16),
	cognome VARCHAR(50),
	nome VARCHAR(50),
	sesso VARCHAR(1),
	data_nascita DATE,
	luogo_nascita VARCHAR(4),
	altre_info VARCHAR(120),
	denominazione VARCHAR(100),
	sede VARCHAR(4),
	hash VARCHAR(64),
	batch_id BIGINT
);