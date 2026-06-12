package it.almaviva.mic.etl.dao.ade;

import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import it.almaviva.mic.etl.converters.ade.AdeConverter;
import it.almaviva.mic.etl.dto.ParsingDTO;
import it.almaviva.mic.etl.dto.ade.titolarita.TitolaritaDTO;
import it.almaviva.mic.etl.entities.ade.AdeTitolaritaHist;
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
			
			logger.info("Lettura codice SQL per l'inserimento dei dati nella tabella di staging...");
			String sqlInserimentoTitolarita = MicdlEtlUtils.readContentFromFile(MicDlEtlConsts.ADE_TITOLARITA_HIST_STAGING_INSERT);
			if(StringUtils.isEmpty(sqlInserimentoTitolarita))
			{
				logger.info("Impossibile leggere il codice per l'inserimento dei dati nella tabella di staging");
				throw new MicdlETLException("Impossibile leggere il codice per l'inserimento dei dati nella tabella di staging", 
						                    HttpStatus.INTERNAL_SERVER_ERROR);
			}
			
			/* creazione del prepared statement */
			PreparedStatement inserimentoStagingPs = conn.prepareStatement(sqlInserimentoTitolarita);
			
			/* contatore dei record */
			int counter = 0;
			
			for(TitolaritaDTO titolarita : listaTitolarita)
			{
				/* riempimento dei parametri per l'i-simo record */
				popolaTitolarita(inserimentoStagingPs, titolarita, batchId);
				
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
			
			String sqlCountRecords = "SELECT COUNT(*) FROM ADE_TITOLARITA_HIST_STAGING";
			Statement countRecords = conn.createStatement();
			
			ResultSet result = countRecords.executeQuery(sqlCountRecords);
			Integer numeroRecordScritti = result.next() ? result.getInt(1) : 0;
			
			logger.info("Titolarita' effettivamente inserite sulla tabella di staging: {}", numeroRecordScritti);
			
			logger.info("Terminato inserimento Titolarita' in tabella di staging");
			
			return numeroRecordScritti;
		}
		
		catch(Throwable ex)
		{
			logger.info("Si e' verificato un errore durante l'inserimento degli indirizzi", ex);
			throw new MicdlETLException("Si e' verificato un errore durante l'inserimento degli indirizzi", 
					                    HttpStatus.INTERNAL_SERVER_ERROR);   
		}
	}
	
	/* popolamento staging */
	private void popolaTitolarita(PreparedStatement ps, TitolaritaDTO titolarita, BigDecimal batchId) throws SQLException
	{
		/* indice di puntamento */
	    int indice = 1;
	    
	    /* conversione DTO -> entita' */
	    AdeTitolaritaHist entita = AdeConverter.convertTitolaritaFromDTO(titolarita);
	    
	    ps.setInt(indice++, titolarita.getRowId());
	    
	    ps.setString(indice++, entita.getCodAmm());
	    ps.setString(indice++, entita.getSezione());
	    ps.setString(indice++, entita.getIdSoggetto());
	    ps.setString(indice++, entita.getIdTipoSoggetto());
	    ps.setString(indice++, entita.getIdImmCatasto());
	    ps.setString(indice++, entita.getIdTipoImmobile());
	    ps.setString(indice++, entita.getCodDiritto());
	    ps.setString(indice++, entita.getTitoloNonCodificato());
	    
	    if(entita.getQuotaNumeratore() != null)
	    	ps.setInt(indice++, entita.getQuotaNumeratore());
	    	
    	else
    		ps.setNull(indice++, java.sql.Types.INTEGER);
    	
	    if(entita.getQuotaDenominatore() != null)
	    	ps.setInt(indice++, entita.getQuotaNumeratore());
	    
	    else
    		ps.setNull(indice++, java.sql.Types.INTEGER);
	    
	    if(StringUtils.isNotBlank(entita.getRegime()) && StringUtils.isNotBlank(entita.getRegime().trim()))
	    		ps.setString(indice++, entita.getRegime().trim());
	    
	    else
	    	ps.setNull(indice++, java.sql.Types.VARCHAR);
	    
	    ps.setString(indice++, entita.getSoggettoRiferimento());
	    if(entita.getDataValiditaReg() != null)
	    	ps.setDate(indice++, java.sql.Date.valueOf(entita.getDataValiditaReg()));
	    
	    else
	    	ps.setNull(indice++, java.sql.Types.DATE);
	    
	    ps.setString(indice++, entita.getTipoNotaReg());
	    ps.setString(indice++, entita.getNumeroNotaReg());
	    ps.setString(indice++, entita.getProgressivoNotaReg());
	    ps.setString(indice++, entita.getAnnoNotaReg());
	    
	    if(entita.getDataRegistrazioneAttiReg() != null)
	    	ps.setDate(indice++, java.sql.Date.valueOf(entita.getDataRegistrazioneAttiReg()));
	    
	    else
	    	ps.setNull(indice++, java.sql.Types.DATE);
	    
	    ps.setString(indice++, entita.getPartitaReg());
	    
	    if(entita.getDataValiditaConcl() != null)
	    	ps.setDate(indice++, java.sql.Date.valueOf(entita.getDataValiditaConcl()));	
	    
	    else
	    	ps.setNull(indice++, java.sql.Types.DATE);
	    
	    ps.setString(indice++, entita.getTipoNotaConcl());
	    ps.setString(indice++, entita.getNumeroNotaConcl());
	    ps.setString(indice++, entita.getProgressivoNotaConcl());
	    ps.setString(indice++, entita.getAnnoNotaConcl());
	    
	    if(entita.getDataRegistrazioneAttiConcl() != null)
	    	ps.setDate(indice++, java.sql.Date.valueOf(entita.getDataRegistrazioneAttiConcl()));
	    
	    else
	    	ps.setNull(indice++, java.sql.Types.DATE);
	    
	    ps.setString(indice++, entita.getIdMutazioneIniz());
	    ps.setString(indice++, entita.getIdMutazioneFin());
	    ps.setString(indice++, entita.getIdTitolarita());
	    ps.setString(indice++, entita.getCdCausaleAttoGenerante());
	    ps.setString(indice++, entita.getDescrizioneAttoGenerante());
	    ps.setString(indice++, entita.getCdCausaleAttoConclusivo());
	    ps.setString(indice++, entita.getDescrizioneAttoConclusivo());
	    ps.setString(indice++, entita.getHash());
	    
	    ps.setBigDecimal(indice++, batchId);
	    
	    if (indice != 36) 
	    {
	    	logger.info("Numero parametri errato per titolarita'");
	        throw new MicdlETLException("Numero parametri errato per titolarita'", HttpStatus.INTERNAL_SERVER_ERROR);
	    }
	    
	}

	@Override
	public Integer executeSCD2Procedure(ParsingDTO result) {

	    logger.info("Invocazione procedura SCD2 per titolarita'...");

	    /* strutture dati */
	    CallableStatement stmt = null;
	    ResultSet rs = null;
	    
	    try 
	    {
	    	/* richiesta connessione */
	    	logger.info("Preparazione connessione...");
	        Session session = entityManager.unwrap(Session.class);
	        Connection connection = session.doReturningWork(c -> c);

	        /* preparazione invocazione stored procedure */
	        stmt = connection.prepareCall("CALL " + MicDlEtlConsts.ADE_TITOLARITA_SP + "(?)");

	        logger.info("Preparazione parametri...");
	        stmt.registerOutParameter(1, Types.INTEGER);

	        /* esecuzione procedure */
	        logger.info("Esecuzione stored procedure...");
	        boolean hasResultSet = stmt.execute();
	        Integer totaleInseriti = stmt.getInt(1);
	       
	        logger.info("Analisi presenza eventuali record non validi...");

	        if (hasResultSet) 
	        {
	        	logger.info("Rilevato ResultSet come risultato della procedure. "
	        			  + "Si procede all'analisi di eventuali record non validi");
	        	
	            rs = stmt.getResultSet();
	        }
	        
	        else
	        {
	        	logger.info("Nessun record non valido rilevato");
	        	return totaleInseriti;
	        }
	        
	        logger.info("Inizio analisi di eventuali record non validi...");
	        Map<Integer, List<String>> temporaryErrorList = new HashMap<>();
	        while(rs.next())
	        {
	        	/* estrazione indici e flag */
	        	Integer rowIndex = rs.getInt(1);
	        	Integer sogMissing = rs.getInt(2);
	        	Integer rifMissing = rs.getInt(3);
	        	Integer immMissing = rs.getInt(4);
                
                if(!temporaryErrorList.containsKey(rowIndex))
                	temporaryErrorList.put(rowIndex, new ArrayList<>());
                
                /* composizione messaggio d'errore */
                if(sogMissing == 1)
                	temporaryErrorList.get(rowIndex).add(MicDlEtlConsts.ERR_TIT_MISSING_ID_SOGGETTO);
                
                if(rifMissing == 1)
                	temporaryErrorList.get(rowIndex).add(MicDlEtlConsts.ERR_TIT_MISSING_SOGGETTO_RIF);
                
                if(immMissing == 1)
                	temporaryErrorList.get(rowIndex).add(MicDlEtlConsts.ERR_TIT_MISSING_ID_IMMOBILE);
	        }
	        	        
	        if(result.getReportRecord() != null)
	        	result.getReportRecord().putAll(temporaryErrorList);
	        
	        else
	        	result.setReportRecord(temporaryErrorList);
	        
	        logger.info("Terminata analisi. Record non validi trovati: {}", temporaryErrorList.size());
	        
	        return totaleInseriti;

	    } 
	    
	    catch (Throwable ex) 
	    {
	        logger.error("Errore durante esecuzione SCD2", ex);
	        throw new MicdlETLException("Errore interno SCD2", HttpStatus.INTERNAL_SERVER_ERROR);
	    }
	    
	    finally
	    {
	    	if (rs != null) 
	    	{
	            try 
	            {
	                rs.close();
	            } 
	            
	            catch (SQLException ex) 
	            {
	                logger.info("Errore chiusura ResultSet", ex);
	                throw new MicdlETLException("Si e' verificata un'eccezione interna", HttpStatus.INTERNAL_SERVER_ERROR);
	            }
	        }

	        if (stmt != null) 
	        {
	            try 
	            {
	                stmt.close();
	            } 
	            
	            catch (SQLException ex) 
	            {
	                logger.warn("Errore chiusura CallableStatement", ex);
	                throw new MicdlETLException("Si e' verificata un'eccezione interna", HttpStatus.INTERNAL_SERVER_ERROR);
	            }
	        }
	    }
	}

}
