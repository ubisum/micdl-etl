INSERT INTO ADE_UNITA_IMM_HIST_STAGING (
  cod_comune, sezione, id_imm_catasto, tipo_catasto, progressivo,
  tipo_record, zona_censuaria, categoria, classe, consistenza,
  superficie, rendita_lire, rendita_euro, lotto, edificio,
  scala, interno1, interno2, piano1, piano2,
  piano3, piano4, reg_data_efficacia, reg_data_reg_atto, reg_tipo_nota,
  reg_numero_nota, reg_progressivo_nota, reg_anno_nota,
  conc_data_efficacia, conc_data_reg_atto, conc_tipo_nota,
  conc_numero_nota, conc_progressivo_nota, conc_anno_nota,
  conc_partita, conc_annotazione, conc_id_mut_iniz, conc_id_mut_fin,
  conc_protocollo_notifica, conc_data_notifica,
  conc_cd_atto_generante, conc_descr_atto_generante,
  conc_cd_atto_conclusivo, conc_descr_atto_conclusivo,
  conc_flag_classamento,
  hash,
  batch_id
)
VALUES (
  ?,?,?,?,?,?,?,?,?,?,
  ?,?,?,?,?,?,?,?,?,?,
  ?,?,?,?,?,?,?,?,?,?,
  ?,?,?,?,?,?,?,?,?,?,
  ?,?,?,?,?,?,?
);
