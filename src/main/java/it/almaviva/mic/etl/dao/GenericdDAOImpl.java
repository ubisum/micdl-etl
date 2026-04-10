package it.almaviva.mic.etl.dao;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import it.almaviva.mic.etl.exceptions.MicdlETLException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Component
public class GenericdDAOImpl implements GenericDAO 
{
	@PersistenceContext
	private EntityManager entityManager;
	
	private static final Logger logger = LoggerFactory.getLogger(GenericdDAOImpl.class);
	
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
			logger.info("Si e' verificato un errroe durante l'esecuzione della procedure {}", procedure);
			throw new MicdlETLException("Si e' verificato un errore interno", HttpStatus.INTERNAL_SERVER_ERROR);
			
		}

	}

}
