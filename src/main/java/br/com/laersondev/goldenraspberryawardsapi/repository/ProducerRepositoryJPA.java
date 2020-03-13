/*package br.com.laersondev.goldenraspberryawardsapi.repository;

import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import org.springframework.data.jpa.repository.support.SimpleJpaRepository;

import br.com.laersondev.goldenraspberryawardsapi.model.Producer;
import br.com.laersondev.goldenraspberryawardsapi.repository.dto.ProducerMovieWinRs;

public class ProducerRepositoryJPA extends SimpleJpaRepository<Producer, Integer> implements ProducerRepository {

	@Inject private EntityManager em;

	public ProducerRepositoryJPA(final EntityManager em) {
		super(Producer.class, em);
		this.em = em;
	}

	@Override
	public List<ProducerMovieWinRs> findProducersWithWinMoviesAtLeastTwice() {
		final TypedQuery<ProducerMovieWinRs> namedQuery = this.em.createNamedQuery("findProducersWithWinMoviesAtLeastTwice", ProducerMovieWinRs.class);
		return namedQuery.getResultList();
	}

}
 */