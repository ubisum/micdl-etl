package it.almaviva.mic.etl.entities.ade;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

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

@NoArgsConstructor
@Data
@Entity
@Table(name = "ade_particella_hist")
public class AdeParticellaHist 
{
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_part_hist")
	private BigDecimal idPartHist;
	
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
	
	@Column(name = "foglio")
	private String foglio;
	
	@Column(name = "particella")
	private String numero;
	
	@Column(name = "denominatore")
	private Integer denominatore;
	
	@Column(name = "subalterno")
	private String subalterno;
	
	@Column(name = "edificialita")
	private String edificialita;
	
	@Column(name = "qualita")
	private String qualita;
	
	@Column(name = "classe")
	private String classe;
	
	@Column(name = "ettari")
	private String ettari;
	
	@Column(name = "are")
	private String are;
	
	@Column(name = "centiare")
	private String centiare;
	
	@Column(name = "flag_reddito")
	private String flagReddito;
	
	@Column(name = "flag_porzione")
	private String flagPorzione;
	
	@Column(name = "flag_deduzioni")
	private String flagDeduzioni;
	
	@Column(name = "reddito_dominicale_lire")
	private String redditoDominicaleLire;
	
	@Column(name = "reddito_agrario_lire")
	private String redditoAgrarioLire;
	
	@Column(name = "reddito_dominicale_euro")
	private String redditoDominicaleEuro;
	
	@Column(name = "reddito_agrario_euro")
	private String redditoAgrarioEuro;
	
	@Column(name = "reg_data_efficacia")
	private String dataEfficaciaReg;
	
	@Column(name = "reg_data_reg_atto")
	private String dataRegistrazioneAttiReg;
	
	@Column(name = "reg_tipo_nota")
	private String tipoNotaReg;
	
	@Column(name = "reg_numero_nota")
	private String numeroNotaReg;
	
	@Column(name = "reg_progressivo_nota")
	private String progressivoNotaReg;
	
	@Column(name = "reg_anno_nota")
	private Integer annoNotaReg;
	
	@Column(name = "conc_data_efficacia")
	private String dataEfficaciaConcl;
	
	@Column(name = "conc_data_reg_atto")
	private String dataRegistrazioneAttiConcl;
	
	@Column(name = "conc_tipo_nota")
	private String tipoNotaConcl;
	
	@Column(name = "conc_numero_nota")
	private String numeroNotaConcl;
	
	@Column(name = "conc_progressivo_nota")
	private String progressivoNotaConcl;
	
	@Column(name = "conc_anno_nota")
	private Integer annoNotaConcl;
	
	@Column(name = "partita")
	private String partita;
	
	@Column(name = "annotazione")
	private String annotazione;
	
	@Column(name = "conc_id_mut_iniz")
	private String idMutazioneIniziale;
	
	@Column(name = "conc_id_mut_fin")
	private String idMutazioneFinale;
	
	@Column(name = "conc_cd_atto_generante")
	private String cdCausaleAttoGenerante;
	
	@Column(name = "conc_descr_atto_generante")
	private String descrizioneAttoGenerante;
	
	@Column(name = "conc_cd_atto_conclusivo")
	private String cdcausaleAttoConclusivo;
	
	@Column(name = "conc_descr_atto_conclusivo")
	private String descrizioneAttoConclusivo;
	
	@Column(name = "hash")
	private String hash;
	
	@Column(name = "valid_from")
	private LocalDate validFrom;
	
	@Column(name = "valid_to")
	private LocalDate validTo;
	
	@Column(name = "is_current")
	private Integer isCurrent;
	
	@ManyToOne
	@JoinColumn(name = "batch_id", nullable = false)
	private BatchJob batchJob;
	
	@OneToMany(mappedBy = "particella")
	private List<AdeDeduzioneTerHist> listaDeduzioni;
}
