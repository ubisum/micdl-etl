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

@NoArgsConstructor
@Data
@Entity
@Table(name = "ade_porzione_ter_hist")
public class AdePorzioneTerHist 
{
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_por_hist")
	private BigDecimal idPorHist;
	
	@Column(name = "id_porzione")
	private String idPorzione;
	
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
	
	@Column(name = "reddito_dominicale_euro")
	private String redditoDominicaleEuro;
	
	@Column(name = "reddito_agrario_euro")
	private String redditoAgrarioEuro;
	
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
