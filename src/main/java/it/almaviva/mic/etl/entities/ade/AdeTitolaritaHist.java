package it.almaviva.mic.etl.entities.ade;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Table(name = "ade_dato_catastale_hist")
public class AdeTitolaritaHist 
{
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_titolarita_hist")
	private BigDecimal idTitolaritaHist;
	
	@Column(name = "cod_comune")
	private String codAmm;
	
	@Column(name = "sezione")
	private String sezione;
	
	@Column(name = "id_soggetto")
	private String idSoggetto;
	
	@Column(name = "tipo_record")
	private String idTipoSoggetto;
	
	@Column(name = "id_imm_catasto")
	private String idImmCatasto;
	
	@Column(name = "tipo_catasto")
	private String idTipoImmobile;
	
	@Column(name = "cod_diritto")
	private String codDiritto;
	
	@Column(name = "titolo_non_codificato")
	private String titoloNonCodificato;
	
	@Column(name = "quota_numeratore")
	private Integer quotaNumeratore;
	
	@Column(name = "quota_denominatore")
	private Integer quotaDenominatore;
	
	@Column(name = "regime")
	private String regime;
	
	@Column(name = "soggetto_riferimento")
	private String soggettoRiferimento;
	
	@Column(name = "reg_data_validita")
	private LocalDate dataValiditaReg;
	
	@Column(name = "reg_tipo_nota")
	private String tipoNotaReg;
	
	@Column(name = "reg_numero_nota")
	private String numeroNotaReg;
	
	@Column(name = "reg_progressivo_nota")
	private String progressivoNotaReg;
	
	@Column(name = "reg_anno_nota")
	private String annoNotaReg;
	
	@Column(name = "reg_data_reg_atto")
	private LocalDate dataRegistrazioneAttiReg;
	
	@Column(name = "partita")
	private String partitaReg;
	
	@Column(name = "conc_data_validita")
	private LocalDate dataValiditaConcl;
	
	@Column(name = "conc_tipo_nota")
	private String tipoNotaConcl;
	
	@Column(name = "conc_numero_nota")
	private String numeroNotaConcl;
	
	@Column(name = "conc_progressivo_nota")
	private String progressivoNotaConcl;
	
	@Column(name = "conc_anno_nota")
	private String annoNotaConcl;
	
	@Column(name = "conc_data_registrazione_atti")
	private LocalDate dataRegistrazioneAttiConcl;
	
	@Column(name = "conc_id_mutazione_iniz")
	private String idMutazioneIniz;
	
	@Column(name = "conc_id_mutazione_fin")
	private String idMutazioneFin;
	
	@Column(name = "id_titolarita")
	private String idTitolarita;
	
	@Column(name = "conc_cd_causale_atto_generante")
	private String cdCausaleAttoGenerante;
	
	@Column(name = "conc_descrizione_atto_generante")
	private String descrizioneAttoGenerante;
	
	@Column(name = "conc_cd_causale_atto_conclusivo")
	private String cdCausaleAttoConclusivo;
	
	@Column(name = "conc_descrizione_atto_conclusivo")
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

	
}
