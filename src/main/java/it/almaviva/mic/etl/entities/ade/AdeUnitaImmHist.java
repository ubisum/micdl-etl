package it.almaviva.mic.etl.entities.ade;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Table(name = "ade_unita_imm_hist")
public class AdeUnitaImmHist 
{
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_imm_hist")
	private BigDecimal idImmHist;
	
	@Column(name = "cod_comune")
	private String codComune;
	
	@Column(name = "sezione")
	private String sezione;
	
	@Column(name = "id_imm_catasto")
	private String idImmCatasto;
	
	@Column(name = "tipo_catasto")
	private String tipoCatasto;
	
	@Column(name = "progressivo")
	private String progressivo;
	
	@Column(name = "tipo_record")
	private String tipoRecord;
	
	@Column(name = "zona_censuaria")
	private String zonaCensuaria;
	
	@Column(name = "categoria")
	private String categoria;
	
	@Column(name = "classe")
	private String classe;
	
	@Column(name = "consistenza")
	private String consistenza;
	
	@Column(name = "superficie")
	private String superficie;
	
	@Column(name = "rendita_lire")
	private String renditaLire;
	
	@Column(name = "rendita_euro")
	private String renditaEuro;
	
	@Column(name = "lotto")
	private String lotto;
	
	@Column(name = "edificio")
	private String edificio;
	
	@Column(name = "scala")
	private String scala;
	
	@Column(name = "interno1")
	private String interno1;
	
	@Column(name = "interno2")
	private String interno2;
	
	@Column(name = "piano1")
	private String piano1;
	
	@Column(name = "piano2")
	private String piano2;
	
	@Column(name = "piano3")
	private String piano3;
	
	@Column(name = "piano4")
	private String piano4;
	
	@Column(name = "reg_data_efficacia")
	private String regDataEfficiacia;
	
	@Column(name = "reg_data_reg_atto")
	private String regDataregAtto;
	
	@Column(name = "reg_tipo_nota")
	private String regTipoNota;
	
	@Column(name = "reg_numero_nota")
	private String regNumeroNota;
	
	@Column(name = "reg_progressivo_nota")
	private String regProgressivoNota;
	
	@Column(name = "reg_anno_nota")
	private Integer regAnnoNota;
	
	@Column(name = "conc_data_efficacia")
	private String concDataEfficacia;
	
	@Column(name = "conc_data_reg_atto")
	private String concDataRegAtto;
	
	@Column(name = "conc_tipo_nota")
	private String concTipoNota;
	
	@Column(name = "conc_numero_nota")
	private String concNumeroNota;
	
	@Column(name = "conc_progressivo_nota")
	private String concProgressivoNota;
	
	@Column(name = "conc_anno_nota")
	private Integer concAnnoNota;
	
	@Column(name = "conc_partita")
	private String concPartita;
	
	@Column(name = "conc_annotazione")
	private String concAnnotazione;
	
	@Column(name = "conc_id_mut_iniz")
	private String concIdMutIniz;
	
	@Column(name = "conc_id_mut_fin")
	private String concIdMutFin;
	
	@Column(name = "conc_protocollo_notifica")
	private String concProtocolloNotifica;
	
	@Column(name = "conc_data_notifica")
	private String concDataNotifica;
	
	@Column(name = "conc_cd_atto_generante")
	private String concCdAttoGenerante;
	
	@Column(name = "conc_descr_atto_generante")
	private String concDescrAttoGenerante;
	
	@Column(name = "conc_cd_atto_conclusivo")
	private String concCdAttoConclusivo;
	
	@Column(name = "conc_descr_atto_conclusivo")
	private String concDescrAttoConclusivo;
	
	@Column(name = "conc_flag_classamento")
	private String concFlagClassamento;
	
	@Column(name = "hash")
	private String hash;
	
	@Column(name = "valid_from")
	private Date validFrom;
	
	@Column(name = "valid_to")
	private Date validTo;
	
	@Column(name = "is_current")
	private Boolean isCurrent;
	
	@Column(name = "batch_id")
	private BigDecimal batchId;
	
	@OneToMany(mappedBy = "unitaImm", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<AdeDatoCatastaleHist> datiCatastali;
	
	@OneToMany(mappedBy = "unitaImm", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<AdeIndirizzoHist> indirizzi;
	
	@ManyToOne
	@JoinColumn(name = "batch_id", nullable = false)
	private BatchJob batchJob;
}
