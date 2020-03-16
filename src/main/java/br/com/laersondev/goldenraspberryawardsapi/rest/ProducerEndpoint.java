package br.com.laersondev.goldenraspberryawardsapi.rest;

import static javax.ws.rs.core.Response.ok;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;

import br.com.laersondev.goldenraspberryawardsapi.dto.RangeProducerAwards;
import br.com.laersondev.goldenraspberryawardsapi.rest.util.ErrorResponse;
import br.com.laersondev.goldenraspberryawardsapi.service.ProducerService;

@RequestScoped
@Path("/producer")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ProducerEndpoint {

	@Inject
	private ProducerService producerService;

	@GET
	@Path("/rangeawards")
	@Operation( description = "Get the range of producers awards with the min and max details."
			)
	@APIResponses(value = {
			@APIResponse(responseCode = "200", description = "Successfull, returning the value",
					content= { @Content(schema=@Schema(implementation=RangeProducerAwards.class))
			}),
			@APIResponse(responseCode = "400", description = "Error, bad request with invalid parameters", //
			content= { @Content(schema=@Schema(implementation=ErrorResponse.class))
			})
	})
	public Response getAwardsRange() {
		return ok(this.producerService.getRangeProducerAwards()).build();
	}
}
