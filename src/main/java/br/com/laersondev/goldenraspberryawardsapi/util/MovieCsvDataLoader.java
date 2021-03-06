package br.com.laersondev.goldenraspberryawardsapi.util;

import static java.lang.Thread.currentThread;
import static java.text.MessageFormat.format;
import static java.util.Optional.ofNullable;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
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

	private static final String SRC_MOVIELIST_DEFAULT1 = "src/main/resources/movielist.csv";

	private final static Logger LOG = Logger.getLogger(MovieCsvDataLoader.class);

	@Inject
	@ConfigProperty(name = "CsvMovieList", defaultValue = SRC_MOVIELIST_DEFAULT1)
	private String csvMovieListFile;

	public void onInit( //
			@Observes @Initialized(ApplicationScoped.class) final Object init) {
		CDIContexts.runInRequestScope((ctx) -> {
			try {
				this.loadData(this.newFile());
			} catch (final Exception e) {
				LOG.error(e.getMessage(), e);
			}
		}, null);
	}

	private File newFile() {
		try {
			if (!this.csvMovieListFile.equalsIgnoreCase(SRC_MOVIELIST_DEFAULT1)) {
				return new File(this.csvMovieListFile);
			}

			final InputStream csvInput = ofNullable(
					currentThread().getContextClassLoader().getResourceAsStream(this.csvMovieListFile)).orElseGet(
							() -> currentThread().getContextClassLoader().getResourceAsStream("movielist.csv"));

			final InputStream csvInput2try = ofNullable(csvInput).orElseThrow(//
					() -> new FileNotFoundException(format("Could not find file: ''{0}''.", this.csvMovieListFile)));

			final long time = System.nanoTime();
			final Path tempFile = Files.createTempFile("movielist-" + time, null);
			Files.copy(csvInput2try, tempFile, StandardCopyOption.REPLACE_EXISTING);

			return tempFile.toFile();
		} catch (final Exception e) {
			throw new RuntimeException(format("Error finding file: ''{0}''.", this.csvMovieListFile), e);
		}
	}

	public void loadData(final File file) throws IOException {
		Precondition.checkIfNotNull(file, "file");
		LOG.info("Starting loading csv data from file '" + file + "' to database.");
		final long startTime = System.currentTimeMillis();
		final Path fileCsv = Paths.get(file.toURI());
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
			final Set<String> producers, final boolean winner, final AtomicInteger lineNumber) {
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

	private static Set<String> splitCommaSeparatedValues(final String col) {
		return Stream.of(col.split(",")).filter(newCol -> !newCol.isEmpty()).map(String::trim)
				.collect(Collectors.toSet());
	}

	private static String[] getCols(final AtomicInteger lineNumber, final String line) {
		final String[] cols = Optional.ofNullable(line.split(";")).orElse(new String[] {});
		final int qtdCols = 4;
		if (cols.length < qtdCols) {
			throw new RuntimeException(MessageFormat.format(
					"Line {0} is invalid, less than {1} columns (separated by '';'')", lineNumber.get(), qtdCols));
		}
		return cols;
	}

}
