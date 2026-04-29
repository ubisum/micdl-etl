package it.almaviva.mic.etl.dao.ade;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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

import it.almaviva.mic.etl.converters.ade.AdeConverter;
import it.almaviva.mic.etl.dto.ade.soggetti.ProprietarioDTO;
import it.almaviva.mic.etl.entities.ade.ProprietarioHist;
import it.almaviva.mic.etl.exceptions.MicdlETLException;
import it.almaviva.mic.etl.utils.MicDlEtlConsts;
import it.almaviva.mic.etl.utils.MicdlEtlUtils;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Component
public class AdeSogDAOImpl implements AdeSogDAO 
{
	@PersistenceContext
	 private EntityManager entityManager;
	 
	 @Value("${spring.jpa.properties.hibernate.jdbc.batch_size}")
	 private String batchSize;
	
	 private static final Logger logger = LoggerFactory.getLogger(AdeSogDAOImpl.class);
	 
	@Override
	public Integer inserisciProprietari(List<ProprietarioDTO> proprietari, BigDecimal idBatch) 
	{
		logger.info("Richiesta di inserimento dei proprietari nella tabella di staging...");
		
		if(CollectionUtils.isEmpty(proprietari))
		{
			logger.info("Nessun proprietario da inserire nella lista ricevuta");
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
			logger.info("Sono presenti {} proprietari da inserire nella tabella di staging", proprietari.size());
			
			logger.info("Creazione connessione verso il DB...");
			Session session = entityManager.unwrap(Session.class);
			Connection conn = session.doReturningWork(c -> c);
			
			logger.info("Lettura del codice SQL per la creazione della tabella temporanea...");
			String sqlTabellaTemporanea = MicdlEtlUtils.readContentFromFile(MicDlEtlConsts.PROPRIETARIO_HIST_STAGING);
			if(StringUtils.isEmpty(sqlTabellaTemporanea))
			{
				logger.info("Impossibile leggere il codice per la creazione della tabella di staging");
				throw new MicdlETLException("Impossibile leggere codice per la creazione della tabella di staging", 
						                    HttpStatus.INTERNAL_SERVER_ERROR);
			}
			
			logger.info("Rimozione della tabella temporanea (se presente)...");
			Statement createStagingStmt = conn.createStatement();
			createStagingStmt.execute("DROP TEMPORARY TABLE IF EXISTS PROPRIETARIO_HIST_STAGING");
			
			logger.info("Creazione tabella temporanea...");
			createStagingStmt.executeUpdate(sqlTabellaTemporanea);
			
			logger.info("Lettura codice SQL per l'inserimento dei proprietari nella tabella di staging...");
			String sqlInserimentoIndirizzi = MicdlEtlUtils.readContentFromFile(MicDlEtlConsts.PROPRIETARIO_HIST_STAGING_INSERT);
			if(StringUtils.isEmpty(sqlInserimentoIndirizzi))
			{
				logger.info("Impossibile leggere il codice per l'inserimento dei proprietari nella tabella di staging");
				throw new MicdlETLException("Impossibile leggere il codice per l'inserimento dei proprietari nella tabella di staging", 
						                    HttpStatus.INTERNAL_SERVER_ERROR);
			}
			
			/* creazione del prepared statement */
			PreparedStatement inserimentoStagingPs = conn.prepareStatement(sqlInserimentoIndirizzi);
			
			/* contatore dei record */
			int counter = 0;
			
			/* iterazione sui record */
			for(ProprietarioDTO ph : proprietari)
			{
				/* conversione */
				ProprietarioHist entity = AdeConverter.convertProprietarioFromDTO(ph);
				
				/* rimepimento coi parametri */
				popolamentoProprietari(inserimentoStagingPs, ph, idBatch);
				
				/* aggiunta al batch */
				inserimentoStagingPs.addBatch();
				
				/* controllo del raggiungimento del numero massimo di elementi per batch */
				if(++counter % Integer.valueOf(batchSize) == 0)
					inserimentoStagingPs.executeBatch();
			}
			
			/* esecuzione del batch, se non avvenuto nel ciclo */
			inserimentoStagingPs.executeBatch();
			
			logger.info("Inserimento terminato");
			
			/* verifica del numero dei record effettivamente scritti */
			logger.info("Verifica dei record effettivamente scritti sulla tabella temporanea...");
			
			String sqlCountRecords = "SELECT COUNT(*) FROM PROPRIETARIO_HIST_STAGING";
			Statement countRecords = conn.createStatement();
			
			ResultSet result = countRecords.executeQuery(sqlCountRecords);
			Integer numeroRecordScritti = result.next() ? result.getInt(1) : 0;
			
			logger.info("Indirizzi effettivamente inseriti sulla tabella di staging: {}", numeroRecordScritti);
			
			logger.info("Terminato inserimento indirizzi in tabella di staging");
			
			return numeroRecordScritti;

		}
		
		catch(Throwable ex)
		{
			logger.info("Si e' verificato un errore durante l'inserimento degli indirizzi", ex);
			throw new MicdlETLException("Si e' verificato un errore durante l'inserimento degli indirizzi", 
					                    HttpStatus.INTERNAL_SERVER_ERROR);   
		}
		
	}

	/* preparazione inserimento dei proprietari */
	private void popolamentoProprietari(PreparedStatement ps, ProprietarioDTO dto, BigDecimal idBatch) throws SQLException
	{
		/* iterazione sui prprietari */
		int counter = 1;
		
		/* conversione */
		ProprietarioHist hist = AdeConverter.convertProprietarioFromDTO(dto);
		
		/* inserimento parametri */
		ps.setString(counter++, hist.getCodiceComune());
		ps.setString(counter++, hist.getSezione());
		ps.setString(counter++, hist.getIdSoggetto());
		ps.setString(counter++, hist.getTipoRecord());
		ps.setString(counter++, hist.getCodiceFiscale());
		ps.setString(counter++, hist.getCognome());
		ps.setString(counter++, hist.getNome());
		ps.setString(counter++, hist.getSesso());
		ps.setObject(counter++, hist.getDataNascita());
		
		ps.setString(counter++, hist.getLuogoNascita());
		ps.setString(counter++, hist.getAltreInfo());
		ps.setString(counter++, hist.getDenominazione());
		ps.setString(counter++, hist.getSede());
		ps.setString(counter++, hist.getHash());
		
		ps.setBigDecimal(counter++, idBatch);
		
		if (counter != 16) 
	    {
	    	logger.info("Numero parametri errato nell'inserimento "
	    			  + "del proprietario nel Prepared Statement");
	        throw new MicdlETLException("Numero parametri errato nell'inserimento "
	    			                  + "del proprietario nel Prepared Statement", HttpStatus.INTERNAL_SERVER_ERROR);
	    }
	
	}
}
