package it.almaviva.mic.etl.controller;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Reader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import it.almaviva.mic.etl.exceptions.MicdlETLException;
import it.almaviva.mic.etl.services.MicDllEtlService;
import it.almaviva.mic.etl.services.ServiceFactory;


@RestController
@RequestMapping("/etl") 
public class CaricamentoDatiController 
{
	private ServiceFactory serviceFactory;
	private static final Logger logger = LoggerFactory.getLogger(CaricamentoDatiController.class);
	
	
	
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
		logger.info("Invocato servizio di caricamento file...");
		
		logger.info("Estrazione del nome file...");
		String filename = file.getOriginalFilename();
		
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
			logger.info("Preparazione alla lettura del file...");
			Reader reader = new BufferedReader(new InputStreamReader(file.getInputStream()));
			
			logger.info("Inizio parsing del file...");
			ParsingDTO result =  service.parseAndStore(reader);
			
			logger.info("Creazione risposta...");
			result.setCodice(HttpStatus.OK.value());
			
			return ResponseEntity.ok(result);
		} 
		
		catch(MicdlETLException micex)
		{
			logger.info("Si e' verificata un'eccezione", micex);
			
			EsitoDTO esito = new EsitoDTO();
			esito.setCodice(micex.getStatus().value());
			esito.setMessaggio(micex.getMessage());
			
			return ResponseEntity.status(micex.getStatus()).body(esito);
			
		}
		
		catch (Throwable ex) 
		{
			logger.info("Si e' verificata un'eccezione interna", ex);
			
			EsitoDTO esito = new EsitoDTO();
			esito.setCodice(HttpStatus.INTERNAL_SERVER_ERROR.value());
			esito.setMessaggio("Si e' verificata un'eccezione interna");
			
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(esito);
		}
		
		return null;
	}
	
}
