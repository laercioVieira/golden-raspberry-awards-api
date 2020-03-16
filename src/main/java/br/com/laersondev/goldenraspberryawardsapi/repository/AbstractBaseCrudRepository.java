package br.com.laersondev.goldenraspberryawardsapi.repository;

import java.io.Serializable;
import java.util.Optional;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import br.com.laersondev.goldenraspberryawardsapi.model.Entity;
import br.com.laersondev.goldenraspberryawardsapi.util.Precondition;

public abstract class AbstractBaseCrudRepository<E extends Entity<I>, I extends Serializable> implements BaseCrudRepository<E, I> {

	@Inject
	private EntityManager em;

	protected AbstractBaseCrudRepository() {
		super();
	}

	protected EntityManager getEntityManager() {
		return this.em;
	}

	/* (non-Javadoc)
	 * @see br.com.laersondev.goldenraspberryawardsapi.repository.IBaseCrudRepository#deleteById(I)
	 */
	@Override
	@Transactional
	public void deleteById(I id) {
		Precondition.checkIfNotNull(id, "id");
		findById(id).ifPresent( entity -> delete(entity));
	}

	/* (non-Javadoc)
	 * @see br.com.laersondev.goldenraspberryawardsapi.repository.IBaseCrudRepository#delete(E)
	 */
	@Override
	@Transactional
	public void delete(E entity) {
		Precondition.checkIfNotNull(entity, "entity");
		if (entity.getId() != null && isNew(entity)) {
			return;
		}
		final E existing = this.em.find(getDomainClass(), entity.getId());
		if (existing == null) {
			return;
		}

		this.em.remove(this.em.contains(entity) ? entity : this.em.merge(entity));
	}


	private boolean isNew(E entity) {
		final Serializable id = entity.getId();
		return (id instanceof Number) && ((Number)id).longValue() <= 0;
	}

	/* (non-Javadoc)
	 * @see br.com.laersondev.goldenraspberryawardsapi.repository.IBaseCrudRepository#findById(I)
	 */
	@Override
	public Optional<E> findById(final I id) {
		Precondition.checkIfNotNull(id, "id");
		return Optional.ofNullable(this.em.find(getDomainClass(), id));
	}

	protected abstract Class<E> getDomainClass();


	/* (non-Javadoc)
	 * @see br.com.laersondev.goldenraspberryawardsapi.repository.IBaseCrudRepository#save(E)
	 */
	@Override
	@Transactional
	public E save(E entity) {
		Precondition.checkIfNotNull(entity, "entity");
		if (isNew(entity)) {
			this.em.persist(entity);
			return entity;
		} else {
			return this.em.merge(entity);
		}
	}


}
