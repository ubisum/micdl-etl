package it.almaviva.mic.etl.dao.ade;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;

import it.almaviva.mic.etl.entities.ade.AdeUnitaImmHist;
import it.almaviva.mic.etl.exceptions.MicdlETLException;
import it.almaviva.mic.etl.utils.MicDlEtlConsts;
import it.almaviva.mic.etl.utils.MicdlEtlUtils;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

public class AdeFabDAOImpl implements AdeFabDAO
{
	 @PersistenceContext
	 private EntityManager entityManager;
	 
	 @Value("micdl.batch.size")
	 private Integer batchSize;
	
	private static final Logger logger = LoggerFactory.getLogger(AdeFabDAOImpl.class);
	
	@Override
	public void insertUnitaImm(List<AdeUnitaImmHist> unitaImmobiliari) 
	{
		logger.info("Inizio salvataggio delle unita' immobiliari sulla tabella di staging...");
		
		if(CollectionUtils.isEmpty(unitaImmobiliari))
		{
			logger.info("Nessuna unita' immobiliare fornita, nessun inserimento verra' effettuato");
			return;
		}
		
		try
		{
			logger.info("Sono presenti {} unita' immobiliari da inserire", unitaImmobiliari.size());
			
			logger.info("Creazione connessione verso il DB...");
			Connection conn = entityManager.unwrap(Connection.class);
			
			logger.info("Lettura del codice SQL per la creazione della tabella temporanea...");
			String sqlTabellaTemporanea = MicdlEtlUtils.readContentFromFile(MicDlEtlConsts.ADE_UNITA_IMM_CREATE_STAGING);
			if(StringUtils.isEmpty(sqlTabellaTemporanea))
			{
				logger.info("Impossibile leggere il codice per la creazione della tabella di staging");
				throw new MicdlETLException("Impossibile leggere codice per la creazione della tabella di staging", 
						                    HttpStatus.INTERNAL_SERVER_ERROR);
			}
			
			logger.info("Creazione tabella temporanea...");
			Statement createStagingStmt = conn.createStatement();
			createStagingStmt.executeUpdate(sqlTabellaTemporanea);
			
			logger.info("Lettura codice SQL per l'inserimento dei dati nella tabella di staging...");
			String sqlInserimentoUnitaImmobiliari = MicdlEtlUtils.readContentFromFile(MicDlEtlConsts.ADE_UNITA_IMM_CREATE_STAGING_INSERT);
			if(StringUtils.isEmpty(sqlInserimentoUnitaImmobiliari))
			{
				logger.info("Impossibile leggere il codice per l'inserimento dei dati nella tabella di staging");
				throw new MicdlETLException("Impossibile leggere il codice per l'inserimento dei dati nella tabella di staging", 
						                    HttpStatus.INTERNAL_SERVER_ERROR);
			}
			
			/* creazione del prepared statement */
			PreparedStatement inserimentoStagingPs = conn.prepareStatement(sqlInserimentoUnitaImmobiliari);
			
			/* contatore dei record */
			int counter = 0;
			
			for(AdeUnitaImmHist unita : unitaImmobiliari)
			{
				/* riempimento dei parametri per l'i-simo record */
				prepareInsertUnitaImm(inserimentoStagingPs, unita);
				
				/* aggiunta al batch */
				inserimentoStagingPs.addBatch();
				
				/* controllo del raggiungimento del numero massimo di elementi per batch */
				if(++counter % batchSize == 0)
					inserimentoStagingPs.executeBatch();
			}
			
			/* esecuzione del batch, se non avvenuto nel ciclo */
			inserimentoStagingPs.executeBatch();
			
			logger.info("Inserimento terminato");
			
			/* verifica del numero dei record effettivamente scritti */
			String sqlCountRecords = "SELECT COUNT(*) FROM ADE_UNITA_IMM_HIST_STAGING";
			Statement countRecords = conn.createStatement();
			ResultSet result = countRecords.executeQuery(sqlCountRecords);
			
			logger.info("Record effettivamente inseriti sulla tabella di staging: {}",
					    result.next() ? result.getInt(1) : 0);
			
			logger.info("Terminato inserimento in tabella di staging");
		}
		
		catch(Throwable ex)
		{
			logger.info("Si e' verificato un errore durante l'inserimento delle unita' immobiliari", ex);
			throw new MicdlETLException("Si e' verificato un errore durante l'inserimento delle unita' immobiliari", 
					                    HttpStatus.INTERNAL_SERVER_ERROR);           
		}
		
		
	}
	
	/* metodo di popolamento della insert delle unita' immobiliari 
	 * nella tabella di staging */
	private void prepareInsertUnitaImm(PreparedStatement ps, AdeUnitaImmHist r) throws Exception {

		/* indice di puntamento */
	    int indice = 1;
	    
	    /* inserimento parametri */
	    ps.setString(indice++, r.getCodComune());
	    ps.setString(indice++, r.getSezione());
	    ps.setString(indice++, r.getIdImmCatasto());
	    ps.setString(indice++, r.getTipoCatasto());
	    ps.setString(indice++, r.getProgressivo());
	    ps.setString(indice++, r.getTipoRecord());
	    ps.setString(indice++, r.getZonaCensuaria());
	    ps.setString(indice++, r.getCategoria());
	    ps.setString(indice++, r.getClasse());
	    ps.setString(indice++, r.getConsistenza());
	    ps.setString(indice++, r.getSuperficie());
	    ps.setString(indice++, r.getRenditaLire());
	    ps.setString(indice++, r.getRenditaEuro());
	    ps.setString(indice++, r.getLotto());
	    ps.setString(indice++, r.getEdificio());
	    ps.setString(indice++, r.getScala());
	    ps.setString(indice++, r.getInterno1());
	    ps.setString(indice++, r.getInterno2());
	    ps.setString(indice++, r.getPiano1());
	    ps.setString(indice++, r.getPiano2());
	    ps.setString(indice++, r.getPiano3());
	    ps.setString(indice++, r.getPiano4());

	    ps.setString(indice++, r.getRegDataEfficiacia());
	    ps.setString(indice++, r.getRegDataregAtto());
	    ps.setString(indice++, r.getRegTipoNota());
	    ps.setString(indice++, r.getRegNumeroNota());
	    ps.setString(indice++, r.getRegProgressivoNota());

	    if (r.getRegAnnoNota() != null) {
	        ps.setInt(indice++, r.getRegAnnoNota());
	    } else {
	        ps.setNull(indice++, java.sql.Types.INTEGER);
	    }

	    ps.setString(indice++, r.getConcDataEfficacia());
	    ps.setString(indice++, r.getConcDataRegAtto());
	    ps.setString(indice++, r.getConcTipoNota());
	    ps.setString(indice++, r.getConcNumeroNota());
	    ps.setString(indice++, r.getConcProgressivoNota());

	    if (r.getConcAnnoNota() != null) {
	        ps.setInt(indice++, r.getConcAnnoNota());
	    } else {
	        ps.setNull(indice++, java.sql.Types.INTEGER);
	    }

	    ps.setString(indice++, r.getConcPartita());
	    ps.setString(indice++, r.getConcAnnotazione());
	    ps.setString(indice++, r.getConcIdMutIniz());
	    ps.setString(indice++, r.getConcIdMutFin());
	    ps.setString(indice++, r.getConcProtocolloNotifica());
	    ps.setString(indice++, r.getConcDataNotifica());
	    ps.setString(indice++, r.getConcCdAttoGenerante());
	    ps.setString(indice++, r.getConcDescrAttoGenerante());
	    ps.setString(indice++, r.getConcCdAttoConclusivo());
	    ps.setString(indice++, r.getConcDescrAttoConclusivo());
	    ps.setString(indice++, r.getConcFlagClassamento());

	    ps.setString(indice++, r.getHash());

	    /* safety check */
	    if (indice != 47) 
	    {
	    	logger.info("Numero parametri errato: {}", indice - 1);
	        throw new MicdlETLException("Numero parametri errato: " + (indice - 1), HttpStatus.INTERNAL_SERVER_ERROR);
	    }
	}


}
