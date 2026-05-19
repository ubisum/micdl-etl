CREATE TEMPORARY TABLE ADE_PORZIONE_HIST_STAGING
(
	cod_comune varchar(4),
	sezione varchar(1),
	id_imm_catasto varchar(9),
	tipo_catasto varchar(1),
	id_porzione varchar(2),
	qualita varchar(3),
	classe varchar(2),
	ettari varchar(5),
	`are` varchar(2),
	centiare varchar(2),
	reddito_dominicale_euro varchar(11),
	reddito_agrario_euro varchar(11),
	hash varchar(64),
	batch_id bigint
);