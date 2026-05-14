CREATE TEMPORARY TABLE ADE_RISERVA_TER_STAGING
(
	cod_comune varchar(4),
	sezione varchar(1),
	id_imm_catasto varchar(9),
	tipo_catasto varchar(1),
	tipo_record varchar(1),
	codice_riserva varchar(1),
	partita_iscrizione_riserva VARCHAR(7),
	seq int,
	hash varchar(64),
	batch_id bigint
);