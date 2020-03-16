package br.com.laersondev.goldenraspberryawardsapi;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.ws.rs.NotFoundException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;

import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.junit.InSequence;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.wildfly.swarm.arquillian.DefaultDeployment;

import br.com.laersondev.goldenraspberryawardsapi.dto.MovieDto;
import br.com.laersondev.goldenraspberryawardsapi.dto.ProducerAwardDetail;
import br.com.laersondev.goldenraspberryawardsapi.dto.RangeProducerAwards;

@RunWith(Arquillian.class)
@DefaultDeployment(type = DefaultDeployment.Type.JAR)
public class ApiEndpointIT {


	private static final String HTTP_LOCALHOST_BASE_API = "http://localhost:8080/api";
	private static final String HTTP_BASE_PATH_MOVIE = HTTP_LOCALHOST_BASE_API + "/movie";
	private static final String HTTP_BASE_PATH_PRODUCER = HTTP_LOCALHOST_BASE_API + "/producer";
	//	@Deployment(testable = false)
	//	public static Archive createDeployment() throws Exception {
	//		//		return ShrinkWrap.create(JAXRSArchive.class, "golden-raspberry-awards-api.war")//
	//		//				.addPackages(true, "br.com.laersondev.goldenraspberryawardsapi")
	//		//				.addClass(RestApplication.class)
	//		//				.setContextRoot("api").addAsResource(
	//		//						Paths.get("META-INF/queries/producer-movie-orm.xml").toFile())
	//		//				.addAsResource("beans.xml").addAsResource("persistence.xml").addAsResource("project-defaults.yml")
	//		//				.addAllDependencies();
	//	}

	private Client client = ClientBuilder.newClient();

	private MovieDto movieCreated;

	@Test(expected = NotFoundException.class)
	@RunAsClient
	public void testContexts() throws IOException {
		this.client.target("http://localhost:8080/").request().buildGet().invoke(String.class);
	}

	@Test
	@InSequence(value = 1)
	@RunAsClient
	public void testGetRangeProducerAwards(final @Context UriInfo uriInfo) throws IOException {

		System.out.println(uriInfo.getBaseUri());

		final RangeProducerAwards content = this.client.target(
				HTTP_BASE_PATH_PRODUCER + "/rangeawards"//
				).request().buildGet().invoke(RangeProducerAwards.class);

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
	@InSequence(value = 2)
	@RunAsClient
	public void testSaveMovie() throws IOException {
		final Set<String> studios = new HashSet<>(Arrays.asList("Studio 1","Studio 2"));
		final Set<String> producers = new HashSet<>(Arrays.asList("Producer 1","Producer 2"));
		final MovieDto movieToCreate = new MovieDto(0, "Test ABC", 2000, studios, producers, true);
		this.movieCreated = this.client.target(HTTP_BASE_PATH_MOVIE).request()//
				.buildPost(Entity.<MovieDto> entity(
						movieToCreate,
						MediaType.APPLICATION_JSON )).invoke(MovieDto.class);

		assertNotNull(this.movieCreated);
		assertTrue(this.movieCreated.getId() > 0);

		assertEquals(movieToCreate.getTitle(), this.movieCreated.getTitle());
		assertEquals(movieToCreate.getWinner(), this.movieCreated.getWinner() );
		assertEquals(movieToCreate.getYear(), this.movieCreated.getYear());

		assertNotNull(this.movieCreated.getProducers());
		assertNotNull(this.movieCreated.getStudios());
		assertEquals(movieToCreate.getStudios(), this.movieCreated.getStudios());
		assertEquals(movieToCreate.getProducers(), this.movieCreated.getProducers());

	}

	@Test
	@InSequence(value = 3)
	@RunAsClient
	public void testDeleteMovie() throws IOException {

		final MovieDto movieDeleted = this.client.target(HTTP_BASE_PATH_MOVIE + "/" + this.movieCreated.getId() ).request()//
				.buildDelete().invoke(MovieDto.class);

		assertNotNull(movieDeleted);
		assertEquals(this.movieCreated.getId(), movieDeleted.getId());

		assertEquals(this.movieCreated.getTitle(), movieDeleted.getTitle());
		assertEquals(this.movieCreated.getWinner(), movieDeleted.getWinner() );
		assertEquals(this.movieCreated.getYear(), movieDeleted.getYear());

		assertNotNull(movieDeleted.getProducers());
		assertNotNull(movieDeleted.getStudios());
		assertEquals(this.movieCreated.getStudios(), movieDeleted.getStudios());
		assertEquals(this.movieCreated.getProducers(), movieDeleted.getProducers());

		this.movieCreated = null;
	}

}
