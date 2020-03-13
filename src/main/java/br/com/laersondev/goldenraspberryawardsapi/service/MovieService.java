package br.com.laersondev.goldenraspberryawardsapi.service;

import static br.com.laersondev.goldenraspberryawardsapi.model.Movie.newFrom;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.laersondev.goldenraspberryawardsapi.dto.MovieDto;
import br.com.laersondev.goldenraspberryawardsapi.model.Movie;
import br.com.laersondev.goldenraspberryawardsapi.repository.MovieRepository;
import br.com.laersondev.goldenraspberryawardsapi.service.exception.MovieNotFoundException;
import br.com.laersondev.goldenraspberryawardsapi.service.exception.ServiceException;

/**
 * MovieService
 */
@Named
@RequestScoped
public class MovieService {

	protected MovieService() {
		super();
	}

	@Inject
	private MovieRepository repository;

	public Movie delete(final int movieId) {
		return this.tryDo(movId -> {
			final Movie movie = getByIdObrigatorio(movieId);
			this.repository.deleteById(movId);
			//this.repository.flush();
			return movie;
		}, movieId);
	}

	public Movie create(final MovieDto movieDto) {
		return this.tryDo((dto) -> this.repository.save(newFrom(dto)), movieDto);
	}

	public Optional<Movie> getById(final int id) {
		return this.tryDo(this.repository::findById, id);
	}

	public Movie getByIdObrigatorio(final int id) {
		return this.getById(id)
				.orElseThrow(() -> new MovieNotFoundException(id));
	}


	public Movie update(final MovieDto movieDto) {
		return this.tryDo((final MovieDto dto) -> {
			final Movie movieToUpdate = this.getByIdObrigatorio(dto.getId());
			movieToUpdate.updateFrom(dto);
			return this.repository.save(movieToUpdate);
		}, movieDto);
	}

	private <T> void tryRun(final Consumer<T> action, final T params) {
		try {
			action.accept(params);
		} catch (final Exception e) {
			throw new ServiceException(e);
		}
	}

	private <T, R> R tryDo(final Function<T, R> action, final T params) {
		try {
			return action.apply(params);
		} catch (final Exception e) {
			throw new ServiceException(e);
		}
	}


}