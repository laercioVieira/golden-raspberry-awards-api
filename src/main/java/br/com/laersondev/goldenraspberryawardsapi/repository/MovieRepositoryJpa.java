package br.com.laersondev.goldenraspberryawardsapi.repository;

import javax.transaction.Transactional;

import br.com.laersondev.goldenraspberryawardsapi.model.Movie;
import br.com.laersondev.goldenraspberryawardsapi.util.Precondition;

public class MovieRepositoryJpa extends AbstractBaseCrudRepository<Movie, Integer> implements MovieRepository {
	@Override
	protected Class<Movie> getDomainClass() {
		return Movie.class;
	}

	@Override
	@Transactional
	public void delete(Movie entity) {
		Precondition.checkIfNotNull(entity, "entity");
		entity.getStudios().forEach(std -> {
			getEntityManager().createNativeQuery("delete from movie_studio where movie_id = :id")
			.setParameter("id", entity.getId() ).executeUpdate();
		});
		entity.getProducers().forEach(prod -> {
			getEntityManager().createNativeQuery("delete from movie_producer where movie_id = :id")
			.setParameter("id", entity.getId() ).executeUpdate();
		});
		super.delete(entity);
	}
}
