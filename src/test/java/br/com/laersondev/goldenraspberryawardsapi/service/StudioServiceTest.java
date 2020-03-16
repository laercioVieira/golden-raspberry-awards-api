package br.com.laersondev.goldenraspberryawardsapi.service;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
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

import br.com.laersondev.goldenraspberryawardsapi.model.Studio;
import br.com.laersondev.goldenraspberryawardsapi.repository.StudioRepository;

@RunWith(MockitoJUnitRunner.class)
public class StudioServiceTest {

	private static final String STUDIO_FOXB = "STUDIO FOXB";

	private static final String STUDIO_FOXA = "STUDIO FOXA";

	@InjectMocks
	private StudioService studioService;

	@Mock
	private StudioRepository studioRepository;

	@Rule
	public ExpectedException nullPointerExpectedExe = ExpectedException.none();

	@Test
	public void testFindOrCreateWithStudioFoundSuccess() {
		final Set<String> expected = new LinkedHashSet<>(Arrays.asList(STUDIO_FOXA, STUDIO_FOXB));
		final AtomicInteger idAuto = new AtomicInteger(1);

		when(this.studioRepository.findByName(any())).thenAnswer((ic) -> {
			return Optional.of(new Studio(idAuto.getAndIncrement(), ic.getArgument(0)));
		});

		final List<Studio> actual = new ArrayList<>(this.studioService.findOrCreate(expected));
		verify(this.studioRepository, Mockito.never()).save(any());

		assertEquals(expected.size(), actual.size());

		final String[] expectedArray = expected.toArray(new String[] {});
		assertEquals(1L, (long)actual.get(0).getId());
		assertEquals(expectedArray[0], actual.get(0).getName());

		assertEquals(2L, (long)actual.get(1).getId());
		assertEquals(expectedArray[1], actual.get(1).getName());

	}

	@Test
	public void testStudioNoFoundSoCreateSuccess() {
		final Set<String> expected = new LinkedHashSet<>(Arrays.asList(STUDIO_FOXA, STUDIO_FOXB));
		final AtomicInteger idAuto = new AtomicInteger(1);

		when(this.studioRepository.findByName(any())).thenReturn(Optional.empty());
		when(this.studioRepository.save(any())).thenAnswer((ic) -> {
			final Studio studio = ic.getArgument(0);
			studio.setId(idAuto.getAndIncrement());
			return studio;
		});

		final List<Studio> actual = new ArrayList<>(this.studioService.findOrCreate(expected));
		verify(this.studioRepository, times(2)).findByName(any());
		verify(this.studioRepository, times(2)).save(any());

		assertEquals(expected.size(), actual.size());

		final String[] expectedArray = expected.toArray(new String[] {});
		assertEquals(1L, (long)actual.get(0).getId());
		assertEquals(expectedArray[0], actual.get(0).getName());

		assertEquals(2L, (long)actual.get(1).getId());
		assertEquals(expectedArray[1], actual.get(1).getName());
	}

	@Test
	public void testExpectedNullPointerStudiosPassed() {

		this.nullPointerExpectedExe.expect(NullPointerException.class);
		this.nullPointerExpectedExe.expectMessage("'studios' must not be null");

		//Service call
		this.studioService.findOrCreate(null);

		//Asserts
		verify(this.studioRepository, never()).findByName(any());
		verify(this.studioRepository, never()).save(any());
	}

}
