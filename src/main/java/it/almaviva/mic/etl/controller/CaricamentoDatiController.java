package it.almaviva.mic.etl.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import it.almaviva.mic.etl.dto.EsitoDTO;

@RestController
@RequestMapping("/etl") 
public class CaricamentoDatiController 
{
	private static final Logger logger = LoggerFactory.getLogger(CaricamentoDatiController.class);
	
	@GetMapping("test")
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
}
