package br.com.laersondev.goldenraspberryawardsapi.rest.util;


import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.jboss.logging.Logger;


@Provider
public class RestExceptionInterceptor implements ExceptionMapper<Exception> {

	private static final Logger LOG =  Logger.getLogger(RestExceptionInterceptor.class);

	public RestExceptionInterceptor() {
		super();
	}

	@Override
	public Response toResponse(Exception exception) {
		LOG.error(exception.getMessage(), exception);
		final ErrorResponse errorResponse = ErrorResponse.fromException(exception);
		return Response.status(errorResponse.getStatusCode(), errorResponse.getReasonPhrase())
				.entity(errorResponse)
				.type(MediaType.APPLICATION_JSON_TYPE)
				.build();
	}

}
