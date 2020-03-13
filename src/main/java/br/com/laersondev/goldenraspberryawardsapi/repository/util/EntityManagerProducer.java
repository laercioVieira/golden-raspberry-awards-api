package br.com.laersondev.goldenraspberryawardsapi.repository.util;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Disposes;
import javax.enterprise.inject.Produces;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

@ApplicationScoped
public class EntityManagerProducer {

	private final EntityManagerFactory entityManagerFactory = Persistence
			.createEntityManagerFactory("goldenRaspberryAwardsPU");

	public EntityManagerProducer() {
		super();
	}

	@Produces
	@RequestScoped
	public EntityManager produce() {
		return this.entityManagerFactory.createEntityManager();
	}

	public void close(@Disposes final EntityManager entityManager) {
		entityManager.close();
	}

}
