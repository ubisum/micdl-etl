package it.almaviva.mic.etl.exceptions;

import org.springframework.http.HttpStatus;

public class MicdlETLException extends RuntimeException
{ 
	private static final long serialVersionUID = -647021387043820310L;
	private HttpStatus status;
	
	public MicdlETLException(String message)
	{
		super(message);
	}
	
	public MicdlETLException(String message, HttpStatus status)
	{
		super(message);
		this.setStatus(status);
	}

	public HttpStatus getStatus() {
		return status;
	}

	public void setStatus(HttpStatus status) {
		this.status = status;
	}
	
	
}
