package br.com.laersondev.goldenraspberryawardsapi.rest;

import static javax.ws.rs.core.Response.ok;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import br.com.laersondev.goldenraspberryawardsapi.dto.MovieDto;
import br.com.laersondev.goldenraspberryawardsapi.service.MovieService;
import br.com.laersondev.goldenraspberryawardsapi.service.exception.MovieNotFoundException;

@RequestScoped
@Path("/movie")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class MovieEndpoint {

	@Inject
	private MovieService movieService;

	@GET
	public Response doGet() {
		return Response.ok("Hello from Thorntail!").build();
	}

	@GET
	@Path("/{id}")
	public Response getById(@PathParam("id") final int id) {
		return ok(this.movieService.getById(id).orElseThrow(() -> new MovieNotFoundException(id))).build();
	}

	@POST
	public Response create(final MovieDto movieDto) {
		return ok(MovieDto.newFrom(this.movieService.create(movieDto))).build();
	}

	@DELETE
	@Path("/{id}")
	public Response delete(@PathParam("id") final int movieId) {
		return ok(this.movieService.delete(movieId)).build();
	}
}
