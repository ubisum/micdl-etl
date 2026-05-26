-- ----------------------------------------------------------
-- ----------------------- ADE_UNITA_IMM_HIST ---------------
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
-- ----------------------- ADE_TITOLARITA_HIST ---------------
-- ----------------------------------------------------------
CREATE TABLE `ade_titolarita_hist` (
  `id_titolarita_hist` bigint NOT NULL AUTO_INCREMENT,
  `cod_comune` varchar(4) COLLATE utf8mb4_unicode_ci NOT NULL,
  `sezione` varchar(1) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `id_soggetto` varchar(9) COLLATE utf8mb4_unicode_ci NOT NULL,
  `tipo_record` varchar(1) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `id_imm_catasto` varchar(9) COLLATE utf8mb4_unicode_ci NOT NULL,
  `tipo_catasto` varchar(1) COLLATE utf8mb4_unicode_ci NOT NULL,
  `cod_diritto` varchar(3) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `titolo_non_codificato` varchar(200) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `quota_numeratore` int DEFAULT NULL,
  `quota_denominatore` int DEFAULT NULL,
  `regime` varchar(1) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `soggetto_riferimento` varchar(9) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `reg_data_validita` date DEFAULT NULL,
  `reg_tipo_nota` varchar(1) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `reg_numero_nota` varchar(6) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `reg_progressivo_nota` varchar(3) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `reg_anno_nota` varchar(4) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `reg_data_reg_atto` date DEFAULT NULL,
  `partita` varchar(7) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `conc_data_validita` date DEFAULT NULL,
  `conc_tipo_nota` varchar(1) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `conc_numero_nota` varchar(6) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `conc_progressivo_nota` varchar(3) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `conc_anno_nota` varchar(4) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `conc_data_registrazione_atti` date DEFAULT NULL,
  `conc_id_mutazione_iniz` varchar(9) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `conc_id_mutazione_fin` varchar(9) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `conc_cd_causale_atto_generante` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `conc_descrizione_atto_generante` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `conc_cd_causale_atto_conclusivo` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `conc_descrizione_atto_conclusivo` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `hash` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `valid_from` date NOT NULL,
  `valid_to` date DEFAULT NULL,
  `is_current` tinyint(1) NOT NULL DEFAULT '0',
  `batch_id` bigint NOT NULL,
  `id_titolarita` varchar(15) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id_titolarita_hist`),
  KEY `ix_tit_ade_hist_assoc` (`cod_comune`,`sezione`,`id_soggetto`,`id_imm_catasto`,`tipo_catasto`),
  KEY `ix_tit_ade_hist_batch` (`batch_id`),
  KEY `id_soggetto` (`id_soggetto`),
  KEY `tipo_record` (`tipo_record`),
  KEY `cod_diritto` (`cod_diritto`),
  KEY `regime` (`regime`),
  KEY `soggetto_riferimento` (`soggetto_riferimento`),
  CONSTRAINT `ade_titolarita_hist_ibfk_1` FOREIGN KEY (`id_soggetto`) REFERENCES `proprietario_hist` (`id_soggetto`),
  CONSTRAINT `ade_titolarita_hist_ibfk_2` FOREIGN KEY (`tipo_record`) REFERENCES `lkp_tipo_proprietario` (`codice`),
  CONSTRAINT `ade_titolarita_hist_ibfk_3` FOREIGN KEY (`cod_diritto`) REFERENCES `lkp_codici_diritto` (`codice`),
  CONSTRAINT `ade_titolarita_hist_ibfk_4` FOREIGN KEY (`regime`) REFERENCES `lkp_regimi` (`codice`),
  CONSTRAINT `ade_titolarita_hist_ibfk_5` FOREIGN KEY (`soggetto_riferimento`) REFERENCES `proprietario_hist` (`id_soggetto`),
  CONSTRAINT `ade_titolarita_hist_ibfk_6` FOREIGN KEY (`batch_id`) REFERENCES `batch_job` (`batch_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='chiave utilizzata in doc SMIDT; SCD2: una sola is_current=1 per NK';


-- ----------------------------------------------------------
-- ----------------------- PROPRIETARIO_HIST ----------------
-- ----------------------------------------------------------
-- asset_mgmt.proprietario_hist definition

CREATE TABLE `proprietario_hist` (
  `id_proprietario_hist` bigint NOT NULL AUTO_INCREMENT,
  `cod_comune` varchar(4) COLLATE utf8mb4_unicode_ci NOT NULL,
  `sezione` varchar(1) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `id_soggetto` varchar(15) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `tipo_record` varchar(1) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `cod_fiscale` varchar(16) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `cognome` varchar(150) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `nome` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `sesso` varchar(1) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `data_nascita` date DEFAULT NULL,
  `luogo_nascita` varchar(4) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `altre_info` varchar(120) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `denominazione` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `sede` varchar(4) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `hash` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `valid_from` date NOT NULL,
  `valid_to` date DEFAULT NULL,
  `is_current` tinyint(1) NOT NULL DEFAULT '0',
  `batch_id` bigint NOT NULL,
  PRIMARY KEY (`id_proprietario_hist`),
  KEY `ix_ade_prop_key_hist_cf` (`cod_comune`,`sezione`,`id_soggetto`,`tipo_record`),
  KEY `ix_ade_prop_hist_cod_fiscale` (`cod_fiscale`),
  KEY `ix_ade_prop_hist_id_sogg` (`id_soggetto`),
  KEY `ix_ade_prop_hist_batch_id` (`batch_id`),
  KEY `tipo_record` (`tipo_record`),
  CONSTRAINT `proprietario_hist_ibfk_1` FOREIGN KEY (`tipo_record`) REFERENCES `lkp_tipo_proprietario` (`codice`),
  CONSTRAINT `proprietario_hist_ibfk_2` FOREIGN KEY (`batch_id`) REFERENCES `batch_job` (`batch_id`)
) ENGINE=InnoDB AUTO_INCREMENT=40963 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='NK: chiave utilizzata in doc SMIDT; SCD2: una sola is_current=1 per NK';

