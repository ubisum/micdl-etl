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
@Table(name = "proprietario_hist")
public class ProprietarioHist 
{
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_proprietario_hist")
	private BigDecimal idProprietarioHist;
	
	@Column(name = "cod_comune")
	private String codiceComune;
	
	@Column(name = "sezione")
	private String sezione;
	
	@Column(name = "id_soggetto")
	private String idSoggetto;
	
	@Column(name = "tipo_record")
	private String tipoRecord;
	
	@Column(name = "cod_fiscale")
	private String codiceFiscale;
	
	@Column(name = "cognome")
	private String cognome;
	
	@Column(name = "nome")
	private String nome;
	
	@Column(name = "sesso")
	private String sesso;
	
	@Column(name = "data_nascita")
	private LocalDate dataNascita;
	
	@Column(name = "luogo_nascita")
	private String luogoNascita;
	
	@Column(name = "altre_info")
	private String altreInfo;
	
	@Column(name = "denominazione")
	private String denominazione;
	
	@Column(name = "sede")
	private String sede;
	
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
