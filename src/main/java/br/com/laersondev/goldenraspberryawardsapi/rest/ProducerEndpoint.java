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

import br.com.laersondev.goldenraspberryawardsapi.service.ProducerService;

@RequestScoped
@Path("/producer")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ProducerEndpoint {

	@Inject
	private ProducerService producerService;

	@GET
	@Path("/awardsinterval")
	public Response getInterval() {
		return ok(this.producerService.getProducerWinInterval()).build();
	}
}
