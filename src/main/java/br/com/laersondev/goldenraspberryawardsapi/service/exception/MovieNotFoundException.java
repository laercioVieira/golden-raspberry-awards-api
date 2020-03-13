package br.com.laersondev.goldenraspberryawardsapi.service.exception;

import java.text.MessageFormat;

import br.com.laersondev.goldenraspberryawardsapi.rest.util.ObjectNotFoundException;

public class MovieNotFoundException extends ObjectNotFoundException{

	private static final long serialVersionUID = 1L;
	private static final String MOVIE_WITH_ID_NOT_FOUND = "Movie with id: {0} not found.";

	public MovieNotFoundException(final int id) {
		super(MessageFormat.format(MOVIE_WITH_ID_NOT_FOUND, id));
	}

	public MovieNotFoundException(final Exception e) {
		super(e);
	}

	public MovieNotFoundException(final String message, final Exception e) {
		super(message, e);
	}

}
