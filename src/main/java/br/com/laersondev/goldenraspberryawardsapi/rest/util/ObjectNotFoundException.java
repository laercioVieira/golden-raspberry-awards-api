package br.com.laersondev.goldenraspberryawardsapi.rest.util;

import java.text.MessageFormat;

public class ObjectNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	private static final String OBJ_WITH_ID_NOT_FOUND = "Object with id: {0} not found.";

	public ObjectNotFoundException(final int id) {
		super(MessageFormat.format(OBJ_WITH_ID_NOT_FOUND, id));
	}

	public ObjectNotFoundException(final String message) {
		super(message);
	}

	public ObjectNotFoundException(final Exception e) {
		super(e);
	}

	public ObjectNotFoundException(final String message, final Exception e) {
		super(message, e);
	}

}
