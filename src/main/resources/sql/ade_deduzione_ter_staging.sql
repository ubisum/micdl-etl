CREATE TEMPORARY TABLE ADE_DEDUZIONE_TER_STAGING
(
	cod_comune varchar(4),
	sezione varchar(1),
	id_imm_catasto varchar(9),
	tipo_catasto varchar(1),
	tipo_record varchar(1),
	simbolo_deduzione varchar(6),
	seq int,
	batch_id bigint
);