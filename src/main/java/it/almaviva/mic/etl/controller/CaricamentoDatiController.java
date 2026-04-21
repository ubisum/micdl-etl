package it.almaviva.mic.etl.controller;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Reader;
import java.math.BigDecimal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import it.almaviva.mic.etl.dto.EsitoDTO;
import it.almaviva.mic.etl.dto.ParsingDTO;
import it.almaviva.mic.etl.enums.AdeEsitoBatchJob;
import it.almaviva.mic.etl.exceptions.MicdlETLException;
import it.almaviva.mic.etl.services.BatchJobService;
import it.almaviva.mic.etl.services.MicDllEtlService;
import it.almaviva.mic.etl.services.ServiceFactory;
import it.almaviva.mic.etl.utils.MicdlEtlUtils;


@RestController
@RequestMapping("/etl") 
public class CaricamentoDatiController 
{
	private ServiceFactory serviceFactory;
	private static final Logger logger = LoggerFactory.getLogger(CaricamentoDatiController.class);
	
	@Autowired
	private BatchJobService batchService;
	
	public CaricamentoDatiController(ServiceFactory serviceFactory) {
		super();
		this.serviceFactory = serviceFactory;
	}

	@GetMapping("/test")
	public ResponseEntity<EsitoDTO> testConnessione()
	{
		logger.info("Inizio procedura");
		
		EsitoDTO esito = new EsitoDTO();
		esito.setCodice(200);
		esito.setMessaggio("Tutto a posto");
		
		logger.debug("Creazione esito");
		logger.error("Fine esecuzione");
		return ResponseEntity.ok().body(esito);
	}
	
	@PostMapping("/uploadFile")
	public ResponseEntity<EsitoDTO> uploadFile(@RequestParam("file") MultipartFile file) 
	{
		/* ordine di caricamento dei flussi:
		   FAB
		   SOG
		   TIT
		 */
		
		logger.info("Invocato servizio di caricamento file...");
		
		logger.info("Estrazione del nome file...");
		String filename = file.getOriginalFilename();
		
		/* esito job */
		AdeEsitoBatchJob esitoJob = null;
		
		/* ID batch */
		BigDecimal idBatch = null;
		
		/* esito elaborazione */
		EsitoDTO esito = new EsitoDTO();
		
		logger.info("Rilevato file con nome {}...", filename);
		
		if(!filename.matches("^[a-zA-Z0-9]+\\.[a-zA-Z]{3}$"))
		{
			logger.info("Il nome del file ricevuto non rispetta lo standard previsto");
			throw new MicdlETLException("Il nome del file ricevuto non rispetta lo standard previsto", HttpStatus.BAD_REQUEST);
		}
		
		logger.info("Ricerca del service associat...");
		MicDllEtlService service = serviceFactory.getService(filename.substring(filename.indexOf(".") + 1));
		
		if(service == null)
			throw new MicdlETLException("Nessun service associato al tipo di file fornito", HttpStatus.BAD_REQUEST);
		
		try 
		{
			/* inizio calcolo tempo di computazione */
			long start = System.nanoTime();
			
			logger.info("Preparazione alla lettura del file...");
			Reader reader = new BufferedReader(new InputStreamReader(file.getInputStream()));
			
			logger.info("Inserimento batch job...");
			idBatch = batchService.insertBatchJob(filename, filename.substring(filename.length() - 3).toUpperCase());
			
			logger.info("Inizio parsing del file...");
			ParsingDTO result = service.parseAndStore(reader, filename, idBatch);
			
			/* termine calcolo di computazione */
			long end = System.nanoTime();
			
			logger.info("Terminato parsing del file e salvataggio dati nel tempo {}", MicdlEtlUtils.misurazioneTempoEsecuzione(start, end));
			
			/* preparazione dell'aggiornamento del batch job */
			esitoJob = AdeEsitoBatchJob.ESITO_OK;
			
			logger.info("Creazione risposta...");
			result.setCodice(HttpStatus.OK.value());
			
			return ResponseEntity.ok(result);
		} 
		
		catch(MicdlETLException micex)
		{
			logger.info("Si e' verificata un'eccezione", micex);
			
			esito.setCodice(micex.getStatus().value());
			esito.setMessaggio(micex.getMessage());
			
			/* esito job negativo */
			esitoJob = AdeEsitoBatchJob.ESITO_KO;
			
			return ResponseEntity.status(micex.getStatus()).body(esito);
			
		}
		
		catch (Throwable ex) 
		{
			logger.info("Si e' verificata un'eccezione interna", ex);
			
			esito.setCodice(HttpStatus.INTERNAL_SERVER_ERROR.value());
			esito.setMessaggio("Si e' verificata un'eccezione interna");
			
			/* esito negativo */
			esitoJob = AdeEsitoBatchJob.ESITO_KO;
			
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(esito);
		}
		
		finally
		{
			try
			{
				logger.info("Aggiornamento batch job...");
				batchService.updateBatchJob(idBatch, esitoJob);
			}
			
			catch(Throwable ex)
			{
				logger.info("Si e' verificata un'eccezione durante l'aggiornamento del batch job", ex);
				esito.setCodice(HttpStatus.INTERNAL_SERVER_ERROR.value());
				esito.setMessaggio("Si e' verificata un'eccezione interna");
				
			}
		}
		
	}
	
}
