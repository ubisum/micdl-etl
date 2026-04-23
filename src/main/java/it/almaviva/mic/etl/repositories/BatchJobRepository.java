package it.almaviva.mic.etl.repositories;

import java.math.BigDecimal;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import it.almaviva.mic.etl.entities.ade.BatchJob;

public interface BatchJobRepository extends JpaRepository<BatchJob, BigDecimal> 
{
	Optional<BatchJob> findTopByOrderByAvvioTsDesc();
}
