package br.com.laersondev.goldenraspberryawardsapi.repository;

import java.util.Optional;

import br.com.laersondev.goldenraspberryawardsapi.model.Studio;
import br.com.laersondev.goldenraspberryawardsapi.util.Precondition;

public class StudioRepositoryJpa extends AbstractBaseCrudRepository<Studio, Integer> implements StudioRepository {

	@Override
	protected Class<Studio> getDomainClass() {
		return Studio.class;
	}

	@Override
	public Optional<Studio> findByName(String name) {
		Precondition.checkIfNotNull(name, "name");
		return getEntityManager().createQuery("from Studio where name = :name", Studio.class)
				.setParameter("name", name)//
				.getResultStream().findFirst();
	}

}
