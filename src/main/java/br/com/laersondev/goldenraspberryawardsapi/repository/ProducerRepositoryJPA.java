package br.com.laersondev.goldenraspberryawardsapi.repository;

import java.util.List;
import java.util.Optional;

import br.com.laersondev.goldenraspberryawardsapi.model.Producer;
import br.com.laersondev.goldenraspberryawardsapi.repository.dto.ProducerMovieWinRs;
import br.com.laersondev.goldenraspberryawardsapi.util.Precondition;

public class ProducerRepositoryJpa extends AbstractBaseCrudRepository<Producer, Integer> implements ProducerRepository {

	@Override
	protected Class<Producer> getDomainClass() {
		return Producer.class;
	}

	@Override
	public Optional<Producer> findByName(String name) {
		Precondition.checkIfNotNull(name, "name");
		return getEntityManager().createQuery("from Producer where name = :name", Producer.class)
				.setParameter("name", name)//
				.getResultStream().findFirst();
	}

	@Override
	public List<ProducerMovieWinRs> findProducersWithWinMoviesAtLeastTwiceOrdened() {
		return getEntityManager() //
				.createNamedQuery("ProducerMovieWinRs.findProducersWithWinMoviesAtLeastTwice", ProducerMovieWinRs.class)
				.getResultList();
	}

}
