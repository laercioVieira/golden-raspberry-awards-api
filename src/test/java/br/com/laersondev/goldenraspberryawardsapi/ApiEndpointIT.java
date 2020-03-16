package br.com.laersondev.goldenraspberryawardsapi;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.Response;

import org.eclipse.microprofile.rest.client.RestClientBuilder;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.junit.InSequence;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.wildfly.swarm.arquillian.DefaultDeployment;

import br.com.laersondev.goldenraspberryawardsapi.dto.MovieDto;
import br.com.laersondev.goldenraspberryawardsapi.dto.ProducerAwardDetail;
import br.com.laersondev.goldenraspberryawardsapi.dto.RangeProducerAwards;
import br.com.laersondev.goldenraspberryawardsapi.rest.client.ProducerServiceClient;

@RunWith(Arquillian.class)
@DefaultDeployment(type = DefaultDeployment.Type.JAR)
public class ApiEndpointIT {


	//	@Inject()
	//	@ConfigProperty(name = "arquillian.host", defaultValue = "localhost")
	//	Optional<String> arquilianHost;
	//
	//	@Inject()
	//	@ConfigProperty(name = "thorntail.arquillian.daemon.port", defaultValue = "28080")
	//	Optional<String> arquilianPort;
	//
	//
	//	@ArquillianResource()
	//	private URL baseURL;

	private static final String HTTP_LOCALHOST_BASE_API = "http://localhost:8080/api";
	private static final String HTTP_BASE_PATH_MOVIE = HTTP_LOCALHOST_BASE_API + "/movie";
	private static final String HTTP_BASE_PATH_PRODUCER = HTTP_LOCALHOST_BASE_API + "/producer";

	private final Client client = ClientBuilder.newClient();

	private MovieDto movieCreated;

	//	@Test(expected = NotFoundException.class)
	//	public void testContexts() throws IOException {
	//
	//		when
	//		this.client.target("http://localhost:8080/").request().buildGet().invoke(String.class);
	//	}



	@Test
	@InSequence(value = 1)
	@RunAsClient
	public void testGetRangeProducerAwards() throws IOException {

		final URL url = new URL(HTTP_LOCALHOST_BASE_API + "/producer");
		final ProducerServiceClient producerService = RestClientBuilder.newBuilder()//
				.baseUrl(url).build(ProducerServiceClient.class);

		final Response response = producerService.getAwardsRange();
		final RangeProducerAwards content = (RangeProducerAwards) response.getEntity();

		//		final RangeProducerAwards content = this.client.target(
		//				HTTP_BASE_PATH_PRODUCER + "/rangeawards"//
		//				).request().buildGet().invoke(RangeProducerAwards.class);

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

	//	@Test
	//	@InSequence(value = 2)
	//	public void testSaveMovie() throws IOException {
	//		final Set<String> studios = new HashSet<>(Arrays.asList("Studio 1","Studio 2"));
	//		final Set<String> producers = new HashSet<>(Arrays.asList("Producer 1","Producer 2"));
	//		final MovieDto movieToCreate = new MovieDto(0, "Test ABC", 2000, studios, producers, true);
	//		this.movieCreated = this.client.target(HTTP_BASE_PATH_MOVIE).request()//
	//				.buildPost(Entity.<MovieDto> entity(
	//						movieToCreate,
	//						MediaType.APPLICATION_JSON )).invoke(MovieDto.class);
	//
	//		assertNotNull(this.movieCreated);
	//		assertTrue(this.movieCreated.getId() > 0);
	//
	//		assertEquals(movieToCreate.getTitle(), this.movieCreated.getTitle());
	//		assertEquals(movieToCreate.getWinner(), this.movieCreated.getWinner() );
	//		assertEquals(movieToCreate.getYear(), this.movieCreated.getYear());
	//
	//		assertNotNull(this.movieCreated.getProducers());
	//		assertNotNull(this.movieCreated.getStudios());
	//		assertEquals(movieToCreate.getStudios(), this.movieCreated.getStudios());
	//		assertEquals(movieToCreate.getProducers(), this.movieCreated.getProducers());
	//
	//	}
	//
	//	@Test
	//	@InSequence(value = 3)
	//	public void testDeleteMovie() throws IOException {
	//
	//		final MovieDto movieDeleted = this.client.target(HTTP_BASE_PATH_MOVIE + "/" + this.movieCreated.getId() ).request()//
	//				.buildDelete().invoke(MovieDto.class);
	//
	//		assertNotNull(movieDeleted);
	//		assertEquals(this.movieCreated.getId(), movieDeleted.getId());
	//
	//		assertEquals(this.movieCreated.getTitle(), movieDeleted.getTitle());
	//		assertEquals(this.movieCreated.getWinner(), movieDeleted.getWinner() );
	//		assertEquals(this.movieCreated.getYear(), movieDeleted.getYear());
	//
	//		assertNotNull(movieDeleted.getProducers());
	//		assertNotNull(movieDeleted.getStudios());
	//		assertEquals(this.movieCreated.getStudios(), movieDeleted.getStudios());
	//		assertEquals(this.movieCreated.getProducers(), movieDeleted.getProducers());
	//
	//		this.movieCreated = null;
	//	}

}
