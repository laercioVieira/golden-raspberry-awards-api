package br.com.laersondev.goldenraspberryawardsapi.service;

import static br.com.laersondev.goldenraspberryawardsapi.model.Movie.newFrom;
import static br.com.laersondev.goldenraspberryawardsapi.util.Precondition.checkIfNotNull;

import java.util.Optional;
import java.util.function.Function;

import javax.inject.Inject;
import javax.inject.Named;
import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;

import br.com.laersondev.goldenraspberryawardsapi.dto.MovieDto;
import br.com.laersondev.goldenraspberryawardsapi.model.Movie;
import br.com.laersondev.goldenraspberryawardsapi.repository.MovieRepository;
import br.com.laersondev.goldenraspberryawardsapi.service.exception.MovieNotFoundException;
import br.com.laersondev.goldenraspberryawardsapi.service.exception.ServiceException;

/**
 * MovieService
 */
@Named
public class MovieService {


	@Inject
	private MovieRepository repository;

	@Inject
	private ProducerService producerService;

	@Inject
	private StudioService studioService;

	protected MovieService() {
		super();
	}

	public Movie delete(final int movieId) {
		return this.tryDo(movId -> {
			final Movie movie = getByIdRequired(movieId);
			this.repository.delete(movie);
			return movie;
		}, movieId);
	}

	@Transactional(TxType.REQUIRED)
	public Movie create(final MovieDto movieDto) {
		return this.tryDo((dto) -> {
			final Movie movie = newFrom(checkIfNotNull(dto, "movieDto"));
			movie.addProducers(this.producerService.findOrCreate(dto.getProducers()));
			movie.addStudios(this.studioService.findOrCreate(dto.getStudios()));
			return this.repository.save(movie);
		}, movieDto);
	}

	public Optional<Movie> getById(final int id) {
		return this.tryDo(this.repository::findById, id);
	}

	public Movie getByIdRequired(final int id) {
		return this.getById(id).orElseThrow(() -> new MovieNotFoundException(id));
	}

	private <T, R> R tryDo(final Function<T, R> action, final T params) {
		try {
			return action.apply(params);
		} catch (final Exception e) {
			throw new ServiceException(e);
		}
	}


}