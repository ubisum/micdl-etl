package it.almaviva.mic.etl.dao.ade;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Statement;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import it.almaviva.mic.etl.dto.ade.titolarita.TitolaritaDTO;
import it.almaviva.mic.etl.exceptions.MicdlETLException;
import it.almaviva.mic.etl.utils.MicDlEtlConsts;
import it.almaviva.mic.etl.utils.MicdlEtlUtils;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Component
public class AdeTitDAOImpl implements AdeTitDAO 
{
	@PersistenceContext
	private EntityManager entityManager;
	 
	@Value("${spring.jpa.properties.hibernate.jdbc.batch_size}")
	private String batchSize;
		
	private static final Logger logger = LoggerFactory.getLogger(AdeTitDAOImpl.class);
	
	@Override
	public Integer insertTitolarita(List<TitolaritaDTO> listaTitolarita, BigDecimal batchId) 
	{
		logger.info("Richiesta di inserimento delle titolarita' nella tabella di staging...");
		
		if(CollectionUtils.isEmpty(listaTitolarita))
		{
			logger.info("Nessuna titolarita' da inserire nella lista ricevuta");
			return 0;
		}
		
		/* grandezza batch */
		Integer maxNumRecords = null;
		if(StringUtils.isBlank(batchSize))
		{
			logger.info("Nessuna property indicante la misura del batch trovata. Si imposta la grandezza massima di default a 1000");
			maxNumRecords = Integer.valueOf(1000);
		}
		
		else
			maxNumRecords = Integer.valueOf(batchSize);
		
		try
		{
			logger.info("Sono presenti {} titolarita' da inserire nella tabella di staging", listaTitolarita.size());
			
			logger.info("Creazione connessione verso il DB...");
			Session session = entityManager.unwrap(Session.class);
			Connection conn = session.doReturningWork(c -> c);
			
			logger.info("Lettura del codice SQL per la creazione della tabella temporanea...");
			String sqlTabellaTemporanea = MicdlEtlUtils.readContentFromFile(MicDlEtlConsts.ADE_TITOLARITA_HIST_STAGING);
			if(StringUtils.isEmpty(sqlTabellaTemporanea))
			{
				logger.info("Impossibile leggere il codice per la creazione della tabella di staging");
				throw new MicdlETLException("Impossibile leggere codice per la creazione della tabella di staging", 
						                    HttpStatus.INTERNAL_SERVER_ERROR);
			}
			
			logger.info("Rimozione della tabella temporanea (se presente)...");
			Statement createStagingStmt = conn.createStatement();
			createStagingStmt.execute("DROP TEMPORARY TABLE IF EXISTS ADE_TITOLARITA_HIST_STAGING");
			
			logger.info("Creazione tabella temporanea...");
			createStagingStmt.executeUpdate(sqlTabellaTemporanea);
		}
		
		catch(Throwable ex)
		{
			logger.info("Si e' verificato un errore durante l'inserimento degli indirizzi", ex);
			throw new MicdlETLException("Si e' verificato un errore durante l'inserimento degli indirizzi", 
					                    HttpStatus.INTERNAL_SERVER_ERROR);   
		}
		
		return null;
	}

}
