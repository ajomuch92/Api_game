package com.agile.api.game;

import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@SuppressWarnings("serial")
@ResponseStatus(HttpStatus.NO_CONTENT)
public class JugadorNotFoundException extends RuntimeException{
	
	private static final Logger logger=Logger.getLogger(JugadorNotFoundException.class);
	
	public JugadorNotFoundException(int id) {
		super("Jugador con id = "+id+" no encontrado");
		logger.error(super.getMessage());
	}
	
	public JugadorNotFoundException(String nombre) {
		super("Jugador con nombre = "+nombre+" no encontrado");
		logger.error(super.getMessage());
	}
}
