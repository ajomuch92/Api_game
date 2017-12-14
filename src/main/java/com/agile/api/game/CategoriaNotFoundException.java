package com.agile.api.game;

import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@SuppressWarnings("serial")
@ResponseStatus(HttpStatus.NO_CONTENT)
public class CategoriaNotFoundException extends RuntimeException{

	private static final Logger logger=Logger.getLogger(CategoriaNotFoundException.class);
	
	public CategoriaNotFoundException(int id) {
		super("Categoría con id= " + id + " no fue encontrada.");
		logger.error(super.getMessage());
	}

	public CategoriaNotFoundException(String categoria) {
		super("Categoría " + categoria + " no fue encontrada.");
	}

}
