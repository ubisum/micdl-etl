CREATE TEMPORARY TABLE ADE_DATO_CATASTALE_HIST_STAGING
(
  cod_comune varchar(4),
  sezione varchar(1),
  id_imm_catasto varchar(9),
  tipo_catasto varchar(1),
  progressivo varchar(3),
  tipo_record varchar(1),
  sezione_urbana varchar(3),
  foglio varchar(4),
  particella varchar(5),
  denominatore int,
  subalterno varchar(4),
  edificialita varchar(1),
  hash varchar(64),
  batch_id bigint
);