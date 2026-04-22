package it.almaviva.mic.etl.repositories;

import java.math.BigDecimal;

import org.springframework.data.jpa.repository.JpaRepository;

import it.almaviva.mic.etl.entities.ade.BatchJobDettaglio;

public interface BatchJobDettaglioRepository extends JpaRepository<BatchJobDettaglio, BigDecimal> 
{

}
