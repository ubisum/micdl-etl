-- ----------------------------------------------------------
-- ----------------------- ADE_UNITA_IMM_HIST ----------------
-- ----------------------------------------------------------
CREATE TABLE `ade_unita_imm_hist` (
  `id_imm_hist` bigint PRIMARY KEY AUTO_INCREMENT,
  `cod_comune` varchar(4) NOT NULL,
  `sezione` varchar(1),
  `id_imm_catasto` varchar(15),
  `tipo_catasto` varchar(1) NOT NULL DEFAULT 'F',
  `progressivo` varchar(3),
  `tipo_record` varchar(1),
  `zona_censuaria` varchar(3),
  `categoria` varchar(4),
  `classe` varchar(2),
  `consistenza` varchar(7),
  `superficie` varchar(5),
  `rendita_lire` varchar(15),
  `rendita_euro` varchar(18),
  `lotto` varchar(2),
  `edificio` varchar(2),
  `scala` varchar(2),
  `interno1` varchar(3),
  `interno2` varchar(3),
  `piano1` varchar(4),
  `piano2` varchar(4),
  `piano3` varchar(4),
  `piano4` varchar(4),
  `reg_data_efficacia` varchar(8),
  `reg_data_reg_atto` varchar(8),
  `reg_tipo_nota` varchar(1),
  `reg_numero_nota` varchar(6),
  `reg_progressivo_nota` varchar(3),
  `reg_anno_nota` int,
  `conc_data_efficacia` varchar(8),
  `conc_data_reg_atto` varchar(8),
  `conc_tipo_nota` varchar(1),
  `conc_numero_nota` varchar(6),
  `conc_progressivo_nota` varchar(3),
  `conc_anno_nota` int,
  `conc_partita` varchar(7),
  `conc_annotazione` varchar(200),
  `conc_id_mut_iniz` varchar(9),
  `conc_id_mut_fin` varchar(9),
  `conc_protocollo_notifica` varchar(18),
  `conc_data_notifica` varchar(8),
  `conc_cd_atto_generante` varchar(3),
  `conc_descr_atto_generante` varchar(100),
  `conc_cd_atto_conclusivo` varchar(3),
  `conc_descr_atto_conclusivo` varchar(100),
  `conc_flag_classamento` varchar(1),
  `hash` varchar(64),
  `valid_from` date NOT NULL,
  `valid_to` date,
  `is_current` boolean NOT NULL DEFAULT false,
  `batch_id` bigint NOT NULL
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = 'SCD2 - Unita immobiliari catasto fabbricati (NCEU). NK: cod_comune+sezione+id_imm_catasto+tipo_catasto+progressivo+tipo_record';
ALTER TABLE `ade_unita_imm_hist` COMMENT = 'NK: cod_comune + sezione + id_imm_catasto + progressivo + tipo_record; SCD2: una sola is_current=1 per NK';
CREATE INDEX `ix_ade_u_imm_hist_nk` ON `ade_unita_imm_hist` (`cod_comune`, `sezione`, `id_imm_catasto`, `progressivo`, `tipo_record`);
CREATE INDEX `ix_ade_imm_hist_batch` ON `ade_unita_imm_hist` (`batch_id`);
ALTER TABLE `ade_unita_imm_hist` ADD FOREIGN KEY (`zona_censuaria`) REFERENCES `lkp_sezione_censuaria` (`codice`);
ALTER TABLE `ade_unita_imm_hist` ADD FOREIGN KEY (`categoria`) REFERENCES `lkp_categoria_catastale` (`codice`);
ALTER TABLE `ade_unita_imm_hist` ADD FOREIGN KEY (`reg_tipo_nota`) REFERENCES `lkp_tipo_nota` (`codice`);
ALTER TABLE `ade_unita_imm_hist` ADD FOREIGN KEY (`conc_tipo_nota`) REFERENCES `lkp_tipo_nota` (`codice`);
ALTER TABLE `ade_unita_imm_hist` ADD FOREIGN KEY (`conc_flag_classamento`) REFERENCES `lkp_classamento` (`codice`);
ALTER TABLE `ade_unita_imm_hist` ADD FOREIGN KEY (`batch_id`) REFERENCES `batch_job` (`batch_id`);

-- ----------------------------------------------------------
-- ----------------------- PROPRIETARIO_HIST ----------------
-- ----------------------------------------------------------
ALTER TABLE proprietario_hist 
MODIFY id_soggetto VARCHAR(15);

ALTER TABLE proprietario_hist 
MODIFY cognome VARCHAR(150)
