INSERT INTO ADE_PORZIONE_HIST_STAGING
(
	cod_comune,
	sezione,
	id_imm_catasto,
	tipo_catasto,
	qualita,
	classe,
	ettari,
	`are`,
	centiare,
	reddito_dominicale_euro,
	reddito_agrario_euro,
	hash,
	batch_id
)
VALUES
(	?,?,?,?,?,?,?,?,?,?,
	?,?,?
);