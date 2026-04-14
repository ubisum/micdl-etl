package it.almaviva.mic.etl.services.ade;

import java.math.BigDecimal;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.almaviva.mic.etl.dao.GenericDAO;
import it.almaviva.mic.etl.dao.ade.AdeFabDAO;
import it.almaviva.mic.etl.dto.ParsingDTO;
import it.almaviva.mic.etl.entities.ade.AdeUnitaImmHist;
import it.almaviva.mic.etl.utils.MicDlEtlConsts;

@Service
public class AdeInternalLogicImpl implements AdeInternalLogic 
{
	@Autowired
	private AdeFabDAO fabDAO;
	
	@Autowired
	private GenericDAO genericDAO;
	
	private static final Logger logger = LoggerFactory.getLogger(AdeInternalLogicImpl.class);
	
	@Override
	public void inserisciUnitaImmobiliari(BigDecimal idBatch, ParsingDTO parsingResult,
			                              List<AdeUnitaImmHist> listaUnita) 
	
	{
		
		logger.info("Richiesta di salvataggio su tabella di staging delle unita' immobiliari...");
		Integer numeroRecordInseriti = fabDAO.insertUnitaImm(listaUnita, idBatch);
		parsingResult.setRecordInseriti(parsingResult.getRecordInseriti() != null ? 
				                        parsingResult.getRecordInseriti() + numeroRecordInseriti : numeroRecordInseriti);
		
		logger.info("Inseriti {} record sulla tabella di staging", numeroRecordInseriti);
		
		logger.info("Esecuzione stored procedure per tabella unita' immobiliari...");
		genericDAO.eseguiStoredProcedure(MicDlEtlConsts.ADE_UNITA_IMM_SP);
		logger.info("Esecuzione stored procedure {} terminata", MicDlEtlConsts.ADE_UNITA_IMM_SP);
	}

}
