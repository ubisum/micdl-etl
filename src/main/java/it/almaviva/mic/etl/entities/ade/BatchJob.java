package it.almaviva.mic.etl.entities.ade;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Table(name = "batch_job")
public class BatchJob 
{
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "batch_id")
	private BigDecimal batchId;
	
	@Column(name = "fonte")
	private String fonte;
	
	@Column(name = "tipo_carico")
	private String tipoCarico;
	
	@Column(name = "avvio_ts")
	private LocalDateTime avvioTs;
	
	@Column(name = "fine_ts")
	private LocalDateTime fineTs;
	
	@Column(name = "esito")
	private String esito;
	
	@OneToMany(mappedBy = "batchJob", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<AdeUnitaImmHist> unitaImm;
	
	@OneToMany(mappedBy = "batchJob", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<AdeDatoCatastaleHist> datiCatastali;
	
	@OneToMany(mappedBy = "batchJob", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<AdeIndirizzoHist> indirizzi;
	
	@OneToMany(mappedBy = "batchJob", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<BatchJobDettaglio> batchJobDettaglio;
	
	@OneToMany(mappedBy = "batchJob", cascade = CascadeType.ALL, orphanRemoval = true)
	List<ProprietarioHist> listaProprietari;
	
	@OneToMany(mappedBy = "batchJob", cascade = CascadeType.ALL, orphanRemoval = true)
	List<AdeParticellaHist> listaParticelle ;
	
	@OneToMany(mappedBy = "batchJob", cascade = CascadeType.ALL, orphanRemoval = true)
	List<AdeDeduzioneTerHist> listaDeduzioni;
}
