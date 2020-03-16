package br.com.laersondev.goldenraspberryawardsapi.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import br.com.laersondev.goldenraspberryawardsapi.dto.ProducerAwardDetail;
import br.com.laersondev.goldenraspberryawardsapi.dto.RangeProducerAwards;
import br.com.laersondev.goldenraspberryawardsapi.model.Producer;
import br.com.laersondev.goldenraspberryawardsapi.repository.ProducerRepository;
import br.com.laersondev.goldenraspberryawardsapi.repository.dto.ProducerMovieWinRs;

@RunWith(MockitoJUnitRunner.class)
public class ProducerServiceTest {

	private static final int INTERVAL_MAT_13 = 13;

	private static final int INTERVAL_DEREK_6 = 6;

	private static final int ONE_RESULT = 1;
	private static final int TWO_RESULT = 2;

	private static final int MINYEAR_DEREK_1984 = 1984;

	private static final int MAXYEAR_MATT_2015 = 2015;

	private static final int MAXYEAR_DEREK_1990 = 1990;

	private static final int MINYEAR_MATT_2002 = 2002;

	private static final String PRODUCER_SANTOS = "PRODUCER SANTOS";

	private static final String PRODUCER_RICHARD = "PRODUCER RICHARD";

	private static final String PRODUCER_DERECK = "Bo Derek";

	private static final String PRODUCER_MATTHEW = "Matthew Vaughn";

	@InjectMocks
	private ProducerService producerService;

	@Mock
	private ProducerRepository producerRepository;

	@Rule
	public ExpectedException nullPointerExpectedExe = ExpectedException.none();

	@Test
	public void testFindOrCreateWithProducerFoundSuccess() {
		final Set<String> expected = new LinkedHashSet<>(Arrays.asList(PRODUCER_RICHARD, PRODUCER_SANTOS));
		final AtomicInteger idAuto = new AtomicInteger(1);

		when(this.producerRepository.findByName(any())).thenAnswer((ic) -> {
			return Optional.of(new Producer(idAuto.getAndIncrement(), ic.getArgument(0)));
		});

		final List<Producer> actual = new ArrayList<>(this.producerService.findOrCreate(expected));
		verify(this.producerRepository, Mockito.never()).save(any());

		assertEquals(expected.size(), actual.size());

		final String[] expectedArray = expected.toArray(new String[] {});
		assertEquals(1L, (long)actual.get(0).getId());
		assertEquals(expectedArray[0], actual.get(0).getName());

		assertEquals(2L, (long)actual.get(1).getId());
		assertEquals(expectedArray[1], actual.get(1).getName());

	}

	@Test
	public void testProducerNoFoundSoCreateSuccess() {
		final Set<String> expected = new LinkedHashSet<>(Arrays.asList(PRODUCER_RICHARD, PRODUCER_SANTOS));
		final AtomicInteger idAuto = new AtomicInteger(1);

		when(this.producerRepository.findByName(any())).thenReturn(Optional.empty());
		when(this.producerRepository.save(any())).thenAnswer((ic) -> {
			final Producer producer = ic.getArgument(0);
			producer.setId(idAuto.getAndIncrement());
			return producer;
		});

		final List<Producer> actual = new ArrayList<>(this.producerService.findOrCreate(expected));
		verify(this.producerRepository, times(2)).findByName(any());
		verify(this.producerRepository, times(2)).save(any());

		assertEquals(expected.size(), actual.size());

		final String[] expectedArray = expected.toArray(new String[] {});
		assertEquals(1L, (long)actual.get(0).getId());
		assertEquals(expectedArray[0], actual.get(0).getName());

		assertEquals(2L, (long)actual.get(1).getId());
		assertEquals(expectedArray[1], actual.get(1).getName());
	}

	@Test
	public void testExpectedNullPointerProducersPassed() {

		this.nullPointerExpectedExe.expect(NullPointerException.class);
		this.nullPointerExpectedExe.expectMessage("'producers' must not be null");

		//Service call
		this.producerService.findOrCreate(null);

		//Asserts
		verify(this.producerRepository, never()).findByName(any());
		verify(this.producerRepository, never()).save(any());
	}

	@Test
	public void testGetRangeProducerAwardsSuccess() {

		final List<ProducerMovieWinRs> listaProducerAwardsOrderByYear = Arrays.asList(//
				new ProducerMovieWinRs(PRODUCER_MATTHEW, "The Cat", MINYEAR_MATT_2002),//
				new ProducerMovieWinRs(PRODUCER_MATTHEW, "The Sinfo", MAXYEAR_MATT_2015),//
				new ProducerMovieWinRs(PRODUCER_DERECK, "Crew", MINYEAR_DEREK_1984),//
				new ProducerMovieWinRs(PRODUCER_DERECK, "Parasita", MAXYEAR_DEREK_1990)//
				);
		when(this.producerRepository.findProducersWithWinMoviesAtLeastTwiceOrdened()).thenReturn(listaProducerAwardsOrderByYear);

		//Service call
		final RangeProducerAwards rangeProducerAwards = this.producerService.getRangeProducerAwards();

		assertsRangeSucess(rangeProducerAwards);
	}

	private void assertsRangeSucess(final RangeProducerAwards rangeProducerAwards) {
		assertNotNull(rangeProducerAwards);
		assertNotNull(rangeProducerAwards.getMin());
		assertNotNull(rangeProducerAwards.getMax());

		assertEquals(ONE_RESULT, rangeProducerAwards.getMin().size());
		assertEquals(ONE_RESULT, rangeProducerAwards.getMax().size());
		final List<ProducerAwardDetail> minList = new ArrayList<>(rangeProducerAwards.getMin());
		assertEquals(PRODUCER_DERECK, minList.get(0).getProducer() );
		assertEquals(INTERVAL_DEREK_6, minList.get(0).getInterval() );
		assertEquals(MAXYEAR_DEREK_1990, minList.get(0).getFollowingWin().longValue() );
		assertEquals(MINYEAR_DEREK_1984, minList.get(0).getPreviousWin().longValue() );

		final List<ProducerAwardDetail> maxList = new ArrayList<>(rangeProducerAwards.getMax());
		assertEquals(PRODUCER_MATTHEW, maxList.get(0).getProducer() );
		assertEquals(INTERVAL_MAT_13, maxList.get(0).getInterval() );
		assertEquals(MAXYEAR_MATT_2015, maxList.get(0).getFollowingWin().longValue() );
		assertEquals(MINYEAR_MATT_2002, maxList.get(0).getPreviousWin().longValue() );
	}

	@Test
	public void testGetRangeProducerAwardsSameMinAndMaxSuccess() {

		final List<ProducerMovieWinRs> listaProducerAwards = Arrays.asList(//
				new ProducerMovieWinRs(PRODUCER_MATTHEW, "The Cat", MINYEAR_MATT_2002),//
				new ProducerMovieWinRs(PRODUCER_MATTHEW, "The Sinfo", MAXYEAR_MATT_2015),//
				new ProducerMovieWinRs(PRODUCER_DERECK, "Parasita", MINYEAR_MATT_2002),//
				new ProducerMovieWinRs(PRODUCER_DERECK, "Crew", MAXYEAR_MATT_2015)//
				);
		when(this.producerRepository.findProducersWithWinMoviesAtLeastTwiceOrdened()).thenReturn(listaProducerAwards);

		//Service call
		final RangeProducerAwards rangeProducerAwards = this.producerService.getRangeProducerAwards();
		assertNotNull(rangeProducerAwards);
		assertNotNull(rangeProducerAwards.getMin());
		assertNotNull(rangeProducerAwards.getMax());

		assertEquals(TWO_RESULT, rangeProducerAwards.getMin().size());
		assertEquals(TWO_RESULT, rangeProducerAwards.getMax().size());

		final List<ProducerAwardDetail> minList = new ArrayList<>(rangeProducerAwards.getMin());
		minList.sort(Comparator.comparing(ProducerAwardDetail::getProducer));
		assertEquals(PRODUCER_DERECK, minList.get(0).getProducer() );
		assertEquals(INTERVAL_MAT_13, minList.get(0).getInterval() );
		assertEquals(MAXYEAR_MATT_2015, minList.get(0).getFollowingWin().longValue() );
		assertEquals(MINYEAR_MATT_2002, minList.get(0).getPreviousWin().longValue() );

		assertEquals(PRODUCER_MATTHEW, minList.get(1).getProducer() );
		assertEquals(INTERVAL_MAT_13, minList.get(1).getInterval() );
		assertEquals(MAXYEAR_MATT_2015, minList.get(1).getFollowingWin().longValue() );
		assertEquals(MINYEAR_MATT_2002, minList.get(1).getPreviousWin().longValue() );

		final List<ProducerAwardDetail> maxList = new ArrayList<>(rangeProducerAwards.getMax());
		maxList.sort(Comparator.comparing(ProducerAwardDetail::getProducer));
		assertEquals(PRODUCER_DERECK, maxList.get(0).getProducer() );
		assertEquals(INTERVAL_MAT_13, maxList.get(0).getInterval() );
		assertEquals(MAXYEAR_MATT_2015, maxList.get(0).getFollowingWin().longValue() );
		assertEquals(MINYEAR_MATT_2002, maxList.get(0).getPreviousWin().longValue() );

		assertEquals(PRODUCER_MATTHEW, maxList.get(1).getProducer() );
		assertEquals(INTERVAL_MAT_13, maxList.get(1).getInterval() );
		assertEquals(MAXYEAR_MATT_2015, maxList.get(1).getFollowingWin().longValue() );
		assertEquals(MINYEAR_MATT_2002, maxList.get(1).getPreviousWin().longValue() );

	}

	@Test
	public void testGetRangeProducerAwardsEmpty() {
		when(this.producerRepository.findProducersWithWinMoviesAtLeastTwiceOrdened()).thenReturn(Collections.emptyList());
		//Service call
		final RangeProducerAwards rangeProducerAwards = this.producerService.getRangeProducerAwards();
		assertNotNull(rangeProducerAwards);
		assertNotNull(rangeProducerAwards.getMin());
		assertNotNull(rangeProducerAwards.getMax());

		assertTrue(rangeProducerAwards.getMin().isEmpty());
		assertTrue(rangeProducerAwards.getMax().isEmpty());
	}

}
