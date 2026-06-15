package it.almaviva.mic.etl.entities.ade;

import java.math.BigDecimal;
import java.time.LocalDateTime;

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
@Table(name = "ade_indirizzo_hist")
public class AdeIndirizzoHist 
{
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_ind_hist")
	private BigDecimal idIndHist;
	
	@Column(name = "seq")
	private Integer sequenza;
	
	@Column(name = "toponimo")
	private String toponimo;
	
	@Column(name = "indirizzo")
	private String indirizzo;
	
	@Column(name = "civico1")
	private String civico1;
	
	@Column(name = "civico2")
	private String civico2;
	
	@Column(name = "civico3")
	private String civico3;
	
	@Column(name = "cod_strada")
	private String codStrada;
	
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
