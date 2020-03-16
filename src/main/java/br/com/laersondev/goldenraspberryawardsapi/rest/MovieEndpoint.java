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

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;

import br.com.laersondev.goldenraspberryawardsapi.dto.MovieDto;
import br.com.laersondev.goldenraspberryawardsapi.rest.util.ErrorResponse;
import br.com.laersondev.goldenraspberryawardsapi.service.MovieService;

@RequestScoped
@Path("/movie")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class MovieEndpoint {

	@Inject
	private MovieService movieService;

	@GET
	@Path("/{id}")
	@Operation( description = "Get the Movie for this id" )
	@APIResponses(value = {
			@APIResponse(responseCode = "200", description = "Successfull, returning the value",
					content= { @Content(schema=@Schema(implementation=MovieDto.class))
			}),
			@APIResponse(responseCode = "404", description = "Error, movie not found with this id",
			content= { @Content(schema=@Schema(implementation=ErrorResponse.class))
			}),
			@APIResponse(responseCode = "400", description = "Error, bad request with invalid parameters",
			content= { @Content(schema=@Schema(implementation=ErrorResponse.class))
			})
	})
	public Response getById(@PathParam("id") final int id) {
		return ok(MovieDto.newFrom(this.movieService.getByIdRequired(id))).build();
	}

	@POST
	@Operation( description = "Create a new Movie, associating it with the Studios and Producers. "
			+ "If Studio is not registered, create a new one, do the same with the Producer."
			)
	@APIResponses(value = {
			@APIResponse(responseCode = "200", description = "Successfull, returning the value with the id generated",
					content= { @Content(schema=@Schema(implementation=MovieDto.class))
			}),
			@APIResponse(responseCode = "400", description = "Error, bad request with invalid parameters",
			content= { @Content(schema=@Schema(implementation=ErrorResponse.class))
			})
	})
	public Response create(final MovieDto movieDto) {
		return ok(MovieDto.newFrom(this.movieService.create(movieDto))).build();
	}

	@DELETE
	@Path("/{id}")
	@Operation( description = "Delete a Movie with this id")
	@APIResponses(value = {
			@APIResponse(responseCode = "200", description = "Successfull, returning the deleted movie",
					content= { @Content(schema=@Schema(implementation=MovieDto.class))
			}),
			@APIResponse(responseCode = "404", description = "Error, movie not found with this id",
			content= { @Content(schema=@Schema(implementation=ErrorResponse.class))
			} ),
			@APIResponse(responseCode = "400", description = "Error, bad request with invalid parameters",
			content= { @Content(schema=@Schema(implementation=ErrorResponse.class))
			} )
	})
	public Response delete(@PathParam("id") final int movieId) {
		return ok(MovieDto.newFrom(this.movieService.delete(movieId))).build();
	}
}
