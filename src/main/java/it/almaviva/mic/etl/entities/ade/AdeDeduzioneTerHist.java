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
@Table(name = "ade_deduzione_ter_hist")
public class AdeDeduzioneTerHist 
{
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_ded_hist")
	private BigDecimal idDedHist;
	
	@Column(name = "simbolo_deduzione")
	private String simboloDeduzione;
	
	@Column(name = "seq")
	private Integer seq;
	
	@Column(name = "hash")
	private String hash;
	
	@Column(name = "valid_from")
	private LocalDate validFrom;
	
	@Column(name = "valid_to")
	private LocalDate validTo;
	
	@Column(name = "is_current")
	private Integer isCurrent;
	
	@ManyToOne
	@JoinColumn(name = "id_part_hist", nullable = false)
	private AdeParticellaHist particella;
	
	@ManyToOne
	@JoinColumn(name = "batch_id", nullable = false)
	private BatchJob batchJob;
}
