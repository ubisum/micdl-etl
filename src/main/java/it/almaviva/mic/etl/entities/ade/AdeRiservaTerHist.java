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
@Table(name = "ade_riserva_ter_hist")
public class AdeRiservaTerHist 
{
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_ris_hist")
	private BigDecimal idRisHist;
	
	@Column(name = "codice_riserva")
	private String codiceRiserva;
	
	@Column(name = "partita_iscrizione_riserva")
	private String partitaIscrizioneRiserva;
	
	@Column(name = "seq")
	private Integer seq;
	
	@Column(name = "hash")
	private String hash;
	
	@Column(name = "valid_from")
	private LocalDateTime validFrom;
	
	@Column(name = "valid_to")
	private LocalDateTime validTo;
	
	@Column(name = "is_current")
	private Integer isCurrent;
	
	@ManyToOne
	@JoinColumn(name = "id_part_hist", nullable = false)
	private AdeParticellaHist particella;
	
	@ManyToOne
	@JoinColumn(name = "batch_id", nullable = false)
	private BatchJob batchJob;
}
