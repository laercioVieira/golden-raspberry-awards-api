package br.com.laersondev.goldenraspberryawardsapi.service;

import static org.hamcrest.core.IsInstanceOf.instanceOf;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;

import br.com.laersondev.goldenraspberryawardsapi.dto.MovieDto;
import br.com.laersondev.goldenraspberryawardsapi.model.Movie;
import br.com.laersondev.goldenraspberryawardsapi.model.Producer;
import br.com.laersondev.goldenraspberryawardsapi.model.Studio;
import br.com.laersondev.goldenraspberryawardsapi.repository.MovieRepository;
import br.com.laersondev.goldenraspberryawardsapi.service.exception.MovieNotFoundException;
import br.com.laersondev.goldenraspberryawardsapi.service.exception.ServiceException;

@RunWith(MockitoJUnitRunner.class)
public class MovieServiceTest {

	private static final String PRODUCER_JACK = "Producer Jack";

	private static final String PRODUCER_PHILL = "Producer Phill";

	private static final String STUDIO_B = "Studio B";

	private static final String STUDIO_A = "Studio A";

	private static final int QTD_ITENS_2 = 2;

	private static final boolean WINNER_MOVIEA = true;

	private static final int YEAR_MOVIEA_2000 = 2000;

	private static final String TITLE_MOVIE_A = "MovieA";

	private static final int ID_1 = 1;
	private static final long ID_1L = 1L;

	private static final long ID_2L = 2L;

	@InjectMocks
	private MovieService movieService;

	@Mock
	private MovieRepository movieRepository;

	@Mock
	private ProducerService producerService;

	@Mock
	private StudioService studioService;

	@Rule
	public ExpectedException notFoundExpectedExe = ExpectedException.none();

	@Rule
	public ExpectedException nullPointerExpectedExe = ExpectedException.none();

	@Test
	public void testDeleteSuccess() {
		final MovieDto movieDtoA = newMovieDtoA();
		final Optional<Movie> movieOpt = movieFromDto(ID_1, movieDtoA);
		Mockito.when(this.movieRepository.findById(ID_1)).thenReturn(movieOpt);
		Mockito.doNothing().when(this.movieRepository).delete(movieOpt.get());

		final Movie movieDeleted = this.movieService.delete(ID_1);

		assertNotNull(movieDeleted);
		assertEquals(ID_1, (long)movieDeleted.getId());
		assertEquals(movieOpt.get().getTitle(), movieDeleted.getTitle());
		assertEquals(movieOpt.get().getWinner(), movieDeleted.getWinner());
		assertEquals(movieOpt.get().getYear(), movieDeleted.getYear());

		assertEquals(movieOpt.get().getStudios(), movieDeleted.getStudios());
		assertEquals(movieOpt.get().getProducers(), movieDeleted.getProducers() );
	}

	@Test()
	public void testDeleteFailWithIdNotFound() {
		this.notFoundExpectedExe.expect(MovieNotFoundException.class);

		Mockito.when(this.movieRepository.findById(ID_1)).thenReturn(Optional.empty());
		assertNull(this.movieService.delete(ID_1));

		fail("Expected Exception: "+MovieNotFoundException.class+" was not throw");
	}

	@Test
	public void testCreateWithNullPointerProducerName() {
		this.nullPointerExpectedExe.expect(ServiceException.class);
		this.nullPointerExpectedExe.expectCause(instanceOf(NullPointerException.class));
		this.nullPointerExpectedExe.expectMessage("'movieDto' must not be null");

		//Service call
		this.movieService.create(null);

		//Asserts
		verify(this.studioService, never()).findOrCreate(any());
		verify(this.movieRepository, never()).save(any());
	}


	@Test
	public void testCreateWithSuccess() {
		final Set<String> studios = new LinkedHashSet<>(Arrays.asList(STUDIO_A, STUDIO_B));
		final Set<String> producers = new LinkedHashSet<>(Arrays.asList(PRODUCER_PHILL, PRODUCER_JACK));

		when(this.producerService.findOrCreate(producers)).thenAnswer(setIdAnswer((id, name)-> new Producer(id, name)));
		when(this.studioService.findOrCreate(studios)).thenAnswer(setIdAnswer((id, name)-> new Studio(id, name)));
		when(this.movieRepository.save(any())).thenAnswer(setIdAnswer());

		final MovieDto expected = newDto(studios, producers);
		final Movie movieCreated = this.movieService.create(expected);

		assertNotNull(movieCreated);
		assertTrue(movieCreated.getId() > 0);
		assertEquals(ID_1L, movieCreated.getId().longValue());
		assertEquals(expected.getTitle(), movieCreated.getTitle());
		assertEquals(expected.getWinner(), movieCreated.getWinner());
		assertEquals(expected.getYear(), movieCreated.getYear());

		assertEquals(QTD_ITENS_2, movieCreated.getStudios().size());
		assertEquals(QTD_ITENS_2, movieCreated.getProducers().size());

		final SortedSet<Studio> studiosToCompare= new TreeSet<>(Comparator.comparing(Studio::getId));
		studiosToCompare.addAll(movieCreated.getStudios());
		assertEquals(ID_1L, studiosToCompare.first().getId().longValue());
		assertEquals(STUDIO_A, studiosToCompare.first().getName());
		assertEquals(ID_2L, studiosToCompare.last().getId().longValue());
		assertEquals(STUDIO_B, studiosToCompare.last().getName());

		final SortedSet<Producer> producersToCompare= new TreeSet<>(Comparator.comparing(Producer::getId));
		producersToCompare.addAll(movieCreated.getProducers());
		assertEquals(ID_1L, producersToCompare.first().getId().longValue());
		assertEquals(PRODUCER_PHILL, producersToCompare.first().getName());

		assertEquals(ID_2L, producersToCompare.last().getId().longValue());
		assertEquals(PRODUCER_JACK, producersToCompare.last().getName());
	}

	private Optional<Movie> movieFromDto(final int id, final MovieDto movieDtoA) {
		final Movie movie = Movie.newFrom(movieDtoA);
		movie.setId(id);
		int idAuto = 1;
		for (final String prodName : movieDtoA.getProducers()) {
			movie.getProducers().add(new Producer(idAuto++, prodName));
		}

		idAuto = 1;
		for (final String stdName : movieDtoA.getStudios()) {
			movie.getStudios().add(new Studio(idAuto++, stdName));
		}

		return Optional.ofNullable(movie);
	}

	private <E>Answer<Set<E>> setIdAnswer(final BiFunction<Integer, String, E> newInstance) {
		return (ic) -> {
			final Set<String> entitiesNames = ic.getArgument(0);
			final AtomicInteger idIncrement = new AtomicInteger(1);
			return entitiesNames.stream()
					.map( name -> newInstance.apply(idIncrement.getAndIncrement(), name) )
					.collect(Collectors.toSet());
		};
	}

	private Answer<?> setIdAnswer() {
		return (ic) -> {
			final Movie movie = ic.getArgument(0);
			movie.setId(ID_1);
			return movie;
		};
	}

	private MovieDto newDto(final Set<String> studios, final Set<String> producers) {
		return new MovieDto(0, TITLE_MOVIE_A, YEAR_MOVIEA_2000, studios, producers, WINNER_MOVIEA);
	}

	private MovieDto newMovieDtoA() {
		final Set<String> studios = new HashSet<>(Arrays.asList(STUDIO_A, STUDIO_B));
		final Set<String> producers = new HashSet<>(Arrays.asList(PRODUCER_PHILL, PRODUCER_JACK));
		return new MovieDto(0, TITLE_MOVIE_A, YEAR_MOVIEA_2000, studios, producers, WINNER_MOVIEA);
	}

	@Test
	public void testGetById() {
		final Optional<Movie> movieOptMock = movieFromDto(ID_1, newMovieDtoA());
		when(this.movieRepository.findById(ID_1)).thenReturn(movieOptMock);
		final Optional<Movie> persistedMovieOpt = this.movieService.getById(ID_1);
		assertEqualsExpected(movieOptMock.get(), persistedMovieOpt.orElse(null));
	}

	@Test
	public void testGetByIdRequired() {
		final Optional<Movie> movieOptMock = movieFromDto(ID_1, newMovieDtoA());
		when(this.movieRepository.findById(ID_1)).thenReturn(movieOptMock);
		final Movie movieExpected = this.movieService.getByIdRequired(ID_1);
		assertEqualsExpected(movieOptMock.get(), movieExpected);
	}

	@Test(expected = MovieNotFoundException.class )
	public void testGetByIdRequiredFailWithIdNotFound() {
		Mockito.when(this.movieRepository.findById(ID_1)).thenReturn(Optional.empty());
		assertNull(this.movieService.getByIdRequired(ID_1));
	}

	private void assertEqualsExpected(final Movie movieMock, final Movie movieExpected) {
		assertNotNull(movieExpected);
		assertEquals((long) movieExpected.getId(), (long)movieMock.getId());
		assertEquals(movieExpected.getTitle(), movieMock.getTitle());
		assertEquals(movieExpected.getWinner(), movieMock.getWinner());
		assertEquals(movieExpected.getYear(), movieMock.getYear());

		assertEquals(movieExpected.getStudios(), movieMock.getStudios());
		assertEquals(movieExpected.getProducers(), movieMock.getProducers() );
	}
}
