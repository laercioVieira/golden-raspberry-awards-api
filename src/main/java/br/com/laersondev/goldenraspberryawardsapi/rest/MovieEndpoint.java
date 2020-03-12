package br.com.laersondev.goldenraspberryawardsapi.rest;

import static br.com.laersondev.goldenraspberryawardsapi.rest.api.ResponseStatus.notFoundError;
import static java.text.MessageFormat.format;
import static javax.ws.rs.core.Response.ok;

import java.util.Optional;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import br.com.laersondev.goldenraspberryawardsapi.dto.MovieDeleteResponse;
import br.com.laersondev.goldenraspberryawardsapi.dto.MovieDto;
import br.com.laersondev.goldenraspberryawardsapi.model.Movie;
import br.com.laersondev.goldenraspberryawardsapi.service.MovieService;

@ApplicationScoped
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
		final Optional<Movie> movieOpt = this.movieService.getById(id);
		if (movieOpt.isPresent()) {
			return ok().build();
		} else {
			return notFoundError(format("Movie with id: {0} not found.", id));
		}
	}

	@POST
	public Response create(final MovieDto movieDto) {
		return ok(new MovieDeleteResponse(this.movieService.create(movieDto).getId())).build();
	}

	@PUT
	public Response update(final MovieDto movieDto) {
		this.movieService.update(movieDto);
		return ok().build();
	}

	@DELETE
	@Path("/{id}")
	public Response delete(@PathParam("id") final int movieId) {
		this.movieService.delete(movieId);
		return ok().build();
	}
}
