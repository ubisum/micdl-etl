package it.almaviva.mic.etl.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import it.almaviva.mic.etl.dto.EsitoDTO;
import it.almaviva.mic.etl.parsers.ParserInterface;
import it.almaviva.mic.etl.utils.ParserServiceFactory;


@RestController
@RequestMapping("/etl") 
public class CaricamentoDatiController 
{
	private ParserServiceFactory parserServiceFactory;
	private static final Logger logger = LoggerFactory.getLogger(CaricamentoDatiController.class);
	
	
	
	public CaricamentoDatiController(ParserServiceFactory parserServiceFactory) {
		super();
		this.parserServiceFactory = parserServiceFactory;
	}

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
	
	@PostMapping("/uploadFile")
	public ResponseEntity<EsitoDTO> ploadFile(@RequestParam("file") MultipartFile file) 
	{
		String filename = file.getOriginalFilename();
		ParserInterface parser = parserServiceFactory.getParserInterface(filename.substring(filename.indexOf(".") + 1));
		
		try {
			Reader reader = new BufferedReader(
				    new InputStreamReader(file.getInputStream())
				);
			
			parser.parseFile(reader);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}
	
}
