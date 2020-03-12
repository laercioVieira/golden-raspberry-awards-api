package br.com.laersondev.goldenraspberryawardsapi.rest.api;

import java.io.Serializable;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

public class ResponseStatus implements Serializable {
	private static final long serialVersionUID = 1L;
	private final Response.Status statusCode;
	private final Object data;
	private final String message;
	private final Throwable error;

	public ResponseStatus(final Response.Status statusCode, final Object data, final String message,
			final Throwable error) {
		this.statusCode = statusCode;
		this.data = data;
		this.message = message;
		this.error = error;
	}

	public ResponseStatus(final Response.Status statusCode, final String message) {
		this(statusCode, null, message, null);
	}

	public ResponseStatus(final Response.Status statusCode) {
		this(statusCode, null, null, null);
	}

	public Object getData() {
		return this.data;
	}

	public Throwable getError() {
		return this.error;
	}

	public String getMessage() {
		return this.message;
	}

	public Response.Status getStatusCode() {
		return this.statusCode;
	}

	public static Response newServerError(final String message, final Throwable error) {
		return Response.status(Status.BAD_GATEWAY).entity(//
				new ResponseStatus(Status.BAD_GATEWAY, null, message, error)).build();
	}

	public static Response newClientError(final String message, final Throwable error) {
		return Response.status(Status.BAD_REQUEST).entity( //
				new ResponseStatus(Status.BAD_REQUEST, null, message, error)).build();
	}

	public static Response notFoundError(final String message, final Throwable error) {
		return Response.status(Status.NOT_FOUND).entity( //
				new ResponseStatus(Status.NOT_FOUND, null, message, error)).build();
	}

	public static Response notFoundError(final String message) {
		return notFoundError(message, null);
	}

	public static ResponseStatus created(final String message) {
		return new ResponseStatus(Status.CREATED, message);
	}

	public static ResponseStatus ok() {
		return new ResponseStatus(Status.OK, "SUCESS");
	}

	public static ResponseStatus ok(final String message) {
		return new ResponseStatus(Status.OK, message);
	}

	public static ResponseStatus noContent(final String message) {
		return new ResponseStatus(Status.NO_CONTENT, message);
	}
}