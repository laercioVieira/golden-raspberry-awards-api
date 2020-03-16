package br.com.laersondev.goldenraspberryawardsapi.util;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.MessageFormat;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.Initialized;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.CDI;
import javax.inject.Inject;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jboss.logging.Logger;

import br.com.laersondev.goldenraspberryawardsapi.dto.MovieDto;
import br.com.laersondev.goldenraspberryawardsapi.service.MovieService;

@ApplicationScoped
public class MovieCsvDataLoader {

	private final static Logger LOG = Logger.getLogger(MovieCsvDataLoader.class);

	@Inject
	@ConfigProperty(name = "CsvMovieList", defaultValue = "src/main/resources/movielist.csv")
	private String csvMovieListFile;

	public void onInit( //
			@Observes @Initialized(ApplicationScoped.class) final Object init) {
		CDIContexts.runInRequestScope((ctx) -> {
			try {
				loadData(this.csvMovieListFile);
			} catch (final Exception e) {
				LOG.error(e.getMessage(), e);
			}
		}, null);
	}

	public void loadData(final String file) throws IOException {
		Precondition.checkIfNotBlank(file, "fileName");
		LOG.info("Starting loading csv data from file '" + file + "' to database.");
		final long startTime = System.currentTimeMillis();
		final Path fileCsv = Paths.get(file);
		if (!Files.exists(fileCsv)) {
			throw new FileNotFoundException(
					MessageFormat.format("Could not load data from csv to database. File: ''{0}'' not found.", file));
		}

		try (final Stream<String> lines = Files.lines(fileCsv)) {
			final AtomicInteger lineNumber = new AtomicInteger(1);
			final MovieService movieService = CDI.current().select(MovieService.class).get();

			lines.skip(1)//
			.map(parseToDto(lineNumber))//
			.filter(dtoN -> dtoN != null)//
			.forEach(dto -> movieService.create(dto));

			LOG.info(MessageFormat.format("Load csv data file: {0} finished. Total time: {1}", file,
					timeSince(startTime)));
		}
	}

	private static String timeSince(final long startTime) {
		final long endTime = System.currentTimeMillis() - startTime;
		final String endTimeStr = Optional.of(endTime).filter(timeInMs -> timeInMs >= 60_000)
				.map(time -> TimeUnit.MILLISECONDS.toSeconds(time) + " sec.").orElseGet(() -> endTime + " ms.");
		return endTimeStr;
	}

	private static Function<? super String, ? extends MovieDto> parseToDto(final AtomicInteger lineNumber) {
		return line -> {
			lineNumber.incrementAndGet();
			final String[] cols = getCols(lineNumber, line);
			final String year = cols[0].trim();
			final String title = cols[1].trim();
			final Set<String> studios = splitCommaSeparatedValues(cols[2]);
			final Set<String> producers = splitCommaSeparatedValues(cols[3]);
			final boolean winner = getWinnerValue(cols);
			return newDto(year, title, studios, producers, winner, lineNumber);
		};
	}

	private static MovieDto newDto(final String year, final String title, final Set<String> studios,
			final Set<String> producers, final boolean winner, AtomicInteger lineNumber) {
		try {
			return new MovieDto(0, title, Integer.parseInt(year), studios, producers, winner);
		} catch (final Exception e) {
			LOG.error(MessageFormat.format(//
					"Error loading data from csv line: {0}. Message: {1}", lineNumber.get(), e.getMessage()), e);
		}
		return null;
	}

	private static boolean getWinnerValue(final String[] cols) {
		boolean winner = false;
		if (cols.length >= 5) {
			winner = Optional.ofNullable(cols[4]).filter(colWinner -> !colWinner.isEmpty())
					.map(col -> col.trim().equalsIgnoreCase("yes")).orElse(false);
		}
		return winner;
	}

	private static Set<String> splitCommaSeparatedValues(String col) {
		return Stream.of(col.split(",")).filter(newCol -> !newCol.isEmpty())
				.map(String::trim)
				.collect(Collectors.toSet());
	}

	private static String[] getCols(final AtomicInteger lineNumber, String line) {
		final String[] cols = Optional.ofNullable(line.split(";")).orElse(new String[] {});
		final int qtdCols = 4;
		if (cols.length < qtdCols) {
			throw new RuntimeException(MessageFormat.format(
					"Line {0} is invalid, less than {1} columns (separated by '';'')", lineNumber.get(), qtdCols));
		}
		return cols;
	}

}
