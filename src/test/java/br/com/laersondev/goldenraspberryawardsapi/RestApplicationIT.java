package br.com.laersondev.goldenraspberryawardsapi;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.ws.rs.NotFoundException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.hamcrest.core.StringContains;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.wildfly.swarm.arquillian.DefaultDeployment;

import br.com.laersondev.goldenraspberryawardsapi.dto.MovieDto;
import br.com.laersondev.goldenraspberryawardsapi.dto.ProducerAwardDetail;
import br.com.laersondev.goldenraspberryawardsapi.dto.RangeProducerAwards;

@RunWith(Arquillian.class)
@DefaultDeployment(type = DefaultDeployment.Type.WAR)
public class RestApplicationIT {

	private static final int ID_NOT_EXISTS_99999 = 99999;
	private static final String TEST_ABC_MOVIE_TITLE = "Test ABC";
	private static final String TEST_DCA_MOVIE_TITLE = "Test DCA";
	private static final String TEST_MOVIE_D3_TITLE = "Test Movie D3";
	private static final String PRODUCER_2 = "Producer 2";
	private static final String PRODUCER_1 = "Producer 1";
	private static final String STUDIO_2 = "Studio 2";
	private static final String STUDIO_1 = "Studio 1";

	//
	@ArquillianResource
	private URL baseURL;

	private final String defaulBaseURL = //
			Optional.ofNullable(this.baseURL).map(URL::toExternalForm)
			.orElse("http://localhost:8080/");

	private final String BASE_API = "api";
	private final String HTTP_BASE_API = this.defaulBaseURL + this.BASE_API;
	private final String MOVIE_API_BASEPATH = this.HTTP_BASE_API + "/movie";
	private final String PRODUCER_API_BASEPATH = this.HTTP_BASE_API + "/producer";

	private static final long QTD_ITENS_2 = 2L;

	private Client client;

	@Before
	public void before() {
		this.client = ClientBuilder.newClient();
	}

	@After
	public void after() {
		this.client.close();
	}

	@Test()
	@RunAsClient
	public void testContexts()  {
		final Client client = ClientBuilder.newClient();
		final Response response = client
				.target(this.defaulBaseURL + "swagger-ui/index.html")
				.request().get();
		assertEquals(200, response.getStatus());
		assertThat(response.readEntity(String.class), StringContains.containsString("<title>Swagger UI</title>"));
	}


	@Test
	@RunAsClient
	public void testGetRangeProducerAwards() {
		final RangeProducerAwards content = this.client//
				.target(URI.create(this.PRODUCER_API_BASEPATH))
				.path("rangeawards").request()//
				.buildGet().invoke(new GenericType<RangeProducerAwards>(RangeProducerAwards.class));

		assertNotNull(content);
		assertFalse(content.getMin().isEmpty());
		assertFalse(content.getMax().isEmpty());

		final List<ProducerAwardDetail> mins =	new ArrayList<>(content.getMin());
		final ProducerAwardDetail minProducerAwardDetail = mins.get(0);
		assertNotNull(minProducerAwardDetail);
		assertEquals("Bo Derek", minProducerAwardDetail.getProducer());
		assertEquals(6, minProducerAwardDetail.getInterval());
		assertNotNull(minProducerAwardDetail.getPreviousWin());
		assertEquals(1984L, minProducerAwardDetail.getPreviousWin().longValue());
		assertNotNull(minProducerAwardDetail.getFollowingWin());
		assertEquals(1990L, minProducerAwardDetail.getFollowingWin().longValue());

		final List<ProducerAwardDetail> max = new ArrayList<>(content.getMax());
		final ProducerAwardDetail maxProducerAwardDetail = max.get(0);
		assertNotNull(maxProducerAwardDetail);
		assertEquals("Matthew Vaughn", maxProducerAwardDetail.getProducer());
		assertEquals(13, maxProducerAwardDetail.getInterval());

		assertNotNull(maxProducerAwardDetail.getPreviousWin());
		assertEquals(2002L, maxProducerAwardDetail.getPreviousWin().longValue());
		assertNotNull(maxProducerAwardDetail.getFollowingWin());
		assertEquals(2015L, maxProducerAwardDetail.getFollowingWin().longValue());
	}

	@Test
	@RunAsClient
	public void testSaveMovie()  {
		final MovieDto movieExpected = newMovieDtoTest(TEST_ABC_MOVIE_TITLE);
		final MovieDto movieCreated = requestCreateNewMovie(movieExpected);
		assertMovieResponse(movieExpected, movieCreated);
	}

	@Test
	@RunAsClient
	public void testDeleteMovie()  {
		final MovieDto movieExpected = requestCreateNewMovie(newMovieDtoTest(TEST_DCA_MOVIE_TITLE));
		assertNotNull(movieExpected);

		final MovieDto movieDeleted = requestDeleteMovie(movieExpected.getId());

		assertMovieResponse(movieExpected, movieDeleted);
		assertEquals(movieExpected.getId(), movieDeleted.getId());
	}

	@Test(expected = NotFoundException.class)
	@RunAsClient
	public void testDeleteMovieIdNotFound()  {
		requestDeleteMovie(ID_NOT_EXISTS_99999);
	}


	@Test
	@RunAsClient
	public void testGetMovieById()  {
		final MovieDto movieExpected = requestCreateNewMovie(newMovieDtoTest(TEST_MOVIE_D3_TITLE));
		assertNotNull(movieExpected);

		final MovieDto moviePrevCreated = this.client.target(
				URI.create(this.MOVIE_API_BASEPATH + "/" + movieExpected.getId())).request()//
				.buildGet().invoke(new GenericType<MovieDto>(MovieDto.class));

		assertMovieResponse(movieExpected, moviePrevCreated);
		assertEquals(movieExpected.getId(), moviePrevCreated.getId());

		requestDeleteMovie(movieExpected.getId());
	}

	@Test(expected = NotFoundException.class )
	@RunAsClient
	public void testGetMovieByIdNotFound()  {
		this.client.target(
				URI.create(this.MOVIE_API_BASEPATH + "/" + ID_NOT_EXISTS_99999)).request()//
		.buildGet().invoke(new GenericType<MovieDto>(MovieDto.class));
	}

	private void assertMovieResponse(final MovieDto movieExpected, final MovieDto movieToVerify) {
		assertNotNull(movieToVerify);
		assertTrue(movieToVerify.getId() > 0);

		assertEquals(movieExpected.getTitle(), movieToVerify.getTitle());
		assertEquals(movieExpected.getWinner(), movieToVerify.getWinner() );
		assertEquals(movieExpected.getYear(), movieToVerify.getYear());

		assertNotNull(movieToVerify.getProducers());
		assertNotNull(movieToVerify.getStudios());
		assertEquals(QTD_ITENS_2, movieToVerify.getStudios().size());
		assertEquals(QTD_ITENS_2, movieToVerify.getProducers().size());

		assertEquals(movieExpected.getStudios(), movieToVerify.getStudios());
		assertEquals(movieExpected.getProducers(), movieToVerify.getProducers());
	}

	private MovieDto newMovieDtoTest(final String title) {
		final Set<String> studios = new HashSet<>(Arrays.asList(STUDIO_1,STUDIO_2));
		final Set<String> producers = new HashSet<>(Arrays.asList(PRODUCER_1,PRODUCER_2));
		final MovieDto movieExpected = new MovieDto(0, title, 2000, studios, producers, true);
		return movieExpected;
	}

	private MovieDto requestCreateNewMovie(final MovieDto movieToCreate) {
		return this.client.target(URI.create(this.MOVIE_API_BASEPATH)).request()//
				.buildPost(Entity.<MovieDto> entity(movieToCreate, MediaType.APPLICATION_JSON ))//
				.invoke(new GenericType<MovieDto>(MovieDto.class));
	}
	private MovieDto requestDeleteMovie(final int movieId) {
		final MovieDto movieDeleted = this.client.target(
				URI.create(this.MOVIE_API_BASEPATH + "/" + movieId)).request()//
				.buildDelete().invoke(new GenericType<MovieDto>(MovieDto.class));
		return movieDeleted;
	}
}

