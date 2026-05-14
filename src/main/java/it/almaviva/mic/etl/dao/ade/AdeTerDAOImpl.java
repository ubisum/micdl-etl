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
import it.almaviva.mic.etl.dto.ade.terreni.DeduzioneParticellaDTO;
import it.almaviva.mic.etl.dto.ade.terreni.RiservaParticellaDTO;
import it.almaviva.mic.etl.dto.ade.terreni.TerrenoTipoRecord1DTO;
import it.almaviva.mic.etl.dto.ade.terreni.TerrenoTipoRecord2DTO;
import it.almaviva.mic.etl.dto.ade.terreni.TerrenoTipoRecord3DTO;
import it.almaviva.mic.etl.entities.ade.AdeDeduzioneTerHist;
import it.almaviva.mic.etl.entities.ade.AdeParticellaHist;
import it.almaviva.mic.etl.entities.ade.AdeRiservaTerHist;
import it.almaviva.mic.etl.exceptions.MicdlETLException;
import it.almaviva.mic.etl.utils.MicDlEtlConsts;
import it.almaviva.mic.etl.utils.MicdlEtlUtils;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Component
public class AdeTerDAOImpl implements AdeTerDAO 
{
	@PersistenceContext
	private EntityManager entityManager;
	 
	@Value("${spring.jpa.properties.hibernate.jdbc.batch_size}")
	private String batchSize;
	
	private static final Logger logger = LoggerFactory.getLogger(AdeTerDAOImpl.class);
	
	@Override
	public Integer insertRiserve(List<TerrenoTipoRecord3DTO> listaRiserve, BigDecimal idBatch) 
	{
		logger.info("Richiesta di inserimento delle riserve nella tabella di staging...");
		
		if(CollectionUtils.isEmpty(listaRiserve))
		{
			logger.info("Nessuna riserva fornita, nessun inserimento verra' effettuato");

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
			Integer recordPrevisti = listaRiserve.stream().mapToInt(ind -> CollectionUtils.isNotEmpty(ind.getListaRiserve()) ? 
					                 ind.getListaRiserve().size() : 0).sum();
			
			logger.info("Sono presenti {} riserve da inserire nella tabella di staging", recordPrevisti);
			
			if(recordPrevisti == 0)
				return 0;
			
			logger.info("Creazione connessione verso il DB...");
			Session session = entityManager.unwrap(Session.class);
			Connection conn = session.doReturningWork(c -> c);
			
			logger.info("Lettura del codice SQL per la creazione della tabella temporanea...");
			String sqlTabellaTemporanea = MicdlEtlUtils.readContentFromFile(MicDlEtlConsts.ADE_RISERVA_TER_STAGING);
			if(StringUtils.isEmpty(sqlTabellaTemporanea))
			{
				logger.info("Impossibile leggere il codice per la creazione della tabella di staging");
				throw new MicdlETLException("Impossibile leggere codice per la creazione della tabella di staging", 
						                    HttpStatus.INTERNAL_SERVER_ERROR);
			}
			
			logger.info("Rimozione della tabella temporanea (se presente)...");
			Statement createStagingStmt = conn.createStatement();
			createStagingStmt.execute("DROP TEMPORARY TABLE IF EXISTS ADE_RISERVA_TER_STAGING");
			
			logger.info("Creazione tabella temporanea...");
			createStagingStmt.executeUpdate(sqlTabellaTemporanea);
			
			logger.info("Lettura codice SQL per l'inserimento dei dati nella tabella di staging...");
			String sqlInserimentoRiserve = MicdlEtlUtils.readContentFromFile(MicDlEtlConsts.ADE_RISERVA_TER_STAGING_INSERT);
			if(StringUtils.isEmpty(sqlInserimentoRiserve))
			{
				logger.info("Impossibile leggere il codice per l'inserimento dei dati nella tabella di staging");
				throw new MicdlETLException("Impossibile leggere il codice per l'inserimento dei dati nella tabella di staging", 
						                    HttpStatus.INTERNAL_SERVER_ERROR);
			}
			
			/* creazione del prepared statement */
			PreparedStatement inserimentoStagingPs = conn.prepareStatement(sqlInserimentoRiserve);
			
			/* contatore dei record */
			int counter = 0;
			
			for(TerrenoTipoRecord3DTO terreno : listaRiserve)
			{
				if(CollectionUtils.isEmpty(terreno.getListaRiserve()))
					continue;
				
				/* incremento sequenza */
				int sequenza = 1;
				
				for(RiservaParticellaDTO riserva : terreno.getListaRiserve())
				{

					/* riempimento dei parametri per l'i-simo record */
					prepareInsertRiserve(inserimentoStagingPs, riserva, terreno.getCodComune(), terreno.getSezione(), 
							             terreno.getIdImmCatasto(), terreno.getTipoCatasto(), sequenza++, idBatch);;
					
					/* aggiunta al batch */
					inserimentoStagingPs.addBatch();
					
					/* controllo del raggiungimento del numero massimo di elementi per batch */
					if(++counter % Integer.valueOf(batchSize) == 0)
						inserimentoStagingPs.executeBatch();
				}
			}
			
			/* esecuzione del batch, se non avvenuto nel ciclo */
			inserimentoStagingPs.executeBatch();
			
			logger.info("Inserimento terminato");
			
			/* verifica del numero dei record effettivamente scritti */
			logger.info("Verifica dei record effettivamente scritti sulla tabella temporanea...");
			
			String sqlCountRecords = "SELECT COUNT(*) FROM ADE_RISERVA_TER_STAGING";
			Statement countRecords = conn.createStatement();
			
			ResultSet result = countRecords.executeQuery(sqlCountRecords);
			Integer numeroRecordScritti = result.next() ? result.getInt(1) : 0;
			
			logger.info("Deduzione effettivamente inserite sulla tabella di staging: {}", numeroRecordScritti);
			
			logger.info("Terminato inserimento deduzioni in tabella di staging");
			
			return numeroRecordScritti;
			
			
		}
		
		catch(Throwable ex)
		{
			logger.info("Si e' verificato un errore durante l'inserimento delle riserve", ex);
			throw new MicdlETLException("Si e' verificato un errore durante l'inserimento delle riserve", 
					                    HttpStatus.INTERNAL_SERVER_ERROR);   
		}
		
	}
	
	@Override
	public Integer insertDeduzioni(List<TerrenoTipoRecord2DTO> listaDeduzioni, BigDecimal idBatch) 
	{
		logger.info("Richiesta di inserimento delle deduzioni nella tabella di staging...");
		
		if(CollectionUtils.isEmpty(listaDeduzioni))
		{
			logger.info("Nessuna deduzione fornita, nessun inserimento verra' effettuato");
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
			logger.info("Sono presenti {} deduzioni da inserire", listaDeduzioni.size());
			
			logger.info("Creazione connessione verso il DB...");
			Session session = entityManager.unwrap(Session.class);
			Connection conn = session.doReturningWork(c -> c);
			
			logger.info("Lettura del codice SQL per la creazione della tabella temporanea...");
			String sqlTabellaTemporanea = MicdlEtlUtils.readContentFromFile(MicDlEtlConsts.ADE_DEDUZIONE_TER_STAGING);
			if(StringUtils.isEmpty(sqlTabellaTemporanea))
			{
				logger.info("Impossibile leggere il codice per la creazione della tabella di staging");
				throw new MicdlETLException("Impossibile leggere codice per la creazione della tabella di staging", 
						                    HttpStatus.INTERNAL_SERVER_ERROR);
			}
			
			logger.info("Rimozione della tabella temporanea (se presente)...");
			Statement createStagingStmt = conn.createStatement();
			createStagingStmt.execute("DROP TEMPORARY TABLE IF EXISTS ADE_DEDUZIONE_TER_STAGING");
			
			logger.info("Creazione tabella temporanea...");
			createStagingStmt.executeUpdate(sqlTabellaTemporanea);
			
			logger.info("Lettura codice SQL per l'inserimento dei dati nella tabella di staging...");
			String sqlInserimentoParticelle = MicdlEtlUtils.readContentFromFile(MicDlEtlConsts.ADE_DEDUZIONE_TER_STAGING_INSERT);
			if(StringUtils.isEmpty(sqlInserimentoParticelle))
			{
				logger.info("Impossibile leggere il codice per l'inserimento dei dati nella tabella di staging");
				throw new MicdlETLException("Impossibile leggere il codice per l'inserimento dei dati nella tabella di staging", 
						                    HttpStatus.INTERNAL_SERVER_ERROR);
			}
			
			/* creazione del prepared statement */
			PreparedStatement inserimentoStagingPs = conn.prepareStatement(sqlInserimentoParticelle);
			
			/* contatore dei record */
			int counter = 0;
			
			for(TerrenoTipoRecord2DTO terreno : listaDeduzioni)
			{
				if(CollectionUtils.isEmpty(terreno.getListaDeduzione()))
					continue;
				
				/* incremento sequenza */
				int sequenza = 1;
				
				for(DeduzioneParticellaDTO deduzione : terreno.getListaDeduzione())
				{

					/* riempimento dei parametri per l'i-simo record */
					prepareInsertDeduzioni(inserimentoStagingPs, deduzione, terreno.getCodComune(), terreno.getSezione(), terreno.getIdImmCatasto(), 
							               terreno.getTipoCatasto(), terreno.getTipoRecord(), sequenza++, idBatch);
					
					/* aggiunta al batch */
					inserimentoStagingPs.addBatch();
					
					/* controllo del raggiungimento del numero massimo di elementi per batch */
					if(++counter % Integer.valueOf(batchSize) == 0)
						inserimentoStagingPs.executeBatch();
				}
			}
			
			/* esecuzione del batch, se non avvenuto nel ciclo */
			inserimentoStagingPs.executeBatch();
			
			logger.info("Inserimento terminato");
			
			/* verifica del numero dei record effettivamente scritti */
			logger.info("Verifica dei record effettivamente scritti sulla tabella temporanea...");
			
			String sqlCountRecords = "SELECT COUNT(*) FROM ADE_DEDUZIONE_TER_STAGING";
			Statement countRecords = conn.createStatement();
			
			ResultSet result = countRecords.executeQuery(sqlCountRecords);
			Integer numeroRecordScritti = result.next() ? result.getInt(1) : 0;
			
			logger.info("Deduzione effettivamente inserite sulla tabella di staging: {}", numeroRecordScritti);
			
			logger.info("Terminato inserimento deduzioni in tabella di staging");
			
			return numeroRecordScritti;
		}
		
		catch(Throwable ex)
		{
			logger.info("Si e' verificato un errore durante l'inserimento delle deduzioni", ex);
			throw new MicdlETLException("Si e' verificato un errore durante l'inserimento delle deduzioni", 
					                    HttpStatus.INTERNAL_SERVER_ERROR);   
		}
		
	}
	
	@Override
	public Integer insertParticelle(List<TerrenoTipoRecord1DTO> listaTerreni, BigDecimal idBatch) 
	{
		logger.info("Richiesta di inserimento delle particelle nella tabella di staging...");
		
		if(CollectionUtils.isEmpty(listaTerreni))
		{
			logger.info("Nessun terreno fornito, nessun inserimento verra' effettuato");
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
			logger.info("Sono presenti {} particelle da inserire", listaTerreni.size());
			
			logger.info("Creazione connessione verso il DB...");
			Session session = entityManager.unwrap(Session.class);
			Connection conn = session.doReturningWork(c -> c);
			
			logger.info("Lettura del codice SQL per la creazione della tabella temporanea...");
			String sqlTabellaTemporanea = MicdlEtlUtils.readContentFromFile(MicDlEtlConsts.ADE_PARTICELLA_CREATE_STAGING);
			if(StringUtils.isEmpty(sqlTabellaTemporanea))
			{
				logger.info("Impossibile leggere il codice per la creazione della tabella di staging");
				throw new MicdlETLException("Impossibile leggere codice per la creazione della tabella di staging", 
						                    HttpStatus.INTERNAL_SERVER_ERROR);
			}
			
			logger.info("Rimozione della tabella temporanea (se presente)...");
			Statement createStagingStmt = conn.createStatement();
			createStagingStmt.execute("DROP TEMPORARY TABLE IF EXISTS ADE_PARTICELLA_HIST_STAGING");
			
			logger.info("Creazione tabella temporanea...");
			createStagingStmt.executeUpdate(sqlTabellaTemporanea);
			
			logger.info("Lettura codice SQL per l'inserimento dei dati nella tabella di staging...");
			String sqlInserimentoParticelle = MicdlEtlUtils.readContentFromFile(MicDlEtlConsts.ADE_PARTICELLA_CREATE_STAGING_INSERT);
			if(StringUtils.isEmpty(sqlInserimentoParticelle))
			{
				logger.info("Impossibile leggere il codice per l'inserimento dei dati nella tabella di staging");
				throw new MicdlETLException("Impossibile leggere il codice per l'inserimento dei dati nella tabella di staging", 
						                    HttpStatus.INTERNAL_SERVER_ERROR);
			}
			
			/* creazione del prepared statement */
			PreparedStatement inserimentoStagingPs = conn.prepareStatement(sqlInserimentoParticelle);
			
			/* contatore dei record */
			int counter = 0;
			
			for(TerrenoTipoRecord1DTO terreno : listaTerreni)
			{
				/* conversione DTO -> entita' */
				AdeParticellaHist particella = AdeConverter.convertParticellaFromDTO(terreno);
				
				/* riempimento dei parametri per l'i-simo record */
				prepareInsertParticelle(inserimentoStagingPs, particella, idBatch);
				
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
			
			String sqlCountRecords = "SELECT COUNT(*) FROM ADE_PARTICELLA_HIST_STAGING";
			Statement countRecords = conn.createStatement();
			
			ResultSet result = countRecords.executeQuery(sqlCountRecords);
			Integer numeroRecordScritti = result.next() ? result.getInt(1) : 0;
			
			logger.info("Particelle effettivamente inserite sulla tabella di staging: {}", numeroRecordScritti);
			
			logger.info("Terminato inserimento particelle in tabella di staging");
			
			return numeroRecordScritti;
		}
		
		catch(Throwable ex)
		{
			logger.info("Si e' verificato un errore durante l'inserimento delle particelle", ex);
			throw new MicdlETLException("Si e' verificato un errore durante l'inserimento delle particelle", 
					                    HttpStatus.INTERNAL_SERVER_ERROR);   
		}
		
	}
	
	/* -------------------------------- FUNZIONI DI UTILITA' ------------------------------------------------ */
	
	/* metodo per il popolamento della tabella di staging delle riserve */
	private void prepareInsertRiserve(PreparedStatement ps,
			                          RiservaParticellaDTO riserva,
			                          String codiceComune,
			                          String sezione,
			                          String idImmCatasto,
			                          String tipoCatasto,
			                          Integer sequenza,
			                          BigDecimal idBatch)throws SQLException
	{
		/* indice di puntamento */
	    int indice = 1;
	    
	    /* conversione DTO -> entita' */
	    AdeRiservaTerHist entita = AdeConverter.convertRiservaFromDTO(riserva);
	    
	    /* inserimento parametri */
	    ps.setString(indice++,codiceComune);
	    ps.setString(indice++,sezione);
	    ps.setString(indice++,idImmCatasto);
	    ps.setString(indice++,tipoCatasto);
	    ps.setString(indice++,entita.getCodiceRiserva());
	    ps.setString(indice++,entita.getPartitaIscrizioneRiserva());
	    
	    ps.setInt(indice++, sequenza);
	    
	    ps.setString(indice++,entita.getHash());
	    
	    ps.setBigDecimal(indice++, idBatch);
	    
	    /* safety check */
	    if (indice != 10) 
	    {
	    	logger.info("Numero parametri errato per riserva: {}", sequenza);
	        throw new MicdlETLException("Numero parametri errato per riserva:" + sequenza, HttpStatus.INTERNAL_SERVER_ERROR);
	    }
	}
	
	/* metodo per il popolamento della tabella di staging delle deduzioni */
	private void prepareInsertDeduzioni(PreparedStatement ps, 
			                            DeduzioneParticellaDTO deduzione,
										String codiceComune,
			                            String sezione,
			                            String idImmCatasto,
			                            String tipoCatasto,
			                            String tipoRecord,
			                            Integer sequenza,
			                            BigDecimal idBatch) throws SQLException
	{
		/* indice di puntamento */
	    int indice = 1;
	    
	    /* conversione DTO -> entita' */
	    AdeDeduzioneTerHist entita = AdeConverter.convertDeduzioneFromDTO(deduzione);
	    
	    /* inserimento parametri */
	    ps.setString(indice++, codiceComune);
	    ps.setString(indice++, sezione);
	    ps.setString(indice++, idImmCatasto);
	    ps.setString(indice++, tipoCatasto);
	    ps.setString(indice++, tipoRecord);
	    ps.setString(indice++, entita.getSimboloDeduzione());
	    
	    ps.setInt(indice++, sequenza);
	    
	    ps.setString(indice++, entita.getHash());
	    
	    ps.setBigDecimal(indice++, idBatch);
	    
	    /* safety check */
	    if (indice != 10) 
	    {
	    	logger.info("Numero parametri errato per deduzione: {}", sequenza);
	        throw new MicdlETLException("Numero parametri errato per deduzione:" + sequenza, HttpStatus.INTERNAL_SERVER_ERROR);
	    }
	}
	
	/* metodo di popolamento delle particelle nella tabella di staging */
	private void prepareInsertParticelle(PreparedStatement ps, AdeParticellaHist particella, BigDecimal idBatch) throws SQLException
	{
		/* indice di puntamento */
	    int indice = 1;
	    
	    /* inserimento parametri */
	    ps.setString(indice++, particella.getCodComune());
	    ps.setString(indice++, particella.getSezione());
	    ps.setString(indice++, particella.getIdImmCatasto());
	    ps.setString(indice++, particella.getTipoCatasto());
	    ps.setString(indice++, particella.getProgressivo());
	    ps.setString(indice++, particella.getTipoRecord());
	    ps.setString(indice++, particella.getFoglio());
	    ps.setString(indice++, particella.getNumero());
	    
	    if(particella.getDenominatore() != null)
	    	ps.setInt(indice++, particella.getDenominatore());
	    
	    else
	    	ps.setNull(indice++, java.sql.Types.INTEGER);
	    
	    ps.setString(indice++, particella.getSubalterno());
	    ps.setString(indice++, particella.getEdificialita());
	    ps.setString(indice++, particella.getQualita());
	    ps.setString(indice++, particella.getClasse());
	    ps.setString(indice++, particella.getEttari());
	    ps.setString(indice++, particella.getAre());
	    ps.setString(indice++, particella.getCentiare());
	    ps.setString(indice++, particella.getFlagReddito());
	    ps.setString(indice++, particella.getFlagPorzione());
	    ps.setString(indice++, particella.getFlagDeduzioni());
	    ps.setString(indice++, particella.getRedditoDominicaleLire());
	    ps.setString(indice++, particella.getRedditoAgrarioLire());
	    ps.setString(indice++, particella.getRedditoDominicaleEuro());
	    ps.setString(indice++, particella.getRedditoAgrarioEuro());
	    ps.setString(indice++, particella.getDataEfficaciaReg());
	    ps.setString(indice++, particella.getDataRegistrazioneAttiReg());
	    ps.setString(indice++, particella.getTipoNotaReg());
	    ps.setString(indice++, particella.getNumeroNotaReg());
	    ps.setString(indice++, particella.getProgressivoNotaReg());
	    
	    if(particella.getAnnoNotaReg() != null)
	    	ps.setInt(indice++, particella.getAnnoNotaReg());
	    
	    else
	    	ps.setNull(indice++, java.sql.Types.INTEGER);
	    
	    ps.setString(indice++, particella.getDataEfficaciaConcl());
	    ps.setString(indice++, particella.getDataRegistrazioneAttiConcl());
	    ps.setString(indice++, particella.getTipoNotaConcl());
	    ps.setString(indice++, particella.getNumeroNotaConcl());
	    ps.setString(indice++, particella.getProgressivoNotaConcl());
	    
	    if(particella.getAnnoNotaConcl() != null)
	    	ps.setInt(indice++, particella.getAnnoNotaConcl());
	    
	    else
	    	ps.setNull(indice++, java.sql.Types.INTEGER);
	   
	    ps.setString(indice++, particella.getPartita());
	    ps.setString(indice++, particella.getAnnotazione());
	    ps.setString(indice++, particella.getIdMutazioneIniziale());
	    ps.setString(indice++, particella.getIdMutazioneFinale());
	    ps.setString(indice++, particella.getCdCausaleAttoGenerante());
	    ps.setString(indice++, particella.getDescrizioneAttoGenerante());
	    ps.setString(indice++, particella.getCdcausaleAttoConclusivo());
	    ps.setString(indice++, particella.getDescrizioneAttoConclusivo());
	    ps.setString(indice++, particella.getHash());
	    
	    ps.setBigDecimal(indice++, idBatch);
	    
	}

	


}
