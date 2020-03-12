package br.com.laersondev.goldenraspberryawardsapi.service.exception;

/**
 * ServiceException
 */
public class ServiceException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public ServiceException(final Exception e) {
		super(e);
	}

	public ServiceException(final String message) {
		super(message);
	}

	public ServiceException(final String message, final Exception e) {
		super(message, e);
	}

}