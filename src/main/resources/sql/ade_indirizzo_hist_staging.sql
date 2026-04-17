CREATE TEMPORARY TABLE ADE_INDIRIZZO_HIST_STAGING
(
	cod_comune varchar(4),
	sezione varchar(1),
	id_imm_catasto varchar(9),
	tipo_catasto varchar(1),
	progressivo varchar(3),
	tipo_record varchar(1),
	seq int,
	toponimo varchar(3),
	indirizzo varchar(50),
	civico1 varchar(6),
	civico2 varchar(6),
	civico3 varchar(6),
	cod_strada varchar(5),
	hash varchar(64),
	batch_id bigint
);