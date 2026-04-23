package it.almaviva.mic.etl.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.stereotype.Component;

import it.almaviva.mic.etl.entities.ade.BatchJob;
import it.almaviva.mic.etl.enums.AdeEsitoBatchJob;
import it.almaviva.mic.etl.exceptions.MicdlETLException;
import it.almaviva.mic.etl.repositories.BatchJobRepository;
import javax.sql.DataSource;
import jakarta.persistence.EntityManager;
import jakarta.persistence.ParameterMode;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.StoredProcedureQuery;


@Component
public class GenericdDAOImpl implements GenericDAO 
{
	@PersistenceContext
	private EntityManager entityManager;
	
	@Autowired
    private DataSource dataSource;
	
	@Autowired
	BatchJobRepository batchRepository;
	
	private static final Logger logger = LoggerFactory.getLogger(GenericdDAOImpl.class);
	
	@Override
	public void inserisciDettagliBatchJob(Map<Integer, List<String>> errori, BigDecimal idJob, String filename) 
	{
		logger.info("Inserimento dettagli del job con identificativo {}", idJob);
		
		if(errori == null || errori.size() == 0)
		{
			logger.info("Nessun errore presente, non verranno inseriti dettagli");
			return;
		}
		
		try
		{
			logger.info("Ricerca delle informazioni relative al batch job...");
			
			Optional<BatchJob> job = batchRepository.findById(idJob);
			if(job.isEmpty())
				throw new MicdlETLException("Nessun job presente con l'ID segnalato", HttpStatus.INTERNAL_SERVER_ERROR);
			
			/* definizione del prepared statement */
			String sql = "INSERT INTO asset_mgmt.batch_job_dettaglio "
					   + "(batch_id, raw_id, file_name, esito, error_message, processed_ts) "
					   + "VALUES(?, ?, ?, ?, ?, ?)";
			
			/* connessione */
			Connection connection = DataSourceUtils.getConnection(dataSource);
			
			/* creazione prepared statement */
			PreparedStatement ps = connection.prepareStatement(sql);
			
			/* popolamento */
			popolamentoDettagli(ps, errori, idJob, LocalDateTime.now(), filename);
			ps.addBatch();
			
			/* esecuzione */
			ps.executeBatch();
			
		}
		
		catch(MicdlETLException mee)
		{
			/* si rilancia l'eccezione verso il controller */
			throw new MicdlETLException(mee.getMessage(), mee.getStatus());
		}
		
		catch(Throwable ex)
		{
			logger.info("Si e' verificata un'eccezione durante l'aggiornamento del job", ex);
			throw new MicdlETLException("Si e' verificato un errore interno", HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
	}
	
	@Override
	public Integer eseguiStoreProcedureContaRecord(String procedure) 
	{
		logger.info("Accesso alla funzione di esecuzione delle stored procedure");
		if(StringUtils.isBlank(procedure))
		{
			logger.info("Nome della storeed procedure fornita e' pari a NULL");
			throw new MicdlETLException("Si e' verificato un errore interno", HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		logger.info("Richiesta esecuzione stored procedure {}", procedure);
		
		try
		{
			/* creazione stored procedure */
			StoredProcedureQuery query = entityManager.createStoredProcedureQuery(procedure);
			
			/* registrazione del parametro di output */
			query.registerStoredProcedureParameter(1, Integer.class, ParameterMode.OUT);
			
			/* esecuzione */
			query.execute();
			
			/* restituzione numero righe inserite */
			return (Integer)query.getOutputParameterValue(1);

		}
		
		catch(Throwable ex)
		{
			logger.info("Si e' verificato un errroe durante l'esecuzione della procedure {}", procedure, ex);
			throw new MicdlETLException("Si e' verificato un errore interno", HttpStatus.INTERNAL_SERVER_ERROR);
			
		}
	}
	
	@Override
	public void eseguiStoredProcedure(String procedure) 
	{
		logger.info("Accesso alla funzione di esecuzione delle stored procedure");
		if(StringUtils.isBlank(procedure))
		{
			logger.info("Nome della storeed procedure fornita e' pari a NULL");
			throw new MicdlETLException("Si e' verificato un errore interno", HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		logger.info("Richiesta esecuzione stored procedure {}", procedure);
		
		try
		{
			entityManager.createNativeQuery("CALL " + procedure + "()").executeUpdate();
		}
		
		catch(Throwable ex)
		{
			logger.info("Si e' verificato un errroe durante l'esecuzione della procedure {}", procedure, ex);
			throw new MicdlETLException("Si e' verificato un errore interno", HttpStatus.INTERNAL_SERVER_ERROR);
			
		}

	}

	@Override
	public BigDecimal insertBatchJob(String fonte, String tipoCarico) 
	{
		logger.info("Inserimento job {}...", fonte);
		try
		{
			logger.info("Creazione dell'entita'...");
			BatchJob job = new BatchJob();
			job.setFonte(fonte);
			job.setTipoCarico(tipoCarico);
			job.setAvvioTs(LocalDateTime.now());
			
			logger.info("Inserimento sul database...");
			BatchJob insertedRow =  batchRepository.save(job);
			
			logger.info("Inserito job con identificativo {}", insertedRow.getBatchId());
			
			return insertedRow.getBatchId();
		}
		
		catch(Throwable ex)
		{
			logger.info("Si e' verificato un errore durante l'inserimento del job {}", fonte, ex);
			throw new MicdlETLException("Si e' verificato un errore interno", HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		
		
	}

	@Override
	public void updateBatchJob(BigDecimal idJob, AdeEsitoBatchJob esito) 
	{
		logger.info("Aggiornamento job con identificativo {}...", idJob);
		
		try
		{
			if(idJob == null)
			{
				logger.info("Identificativo del job non valido");
				throw new MicdlETLException("Si e' verificato un errore interno", HttpStatus.INTERNAL_SERVER_ERROR);
			}
			
			logger.info("Ricerca del job sul database...");
			Optional<BatchJob> job = batchRepository.findById(idJob);
			
			if(job.isEmpty())
			{
				logger.info("Impossibile trovare un job con l'identificativo specificato");
				throw new MicdlETLException("Si e' verificato un errore interno", HttpStatus.INTERNAL_SERVER_ERROR);
			}
			
			logger.info("Preparazione all'aggiornamento del job...");
			job.get().setEsito(esito.getEsito());
			job.get().setFineTs(LocalDateTime.now());
			
			logger.info("Salvataggio in corso...");
			batchRepository.save(job.get());
			
			logger.info("Salvataggio job completatao");
		}
		
		catch(MicdlETLException mee)
		{
			/* si rilancia l'eccezione verso il controller */
			throw new MicdlETLException(mee.getMessage(), mee.getStatus());
		}
		
		catch(Throwable ex)
		{
			logger.info("Si e' verificata un'eccezione durante l'aggiornamento del job", ex);
			throw new MicdlETLException("Si e' verificato un errore interno", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	/* metodo di popolamento della insert degli indirizzi */
	private void popolamentoDettagli(PreparedStatement ps, Map<Integer, List<String>> validationErrors, BigDecimal idBatch, LocalDateTime ldt, String filename) throws SQLException
	{
		/* iterazione sugli errori */
		for(Integer row : validationErrors.keySet())
		{
			/* elenco violazioni */
			String violations = String.join(" - ", validationErrors.get(row));
			
			/* riempimento */
			ps.setBigDecimal(1, idBatch);
			ps.setString(2, row.toString());
			ps.setString(3, filename);
			ps.setString(4, "KO");
			ps.setString(5, violations);
			ps.setObject(6, ldt);
			
		}
	}

}
