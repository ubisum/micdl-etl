package it.almaviva.mic.etl.entities.ade;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;

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
public class AdeDatoCatastaleHist 
{
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_dc_hist")
	private BigDecimal idDcHist;
	
	@Column(name = "sezione_urbana")
	private String sezioneUrbana;
	
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
	
	@Column(name = "hash")
	private String hash;
	
	@Column(name = "valid_from")
	private LocalDateTime validFrom;
	
	@Column(name = "valid_to")
	private LocalDateTime validTo;
	
	@Column(name = "is_current")
	private Boolean isCurrent;
	
	@ManyToOne
	@JoinColumn(name = "id_imm_hist", nullable = false)
	private AdeUnitaImmHist unitaImm;
	
	@ManyToOne
	@JoinColumn(name = "batch_id", nullable = false)
	private BatchJob batchJob;
}
