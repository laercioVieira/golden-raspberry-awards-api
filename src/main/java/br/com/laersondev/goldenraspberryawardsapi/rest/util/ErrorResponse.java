package br.com.laersondev.goldenraspberryawardsapi.rest.util;

import java.io.PrintWriter;
import java.io.Serializable;
import java.io.StringWriter;
import java.util.Optional;

import javax.ws.rs.core.Response;

import org.eclipse.microprofile.openapi.annotations.media.Schema;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonRootName;

@JsonRootName("error")
@Schema(name = "Error")
public class ErrorResponse implements Serializable {
	private static final long serialVersionUID = 1L;
	private final int statusCode;
	private final String message;
	private final String details;
	@JsonIgnore
	private final Throwable error;

	public ErrorResponse(int statusCode, String message, String details, Throwable error) {
		super();
		this.statusCode = statusCode;
		this.message = message;
		this.details = details;
		this.error = error;
	}

	@Override
	public String toString() {
		return String.format("ErrorResponse [statusCode: %s, message: %s, details: %s]", this.statusCode, this.message,
				this.details);
	}

	public static ErrorResponse fromException(Exception exception) {
		final String details = Optional.ofNullable(exception).map(ex -> {
			final StringWriter sw = new StringWriter();
			final PrintWriter pw = new PrintWriter( sw );
			ex.printStackTrace( pw );
			return sw.toString();
		}).orElse("");

		if (exception instanceof ObjectNotFoundException) {
			return new ErrorResponse(Response.Status.NOT_FOUND.getStatusCode(), exception.getMessage(), exception.getMessage(),
					exception);
		} else if (exception instanceof RuntimeException) {
			return new ErrorResponse(Response.Status.BAD_REQUEST.getStatusCode(), exception.getMessage(), details,
					exception);
		} else {
			return new ErrorResponse(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(), exception.getMessage(),
					details, exception);
		}

	}

	public int getStatusCode() {
		return this.statusCode;
	}

	public String getReasonPhrase() {
		return this.statusCode + " - "+ this.message;
	}

	public String getMessage() {
		return this.message;
	}

	public String getDetails() {
		return this.details;
	}

	public Throwable getError() {
		return this.error;
	}

}
